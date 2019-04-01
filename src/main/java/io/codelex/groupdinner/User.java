package io.codelex.groupdinner;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity(name = "User")
public class User {

    @Id
    @GeneratedValue
    Long id;
    String fullName;
    String email;
}
