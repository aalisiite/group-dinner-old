package io.codelex.groupdinner.api;

import java.util.Objects;

public class Attendee {

    private Long id;
    private Dinner dinner;
    private User user;
    private boolean accepted;

    public Attendee(Long id, Dinner dinner, User user, boolean accepted) {
        this.id = id;
        this.dinner = dinner;
        this.user = user;
        this.accepted = accepted;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendee attendee = (Attendee) o;
        return id.equals(attendee.id)
                && dinner.equals(attendee.dinner)
                && user.equals(attendee.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dinner, user);
    }
}
