package org.example.bo.custom;

import org.example.bo.SuperBO;
import org.example.dto.UserDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface UserBo extends SuperBO {
    List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException, IOException;

    boolean save(UserDTO dto) throws SQLException, ClassNotFoundException, IOException;

    boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException, IOException;

    boolean deleteUser(String id) throws SQLException, ClassNotFoundException, IOException;

    String generateNewUserID() throws SQLException, ClassNotFoundException, IOException;

    String getRole(String username) throws SQLException, ClassNotFoundException, IOException;

    boolean checkPassword(String username, String password) throws IOException;
}
