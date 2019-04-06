package io.codelex.groupdinner.api;

public class Attendee {

    private Long id;
    private Dinner dinner;
    private User user;
    private boolean isAccepted;

    public Attendee(Long id, Dinner dinner, User user, boolean isAccepted) {
        this.id = id;
        this.dinner = dinner;
        this.user = user;
        this.isAccepted = isAccepted;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsAccepted () {
        return isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
}
