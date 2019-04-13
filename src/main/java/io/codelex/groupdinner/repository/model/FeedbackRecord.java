package io.codelex.groupdinner.repository.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "feedbacks")
public class FeedbackRecord {

    @Id
    @GeneratedValue(generator = "feedback_seq_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "feedback_seq_generator", sequenceName = "feedback_seq", allocationSize = 1)
    private Long id;
    @ManyToOne
    private UserRecord provider;
    @ManyToOne
    private UserRecord receiver;
    private Boolean rating;

    public FeedbackRecord(UserRecord provider, UserRecord receiver, Boolean rating) {
        this.provider = provider;
        this.receiver = receiver;
        this.rating = rating;
    }

    public FeedbackRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRecord getProvider() {
        return provider;
    }

    public void setProvider(UserRecord provider) {
        this.provider = provider;
    }

    public UserRecord getReceiver() {
        return receiver;
    }

    public void setReceiver(UserRecord receiver) {
        this.receiver = receiver;
    }

    public Boolean isRating() {
        return rating;
    }

    public void setRating(Boolean rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackRecord that = (FeedbackRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}