package org.example.bo.custom.impl;

import org.example.bo.custom.StudentBo;
import org.example.dao.DAOFactory;
import org.example.dao.custom.StudentDAO;
import org.example.dao.custom.UserDAO;
import org.example.dto.StudentDTO;
import org.example.entity.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentBoImpl implements StudentBo {

StudentDAO studentDAO = (StudentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.STUDENT);
UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USER);
    @Override
    public boolean save(StudentDTO studentDTO) throws Exception {
       return studentDAO.save(new Student(studentDTO.getId(),studentDTO.getName(),studentDTO.getAddress(),studentDTO.getContact(),studentDTO.getDob(),studentDTO.getGen(),studentDTO.getRegDate()));
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return studentDAO.delete(id);
    }

    @Override
    public boolean update(StudentDTO studentDTO) throws Exception {
        return studentDAO.update(new Student(studentDTO.getId(),studentDTO.getName(),studentDTO.getAddress(),studentDTO.getContact(),studentDTO.getDob(),studentDTO.getGen(),studentDTO.getRegDate()));
    }

    @Override
    public List<StudentDTO> getAll() throws SQLException, ClassNotFoundException {
        List<StudentDTO> allStudents= new ArrayList<>();
        List<Student> all = studentDAO.getAll();
        for (Student student : all) {
            allStudents.add(new StudentDTO(student.getId(), student.getName(), student.getAddress(),student.getContact(), student.getDob(),student.getGen(),student.getRegDate()));
        }
        return allStudents;
    }

    @Override
    public List<String> getStudentID() {
        return studentDAO.getStudentID();
    }

    @Override
    public int getStudentCount() throws Exception {
        return studentDAO.getStudentCount();
    }

    @Override
    public String generateNewStudentID() throws SQLException, ClassNotFoundException, IOException {
        return studentDAO.generateNewID();
    }

    @Override
    public String getNames(String id) {
        return studentDAO.getNames(id);
    }

    @Override
    public int findStudentById(String studentId) throws Exception {
        return studentDAO.getStudentCount();
    }

}
