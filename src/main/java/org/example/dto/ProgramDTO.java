package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProgramDTO {
    private String program_id;

    private String program_name;

    private String duration;

    private double program_fee;
}
