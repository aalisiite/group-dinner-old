package io.codelex.groupdinner.repository.mapper;

import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.model.UserRecord;

import java.util.function.Function;

public class MapUserRecordToUser implements Function<UserRecord, User> {

    @Override
    public User apply(UserRecord userRecord) {
        return new User(
                userRecord.getId(),
                userRecord.getFirstName(),
                userRecord.getLastName(),
                userRecord.getEmail()
        );
    }
}
