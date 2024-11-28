package org.example.dao.custom;

import org.example.dao.CrudDAO;
import org.example.entity.User;

import java.io.IOException;

public interface UserDAO extends CrudDAO<User> {
    boolean checkPassword(String username, String password) throws IOException;

    public User findUserById(String userId) throws Exception;

    String getRole(String username);
}
