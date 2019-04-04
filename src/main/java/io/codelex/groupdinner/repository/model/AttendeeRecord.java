package io.codelex.groupdinner.repository.model;

import javax.persistence.*;

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
}
