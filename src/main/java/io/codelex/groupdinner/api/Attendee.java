package io.codelex.groupdinner.api;

import java.util.Objects;

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

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendee attendee = (Attendee) o;
        return id.equals(attendee.id) &&
                dinner.equals(attendee.dinner) &&
                user.equals(attendee.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dinner, user);
    }
}
