package io.codelex.groupdinner.repository.mapper;

import io.codelex.groupdinner.api.UserAuthorization;
import io.codelex.groupdinner.repository.model.UserAuthorizationRecord;

import java.util.function.Function;

public class MapUserAuthorizationRecordToUserAuthorization implements Function<UserAuthorizationRecord, UserAuthorization> {
    
    @Override
    public UserAuthorization apply(UserAuthorizationRecord userAuthorizationRecord) {
        return new UserAuthorization(
                userAuthorizationRecord.getEmail(),
                userAuthorizationRecord.getPassword()
        );
    }
}
