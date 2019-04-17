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

//todo separate auth methods from functionality of app
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
            return toDinner.apply(dinner);
        } else {
            DinnerRecord dinner = dinnerRecordRepository.getDinner(
                    user.getId(),
                    request.getTitle(),
                    request.getMaxGuests(),
                    request.getDescription(),
                    request.getLocation(),
                    request.getDateTime()
            );
            return toDinner.apply(dinner);
        }
    }
    
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
            return toUser.apply(user);
        } else {
            throw new IllegalStateException("Email already exists");
        }
    }

    //todo test this
    @Override
    public User authenticateUser(SignInRequest request) {
        UserRecord user = userRecordRepository.findByEmail(request.getEmail().toLowerCase().trim());
        if (user != null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return toUser.apply(user);
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
                return toAttendee.apply(attendee);
            } else {
                AttendeeRecord attendee = attendeeRecordRepository.getAttendee(dinnerId, user.getId());
                return toAttendee.apply(attendee);
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
            return toFeedback.apply(feedback);
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
            return toFeedback.apply(feedback);
        } else {
            throw new IllegalStateException("No such common dinner for users");
        }
    }

    //todo test this ?
    @Override
    public Dinner findDinner(Long id) {
        Optional<DinnerRecord> dinner = dinnerRecordRepository.findById(id);
        return dinner.map(toDinner).orElse(null);
    }
    
    //todo test this
    @Override
    public List<User> findDinnerAttendees(Long dinnerId, boolean accepted) {
        List<AttendeeRecord> attendees = attendeeRecordRepository.findDinnerAttendees(dinnerId, accepted);
        List<UserRecord> users = new ArrayList<>();
        for (AttendeeRecord attendee : attendees) {
            Optional<UserRecord> user = userRecordRepository.findById(attendee.getUser().getId());
            user.ifPresent(users::add);
        }
        return users.stream().map(toUser).collect(Collectors.toList());
    }
    
    //todo test this
    @Override //todo now only shows dinners only with guests that had good feedback from this user
    public List<Dinner> getGoodMatchDinners(String userEmail) {
        UserRecord user = userRecordRepository.findByEmail(userEmail);
        List<UserRecord> badFeedbackUsers = feedbackRecordRepository.getBadFeedbackUsers(user.getId());
        List<DinnerRecord> dinners = dinnerRecordRepository.findAll();
        for (DinnerRecord dinnerRecord : dinners) {
            for (UserRecord userRecord : badFeedbackUsers) {
                if (attendeeRecordRepository.userJoinedDinner(dinnerRecord.getId(), userRecord.getId())) {
                    dinners.remove(dinnerRecord);
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
    
    //todo test this ??
    private DinnerRecord createDinnerRecordFromRequest(Long creatorId, CreateDinnerRequest request) {
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
