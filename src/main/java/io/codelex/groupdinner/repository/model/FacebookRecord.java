package io.codelex.groupdinner.repository.model;

import javax.persistence.*;

@Entity
@Table(name = "fb_users")
public class FacebookRecord {
    @Id
    @GeneratedValue(generator = "user_seq_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    int id;
    @Column(name = "access_token", nullable = false)
    private Long accessToken;
    @Column(name = "picture")
    private String picture;

    public FacebookRecord(Long accessToken, String picture) {
        this.accessToken = accessToken;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Long accessToken) {
        this.accessToken = accessToken;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
