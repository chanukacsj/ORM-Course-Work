package org.example.dao.custom;

import org.example.dao.CrudDAO;
import org.example.entity.Enrollment;

import java.sql.SQLException;
import java.util.List;

public interface EnrollmentDAO extends CrudDAO<Enrollment> {
    public List<String> getAllEnrollmentIds() throws SQLException, ClassNotFoundException;
    public boolean isStudentEnrolledInCourse(String studentId, String courseId) throws Exception;
    public Enrollment findEnrollmentById(String enrollmentId) throws Exception;
    public double getRemainingFeeByEnrollmentId(String enrollmentId) throws SQLException, ClassNotFoundException;
    public boolean updateRemainingFee(String enrollmentId, double newFee) throws SQLException, ClassNotFoundException;

    public int getEnrollmentCount() throws SQLException, ClassNotFoundException;

    public boolean IdExists(String id) throws SQLException, ClassNotFoundException;
    List<String> getEnrollmentIds();

    String getStudentNameByEnrollmentId(String id);
}
