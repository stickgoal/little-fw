package me.maiz.app.little.littlespring.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Field;

/**
 * 表示一个属性
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    private String name;

    private String value;

    private String ref;

    private Field field;
}
