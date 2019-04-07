package io.codelex.groupdinner.authorization.repository;

import io.codelex.groupdinner.authorization.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
