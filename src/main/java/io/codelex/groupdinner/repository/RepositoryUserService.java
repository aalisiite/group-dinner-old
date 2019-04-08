package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.UserService;
import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
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
    private final MapUserRecordToUser toUser = new MapUserRecordToUser();
    private final MapDinnerRecordToDinner toDinner = new MapDinnerRecordToDinner();
    private final MapAttendeeRecordToAttendee toAttendee = new MapAttendeeRecordToAttendee();

    public RepositoryUserService(DinnerRecordRepository dinnerRecordRepository, UserRecordRepository userRecordRepository, AttendeeRecordRepository attendeeRecordRepository) {
        this.dinnerRecordRepository = dinnerRecordRepository;
        this.userRecordRepository = userRecordRepository;
        this.attendeeRecordRepository = attendeeRecordRepository;
    }

    @Override
    public Dinner createDinner(CreateDinnerRequest request) {
        if (!dinnerRecordRepository.isDinnerPresent(
                request.getTitle(),
                request.getCreator().getId(),
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
    public Attendee joinDinner(String userId, Long dinnerId) {
        Optional<DinnerRecord> dinnerRecord = dinnerRecordRepository.findById(dinnerId);
        Long userIdLong = Long.parseLong(userId);
        if (dinnerRecord.isPresent()) {
            boolean isAccepted = dinnerRecord.get().shouldAcceptRequest();
            AttendeeRecord attendeeRecord = new AttendeeRecord(
                    dinnerRecord.get(),
                    userRecordRepository.findById(userIdLong).get(),
                    isAccepted
            );
            attendeeRecordRepository.save(attendeeRecord);
            dinnerRecord.get().incrementCurrentGuests();
            dinnerRecordRepository.incrementCurrentGuests(dinnerRecord.get().getId());
            return toAttendee.apply(attendeeRecord);
        } else {
            throw new IllegalArgumentException("No such dinner present");
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
        dinnerRecord.setCreator(createOrGetUser(request.getCreator()));
        dinnerRecord.setMaxGuests(request.getMaxGuests());
        dinnerRecord.setCurrentGuests(1);
        dinnerRecord.setDescription(request.getDescription());
        dinnerRecord.setLocation(request.getLocation());
        dinnerRecord.setDateTime(request.getDateTime());
        return dinnerRecord;
    }

    private UserRecord createOrGetUser(User user) {
        return userRecordRepository.findById(user.getId())
                .orElseGet(() -> {
                    UserRecord created = new UserRecord(
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail()
                    );
                    return userRecordRepository.save(created);
                });
    }

}
