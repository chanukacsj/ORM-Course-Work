package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "pay_id")
    private String pay_id;

    @Column(name = "pay_date")
    private String pay_date;

    @Column(name = "pay_amount")
    private double pay_amount;

    @ManyToOne
    @JoinColumn(name = "eid")
    private Enrollment enrollment;



}
