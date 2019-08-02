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
import java.util.List;

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


        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {

            if (field.isAnnotationPresent(Id.class)) {

                ColumnMapping cm = new ColumnMapping();

                cm.setPk(true);
                Id idAnno = field.getAnnotation(Id.class);

                cm.setProperty(field.getName());
                cm.setPropertyType(field.getType());
                cm.setColumn(idAnno.idName());
                cm.setColumnType(getJDBCType(field.getType()));

                mapping.setPkName(idAnno.idName());
                mapping.setPkType(field.getType());

                columnMappings.add(cm);
            } else if (field.isAnnotationPresent(Column.class)) {
                ColumnMapping cm = new ColumnMapping();

                Column columnAnno = field.getAnnotation(Column.class);

                cm.setProperty(field.getName());
                cm.setPropertyType(field.getType());
                cm.setColumn(columnAnno.columnName());
                cm.setColumnType(getJDBCType(field.getType()));

                columnMappings.add(cm);
            } else {
                log.warn("[" + field + "]没有被@Id或者@Column注解，忽略");
            }
        }

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
