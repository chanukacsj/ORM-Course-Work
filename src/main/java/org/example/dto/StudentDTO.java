package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class StudentDTO {
    private String id;
    private String name;
    private String address;
    private String contact;
    private String dob;
    private String gen;
    private String regDate;

}
