package me.maiz.app.little.littlespring.test2;

import me.maiz.app.little.littlespring.metadata.annotations.Autowired;
import me.maiz.app.little.littlespring.metadata.annotations.Service;
import me.maiz.app.little.littlespring.metadata.annotations.Value;
import me.maiz.app.little.littlespring.test2.model.User;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Value("admin")
    private String adminName;

    @Override
    public List<User> findAll() {
        System.out.println("service adminName : "+adminName);
        return userDAO.findAll();
    }
}
