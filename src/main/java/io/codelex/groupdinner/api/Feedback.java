package io.codelex.groupdinner.api;

import io.codelex.groupdinner.repository.model.User;

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

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isRating() {
        return rating;
    }

    public void setRating(boolean rating) {
        this.rating = rating;
    }
}
