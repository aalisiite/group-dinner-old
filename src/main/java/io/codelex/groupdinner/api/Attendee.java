package io.codelex.groupdinner.api;

public class Attendee {

    private Long id;
    private Dinner dinner;
    private User user;
    private boolean status;

    public Attendee(Long id, Dinner dinner, User user, boolean status) {
        this.id = id;
        this.dinner = dinner;
        this.user = user;
        this.status = status;
    }

}
