package org.example.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTM {
    private String pay_id;
    private String pay_date;
    private double pay_amount;
    private String eid;


}
