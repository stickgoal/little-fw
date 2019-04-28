package me.maiz.app.little.littlespring.metadata;

import lombok.extern.log4j.Log4j;
import me.maiz.app.little.littlespring.metadata.annotations.*;
import me.maiz.app.little.littlespring.metadata.model.BeanDefinition;
import me.maiz.app.little.littlespring.metadata.model.Property;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.reflections.Reflections;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载xml配置文件
 */
@Log4j
public class XmlConfigReader {


    /**
     * 从指定位置加载xml配置并解析出配置信息
     *
     * @param configLocation
     * @return
     */
    public Map<String, BeanDefinition> parse(String configLocation) {
        Map<String, BeanDefinition> beanDefinitionList = new ConcurrentHashMap<>();
        SAXReader reader = new SAXReader();
        try {
            InputStream is = this.getClass().getResourceAsStream("/" + configLocation);
            final Document document = reader.read(is);
            final Element beansElement = document.getRootElement();
            for (Iterator<Element> it = beansElement.elementIterator(); it.hasNext(); ) {
                BeanDefinition bd = new BeanDefinition();
                Element beanElement = it.next();
                if ("component-scan".equals(beanElement.getName())) {
                    //扫描某个包下所有类并获取到注解的类，生成bean定义
                    String basePackage = beanElement.attributeValue("base-package");
                    log.info("找到component-scan元素，base-package为" + basePackage);
                    scanAndRegister(basePackage, beanDefinitionList);
                    continue;
                }

                //从xml解析出的内容生成bean的定义
                final String id = beanElement.attributeValue("id");
                bd.setId(id);
                bd.setType(beanElement.attributeValue("class"));
                setProperties(bd, beanElement);
                beanDefinitionList.put(id, bd);
            }

        } catch (DocumentException e) {
            throw new RuntimeException("解析配置文档出错", e);
        }


        return beanDefinitionList;
    }

    private void scanAndRegister(String basePackage, Map<String, BeanDefinition> beanDefinitionList) {
        log.info("开始扫描[" + basePackage + "]");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> allAnnotatedClass = new HashSet<>();
        allAnnotatedClass.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        allAnnotatedClass.addAll(reflections.getTypesAnnotatedWith(Component.class));
        allAnnotatedClass.addAll(reflections.getTypesAnnotatedWith(Service.class));
        log.info("找到含有注解的类:" + allAnnotatedClass);
        for (Class<?> clazz : allAnnotatedClass) {
            BeanDefinition bd = new BeanDefinition();
            bd.setTypeClass(clazz);
            bd.setId(getBeanName(clazz));
            List<Property> properties = new ArrayList<>();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                //处理被@Autowired和@Value的注解的字段
                if (f.isAnnotationPresent(Autowired.class)) {
                    log.info("找到autowired的字段 " + f.getName());
                    Property p = new Property();
                    p.setField(f);
                    p.setName(f.getName());
                    properties.add(p);
                }
                if (f.isAnnotationPresent(Value.class)) {
                    final Value annotation = f.getAnnotation(Value.class);
                    log.info("找到value的字段 " + f.getName()+"值为："+annotation.value());
                    Property p = new Property();
                    p.setName(f.getName());
                    p.setValue(annotation.value());
                    p.setField(f);
                    properties.add(p);
                }

            }
            bd.setProperties(properties);

            beanDefinitionList.put(getBeanName(clazz),bd);
        }
    }

    private String getBeanName(Class<?> clazz) {
        Repository repo = clazz.getAnnotation(Repository.class);
        Service service = clazz.getAnnotation(Service.class);
        Component component = clazz.getAnnotation(Component.class);
        return repo!=null?repo.value():(service!=null?service.value():component.value());
    }

    private void setProperties(BeanDefinition bd, Element beanElement) {
        List<Property> properties = new ArrayList<>();
        final List<Element> propertyElements = beanElement.elements();
        if (propertyElements != null && propertyElements.size() > 0) {
            for (Element p : propertyElements) {
                String name = p.attributeValue("name");
                String value = p.attributeValue("value");
                String ref = p.attributeValue("ref");
                properties.add(new Property(name, value, ref, null));
            }
        }
        bd.setProperties(properties);
    }


}
