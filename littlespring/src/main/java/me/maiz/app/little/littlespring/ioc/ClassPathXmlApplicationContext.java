package me.maiz.app.little.littlespring.ioc;

import lombok.extern.log4j.Log4j;
import me.maiz.app.little.littlespring.ioc.exceptions.BeanRegistrationException;
import me.maiz.app.little.littlespring.ioc.exceptions.BeansException;
import me.maiz.app.little.littlespring.metadata.XmlConfigReader;
import me.maiz.app.little.littlespring.metadata.model.BeanDefinition;
import me.maiz.app.little.littlespring.metadata.model.Property;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
public class ClassPathXmlApplicationContext implements BeanFactory {
    //持有beanDefinition的容器
    private  Map<String, BeanDefinition> definitionMap;
    //持有bean的容器
    private Map<String,Object> container = new ConcurrentHashMap<>();

    public ClassPathXmlApplicationContext(String configLocation){
        //从xml文件中解析出bean的定义
        definitionMap = new XmlConfigReader().parse(configLocation);
        log.debug("bean定义列表："+definitionMap.toString());
        //注册所有bean
        registerBeans(definitionMap);
    }

    private void registerBeans(Map<String, BeanDefinition> definitions) {
        for (BeanDefinition def:definitions.values()) {
            try {
                container.put(def.getId(),createBean(def));
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException|NoSuchFieldException e) {
                throw new BeanRegistrationException("注册bean是出现异常",e);
            }
        }
    }

    /**
     * 根据bean的定义创建bean对象，若有需要注入的属性则递归创建之。
     * @param def
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    private Object createBean(BeanDefinition def) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {

        final Class<?> type = def.getTypeClass()!=null?def.getTypeClass():Class.forName(def.getType());
        final Object instance = type.newInstance();
        final List<Property> properties = def.getProperties();
        if (properties != null&&properties.size()>0) {
            for (Property property : properties) {
                Field field = validateMethod(type,property.getName());
                field.setAccessible(true);
                //value直接设值
                if(property.getValue()!=null){
                    field.set(instance,property.getValue());
                    continue;
                }
                //ref找到相应的bean进行注册
                if(property.getRef()!=null){
                    //容器中是否有ref的bean
                     Object target = container.get(property.getRef());
                    if(target==null){
                        //递归创建bean
                        final BeanDefinition propDef = definitionMap.get(property.getRef());
                        target =  createBean(propDef);
                    }
                    field.set(instance,target);
                    continue;
                }
                //Autowired字段
                if(property.getField()!=null){
                    property.getField().setAccessible(true);
                    log.info("为"+instance+"的字段"+property.getName()+"注入值");
                    //容器中获取到是否已经存在指定的bean
                     Object target = container.get(property.getName());
                     if(target==null){
                         log.info("- 容器获取失败，创建bean");
                         final BeanDefinition propDef = definitionMap.get(property.getName());
                         if(propDef==null){
                             throw new BeanRegistrationException("为"+instance+"注入"+property.getName()+"值时失败，找不到指定的bean的定义");
                         }
                         target =  createBean(propDef);
                     }
                    property.getField().set(instance,target);
                }
            }
        }

        return instance;
    }

    /**
     * 验证是否有该属性名
     * @param type
     * @param name
     */
    private Field validateMethod(Class<?> type, String name) throws NoSuchFieldException {
        return type.getDeclaredField(name);
    }


    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
         Object target = container.get(name);

         if(target!=null&& requiredType.isAssignableFrom(target.getClass())) {
             return (T) target;
         }else{
             throw new BeansException("没有找到指定类型的Bean");
         }
    }

    @Override
    public boolean containsBean(String name) {
        return definitionMap.containsKey(name);
    }

    public String describe(){
        return container.toString();
    }

}
