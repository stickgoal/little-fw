package me.maiz.app.little;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        final Method[] methods = String.class.getMethods();
        for (Method method : methods) {
            System.out.println(method);
            if(method.getParameters().length>0)
            System.out.println(method.getParameters()[0].getName());
        }

    }
}
