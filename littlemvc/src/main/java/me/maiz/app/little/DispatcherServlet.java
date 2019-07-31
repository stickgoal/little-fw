package me.maiz.app.little;

import me.maiz.app.little.annotations.LittleController;
import me.maiz.app.little.annotations.LittleRequestMapping;
import me.maiz.app.little.annotations.RequestMethod;
import me.maiz.app.little.config.ConfigParser;
import me.maiz.app.little.config.Configuration;
import me.maiz.app.little.config.PackageScan;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DispatcherServlet extends HttpServlet {

    private Map<String,Method> urlHandlerMethodMapping = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        // 加载配置
        String servletName = getServletName();
        //默认命名约定为servletName再拼接上 "-servlet.xml"
        Configuration config = loadConfig(servletName+"-servlet.xml");

        //扫描和注册Controller
        scanAndRegister(config.getBasePackage());

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //分发请求

    }

    /**
     * 扫描被注解标识出来的类和方法
     * @param basePackage
     */
    private void scanAndRegister(String basePackage) {
        final Iterable<Class> classesAnnotatedWith = new PackageScan().getClassesAnnotatedWith(LittleController.class, basePackage);
        for (Class aClass : classesAnnotatedWith) {
            //只需要public方法
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(LittleRequestMapping.class)){
                    LittleRequestMapping requestMapping = method.getAnnotation(LittleRequestMapping.class);
                    final String urlPath = requestMapping.value();
                    final RequestMethod[] reqMethods = requestMapping.method();
                    for (RequestMethod rm : reqMethods) {
                        logger.debug("定位到请求映射方法{}({})->{}",urlPath,rm.name(),method.getName());
                        urlHandlerMethodMapping.put(urlPath + rm.name(),method);
                    }

                }
            }
        }
    }

    /**
     * 从配置文件加载配置
     * @param configFile
     * @return
     */
    private Configuration loadConfig(String configFile) {

        return new ConfigParser().parse(configFile);
    }
}
