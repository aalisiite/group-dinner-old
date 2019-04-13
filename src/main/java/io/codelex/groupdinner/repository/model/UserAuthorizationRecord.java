package io.codelex.groupdinner.repository.model;

import io.codelex.groupdinner.repository.mapper.PasswordEncrypt;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "authorizations")
public class UserAuthorizationRecord {

    PasswordEncrypt passwordEncrypt = new PasswordEncrypt();
    
    private String email;
    //private UserRecord user???
    private String password;

    public UserAuthorizationRecord(String email, String password) {
        this.email = email;
        this.password = passwordEncrypt.hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
