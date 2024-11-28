package org.example.bo.custom.impl;

import org.example.bo.custom.ProgramBo;
import org.example.dao.DAOFactory;
import org.example.dao.custom.ProgramDAO;
import org.example.dto.ProgramDTO;
import org.example.entity.Program;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramBoImpl implements ProgramBo {
    ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PROGRAM);

    @Override
    public boolean save(ProgramDTO programDTO) throws IOException {
        return programDAO.save(new Program(programDTO.getProgram_id(),programDTO.getProgram_name(),programDTO.getDuration(),programDTO.getProgram_fee()));
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return programDAO.delete(id);
    }

    @Override
    public boolean update(ProgramDTO programDTO) throws SQLException, ClassNotFoundException {
        return programDAO.update(new Program(programDTO.getProgram_id(),programDTO.getProgram_name(),programDTO.getDuration(),programDTO.getProgram_fee()));
    }

    @Override
    public List<ProgramDTO> getAll() throws SQLException, ClassNotFoundException {
        List<ProgramDTO> allPrograms= new ArrayList<>();
        List<Program> all = programDAO.getAll();
        for (Program program : all) {
            allPrograms.add(new ProgramDTO(program.getId(),program.getProgram_name(),program.getDuration(),program.getProgram_fee()));
        }
        return allPrograms;
    }

    @Override
    public List<String> getProgramIds() {
        return programDAO.getProgramID();
    }

    @Override
    public String getProgramName(String id) {
        return programDAO.getProgramNames(id);
    }

    @Override
    public Program findProgramById(String programId) throws Exception {
        return programDAO.findCourseById(programId);
    }

    @Override
    public String generateNewProgramID() throws SQLException, ClassNotFoundException, IOException {
        return programDAO.generateNewID();
    }
}
