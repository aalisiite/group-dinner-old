package io.codelex.groupdinner.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaveFeedbackRequest that = (LeaveFeedbackRequest) o;
        return receiver.equals(that.receiver)
                && rating.equals(that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiver, rating);
    }
}
