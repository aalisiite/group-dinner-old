package io.codelex.groupdinner.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;


public class CreateDinnerRequest {

    @NotEmpty
    private String title;
    @NotNull
    private int maxGuests;
    @NotEmpty
    private String description;
    private String location;
    private LocalDateTime dateTime;

    @JsonCreator
    public CreateDinnerRequest(
            @JsonProperty("title") @NotEmpty String title,
            @JsonProperty("maxGuests") @NotNull Integer maxGuests,
            @JsonProperty("description") @NotEmpty String description,
            @JsonProperty("location") String location,
            @JsonProperty("dateTime") LocalDateTime dateTime) {
        this.title = title;
        this.maxGuests = maxGuests;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateDinnerRequest request = (CreateDinnerRequest) o;
        return maxGuests == request.maxGuests
                && title.equals(request.title)
                && description.equals(request.description)
                && Objects.equals(location, request.location)
                && Objects.equals(dateTime, request.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, maxGuests, description, location, dateTime);
    }
}