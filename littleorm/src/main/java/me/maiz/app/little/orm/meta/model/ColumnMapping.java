package me.maiz.app.little.orm.meta.model;

import lombok.Data;

@Data
public class ColumnMapping {
    /**
     * 对象属性名
     */
    private String property;

    /**
     * 对象属性类型
     */
    private Class propertyType;

    /**
     * 数据库表列名
     */
    private String column;

    /**
     * 数据库表列类型
     */
    private String columnType;

    /**
     * 是否为主键
     */
    private boolean isPk;

}
