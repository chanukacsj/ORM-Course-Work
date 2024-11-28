package org.example.bo.custom.impl;

import org.example.bo.custom.EnrollmentBo;
import org.example.dao.DAOFactory;
import org.example.dao.custom.EnrollmentDAO;
import org.example.dao.custom.ProgramDAO;
import org.example.dao.custom.StudentDAO;
import org.example.dto.EnrollmentDTO;
import org.example.entity.Enrollment;
import org.example.entity.Program;
import org.example.entity.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentBoImpl implements EnrollmentBo {

    StudentDAO studentDAO = (StudentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.STUDENT);
    ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PROGRAM);
    EnrollmentDAO enrollmentDAO = (EnrollmentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ENROLLMENT);

    @Override
    public boolean saveEnrollment(EnrollmentDTO dto) throws Exception {
        Student student = studentDAO.findStudentById(dto.getSid());
        Program program = programDAO.findCourseById(dto.getPid());


        if (student == null || program == null) {
            throw new Exception("Student or Course not found.");
        }

        Enrollment enrollment = new Enrollment(
                dto.getEid(),
                student,
                program,
                dto.getDate(),
                dto.getUpFrontPayment(),
                dto.getRemainingFee(),
                dto.getComment()
        );

        return enrollmentDAO.save(enrollment);
    }

    @Override
    public boolean updateEnrollment(EnrollmentDTO dto) throws Exception {
        Student student = studentDAO.findStudentById(dto.getSid());
        Program program = programDAO.findCourseById(dto.getPid());

        if (student == null || program == null) {
            throw new Exception("Student or Course not found.");
        }

        Enrollment enrollment = new Enrollment(
                dto.getEid(),
                student,
                program,
                dto.getDate(),
                dto.getUpFrontPayment(),
                dto.getRemainingFee(),
                dto.getComment()
        );

        return enrollmentDAO.update(enrollment);
    }

    @Override
    public boolean deleteEnrollment(String ID) throws Exception {
        return enrollmentDAO.delete(ID);
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollment() throws SQLException, ClassNotFoundException {
        List<Enrollment> enrollments = enrollmentDAO.getAll();
        List<EnrollmentDTO> dtos = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            String studentId = enrollment.getStudent() != null ? enrollment.getStudent().getId() : null;
            String studentName = enrollment.getStudent() != null ? enrollment.getStudent().getName() : null;
            String courseId = enrollment.getProgram() != null ? enrollment.getProgram().getId() : null;
            String courseName = enrollment.getProgram() != null ? enrollment.getProgram().getProgram_name() : null;
            dtos.add(new EnrollmentDTO(enrollment.getEid(),studentId,studentName,courseId,courseName,enrollment.getDate(),enrollment.getUpfrontpayment(),enrollment.getRemainingfee(),enrollment.getComment()));
        }
        return dtos;
    }

    @Override
    public String generateNewEnrollmentID() throws SQLException, ClassNotFoundException, IOException {
        return enrollmentDAO.generateNewID();
    }

    @Override
    public boolean EnrollmentIdExists(String enrollmentId) throws SQLException, ClassNotFoundException {
        return enrollmentDAO.IdExists(enrollmentId);
    }

    @Override
    public List<String> getAllEnrollmentIds() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean isStudentEnrolledInCourse(String studentId, String courseId) throws Exception {
        return enrollmentDAO.isStudentEnrolledInCourse(studentId, courseId);
    }

    @Override
    public Enrollment findEnrollmentById(String enrollmentId) throws Exception {
        return enrollmentDAO.findEnrollmentById(enrollmentId);
    }

    @Override
    public double getRemainingFeeByEnrollmentId(String enrollmentId) throws SQLException, ClassNotFoundException {
        return enrollmentDAO.getRemainingFeeByEnrollmentId(enrollmentId);
    }

    @Override
    public boolean updateRemainingFee(String enrollmentId, double newFee) throws SQLException, ClassNotFoundException {
        return enrollmentDAO.updateRemainingFee(enrollmentId, newFee);
    }

    @Override
    public int getEnrollmentCount() throws Exception {
        return enrollmentDAO.getEnrollmentCount();
    }

    @Override
    public List<String> getEnrollmentIds() throws SQLException, ClassNotFoundException {
        return enrollmentDAO.getAllEnrollmentIds();
    }

}
