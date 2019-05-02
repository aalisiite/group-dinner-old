package io.codelex.groupdinner.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookRequest {
    
    private int id;
    private String email;
    private String name;

    @JsonCreator
    public FacebookRequest(@JsonProperty("id") int id,
                           @JsonProperty("email") String email,
                           @JsonProperty("name") String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}