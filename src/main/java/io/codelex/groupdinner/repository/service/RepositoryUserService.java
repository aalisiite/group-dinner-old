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

//todo separate auth methods from functionality of app
@Component
public class RepositoryUserService implements UserService {

    private final DinnerRecordRepository dinnerRecordRepository;
    private final UserRecordRepository userRecordRepository;
    private final AttendeeRecordRepository attendeeRecordRepository;
    private final FeedbackRecordRepository feedbackRecordRepository;
    private final MapDBRecordToApiCompatible toApiCompatible = new MapDBRecordToApiCompatible();
    private final PasswordEncoder passwordEncoder;

    public RepositoryUserService(DinnerRecordRepository dinnerRecordRepository,
                                 UserRecordRepository userRecordRepository,
                                 AttendeeRecordRepository attendeeRecordRepository, FeedbackRecordRepository feedbackRecordRepository, PasswordEncoder passwordEncoder) {
        this.dinnerRecordRepository = dinnerRecordRepository;
        this.userRecordRepository = userRecordRepository;
        this.attendeeRecordRepository = attendeeRecordRepository;
        this.feedbackRecordRepository = feedbackRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //tested
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

    //tested
    @Override
    public User registerUser(RegistrationRequest request) {
        if (!userRecordRepository.isUserPresent(request.getEmail())) {
            UserRecord user = new UserRecord(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail().toLowerCase().trim(),
                    passwordEncoder.encode(request.getPassword())
            );
            user = userRecordRepository.save(user);
            return toApiCompatible.apply(user);
        } else {
            throw new IllegalStateException("Email already exists");
        }
    }

    //tested
    @Override
    public User authenticateUser(SignInRequest request) {
        UserRecord user = userRecordRepository.findByEmail(request.getEmail().toLowerCase().trim());
        if (user != null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return toApiCompatible.apply(user);
            } else {
                throw new IllegalStateException("Password incorrect");
            }
        } else {
            throw new IllegalStateException("Email incorrect");
        }
    }

    //tested
    @Override
    public Attendee joinDinner(String userEmail, Long dinnerId) {
        Optional<DinnerRecord> dinner = dinnerRecordRepository.findById(dinnerId);
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        if (dinner.isPresent()) { //todo needed if dinnerId we get from pathvariable?
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
        } else {
            throw new IllegalArgumentException("No such dinner present");
        }
    }

    //tested
    @Override
    public Feedback leaveFeedback(String providerEmail, Long dinnerId, LeaveFeedbackRequest request) {
        UserRecord provider = userRecordRepository.findByEmail(providerEmail);
        UserRecord receiver = userRecordRepository.findByEmail(request.getReceiver());
        if (receiver == null) {
            throw new IllegalArgumentException("No such user");
        } else if (provider.equals(receiver)) {
            throw new IllegalStateException("Provider is same as receiver");
        } else if (feedbackRecordRepository.isFeedbackPresent(dinnerId, provider.getId(), receiver.getId())) {
            FeedbackRecord feedback = feedbackRecordRepository.getFeedback(dinnerId, provider.getId(), receiver.getId());
            return toApiCompatible.apply(feedback);
        } else if (attendeeRecordRepository.userJoinedDinner(dinnerId, provider.getId())
                && attendeeRecordRepository.userJoinedDinner(dinnerId, receiver.getId())) {
            Optional<DinnerRecord> dinner = dinnerRecordRepository.findById(dinnerId);
            FeedbackRecord feedback = new FeedbackRecord(
                    dinner.get(),
                    provider,
                    receiver,
                    request.getFeedback()
            );
            feedback = feedbackRecordRepository.save(feedback);
            return toApiCompatible.apply(feedback);
        } else {
            throw new IllegalStateException("No such common dinner for users");
        }
    }

    //no need to test
    @Override
    public Dinner findDinner(Long id) {
        Optional<DinnerRecord> dinner = dinnerRecordRepository.findById(id);
        return dinner.map(toApiCompatible::apply).orElse(null);
    }

    //todo test this finish
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

    //todo test this finish
    @Override //todo now only shows dinners only with guests that had good feedback from this user
    public List<Dinner> getGoodMatchDinners(String userEmail) {
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        List<UserRecord> badFeedbackUsers = feedbackRecordRepository.getBadFeedbackUsers(user.getId());
        List<DinnerRecord> dinners = dinnerRecordRepository.findAll();
        for (DinnerRecord dinnerRecord : dinners) {
            for (UserRecord userRecord : badFeedbackUsers) {
                if (attendeeRecordRepository.userJoinedDinner(dinnerRecord.getId(), userRecord.getId())) {
                    dinners = deleteDinnerFromList(dinners, dinnerRecord);
                    //todo why dinners.remove(dinnerrecord) wont work???
                    break;
                }
            }
        }
        List<Dinner> resultingDinners = new ArrayList<>();
        for (DinnerRecord dinnerRecord : dinners) {
            resultingDinners.add(toApiCompatible.apply(dinnerRecord));
        }
        return resultingDinners;
    }

    //todo needed if dinners.remove works?
    private List<DinnerRecord> deleteDinnerFromList(List<DinnerRecord> dinners, DinnerRecord delete) {
        List<DinnerRecord> result = new ArrayList<>();
        for (DinnerRecord dinner : dinners) {
            if (!dinner.equals(delete)) {
                result.add(dinner);
            }
        }
        return result;
    }

    //todo how to make private and be able to test
    public DinnerRecord createDinnerRecordFromRequest(Long creatorId, CreateDinnerRequest request) {
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
