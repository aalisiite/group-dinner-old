package io.codelex.groupdinner.repository.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "dinners")
public class DinnerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private UserRecord creator;
    private int maxGuests;
    private int currentGuests;
    private String description;
    private String location;
    private LocalDateTime dateTime;

    public DinnerRecord() {
    }

    public DinnerRecord(Long id, String title, UserRecord creator, int maxGuests, String description, String location, LocalDateTime dateTime) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.maxGuests = maxGuests;
        this.currentGuests = 1;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
    }

    public boolean shouldAcceptRequest() {
        return maxGuests > currentGuests;
    }

    public void incrementCurrentGuests() {
        currentGuests++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserRecord getCreator() {
        return creator;
    }

    public void setCreator(UserRecord creator) {
        this.creator = creator;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public int getCurrentGuests() {
        return currentGuests;
    }

    public void setCurrentGuests(int currentGuests) {
        this.currentGuests = currentGuests;
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
        DinnerRecord that = (DinnerRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}