package me.maiz.app.little;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

public class DispatcherTest extends TestCase {

    @Test
    public void test(){
        DispatcherServlet ds = new DispatcherServlet();

        try {
            ds.init();
        } catch (ServletException e) {
            e.printStackTrace();
            fail();
        }
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("hello");
        request.setMethod("post");
        request.setParameter("username","lucas");
        MockHttpServletResponse response = new MockHttpServletResponse();
        try {
            ds.doPost(request,response);

            final Object username = request.getAttribute("username");
            assertNotNull(username);
            assertEquals("lucas",username);

            final String forwardedUrl = response.getForwardedUrl();
            assertNotNull(forwardedUrl);
            assertEquals("/WEB-INF/pages/hello.jsp",forwardedUrl);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail();
        }


    }
    @Test
    public void test2(){
        DispatcherServlet ds = new DispatcherServlet();

        try {
            ds.init();
        } catch (ServletException e) {
            e.printStackTrace();
            fail();
        }
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("hello2");
        request.setMethod("post");
        request.setParameter("username","lucas");
        MockHttpServletResponse response = new MockHttpServletResponse();
        try {
            ds.doPost(request,response);
            System.out.println(response.getContentAsString());
            assertNotNull(response.getContentAsString());
            assertEquals("\"hello\"",response.getContentAsString());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail();
        }


    }


}
