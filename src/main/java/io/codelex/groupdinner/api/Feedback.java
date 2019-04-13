package io.codelex.groupdinner.api;

public class Feedback {

    private Long id;
    private User provider;
    private User receiver;
    private boolean rating;

    public Feedback(Long id, User provider, User receiver, Boolean rating) {
        this.id = id;
        this.provider = provider;
        this.receiver = receiver;
        this.rating = rating;
    }

    public Feedback() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
