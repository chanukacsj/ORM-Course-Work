package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Enrollment {
    @Id
    @Column(name = "eid")
    private String eid;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "program_id") // Matches "program_id" in Program table
    private Program program;

    private LocalDate date;
    private Double upfrontpayment;
    private Double remainingfee;
    private String comment;


}
