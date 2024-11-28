package org.example.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class EnrollmentDTO {
    private String eid;
    private String sid;
    private String StudentName;
    private String pid;
    private String ProgramName;
    private LocalDate date;
    private Double upFrontPayment;
    private Double remainingFee;
    private String comment;
}
