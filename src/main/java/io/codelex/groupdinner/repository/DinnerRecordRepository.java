package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.DinnerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DinnerRecordRepository extends JpaRepository<DinnerRecord, Long> {
    
    
}
