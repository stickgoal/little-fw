package me.maiz.app.little.littlespring.test;

import me.maiz.app.little.littlespring.test.model.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();

}
