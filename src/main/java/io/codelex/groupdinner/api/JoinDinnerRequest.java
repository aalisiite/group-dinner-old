package io.codelex.groupdinner.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class JoinDinnerRequest {


    @NotNull
    private User user;

    @NotNull
    private Dinner dinner;

    public JoinDinnerRequest(
            @JsonProperty("user") @NotNull User user,
            @JsonProperty("dinner") @NotNull Dinner dinner) {
        this.user = user;
        this.dinner = dinner;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public void setDinner(Dinner dinner) {
        this.dinner = dinner;
    }
}