package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {

    @Query("select user from UserRecord user"
            + " where user.id = :id "
            + " and user.firstName = :firstName"
            + " and user.lastName = :lastName "
            + " and user.email = :email ")
    List<UserRecord> findUserRecord(@Param("id") Long id,
                                    @Param("firstName") String firstName,
                                    @Param("lastName") String lastName,
                                    @Param("email") String email);
}


