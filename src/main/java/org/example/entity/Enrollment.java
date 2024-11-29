package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @Column(name = "eid")
    private String eid;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Payment> paymentList = new ArrayList<>();


    private LocalDate date;
    private Double upfrontpayment;
    private Double remainingfee;
    private String comment;

    public Enrollment(String eid, Student student, Program program, LocalDate date, Double upfrontpayment, Double remainingfee, String comment) {
        this.eid = eid;
        this.student = student;
        this.program = program;
        this.date = date;
        this.upfrontpayment = upfrontpayment;
        this.remainingfee = remainingfee;
        this.comment = comment;
    }

    public Enrollment() {
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getUpfrontpayment() {
        return upfrontpayment;
    }

    public void setUpfrontpayment(Double upfrontpayment) {
        this.upfrontpayment = upfrontpayment;
    }

    public Double getRemainingfee() {
        return remainingfee;
    }

    public void setRemainingfee(Double remainingfee) {
        this.remainingfee = remainingfee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "eid='" + eid + '\'' +
                ", student=" + student +
                ", program=" + program +
                ", paymentList=" + paymentList +
                ", date=" + date +
                ", upfrontpayment=" + upfrontpayment +
                ", remainingfee=" + remainingfee +
                ", comment='" + comment + '\'' +
                '}';
    }
}
