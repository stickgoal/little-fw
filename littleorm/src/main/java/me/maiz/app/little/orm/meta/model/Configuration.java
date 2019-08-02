package me.maiz.app.little.orm.meta.model;

import lombok.Data;

@Data
public class Configuration {
    /**
     * 实体所在的包
     */
    private String entityPackage;

    /**
     * 数据库URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库驱动类名
     */
    private String driverClassName;

    /**
     * sql方言
     */
    private String dialect;



}
