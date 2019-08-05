package me.maiz.app.little.orm.meta;

import lombok.extern.slf4j.Slf4j;
import me.maiz.app.little.orm.annotation.Column;
import me.maiz.app.little.orm.annotation.Entity;
import me.maiz.app.little.orm.annotation.Id;
import me.maiz.app.little.orm.exceptions.EntityNotManagedException;
import me.maiz.app.little.orm.meta.model.ColumnMapping;
import me.maiz.app.little.orm.meta.model.Mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MetaExtractor {

    public static Mapping extract(Class entityClass) {

        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new EntityNotManagedException("[" + entityClass.getCanonicalName() + "]没有加@Entity注释，它是一个实体吗？");
        }

        Mapping mapping = new Mapping();
        final Entity entityAnno = (Entity) entityClass.getAnnotation(Entity.class);
        mapping.setEntityClass(entityClass);
        mapping.setTableName(entityAnno.tableName());
        List<ColumnMapping> columnMappings = new ArrayList<>();
        Map<String,ColumnMapping> columnNameMapping = new HashMap<>();


        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            ColumnMapping cm = new ColumnMapping();

           if (field.isAnnotationPresent(Column.class)) {

                Column columnAnno = field.getAnnotation(Column.class);

                cm.setProperty(field.getName());
                cm.setPropertyType(field.getType());
                cm.setColumn(columnAnno.columnName());
                cm.setColumnType(getJDBCType(field.getType()));

                columnMappings.add(cm);
                columnNameMapping.put(columnAnno.columnName(),cm);

               if (field.isAnnotationPresent(Id.class)) {
                   cm.setPk(true);

                   mapping.setPkName(columnAnno.columnName());
                   mapping.setPkType(field.getType());

               }
            } else {
                log.warn("[" + field + "]没有被@Column注解，忽略");
            }
        }

        mapping.setColumnNameMapping(columnNameMapping);
        mapping.setColumnMappings(columnMappings);

        return mapping;
    }

    /**
     * @param type
     * @return
     */
    private static String getJDBCType(Class<?> type) {
        //TODO 根据java类的字段类型获取到对应的JDBC类型
        return null;
    }

}
