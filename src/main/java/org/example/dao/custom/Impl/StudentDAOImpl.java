package org.example.dao.custom.Impl;

import org.example.config.SessionFactoryConfig;
import org.example.dao.custom.StudentDAO;
import org.example.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {


    @Override
    public boolean save(Student DTO) throws IOException {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(DTO);
        transaction.commit();
        session.close();
        return true;

    }

    @Override
    public boolean update(Student DTO) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(DTO);
        transaction.commit();
        session.close();
        return false;
    }

    @Override
    public boolean delete(String id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            String sql2 = "DELETE FROM payment WHERE eid IN (SELECT eid FROM enrollment WHERE student_id = :id)";
            NativeQuery paymentQuery = session.createNativeQuery(sql2);
            paymentQuery.setParameter("id", id);
            paymentQuery.executeUpdate();

            String sql1 = "DELETE FROM enrollment WHERE student_id = :id";
            NativeQuery enrollmentQuery = session.createNativeQuery(sql1);
            enrollmentQuery.setParameter("id", id);
            enrollmentQuery.executeUpdate();

            String sql = "DELETE FROM student WHERE student_id = :id";
            NativeQuery studentQuery = session.createNativeQuery(sql);
            studentQuery.setParameter("id", id);
            studentQuery.executeUpdate();


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

        return true;
    }


    @Override
    public List<Student> getAll() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM student");
        nativeQuery.addEntity(Student.class);
        List<Student> students = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return students;
    }

    @Override
    public Student searchById(String id) {
        return null;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String lastID = (String) session.createQuery("SELECT s.id FROM Student s ORDER BY s.id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastID != null) {
                int id = Integer.parseInt(lastID.replace("S", "")) + 1;
                return "S" + String.format("%03d", id);
            } else {
                return "S001";
            }
        }
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getStudentID() {
        List<String> ids = new ArrayList<>();

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String sql = "SELECT student_id FROM student";
            NativeQuery<String> nativeQuery = session.createNativeQuery(sql);

            ids = nativeQuery.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return ids;
    }

    @Override
    public String getNames(String id) {
        String studentName = null;

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String sql = "SELECT name FROM student WHERE student_id = :id";
            NativeQuery<String> nativeQuery = session.createNativeQuery(sql);

            nativeQuery.setParameter("id", id);

            studentName = nativeQuery.getSingleResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return studentName;
    }

    @Override
    public Student findStudentById(String studentId) throws Exception {
        Transaction transaction = null;
        Student student = null;

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<Student> query = session.createQuery("FROM Student s WHERE s.id = :id", Student.class);
            query.setParameter("id", studentId);
            student = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }

        return student;
    }

    @Override
    public int getStudentCount() throws SQLException, ClassNotFoundException {
        int studentCount = 0;
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(s) FROM Student s", Long.class);
            studentCount = query.uniqueResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Failed to fetch student count from the database", e);
        }
        return studentCount;
    }


}
