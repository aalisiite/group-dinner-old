package io.codelex.groupdinner.api;

import io.codelex.groupdinner.repository.model.UserRecord;

//@Table
//@Entity
public class Feedback {

    private User provider;
    private User receiver;
    private boolean rating;

    public Feedback(User provider, User receiver, boolean rating) {
        this.provider = provider;
        this.receiver = receiver;
        this.rating = rating;
    }
}
