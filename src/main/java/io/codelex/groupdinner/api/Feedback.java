package io.codelex.groupdinner.api;

public class Feedback {

    private Long id;
    private Dinner dinner;
    private User provider;
    private User receiver;
    private boolean rating;

    public Feedback(Long id, Dinner dinner, User provider, User receiver, boolean rating) {
        this.id = id;
        this.dinner = dinner;
        this.provider = provider;
        this.receiver = receiver;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public void setDinner(Dinner dinner) {
        this.dinner = dinner;
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
