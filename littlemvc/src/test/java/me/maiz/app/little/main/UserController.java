package me.maiz.app.little.main;

import me.maiz.app.little.annotations.LittleController;
import me.maiz.app.little.annotations.LittleRequestMapping;
import me.maiz.app.little.annotations.LittleRequestParam;
import me.maiz.app.little.annotations.LittleResponseBody;
import me.maiz.app.little.mvc.ModelMap;

import javax.servlet.http.HttpServletRequest;

@LittleController
public class UserController {

    @LittleRequestMapping("hello")
    public String sayHello(HttpServletRequest req, ModelMap modelMap, @LittleRequestParam("username") String username){
        modelMap.put("username",username);
        return "hello";
    }

    @LittleRequestMapping("hello2")
    @LittleResponseBody
    public String sayHello2(HttpServletRequest req, ModelMap modelMap, @LittleRequestParam("username") String username){
        modelMap.put("username",username);
        return "hello";
    }
}
