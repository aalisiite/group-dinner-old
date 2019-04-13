package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.UserAuthorizationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorizationRecordRepository extends JpaRepository<UserAuthorizationRecord, Long> {
    
}
