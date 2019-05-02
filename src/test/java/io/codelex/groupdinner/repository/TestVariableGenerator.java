package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.MapDBRecordToApiCompatible;
import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.Feedback;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.CreateDinnerRequest;
import io.codelex.groupdinner.api.request.LeaveFeedbackRequest;
import io.codelex.groupdinner.api.request.RegistrationRequest;
import io.codelex.groupdinner.api.request.SignInRequest;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;

import java.time.LocalDateTime;

public class TestVariableGenerator {

    private final MapDBRecordToApiCompatible toApiCompatible = new MapDBRecordToApiCompatible();

    public UserRecord createUserRecord1() {
        return new UserRecord(
                "Janis Berzins",
                "berzins@gmai.com",
                "password"
        );
    }

    public UserRecord createUserRecord2() {
        return new UserRecord(
                "Anna Kalniņa",
                "a.kalnina@gmail.com",
                "password"
        );
    }

    public LocalDateTime createDateTime() {
        return LocalDateTime.of(2019, 1, 1, 0, 0);
    }

    public AttendeeRecord createAcceptedAttendeeRecord(DinnerRecord dinnerRecord, UserRecord userRecord) {
        return new AttendeeRecord(
                dinnerRecord,
                userRecord,
                true
        );
    }


    public AttendeeRecord createPendingAttendeeRecord(DinnerRecord dinnerRecord, UserRecord userRecord) {
        return new AttendeeRecord(
                dinnerRecord,
                userRecord,
                false
        );
    }

    public DinnerRecord createDinnerRecord(UserRecord userRecord, String location, LocalDateTime localDateTime) {
        return new DinnerRecord(
                "This is a title",
                userRecord,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    public FeedbackRecord createGoodFeedbackRecord(DinnerRecord dinnerRecord, UserRecord userRecord1, UserRecord userRecord2) {
        return new FeedbackRecord(
                dinnerRecord,
                userRecord1,
                userRecord2,
                true
        );
    }

    public FeedbackRecord createBadFeedbackRecord(DinnerRecord dinnerRecord, UserRecord userRecord1, UserRecord userRecord2) {
        return new FeedbackRecord(
                dinnerRecord,
                userRecord1,
                userRecord2,
                false
        );
    }

    public CreateDinnerRequest createDinnerRequest(String location, LocalDateTime localDateTime) {
        return new CreateDinnerRequest(
                "This is a title",
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    public LeaveFeedbackRequest createLeaveFeedbackRequest(User user) {
        return new LeaveFeedbackRequest(
                user.getEmail(),
                true
        );
    }

    public RegistrationRequest createRegistrationRequest() {
        return new RegistrationRequest(
                "Janis Berzins",
                "berzins@gmai.com",
                "password"
        );
    }

    public SignInRequest createSignInRequest() {
        return new SignInRequest(
                "berzins@gmai.com",
                "password"
        );
    }

    public String createLocation() {
        return "Jurmalas Gatve 76";
    }


    public User getUserFromUserRecord(Long id, UserRecord userRecord) {
        userRecord.setId(id);
        return toApiCompatible.apply(userRecord);
    }

    public Dinner getDinnerFromDinnerRecord(Long id, DinnerRecord dinnerRecord) {
        dinnerRecord.setId(id);
        return toApiCompatible.apply(dinnerRecord);
    }

    public Feedback getFeedbackFromFeedbackRecord(Long id, FeedbackRecord feedbackRecord) {
        feedbackRecord.setId(id);
        return toApiCompatible.apply(feedbackRecord);
    }

    public Attendee getAttendeeFromAttendeeRecord(Long id, AttendeeRecord attendeeRecord) {
        attendeeRecord.setId(id);
        return toApiCompatible.apply(attendeeRecord);
    }


}
