package io.codelex.groupdinner.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class LeaveFeedbackRequest {

    @NotNull
    private User receiver;
    @NotNull
    private Boolean rating;

    public LeaveFeedbackRequest(
            @JsonProperty("receiver") @NotNull User receiver,
            @JsonProperty("rating") @NotNull Boolean rating) {
        this.receiver = receiver;
        this.rating = rating;
    }

    public LeaveFeedbackRequest() {
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Boolean getFeedback() {
        return rating;
    }

    public void setFeedback(Boolean rating) {
        this.rating = rating;
    }
}
