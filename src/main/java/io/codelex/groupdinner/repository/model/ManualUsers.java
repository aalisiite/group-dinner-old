package io.codelex.groupdinner.repository.model;

import javax.persistence.*;

@Entity
@Table(name = "manual_users")
public class ManualUsers {
    @Id
    @GeneratedValue(generator = "user_seq_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    int id;
    @Column(name = "password", nullable = false)
    private String password;

    public ManualUsers(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
