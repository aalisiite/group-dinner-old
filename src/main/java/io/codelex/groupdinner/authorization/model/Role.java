package io.codelex.groupdinner.authorization.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    private Set<LoginDetails> detailsSet;

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LoginDetails> getDetailsSet() {
        return detailsSet;
    }

    public void setDetailsSet(Set<LoginDetails> detailsSet) {
        this.detailsSet = detailsSet;
    }
}
