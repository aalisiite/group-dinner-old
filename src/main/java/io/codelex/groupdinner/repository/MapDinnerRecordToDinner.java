package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;

import java.util.function.Function;

public class MapDinnerRecordToDinner implements Function<DinnerRecord, Dinner> {
    private MapUserRecordToUser toUser = new MapUserRecordToUser();

    @Override
    public Dinner apply(DinnerRecord dinnerRecord) {
        return new Dinner(
                dinnerRecord.getId(),
                dinnerRecord.getTitle(),
                toUser.apply(dinnerRecord.getCreator()),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );
    }
}
