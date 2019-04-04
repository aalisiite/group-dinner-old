package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.UserModule;
import io.codelex.groupdinner.api.CreateDinnerRequest;
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
    public DinnerRecord createDinner(CreateDinnerRequest request) {
        return null;
    }

    @Override
    public Boolean joinDinner(JoinDinnerRequest request) {
        return null;
    }
    
}
