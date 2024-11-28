package org.example.dao.custom;

import org.example.dao.CrudDAO;
import org.example.entity.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDAO extends CrudDAO<Student> {
    List<String> getStudentID();

    String getNames(String id);

    public Student findStudentById(String studentId) throws Exception;

    public int getStudentCount() throws SQLException, ClassNotFoundException;


}
