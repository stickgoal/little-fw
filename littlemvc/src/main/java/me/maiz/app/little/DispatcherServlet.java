package me.maiz.app.little;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.maiz.app.little.annotations.*;
import me.maiz.app.little.config.ConfigParser;
import me.maiz.app.little.config.Configuration;
import me.maiz.app.little.util.PackageScan;
import me.maiz.app.little.mvc.ModelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    //存放请求URL与处理方法的映射  /user/add => UserControler.userAdd(String username,String password)
    private Map<String, Method> urlHandlerMethodMapping = new ConcurrentHashMap<>();

    //存放controller实例   userControler => new UserController()
    private Map<String, Object> controllerInstanceMap = new ConcurrentHashMap<>();

    //持有配置信息
    private Configuration configuration = null;

    @Override
    public void init() throws ServletException {
        super.init();
        // 加载配置
//        String servletName = getServletName();

        //默认命名约定为servletName再拼接上 "-servlet.xml"
        configuration = loadConfig("mvc.xml");

        //扫描和注册Controller
        scanAndRegister(configuration.getBasePackage());

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //分发请求
        String requestURI = req.getRequestURI();
        requestURI = normalize(requestURI);

        String reqMethod = req.getMethod();

        //定位请求处理方法
        final Method handleMethod = urlHandlerMethodMapping.get(requestURI + reqMethod.toUpperCase());
        if (handleMethod == null) {
            throw new IllegalStateException("没有找到为路径" + requestURI + "合适的处理方法");
        }
        //定位控制器实例
        final Class<?> targetClass = handleMethod.getDeclaringClass();
        final Object controller = controllerInstanceMap.get(targetClass.getSimpleName());

        try {
            //准备特殊的系统参数，如request\response\session\modelMap
            ModelMap modelMap = new ModelMap();

            Map<Class<?>, Object> systemParams = setSystemParam(req, resp, modelMap);

            //执行调用
            final Object result = handleMethod.invoke(controller, getParam(handleMethod, systemParams));

            //视图解析和跳转
            final boolean isResponseBody = handleMethod.isAnnotationPresent(LittleResponseBody.class);
            if (isResponseBody) {
                final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                final String json = gson.toJson(result);
                resp.getWriter().write(json);
            } else {
                String fullView = resolveView(result.toString());
                //将modelMap中的值转存到请求中，便于在页面上使用
                for (String k : modelMap.keySet()) {
                    req.setAttribute(k, modelMap.get(k));
                }
                //跳转到页面
                req.getRequestDispatcher(fullView).forward(req, resp);

            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("调用处理方法失败", e);
        }

    }

    private String normalize(String requestURI) {
        if (!requestURI.endsWith("/")) {
            requestURI += "/";
        }
        return requestURI;
    }

    private Map<Class<?>, Object> setSystemParam(HttpServletRequest req, HttpServletResponse resp, ModelMap modelMap) {
        //系统提供的特殊参数
        Map<Class<?>, Object> systemParams = new HashMap<>();
        systemParams.put(HttpServletRequest.class, req);
        systemParams.put(HttpServletResponse.class, resp);
        systemParams.put(ModelMap.class, modelMap);
        systemParams.put(HttpSession.class, req.getSession());
        return systemParams;
    }

    //解析视图
    private String resolveView(Object viewName) {
        return configuration.getViewPrefix() + viewName + configuration.getViewSuffix();
    }

    //处理参数
    private Object[] getParam(Method handleMethod, Map<Class<?>, Object> systemParams) {
        HttpServletRequest req = (HttpServletRequest) systemParams.get(HttpServletRequest.class);

        Map<String, String[]> parameterMap = req.getParameterMap();
        List params = new ArrayList();
        Parameter[] parameters = handleMethod.getParameters();
        for (Parameter p : parameters) {
            final Class<?> paramType = p.getType();
            final Object systemParam = systemParams.get(paramType);
            if (systemParam != null) {
                //特殊参数处理
                params.add(systemParam);
            } else {
                //从@RequestParam注解中获取参数名，从请求中获取对应的参数值
                final LittleRequestParam requestParam = p.getAnnotation(LittleRequestParam.class);
                if (requestParam == null) {
                    throw new RuntimeException(paramType.getSimpleName() + "类型的参数没有添加@RequestParam注解");
                }
                String paramName = requestParam.value();
                String[] paramValue = parameterMap.get(paramName);
                //非空校验
                if (paramValue == null && requestParam.required()) {
                    throw new RuntimeException(paramName + "是必填参数，不可为空");
                }
                //处理字符串参数
                if (paramType == String.class) {
                    params.add(paramValue != null ? paramValue[0] : null);
                } else {
                    //TODO 参数类型转换
                }
            }
        }

        return params.toArray();
    }

    /**
     * 扫描被注解标识出来的类和方法
     *
     * @param basePackage
     */
    private void scanAndRegister(String basePackage) {
        final Iterable<Class> classesAnnotatedWith = new PackageScan().getClassesAnnotatedWith(LittleController.class, basePackage);

        for (Class aClass : classesAnnotatedWith) {
            try {
                controllerInstanceMap.put(aClass.getSimpleName(), aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("实例化Controller失败", e);
            }
            //只需要public方法
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(LittleRequestMapping.class)) {

                    LittleRequestMapping requestMapping = method.getAnnotation(LittleRequestMapping.class);
                    final String urlPath = normalize(requestMapping.value());
                    final RequestMethod[] reqMethods = requestMapping.method();
                    for (RequestMethod rm : reqMethods) {
                        logger.debug("定位到请求映射方法{}({})->{}", urlPath, rm.name(), method.getName());
                        urlHandlerMethodMapping.put(urlPath + rm.name(), method);
                    }

                }
            }
        }
        logger.info("扫描注解完成 \ncontroller实例：{}\n 请求映射：{}", controllerInstanceMap, urlHandlerMethodMapping);
    }

    /**
     * 从配置文件加载配置
     *
     * @param configFile
     * @return
     */
    private Configuration loadConfig(String configFile) {

        return new ConfigParser().parse(configFile);
    }
}
