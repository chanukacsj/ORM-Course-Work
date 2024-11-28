package org.example.dao.custom.Impl;

import org.example.config.SessionFactoryConfig;
import org.example.dao.custom.PaymentDAO;
import org.example.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public boolean save(Payment DTO) throws IOException {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(DTO);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Payment DTO) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(DTO);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String sql = "DELETE FROM payment WHERE pay_id = :id";
        NativeQuery<Payment> nativeQuery = session.createNativeQuery(sql);
        nativeQuery.setParameter("id",id);
        nativeQuery.executeUpdate();

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public List<Payment> getAll() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM payment");
        nativeQuery.addEntity(Payment.class);
        List<Payment> payment = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return payment;
    }

    @Override
    public Payment searchById(String id) {
        return null;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String lastID = (String) session.createQuery("SELECT pay.id FROM Payment pay ORDER BY pay.id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastID != null) {
                int id = Integer.parseInt(lastID.replace("PAY", "")) + 1;
                return "PAY" + String.format("%03d", id);
            } else {
                return "PAY001";
            }
        }
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Payment findPaymentById(String paymentId) throws Exception {
        return null;
    }
}
