package io.codelex.groupdinner.repository.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "attendee")
public class AttendeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private DinnerRecord dinner;
    @ManyToOne
    private UserRecord user;
    private boolean isAccepted;

    public AttendeeRecord(DinnerRecord dinner, UserRecord user, boolean isAccepted) {
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

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted (boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendeeRecord that = (AttendeeRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
