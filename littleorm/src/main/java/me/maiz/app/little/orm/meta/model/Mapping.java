package me.maiz.app.little.orm.meta.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表和实体的映射信息信息
 */
@Data
public class Mapping {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 主键列名
     */
    private String pkName;

    /**
     * 实体类
     */
    private Class entityClass;

    /**
     * 主键类型
     */
    private Class pkType;

    /**
     * 列映射列表
     */
    private List<ColumnMapping> columnMappings;

}
