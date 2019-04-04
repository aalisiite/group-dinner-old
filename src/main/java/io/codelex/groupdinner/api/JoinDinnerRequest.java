package io.codelex.groupdinner.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;

import javax.validation.constraints.NotNull;

public class JoinDinnerRequest {


    @NotNull
    private UserRecord user;
    
    @NotNull
    private DinnerRecord dinner;

    public JoinDinnerRequest(
            @JsonProperty("user") @NotNull UserRecord user,
            @JsonProperty("dinner") @NotNull DinnerRecord dinner) {
        this.user = user;
        this.dinner = dinner;
    }

    public UserRecord getUser() {
        return user;
    }

    public void setUser(UserRecord user) {
        this.user = user;
    }

    public DinnerRecord getDinner() {
        return dinner;
    }

    public void setDinner(DinnerRecord dinner) {
        this.dinner = dinner;
    }
}