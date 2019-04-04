package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.AttendeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRecordRepository extends JpaRepository<AttendeeRecord, Long> {
    
}
