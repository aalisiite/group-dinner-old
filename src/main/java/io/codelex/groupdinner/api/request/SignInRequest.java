package io.codelex.groupdinner.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class SignInRequest {

    @Email(message = "Please enter a valid email")
    @NotEmpty
    private String email;
    @NotEmpty(message = "Please enter your password")
    private String password;

    @JsonCreator
    public SignInRequest(
            @JsonProperty("email") @Email @NotEmpty String email,
            @JsonProperty("password") @NotEmpty String password) {
        this.email = email;
        this.password = password;
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
