package org.example.bo.custom.impl;

import org.example.bo.custom.PaymentBo;
import org.example.dao.DAOFactory;
import org.example.dao.custom.EnrollmentDAO;
import org.example.dao.custom.PaymentDAO;
import org.example.dto.PaymentDTO;
import org.example.entity.Payment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentBoImpl implements PaymentBo {
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);
    EnrollmentDAO enrollmentDAO = (EnrollmentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ENROLLMENT);

    @Override
    public boolean save(PaymentDTO paymentDTO) throws Exception {

        return paymentDAO.save(new Payment(paymentDTO.getPay_id(), paymentDTO.getPay_date(), paymentDTO.getPay_amount(),paymentDTO.getEid()));
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return paymentDAO.delete(id);
    }

    @Override
    public boolean update(PaymentDTO paymentDTO) throws Exception {

        return paymentDAO.update(new Payment(paymentDTO.getPay_id(), paymentDTO.getPay_date(), paymentDTO.getPay_amount(),paymentDTO.getEid()));
    }

    @Override
    public List<PaymentDTO> getAll() throws SQLException, ClassNotFoundException {
        List<PaymentDTO> allPay = new ArrayList<>();
        List<Payment> all = paymentDAO.getAll();
        for (Payment payment : all) {
            allPay.add(new PaymentDTO(payment.getPay_id(), payment.getPay_date(), payment.getPay_amount(),payment.getEnrollmentId()));
        }
        return allPay;
    }

    @Override
    public String generateNewPaymentID() throws SQLException, ClassNotFoundException, IOException {
        return paymentDAO.generateNewID();
    }
}