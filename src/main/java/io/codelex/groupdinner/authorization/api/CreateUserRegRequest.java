package io.codelex.groupdinner.authorization.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class CreateUserRegRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    @JsonCreator
    public CreateUserRegRequest(
            @JsonProperty("email") @NotEmpty String email,
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
