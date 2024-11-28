package org.example.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "programs")

public class Program {
    @Id
    @Column(name = "program_id")
    private String id;

    @Column(name = "program_name")
    private String program_name;

    @Column(name = "duration")
    private String duration;

    @Column(name = "course_fee")
    private double program_fee;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Enrollment> enrollmentList;
//    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Enrollment> enrollmentList;
    public void addEnrollment(Enrollment enrollment) {
        enrollmentList.add(enrollment);
        enrollment.setProgram(this);  // This ensures the enrollment knows about the course
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollmentList.remove(enrollment);
        enrollment.setProgram(null); // Break the relationship
    }


    public Program() {

    }
    public Program(String id, String program_name, String duration, double program_fee) {
        this.id = id;
        this.program_name = program_name;
        this.duration = duration;
        this.program_fee = program_fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getProgram_fee() {
        return program_fee;
    }

    public void setProgram_fee(double program_fee) {
        this.program_fee = program_fee;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id='" + id + '\'' +
                ", program_name='" + program_name + '\'' +
                ", duration='" + duration + '\'' +
                ", program_fee=" + program_fee +
                '}';
    }
}
