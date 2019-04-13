package io.codelex.groupdinner.repository.mapper;

import io.codelex.groupdinner.api.AuthRequest;
import io.codelex.groupdinner.repository.model.UserAuthorizationRecord;

import java.util.function.Function;

public class MapUserAuthorizationRecordToUserAuthorization implements Function<UserAuthorizationRecord, AuthRequest> {
    
    @Override
    public AuthRequest apply(UserAuthorizationRecord userAuthorizationRecord) {
        return new AuthRequest(
                userAuthorizationRecord.getEmail(),
                userAuthorizationRecord.getPassword()
        );
    }
}
