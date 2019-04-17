package io.codelex.groupdinner.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LeaveFeedbackRequest {

    @NotEmpty
    private String receiver;
    @NotNull
    private Boolean rating;

    public LeaveFeedbackRequest(
            @JsonProperty("receiver") @NotEmpty String receiver,
            @JsonProperty("rating") @NotNull Boolean rating) {
        this.receiver = receiver;
        this.rating = rating;
    }

    public LeaveFeedbackRequest() {
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getFeedback() {
        return rating;
    }

    public void setFeedback(Boolean rating) {
        this.rating = rating;
    }
}
