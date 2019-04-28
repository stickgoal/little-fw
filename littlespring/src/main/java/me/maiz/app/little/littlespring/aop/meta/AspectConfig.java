package me.maiz.app.little.littlespring.aop.meta;

import lombok.Data;

import java.util.List;

@Data
public class AspectConfig {
    /**
     * 切入点表达式
     */
    private String pointcut;

    private Class<?> aspectClass;

    private List<String> beforeMethods;

    private List<String> afterMethods;

    private List<String> aroundMethods;
}
