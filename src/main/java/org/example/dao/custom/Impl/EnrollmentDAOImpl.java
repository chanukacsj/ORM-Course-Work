package org.example.dao.custom.Impl;

import org.example.config.SessionFactoryConfig;
import org.example.dao.custom.EnrollmentDAO;
import org.example.entity.Enrollment;
import org.example.entity.Program;
import org.example.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImpl implements EnrollmentDAO {


    @Override
    public boolean save(Enrollment DTO) throws IOException {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.save(DTO);

        Student student = DTO.getStudent();
        Program program = DTO.getProgram();
        if (student != null) {
            student.addEnrollment(DTO);
        }
        if (program != null) {
            program.addEnrollment(DTO);
        }

        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Enrollment DTO) {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Transaction tx = session.beginTransaction();
            session.update(DTO);
            tx.commit();
            return true;
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            Enrollment enrollment = session.get(Enrollment.class, id);
            if (enrollment != null) {
                Student student = enrollment.getStudent();
                Program program = enrollment.getProgram();

                if (student != null) {
                    student.removeEnrollment(enrollment);
                }
                if (program != null) {
                    program.removeEnrollment(enrollment);
                }

                session.delete(enrollment);
                tx.commit();
                return true;
            } else {
                tx.rollback();
                return false;
            }
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Enrollment> getAll() {
        List<Enrollment> all = new ArrayList<>();
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        all = session.createQuery("from Enrollment", Enrollment.class).list();
        transaction.commit();
        session.close();
        return all;
    }

    @Override
    public Enrollment searchById(String id) {
        return null;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String lastID = (String) session.createQuery("SELECT e.eid FROM Enrollment e ORDER BY e.eid DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastID != null) {
                int id = Integer.parseInt(lastID.replace("E", "")) + 1;
                return "E" + String.format("%03d", id);
            } else {
                return "E001";
            }
        }
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getAllEnrollmentIds() throws SQLException, ClassNotFoundException {
        List<String> enrollmentIds = new ArrayList<>();
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Query<String> query = session.createQuery("SELECT e.eid FROM Enrollment e", String.class);
            enrollmentIds = query.list();
        }
        return enrollmentIds;
    }

    @Override
    public boolean isStudentEnrolledInCourse(String studentId, String program_id) throws Exception {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String hql = "SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND e.program.id = :program_id";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("studentId", studentId)
                    .setParameter("program_id", program_id)
                    .uniqueResult();
            return count > 0; // true if the student is already enrolled
        }
    }

    @Override
    public Enrollment findEnrollmentById(String enrollmentId) throws Exception {
        Transaction transaction = null;
        Enrollment enrollment = null;

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<Enrollment> query = session.createQuery("FROM Enrollment e WHERE e.id = :id", Enrollment.class);
            query.setParameter("id", enrollmentId);
            enrollment = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }

        return enrollment;
    }

    @Override
    public double getRemainingFeeByEnrollmentId(String enrollmentId) throws SQLException, ClassNotFoundException {
        Transaction transaction = null;
        Double remainFee = null;

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<Enrollment> query = session.createQuery("FROM Enrollment e WHERE e.id = :id", Enrollment.class);
            query.setParameter("id", enrollmentId);
            Enrollment enrollment = query.uniqueResult();

            transaction.commit();


            if (enrollment != null) {
                remainFee = enrollment.getRemainingfee();
            } else {
                throw new Exception("Enrollment ID not found!");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            try {
                throw e;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return remainFee;
    }

    @Override
    public boolean updateRemainingFee(String enrollmentId, double newFee) throws SQLException, ClassNotFoundException {
        Transaction transaction = null;

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            transaction = session.beginTransaction();


            Query query = session.createQuery("UPDATE Enrollment e SET e.remainingfee = :newFee WHERE e.id = :id");
            query.setParameter("newFee", newFee);
            query.setParameter("id", enrollmentId);

            int result = query.executeUpdate();

            transaction.commit();

            return result > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public int getEnrollmentCount() throws SQLException, ClassNotFoundException {
        int enrollmentCount = 0;
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(e) FROM Enrollment e", Long.class);
            enrollmentCount = query.uniqueResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Failed to fetch enrollment count from the database", e);
        }
        return enrollmentCount;
    }

    @Override
    public boolean IdExists(String id) throws SQLException, ClassNotFoundException {

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String hql = "SELECT COUNT(e.eid) FROM Enrollment e WHERE e.eid = :id";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("id", id);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public List<String> getEnrollmentIds() {
        List<String> ids = new ArrayList<>();

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String sql = "SELECT eid FROM Enrollment";
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
    public String getStudentNameByEnrollmentId(String id) {
        String name = null;

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String sql = "select c.name from payment a " +
                    "inner join enrollment b on a.eid = b.eid " +
                    "inner join student c on b.student_id = c.student_id " +
                    "where a.eid = :id";

            NativeQuery<String> nativeQuery = session.createNativeQuery(sql);
            nativeQuery.setParameter("id", id);

            List<String> result = nativeQuery.getResultList();
            if (!result.isEmpty()) {
                name = result.get(0);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return name;
    }

}
