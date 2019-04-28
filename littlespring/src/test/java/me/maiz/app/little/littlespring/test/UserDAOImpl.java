package me.maiz.app.little.littlespring.test;

import me.maiz.app.little.littlespring.test.model.User;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private String name;

    @Override
    public List<User> findAll() {
        System.out.println("属性注入："+name);
        return new ArrayList<>();
    }
}
