package me.maiz.app.little.orm.meta.model;

import lombok.Data;

@Data
public class ColumnMapping {

    private String property;

    private String column;

    private Class propertyType;

    private String columnType;

    private boolean isPk;

}
