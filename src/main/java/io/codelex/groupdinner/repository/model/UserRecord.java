package io.codelex.groupdinner.repository.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users_data")
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class UserRecord {

    @Id
    @GeneratedValue(generator = "user_seq_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email")
    private String email;

    public UserRecord() {
    }

    public UserRecord(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecord that = (UserRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}