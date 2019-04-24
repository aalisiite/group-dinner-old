package io.codelex.groupdinner.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegistrationRequest {

    @NotEmpty(message = "Please enter your name")
    private String firstName;
    @NotEmpty(message = "Please enter your last name")
    private String lastName;
    @Email(message = "Please enter a valid email")
    private String email;
    @Size(min = 8, max = 30, message = "Password must be at least 8 characters long")
    @NotEmpty
    private String password;

    @JsonCreator
    public RegistrationRequest(
            @JsonProperty("firstName") @NotEmpty String firstName,
            @JsonProperty("lastName") @NotEmpty String lastName,
            @JsonProperty("email") @Email String email,
            @JsonProperty("password") @NotEmpty String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
