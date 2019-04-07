package io.codelex.groupdinner.authorization.repository;

import io.codelex.groupdinner.authorization.model.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginDetails, Long> {

   LoginDetails findByUsername(String username) ;
}
