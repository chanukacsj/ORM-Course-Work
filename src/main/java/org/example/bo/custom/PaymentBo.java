package org.example.bo.custom;

import org.example.bo.SuperBO;
import org.example.dto.PaymentDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PaymentBo extends SuperBO {

    boolean save(PaymentDTO paymentDTO) throws Exception;

    boolean delete(String id) throws SQLException, ClassNotFoundException;

    boolean update(PaymentDTO paymentDTO) throws Exception;

    List<PaymentDTO> getAll() throws SQLException, ClassNotFoundException;

    public String generateNewPaymentID() throws SQLException, ClassNotFoundException, IOException;

}
