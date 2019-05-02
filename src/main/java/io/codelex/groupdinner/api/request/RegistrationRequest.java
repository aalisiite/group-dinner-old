package io.codelex.groupdinner.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegistrationRequest {
    
    @NotEmpty(message = "Please enter your full name")
    private String fullName;
    @Email(message = "Please enter a valid email")
    private String email;
    @Size(min = 8, max = 30, message = "Password must be at least 8 characters long")
    @NotEmpty
    private String password;

    @JsonCreator
    public RegistrationRequest(
            @JsonProperty("lastName") @NotEmpty String fullName,
            @JsonProperty("email") @Email String email,
            @JsonProperty("password") @NotEmpty String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
