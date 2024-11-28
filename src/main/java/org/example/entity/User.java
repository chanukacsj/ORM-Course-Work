package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "jobRole")
    private String jobRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Student> studentList = new ArrayList<>();

    public User(String id, String username, String password, String jobRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.jobRole = jobRole;
    }
}
