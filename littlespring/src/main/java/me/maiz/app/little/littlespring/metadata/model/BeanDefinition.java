package me.maiz.app.little.littlespring.metadata.model;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * littlespring bean定义
 */
@Data
public class BeanDefinition {

    private String type;

    private String id;

    private List<Property> properties;

    private Class<?> typeClass;

}
