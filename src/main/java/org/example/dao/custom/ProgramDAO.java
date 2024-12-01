package org.example.dao.custom;

import org.example.dao.CrudDAO;
import org.example.entity.Program;

import java.util.List;

public interface ProgramDAO extends CrudDAO<Program> {
    List<String> getProgramID();
    String getProgramNames(String id);
    public Program findCourseById(String ProgramId) throws Exception;


    double getProgramFee(String id);
}
