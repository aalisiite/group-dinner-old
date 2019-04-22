package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.UserService;
import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.AttendeeRecordRepository;
import io.codelex.groupdinner.repository.DinnerRecordRepository;
import io.codelex.groupdinner.repository.FeedbackRecordRepository;
import io.codelex.groupdinner.repository.UserRecordRepository;
import io.codelex.groupdinner.repository.mapper.MapDBRecordToApiCompatible;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RepositoryUserService implements UserService {

    private final DinnerRecordRepository dinnerRecordRepository;
    private final UserRecordRepository userRecordRepository;
    private final AttendeeRecordRepository attendeeRecordRepository;
    private final FeedbackRecordRepository feedbackRecordRepository;
    private final MapDBRecordToApiCompatible toApiCompatible = new MapDBRecordToApiCompatible();

    public RepositoryUserService(DinnerRecordRepository dinnerRecordRepository,
                                 UserRecordRepository userRecordRepository,
                                 AttendeeRecordRepository attendeeRecordRepository, FeedbackRecordRepository feedbackRecordRepository, PasswordEncoder passwordEncoder) {
        this.dinnerRecordRepository = dinnerRecordRepository;
        this.userRecordRepository = userRecordRepository;
        this.attendeeRecordRepository = attendeeRecordRepository;
        this.feedbackRecordRepository = feedbackRecordRepository;
    }

    @Override
    public Dinner createDinner(String userEmail, CreateDinnerRequest request) {
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        if (!dinnerRecordRepository.isDinnerPresent(
                user.getId(),
                request.getTitle(),
                request.getMaxGuests(),
                request.getDescription(),
                request.getLocation(),
                request.getDateTime()
        )) {
            DinnerRecord dinner = createDinnerRecordFromRequest(user.getId(), request);
            dinner = dinnerRecordRepository.save(dinner);
            AttendeeRecord attendee = new AttendeeRecord(
                    dinner,
                    dinner.getCreator(),
                    true
            );
            attendeeRecordRepository.save(attendee);
            return toApiCompatible.apply(dinner);
        } else {
            DinnerRecord dinner = dinnerRecordRepository.getDinner(
                    user.getId(),
                    request.getTitle(),
                    request.getMaxGuests(),
                    request.getDescription(),
                    request.getLocation(),
                    request.getDateTime()
            );
            return toApiCompatible.apply(dinner);
        }
    }

    @Override
    public Attendee joinDinner(String userEmail, Long dinnerId) {
        Optional<DinnerRecord> dinner = dinnerRecordRepository.findById(dinnerId);
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        if (dinner.isEmpty()) {
            throw new IllegalArgumentException("No such dinner exists");
        }
        if (!attendeeRecordRepository.userJoinedDinner(dinner.get().getId(), user.getId())) {
            Integer attendeeCount = attendeeRecordRepository.countDinnerAttendees(dinnerId);
            Boolean addToAcceptedGuests = attendeeCount < dinner.get().getMaxGuests();
            AttendeeRecord attendee = new AttendeeRecord(
                    dinner.get(),
                    user,
                    addToAcceptedGuests
            );
            attendee = attendeeRecordRepository.save(attendee);
            return toApiCompatible.apply(attendee);
        } else {
            AttendeeRecord attendee = attendeeRecordRepository.getAttendee(dinnerId, user.getId());
            return toApiCompatible.apply(attendee);
        }
    }

    @Override
    public Feedback leaveFeedback(String providerEmail, Long dinnerId, LeaveFeedbackRequest request) {
        UserRecord provider = userRecordRepository.findByEmail(providerEmail);
        UserRecord receiver = userRecordRepository.findByEmail(request.getReceiver());
        Optional<DinnerRecord> dinner = dinnerRecordRepository.findById(dinnerId);
        if (receiver == null) {
            throw new IllegalArgumentException("No such user exists");
        }
        if (provider.equals(receiver)) {
            throw new IllegalArgumentException("Provider is same as receiver");
        }
        if (dinner.isEmpty()) {
            throw new IllegalArgumentException("No such dinner exists");
        }
        if (feedbackRecordRepository.isFeedbackPresent(dinnerId, provider.getId(), receiver.getId())) {
            FeedbackRecord feedback = feedbackRecordRepository.getFeedback(dinnerId, provider.getId(), receiver.getId());
            return toApiCompatible.apply(feedback);
        }
        if (attendeeRecordRepository.userJoinedDinner(dinnerId, provider.getId())
                && attendeeRecordRepository.userJoinedDinner(dinnerId, receiver.getId())) {
            FeedbackRecord feedback = new FeedbackRecord(
                    dinner.get(),
                    provider,
                    receiver,
                    request.getFeedback()
            );
            feedback = feedbackRecordRepository.save(feedback);
            return toApiCompatible.apply(feedback);
        } else {
            throw new IllegalArgumentException("No such common dinner for users");
        }
    }

    @Override
    public Dinner findDinner(Long id) {
        Optional<DinnerRecord> dinner = dinnerRecordRepository.findById(id);
        return dinner.map(toApiCompatible::apply).orElse(null);
    }

    @Override
    public List<User> findDinnerAttendees(Long dinnerId, boolean accepted) {
        List<AttendeeRecord> attendees = attendeeRecordRepository.findDinnerAttendees(dinnerId, accepted);
        List<UserRecord> users = new ArrayList<>();
        for (AttendeeRecord attendee : attendees) {
            Optional<UserRecord> user = userRecordRepository.findById(attendee.getUser().getId());
            user.ifPresent(users::add);
        }
        return users.stream()
                .map(toApiCompatible::apply)
                .collect(Collectors.toList());
    }

    @Override
    public List<Dinner> getGoodMatchDinners(String userEmail) {
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        List<DinnerRecord> goodDinners = dinnerRecordRepository.getGoodDinners(user.getId());
        List<Dinner> resultingDinners = new ArrayList<>();
        for (DinnerRecord dinnerRecord : goodDinners) {
            resultingDinners.add(toApiCompatible.apply(dinnerRecord));
        }
        return resultingDinners;
    }

    DinnerRecord createDinnerRecordFromRequest(Long creatorId, CreateDinnerRequest request) {
        DinnerRecord dinner = new DinnerRecord();
        dinner.setTitle(request.getTitle());
        Optional<UserRecord> creator = userRecordRepository.findById(creatorId);
        dinner.setCreator(creator.get());
        dinner.setMaxGuests(request.getMaxGuests());
        dinner.setDescription(request.getDescription());
        dinner.setLocation(request.getLocation());
        dinner.setDateTime(request.getDateTime());
        return dinner;
    }
}
