package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.repository.AttendeeRecordRepository;
import io.codelex.groupdinner.repository.DinnerRecordRepository;
import io.codelex.groupdinner.repository.FeedbackRecordRepository;
import io.codelex.groupdinner.repository.UserRecordRepository;
import io.codelex.groupdinner.repository.mapper.*;
import io.codelex.groupdinner.UserService;
import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    public Dinner createDinner(CreateDinnerRequest request) {
        if (!dinnerRecordRepository.isDinnerPresent(
                request.getTitle(),
                request.getMaxGuests(),
                request.getDescription(),
                request.getLocation(),
                request.getDateTime()
        )) {
            DinnerRecord dinnerRecord = createDinnerRecordFromRequest(request);
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
            throw new IllegalStateException("email already exists");
        }
    }

    @Override
    public User authenticateUser(SignInRequest request) {
        UserRecord userRecord = userRecordRepository.findByEmail(request.getEmail().toLowerCase().trim());
        if (userRecord != null) {
            if (passwordEncoder.matches(request.getPassword(), userRecord.getPassword())){
                return toUser.apply(userRecord);
            } else {
                throw new IllegalStateException("password incorrect");
            }
        } else {
            throw new IllegalStateException("email incorrect");
        }
    }


    @Override
    public Attendee joinDinner(String userId, Long dinnerId) {
        Optional<DinnerRecord> dinnerRecord = dinnerRecordRepository.findById(dinnerId);
        Long userIdLong = Long.parseLong(userId);
        if (dinnerRecord.isPresent()) {
            boolean isAccepted = dinnerRecord.get().shouldAcceptRequest();
            dinnerRecordRepository.incrementCurrentGuests(dinnerRecord.get().getId());
            AttendeeRecord attendeeRecord = new AttendeeRecord(
                    dinnerRecord.get(),
                    userRecordRepository.findById(userIdLong).get(),
                    isAccepted
            );
            attendeeRecordRepository.save(attendeeRecord);
            return toAttendee.apply(attendeeRecord);
        } else {
            throw new IllegalArgumentException("No such dinner present");
        }
    }

    @Override
    public Feedback leaveFeedback(String provider, Long dinnerId, LeaveFeedbackRequest request) {
        Long providerId = Long.parseLong(provider);
        if (feedbackRecordRepository.isFeedbackPresent(providerId, request.getReceiver().getId())) {
            throw new IllegalStateException("Feedback for users already exist");
        } else {
            //todo
            //need to check if provider & receiver attended same dinner before leaving feedback?
            Optional<UserRecord> providerRecord = userRecordRepository.findById(providerId);
            Optional<UserRecord> receiverRecord = userRecordRepository.findById(request.getReceiver().getId());
            FeedbackRecord feedbackRecord = new FeedbackRecord(
                    providerRecord.get(),
                    receiverRecord.get(),
                    request.getFeedback()
            );
            feedbackRecordRepository.save(feedbackRecord);
            return toFeedback.apply(feedbackRecord);
        }
    }

    public Dinner findDinnerById(Long id) {
        Optional<DinnerRecord> dinnerRecord = dinnerRecordRepository.findById(id);
        return dinnerRecord.map(toDinner).orElse(null);
    }

    public List<User> findUsersWithAcceptedStatus(Long dinnerId, boolean isAccepted) {
        List<AttendeeRecord> attendees = attendeeRecordRepository.findDinnerAttendees(dinnerId, isAccepted);
        List<UserRecord> users = Collections.emptyList();
        for (AttendeeRecord attendee : attendees) {
            Optional<UserRecord> userRecord = userRecordRepository.findById(attendee.getUser().getId());
            userRecord.ifPresent(users::add);
        }
        return users.stream().map(toUser).collect(Collectors.toList());
    }


    private DinnerRecord createDinnerRecordFromRequest(CreateDinnerRequest request) {
        DinnerRecord dinnerRecord = new DinnerRecord();
        dinnerRecord.setTitle(request.getTitle());
        dinnerRecord.setMaxGuests(request.getMaxGuests());
        dinnerRecord.setDescription(request.getDescription());
        dinnerRecord.setLocation(request.getLocation());
        dinnerRecord.setDateTime(request.getDateTime());
        return dinnerRecord;
    }

}
