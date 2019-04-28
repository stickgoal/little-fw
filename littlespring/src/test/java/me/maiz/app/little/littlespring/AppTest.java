package me.maiz.app.little.littlespring;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import me.maiz.app.little.littlespring.ioc.ClassPathXmlApplicationContext;
import me.maiz.app.little.littlespring.metadata.XmlConfigReader;
import me.maiz.app.little.littlespring.metadata.model.BeanDefinition;
import me.maiz.app.little.littlespring.test.UserService;
import me.maiz.app.little.littlespring.test.UserServiceImpl;
import org.junit.Test;

import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }


    @Test
    public void read() {
        try {

            Map<String,BeanDefinition> beanDefinitions = new XmlConfigReader().parse("applicationContext.test.xml");
            System.out.println(beanDefinitions);
            assertNotNull(beanDefinitions);
            assertTrue(beanDefinitions.size()==2);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void build() {
        try {

            final ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.test.xml");
            assertNotNull(classPathXmlApplicationContext.describe());
            System.out.println(classPathXmlApplicationContext.describe());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test(){
        System.out.println(UserServiceImpl.class.isAssignableFrom(UserService.class));
        System.out.println(UserService.class.isAssignableFrom(UserServiceImpl.class));
    }

    @Test
    public void use() {
        try {

            final ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.test.xml");
            final UserService userService = classPathXmlApplicationContext.getBean("userService", UserService.class);
            userService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void useScan() {
        try {

            final ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.test.scan.xml");
            System.out.println(classPathXmlApplicationContext.describe());
            final me.maiz.app.little.littlespring.test2.UserService  userService = classPathXmlApplicationContext.getBean("userService", me.maiz.app.little.littlespring.test2.UserService .class);
            userService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
