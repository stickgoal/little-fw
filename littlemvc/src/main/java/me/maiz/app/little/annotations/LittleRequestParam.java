package me.maiz.app.little.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识一个请求参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface LittleRequestParam {

    /**
     * 参数名
     * @return
     */
    String value() ;

    /**
     * 是否必填
     * @return
     */
    boolean required() default false;

}
