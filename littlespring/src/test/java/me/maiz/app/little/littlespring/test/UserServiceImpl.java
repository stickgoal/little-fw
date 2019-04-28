package me.maiz.app.little.littlespring.test;

import me.maiz.app.little.littlespring.test.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    private String adminName;

    @Override
    public List<User> findAll() {
        System.out.println("service adminName : "+adminName);
        return userDAO.findAll();
    }
}
