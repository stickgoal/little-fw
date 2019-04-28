package me.maiz.app.little.littlespring.ioc;

import me.maiz.app.little.littlespring.ioc.exceptions.BeansException;

/**
 * IOC容器的接口
 */
public interface BeanFactory {
    /**
     * 从容器中获取bean
     * @param name
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * 判断是否包含bean
     * @param name
     * @return
     */
    boolean containsBean(String name);


}
