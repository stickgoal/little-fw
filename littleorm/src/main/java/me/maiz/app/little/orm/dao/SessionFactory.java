package me.maiz.app.little.orm.dao;

import me.maiz.app.little.orm.meta.MetaExtractor;
import me.maiz.app.little.orm.meta.model.Configuration;
import me.maiz.app.little.orm.meta.model.Mapping;
import me.maiz.app.little.orm.util.DBHelper;
import me.maiz.app.little.orm.util.PackageScan;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class SessionFactory {

    //配置文件位置
    private String configLocation;

    //配置信息对象
    private Configuration configuration = new Configuration();

    //表信息的Map
    Map<String, Mapping> metaMap = new HashMap<>();

    //强制提供配置文件位置
    public SessionFactory(String configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * 启动阶段
     * <ol>
     *     <li>加载解析配置</li>
     *     <li>扫描实体类所在位置</li>
     * </ol>
     *
     */
    public void configure() {
        //解析配置文件
        loadConfig();

        //扫描实体并缓存配置信息
        List<Class> entityClasses = new PackageScan().getClassesIn(configuration.getEntityPackage());

        extractMapping(entityClasses);

        //配置数据库信息
        DBHelper.init(configuration.getUrl(), configuration.getUsername(), configuration.getPassword(), configuration.getDriverClassName());
    }

    /**
     * 将实体类转换成映射信息
     * @param entityClasses
     */
    private void extractMapping(List<Class> entityClasses) {

        for (Class entityClass : entityClasses) {

            metaMap.put(entityClass.getSimpleName(),MetaExtractor.extract(entityClass));

        }

    }

    private void loadConfig() {
        InputStream in = null;
        Properties p = new Properties();

        try {
            in = new BufferedInputStream(new FileInputStream(configLocation));
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration.setUrl(p.getProperty("jdbc.url"));
        configuration.setUsername(p.getProperty("jdbc.username"));
        configuration.setPassword(p.getProperty("jdbc.password"));
        configuration.setDriverClassName(p.getProperty("jdbc.driverClassName"));
        configuration.setDialect(p.getProperty("orm.sql.dialect"));
        configuration.setEntityPackage(p.getProperty("orm.entityPackage"));


    }

    public Session openSession() {
        return new SessionImpl(this);
    }




}
