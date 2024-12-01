package org.example.bo.custom;

import org.example.bo.SuperBO;
import org.example.dto.ProgramDTO;
import org.example.entity.Program;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ProgramBo extends SuperBO {
    boolean save(ProgramDTO programDTO) throws IOException;

    boolean delete(String id) throws SQLException, ClassNotFoundException;

    boolean update(ProgramDTO programDTO) throws SQLException, ClassNotFoundException;

    List<ProgramDTO> getAll() throws SQLException, ClassNotFoundException;

    List<String> getProgramIds();

    String getProgramName(String id);
    public Program findProgramById(String programId) throws Exception;

    public String generateNewProgramID() throws SQLException, ClassNotFoundException, IOException;

    double getProgramFee(String id);
}
