package org.example.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO {
    boolean save(T DTO) throws IOException;

    boolean update(T DTO);

    boolean delete(String id);

    List<T> getAll();

    T searchById(String id);

    public String generateNewID() throws SQLException, ClassNotFoundException, IOException;

    String currentId() throws SQLException, ClassNotFoundException;


}
