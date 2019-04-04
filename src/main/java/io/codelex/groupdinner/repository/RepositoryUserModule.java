package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.UserModule;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.JoinDinnerRequest;
import io.codelex.groupdinner.repository.model.DinnerRecord;

import java.util.concurrent.atomic.AtomicLong;

public class RepositoryUserModule implements UserModule {
    
    private final DinnerRecordRepository dinnerRecordRepository;
    private final UserRecordRepository userRecordRepository;
    private final AttendeeRecordRepository attendeeRecordRepository;
    private final MapDinnerRecordToDinner toDinner = new MapDinnerRecordToDinner();
    private final MapAttendeeRecordToAttendee toAttendee = new MapAttendeeRecordToAttendee();
    private final AtomicLong id = new AtomicLong();

    public RepositoryUserModule(DinnerRecordRepository dinnerRecordRepository, UserRecordRepository userRecordRepository, AttendeeRecordRepository attendeeRecordRepository) {
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
            return toDinner.apply(dinnerRecord);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public Boolean joinDinner(JoinDinnerRequest request) {
        return null;
    }

    private DinnerRecord createDinnerRecordFromRequest(CreateDinnerRequest request) {
        DinnerRecord dinnerRecord = new DinnerRecord();
        dinnerRecord.setId(id.incrementAndGet());
        dinnerRecord.setCreator(request.getCreator());
        dinnerRecord.setMaxGuests(request.getMaxGuests());
        dinnerRecord.setCurrentGuests(1);
        dinnerRecord.setDescription(request.getDescription());
        dinnerRecord.setLocation(request.getLocation());
        dinnerRecord.setDateTime(request.getDateTime());
        return dinnerRecord;
    }
    
}
