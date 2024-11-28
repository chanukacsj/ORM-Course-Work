package org.example.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramTM {
    private String program_id;

    private String program_name;

    private String duration;

    private double program_fee;
}
