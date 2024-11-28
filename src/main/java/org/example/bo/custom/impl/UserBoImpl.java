package org.example.bo.custom.impl;

import org.example.bo.custom.UserBo;
import org.example.dao.DAOFactory;
import org.example.dao.custom.UserDAO;
import org.example.dto.UserDTO;
import org.example.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBoImpl implements UserBo {
    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USER);
    @Override
    public List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException, IOException {
        List<UserDTO> allUsers= new ArrayList<>();
        List<User> all = userDAO.getAll();
        for (User user : all) {
            allUsers.add(new UserDTO(user.getId(),user.getUsername(),user.getPassword(),user.getJobRole()));
        }
        return allUsers;
    }

    @Override
    public boolean save(UserDTO dto) throws SQLException, ClassNotFoundException, IOException {
        return userDAO.save(new User(dto.getUserId(),dto.getUserName(),dto.getPassword(),dto.getRole()));
    }

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException, IOException {
        return userDAO.update(new User(dto.getUserId(),dto.getUserName(),dto.getPassword(),dto.getRole()));
    }

    @Override
    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException, IOException {
        return userDAO.delete(id);
    }

    @Override
    public String generateNewUserID() throws SQLException, ClassNotFoundException, IOException {
        return userDAO.generateNewID();
    }

    @Override
    public String getRole(String username) throws SQLException, ClassNotFoundException, IOException {
        return userDAO.getRole(username);
    }

    @Override
    public boolean checkPassword(String username, String password) throws IOException {
        return userDAO.checkPassword(username,password);
    }
}
