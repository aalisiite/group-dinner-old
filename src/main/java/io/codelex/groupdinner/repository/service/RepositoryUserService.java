package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.UserService;
import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.AttendeeRecordRepository;
import io.codelex.groupdinner.repository.DinnerRecordRepository;
import io.codelex.groupdinner.repository.FeedbackRecordRepository;
import io.codelex.groupdinner.repository.UserRecordRepository;
import io.codelex.groupdinner.repository.mapper.MapAttendeeRecordToAttendee;
import io.codelex.groupdinner.repository.mapper.MapDinnerRecordToDinner;
import io.codelex.groupdinner.repository.mapper.MapFeedbackRecordToFeedback;
import io.codelex.groupdinner.repository.mapper.MapUserRecordToUser;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RepositoryUserService implements UserService {

    private final DinnerRecordRepository dinnerRecordRepository;
    private final UserRecordRepository userRecordRepository;
    private final AttendeeRecordRepository attendeeRecordRepository;
    private final FeedbackRecordRepository feedbackRecordRepository;
    private final MapUserRecordToUser toUser = new MapUserRecordToUser();
    private final MapDinnerRecordToDinner toDinner = new MapDinnerRecordToDinner();
    private final MapAttendeeRecordToAttendee toAttendee = new MapAttendeeRecordToAttendee();
    private final MapFeedbackRecordToFeedback toFeedback = new MapFeedbackRecordToFeedback();
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
            DinnerRecord dinnerRecord = createDinnerRecordFromRequest(user.getId(), request);
            dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
            AttendeeRecord attendeeRecord = new AttendeeRecord(
                    dinnerRecord,
                    dinnerRecord.getCreator(),
                    true
            );
            attendeeRecordRepository.save(attendeeRecord);
            return toDinner.apply(dinnerRecord);
        } else {
            throw new IllegalStateException("Dinner already present");
        }
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        if (!userRecordRepository.isUserPresent(request.getEmail())) {
            UserRecord userRecord = new UserRecord(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail().toLowerCase().trim(),
                    passwordEncoder.encode(request.getPassword())
            );
            userRecord = userRecordRepository.save(userRecord);
            return toUser.apply(userRecord);
        } else {
            throw new IllegalStateException("Email already exists");
        }
    }

    @Override
    public User authenticateUser(SignInRequest request) {
        UserRecord userRecord = userRecordRepository.findByEmail(request.getEmail().toLowerCase().trim());
        if (userRecord != null) {
            if (passwordEncoder.matches(request.getPassword(), userRecord.getPassword())) {
                return toUser.apply(userRecord);
            } else {
                throw new IllegalStateException("Password incorrect");
            }
        } else {
            throw new IllegalStateException("Email incorrect");
        }
    }


    @Override
    public Attendee joinDinner(String userEmail, Long dinnerId) {
        Optional<DinnerRecord> dinnerRecord = dinnerRecordRepository.findById(dinnerId);
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        if (dinnerRecord.isPresent()) {
            if (!attendeeRecordRepository.userAttendsDinner(dinnerRecord.get().getId(), user.getId())) {
                Integer attendeeCount = attendeeRecordRepository.countDinnerAttendees(dinnerId);
                Boolean addToAcceptedGuests = attendeeCount < dinnerRecord.get().getMaxGuests();
                AttendeeRecord attendeeRecord = new AttendeeRecord(
                        dinnerRecord.get(),
                        userRecordRepository.findById(user.getId()).get(),
                        addToAcceptedGuests
                );
                attendeeRecord = attendeeRecordRepository.save(attendeeRecord);
                return toAttendee.apply(attendeeRecord);
            } else {
                throw new IllegalStateException("User already joined dinner");
            }
        } else {
            throw new IllegalArgumentException("No such dinner present");
        }
    }

    @Override
    public Feedback leaveFeedback(String providerEmail, Long dinnerId, LeaveFeedbackRequest request) {
        UserRecord provider = userRecordRepository.findByEmail(providerEmail);
        UserRecord receiver = userRecordRepository.findByEmail(request.getReceiver());
        if (receiver == null) {
            throw new IllegalArgumentException("No such user");
        } else if (provider.equals(receiver)) {
            throw new IllegalStateException("Provider is same as receiver");
        } else if (feedbackRecordRepository.isFeedbackPresent(dinnerId, provider.getId(), receiver.getId())) {
            throw new IllegalStateException("Feedback for users already exist");
        } else if (!attendeeRecordRepository.userAttendsDinner(dinnerId, provider.getId())
                || !attendeeRecordRepository.userAttendsDinner(dinnerId, receiver.getId())) {
            throw new IllegalStateException("No such common dinner for users");
        } else {
            Optional<DinnerRecord> dinnerRecord = dinnerRecordRepository.findById(dinnerId);
            FeedbackRecord feedbackRecord = new FeedbackRecord(
                    dinnerRecord.get(),
                    provider,
                    receiver,
                    request.getFeedback()
            );
            feedbackRecord = feedbackRecordRepository.save(feedbackRecord);
            return toFeedback.apply(feedbackRecord);
        }
    }

    @Override
    public Dinner findDinner(Long id) {
        Optional<DinnerRecord> dinnerRecord = dinnerRecordRepository.findById(id);
        return dinnerRecord.map(toDinner).orElse(null);
    }

    @Override
    public List<User> findDinnerAttendees(Long dinnerId, boolean accepted) {
        List<AttendeeRecord> attendees = attendeeRecordRepository.findDinnerAttendees(dinnerId, accepted);
        List<UserRecord> users = new ArrayList<>();
        for (AttendeeRecord attendee : attendees) {
            Optional<UserRecord> userRecord = userRecordRepository.findById(attendee.getUser().getId());
            userRecord.ifPresent(users::add);
        }
        return users.stream().map(toUser).collect(Collectors.toList());
    }

    @Override //todo now only shows dinners only with guests that had good feedback from this user
    public List<Dinner> getGoodMatchDinners(String userEmail) {
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        List<UserRecord> badFeedbackUsers = feedbackRecordRepository.getBadFeedbackUsers(user.getId());
        List<DinnerRecord> dinners = dinnerRecordRepository.findAll();
        for (DinnerRecord dinner : dinners) {
            for (UserRecord userRecord : badFeedbackUsers) {
                if (attendeeRecordRepository.userAttendsDinner(dinner.getId(), userRecord.getId())) {
                    dinners.remove(dinner);
                    break;
                }
            }
        }
        List<Dinner> resultingDinners = new ArrayList<>();
        for (DinnerRecord dinnerRecord : dinners) {
            resultingDinners.add(toDinner.apply(dinnerRecord));
        }
        return resultingDinners;
    }


    private DinnerRecord createDinnerRecordFromRequest(Long creatorId, CreateDinnerRequest request) {
        DinnerRecord dinnerRecord = new DinnerRecord();
        dinnerRecord.setTitle(request.getTitle());
        Optional<UserRecord> creator = userRecordRepository.findById(creatorId);
        dinnerRecord.setCreator(creator.get());
        dinnerRecord.setMaxGuests(request.getMaxGuests());
        dinnerRecord.setDescription(request.getDescription());
        dinnerRecord.setLocation(request.getLocation());
        dinnerRecord.setDateTime(request.getDateTime());
        return dinnerRecord;
    }

}
