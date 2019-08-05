package me.maiz.app.little.orm.dao;

import me.maiz.app.little.orm.main.entity.User;
import me.maiz.app.little.orm.util.DBHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;


public class SessionTest {
    DBHelper dbHelper = new DBHelper();

    @Before
    public void setUp() {
        DBHelper.init("jdbc:mysql://localhost:3306/test", "root", "root123", "com.mysql.cj.jdbc.Driver");
    }

    @Test
    public void testSave() {
        dbHelper.remove("delete from test_user where user_id=?", 1);
        SessionFactory sessionFactory = new SessionFactory("/orm.properties");
        sessionFactory.configure();
        final Session session = sessionFactory.openSession();
        User user = new User();
        user.setUserId(1);
        user.setUsername("lucas");
        user.setPassword("123456");
        session.save(user);
    }

    @Test
    public void testQuery() {

        //清理
        Date date = null;
        try {
            date = DateUtils.parseDate("2019-01-01 00:00:00", "yyyy-MM-dd hh:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dbHelper.remove("delete from test_user where user_id=?", 1);
        dbHelper.add("insert into test_user values(?,?,?,?)", 1, "lucas", "123", date);
        //测试
        SessionFactory sessionFactory = new SessionFactory("/orm.properties");
        sessionFactory.configure();
        final Session session = sessionFactory.openSession();

        User user = session.get(User.class, 1);

        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("lucas", user.getUsername());
        assertEquals("123", user.getPassword());

        assertEquals(date, user.getRegisterDate());


    }


}