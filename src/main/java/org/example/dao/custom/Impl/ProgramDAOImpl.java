package org.example.dao.custom.Impl;

import org.example.config.SessionFactoryConfig;
import org.example.dao.custom.ProgramDAO;
import org.example.entity.Program;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramDAOImpl implements ProgramDAO {

    @Override
    public boolean save(Program DTO) throws IOException {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(DTO);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Program DTO) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(DTO);
        transaction.commit();
        session.close();
        return true;

    }

    @Override
    public boolean delete(String id) {
//        Session session = SessionFactoryConfig.getInstance().getSession();
//        Transaction transaction = session.beginTransaction();
//
//        String sql = "DELETE FROM programs WHERE program_id = :id";
//        NativeQuery<Program> nativeQuery = session.createNativeQuery(sql);
//        nativeQuery.setParameter("id",id);
//        nativeQuery.executeUpdate();
//
//        transaction.commit();
//        session.close();
//        return true;

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String deleteEnrollmentsSql = "DELETE FROM enrollment WHERE program_id = :id";
            NativeQuery<?> deleteEnrollmentsQuery = session.createNativeQuery(deleteEnrollmentsSql);
            deleteEnrollmentsQuery.setParameter("id", id);
            deleteEnrollmentsQuery.executeUpdate();

            String deleteProgramSql = "DELETE FROM programs WHERE program_id = :id";
            NativeQuery<?> deleteProgramQuery = session.createNativeQuery(deleteProgramSql);
            deleteProgramQuery.setParameter("id", id);
            deleteProgramQuery.executeUpdate();

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }


    @Override
    public List<Program> getAll() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM programs");
        nativeQuery.addEntity(Program.class);
        List<Program> programs = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return programs;
    }

    @Override
    public Program searchById(String id) {
        return null;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String lastID = (String) session.createQuery("SELECT p.id FROM Program p ORDER BY p.id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastID != null) {
                int id = Integer.parseInt(lastID.replace("CA1", "")) + 1;
                return "CA1" + String.format("%03d", id);
            } else {
                return "CA1001";
            }
        }
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getProgramID() {
        List<String> ids = new ArrayList<>();

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String sql = "SELECT program_id FROM programs";
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
    public String getProgramNames(String id) {
        String programName = null;

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String sql = "SELECT program_name FROM programs WHERE program_id = :id";
            NativeQuery<String> nativeQuery = session.createNativeQuery(sql);

            nativeQuery.setParameter("id", id);

            programName = nativeQuery.getSingleResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return programName;
    }

    @Override
    public Program findCourseById(String ProgramId) throws Exception {
        Transaction transaction = null;
        Program program = null;

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<Program> query = session.createQuery("FROM Program p WHERE p.id = :id", Program.class);
            query.setParameter("id", ProgramId);
            program = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }

        return program;
    }

    @Override
    public double getProgramFee(String id) {
        double programFee = 0.0;

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String sql = "SELECT course_fee FROM programs WHERE program_id = :id";
            NativeQuery<Double> nativeQuery = session.createNativeQuery(sql, Double.class);

            nativeQuery.setParameter("id", id);

            programFee = nativeQuery.getSingleResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return programFee;
    }
}
