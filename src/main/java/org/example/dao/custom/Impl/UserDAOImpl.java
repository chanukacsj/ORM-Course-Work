package org.example.dao.custom.Impl;

import org.example.config.SessionFactoryConfig;
import org.example.dao.custom.UserDAO;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean save(User DTO) throws IOException {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(DTO);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(User DTO) {
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

        String sql = "DELETE FROM user WHERE user_id = :id";
        NativeQuery<User> nativeQuery = session.createNativeQuery(sql);
        nativeQuery.setParameter("id", id);
        nativeQuery.executeUpdate();

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public List<User> getAll() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM user");
        nativeQuery.addEntity(User.class);
        List<User> users = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return users;
    }

    @Override
    public User searchById(String id) {
        return null;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String lastID = (String) session.createQuery("SELECT u.id FROM User u ORDER BY u.id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastID != null) {
                int id = Integer.parseInt(lastID.replace("U", "")) + 1;
                return "U" + String.format("%03d", id);
            } else {
                return "U001";
            }
        }
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean checkPassword(String username, String password) throws IOException {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        NativeQuery<String> nativeQuery = session.createNativeQuery("SELECT password FROM user WHERE username = :username");
        nativeQuery.setParameter("username", username);

        String pass = nativeQuery.uniqueResult();

        transaction.commit();
        session.close();

        if (password.equalsIgnoreCase(pass)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User findUserById(String userId) throws Exception {
        Transaction transaction = null;
        User user = null;

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<User> query = session.createQuery("FROM User u WHERE u.id = :userId", User.class);
            query.setParameter("user_id", userId);
            user = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }

        return user;
    }

    @Override
    public String getRole(String username) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        NativeQuery<String> nativeQuery = session.createNativeQuery("SELECT jobRole FROM user WHERE username = :username");
        nativeQuery.setParameter("username", username);

        String role = nativeQuery.uniqueResult();

        transaction.commit();
        session.close();
        System.out.println(role);
        return role;
    }
}

