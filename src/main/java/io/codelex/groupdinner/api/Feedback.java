package io.codelex.groupdinner.api;

import io.codelex.groupdinner.repository.model.UserRecord;

//@Table
//@Entity
public class Feedback {
    
    private UserRecord provider;
    private UserRecord receiver;
    private boolean rating;

    public Feedback(UserRecord provider, UserRecord receiver, boolean rating) {
        this.provider = provider;
        this.receiver = receiver;
        this.rating = rating;
    }

    public UserRecord getProvider() {
        return provider;
    }

    public void setProvider(UserRecord provider) {
        this.provider = provider;
    }

    public UserRecord getReceiver() {
        return receiver;
    }

    public void setReceiver(UserRecord receiver) {
        this.receiver = receiver;
    }

    public boolean isRating() {
        return rating;
    }

    public void setRating(boolean rating) {
        this.rating = rating;
    }
}
