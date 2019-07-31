仿springmvc

- 流程
    1. DispatcherServlet 初次加载配置
    2. 扫描包下的Controller
    3. 解析存储Controller及RequestMapping配置
    4. 运行时的MVC流程
    
- 知识点
    - DOM4j XML解析
    - 反射
    - 注解的使用
        - 元注解
            - @Retention 保留期
                - RetentionPolicy.SOURCE 注解只在源码阶段保留，在编译器进行编译时它将被丢弃忽视。
                - RetentionPolicy.CLASS 注解只被保留到编译进行的时候，它并不会被加载到 JVM 中。
                - RetentionPolicy.RUNTIME 注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，所以在程序运行时可以获取到它们。
            - @Documented 顾名思义，这个元注解肯定是和文档有关。它的作用是能够将注解中的元素包含到 Javadoc 中去。
            - @Target 指定可以标注的位置
                - ElementType.ANNOTATION_TYPE 可以给一个注解进行注解
                - ElementType.CONSTRUCTOR 可以给构造方法进行注解
                - ElementType.FIELD 可以给属性进行注解
                - ElementType.LOCAL_VARIABLE 可以给局部变量进行注解
                - ElementType.METHOD 可以给方法进行注解
                - ElementType.PACKAGE 可以给一个包进行注解
                - ElementType.PARAMETER 可以给一个方法内的参数进行注解
                - ElementType.TYPE 可以给一个类型进行注解，比如类、接口、枚举
            - @Inherited 一个超类被 @Inherited 注解过的注解进行注解的话，那么如果它的子类没有被任何注解应用的话，那么这个子类就继承了超类的注解。
            - @Repeatable (1.8新增)可以多次加入到同一个目标上
