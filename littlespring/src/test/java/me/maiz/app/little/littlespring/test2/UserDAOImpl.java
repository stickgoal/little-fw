package me.maiz.app.little.littlespring.test2;

import me.maiz.app.little.littlespring.metadata.annotations.Repository;
import me.maiz.app.little.littlespring.metadata.annotations.Service;
import me.maiz.app.little.littlespring.metadata.annotations.Value;
import me.maiz.app.little.littlespring.test2.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    @Value("luas")
    private String name;

    @Override
    public List<User> findAll() {
        System.out.println("属性注入："+name);
        return new ArrayList<>();
    }
}
