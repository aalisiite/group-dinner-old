package io.codelex.groupdinner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;


public class CreateDinnerRequest {

    @NotEmpty
    private String title;
    @OneToMany
    @NotNull
    private User creator;
    @NotNull
    private int maxGuests;
    @NotEmpty
    private String description;
    private Location location;
    private LocalDateTime dateTime;

    @JsonCreator
    public CreateDinnerRequest(
            @JsonProperty("title") @NotEmpty String title,
            @JsonProperty("creator") @NotNull User creator,
            @JsonProperty("maxGuests") @NotNull int maxGuests,
            @JsonProperty("description") @NotEmpty String description,
            @JsonProperty("location") Location location,
            @JsonProperty("dateTime") LocalDateTime dateTime) {
        this.title = title;
        this.creator = creator;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
        CreateDinnerRequest that = (CreateDinnerRequest) o;
        return maxGuests == that.maxGuests &&
                title.equals(that.title) &&
                creator.equals(that.creator) &&
                description.equals(that.description) &&
                Objects.equals(location, that.location) &&
                Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, creator, maxGuests, description, location, dateTime);
    }
}
