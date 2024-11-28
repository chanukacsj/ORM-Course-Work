package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "student")

public class Student {
    @Id
    @Column(name = "student_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Column(name = "dob")
    private String dob;

    @Column(name = "gender")
    private String gen;

    @Column(name = "register_date")
    private String regDate;

    @ManyToOne
    @JoinColumn(name = "user_id") // This should match the "user_id" column in the User table
    private User user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Enrollment> enrollmentList = new ArrayList<>();


    public void addEnrollment(Enrollment enrollment) {
        enrollmentList.add(enrollment);
        enrollment.setStudent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollmentList.remove(enrollment);
        enrollment.setStudent(null);
    }

    public Student(String id, String name, String address, String contact, String dob, String gen,String regDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.dob = dob;
        this.gen = gen;
        this.regDate = regDate;

    }
}
