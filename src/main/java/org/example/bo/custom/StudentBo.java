package org.example.bo.custom;

import org.example.bo.SuperBO;
import org.example.dto.StudentDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface StudentBo extends SuperBO {
    boolean save(StudentDTO studentDTO) throws Exception;

    boolean delete(String id) throws SQLException, ClassNotFoundException;

    boolean update(StudentDTO studentDto) throws Exception;

    List<StudentDTO> getAll() throws SQLException, ClassNotFoundException;

    List<String> getStudentID();

    public int getStudentCount() throws Exception;

    public String generateNewStudentID() throws SQLException, ClassNotFoundException, IOException;

    String getNames(String id);

    public int findStudentById(String studentId) throws Exception;


}
