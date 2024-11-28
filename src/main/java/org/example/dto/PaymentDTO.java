package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class PaymentDTO {
    private String pay_id;
    private String pay_date;
    private double pay_amount;
    private String eid;


}
