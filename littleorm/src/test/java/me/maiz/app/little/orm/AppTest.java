package me.maiz.app.little.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import me.maiz.app.little.orm.main.entity.User;
import me.maiz.app.little.orm.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User();
        user.setUsername("lucas");
        user.setPassword("123abc");
        final Map<String, String> describe = BeanUtils.describe(user);
        System.out.println(describe);
    }

    @Test
    public void testCamelCaseTransform(){
        assertEquals("look",StringUtil.camelCaseToUnderScore("Look"));
        assertEquals("look_back",StringUtil.camelCaseToUnderScore("LookBack"));
        assertEquals("look_back_now",StringUtil.camelCaseToUnderScore("LookBackNow"));
        assertEquals("look_back_now_n",StringUtil.camelCaseToUnderScore("LookBackNowN"));
    }
    @Test
    public void testUnderscoreTransform(){
        assertEquals("lookBack",StringUtil.underScoreToCamelCase("look_back"));
        assertEquals("lookBackNow",StringUtil.underScoreToCamelCase("look_back_now"));
        assertEquals("lookBackNow",StringUtil.underScoreToCamelCase("look_back_now_"));
        assertEquals("lookBackNow",StringUtil.underScoreToCamelCase("_look_back_now_"));

    }
}
