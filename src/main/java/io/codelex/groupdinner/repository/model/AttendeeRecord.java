package io.codelex.groupdinner.repository.model;

import javax.persistence.*;
import java.util.Objects;

@Table
@Entity(name = "attendee")
public class AttendeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private DinnerRecord dinner;
    @ManyToOne
    private UserRecord user;
    private boolean status;

    public AttendeeRecord(DinnerRecord dinner, UserRecord user, boolean status) {
        this.dinner = dinner;
        this.user = user;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DinnerRecord getDinner() {
        return dinner;
    }

    public void setDinner(DinnerRecord dinner) {
        this.dinner = dinner;
    }

    public UserRecord getUser() {
        return user;
    }

    public void setUser(UserRecord user) {
        this.user = user;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendeeRecord that = (AttendeeRecord) o;
        return status == that.status &&
                id.equals(that.id) &&
                dinner.equals(that.dinner) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dinner, user, status);
    }
}
