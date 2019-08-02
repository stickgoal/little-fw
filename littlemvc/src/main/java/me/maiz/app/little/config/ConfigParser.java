package me.maiz.app.little.config;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

@Slf4j
public class ConfigParser {

    public Configuration parse(String configFile){
        Configuration configuration = new Configuration();

        SAXReader reader = new SAXReader();
        try {
            if (!configFile.startsWith("/")){
                configFile = "/"+configFile;
            }
            log.info("准备解析配置文件 {}",configFile);
            Document configDoc = reader.read(this.getClass().getResourceAsStream(configFile));

            Element root = configDoc.getRootElement();
            final Iterator<Element> iterator = root.elementIterator();
            while(iterator.hasNext()){
                Element element = iterator.next();
                if("component-scan".equals(element.getName())){
                    //
                    log.info("解析[component-scan]节点{}",element);
                    final String basePackage = element.attributeValue("base-package");
                    configuration.setBasePackage(basePackage);
                }
                if("view-resolver".equals(element.getName())){
                    //
                    log.info("解析[view-resolver]节点{}",element);
                    final Iterator<Element> subIterator = element.elementIterator();
                    while(subIterator.hasNext()){
                        final Element sub = subIterator.next();
                        if("prefix".equals(sub.getName())){
                            configuration.setViewPrefix(sub.attributeValue("value"));
                        }
                        if("suffix".equals(sub.getName())){
                            configuration.setViewSuffix(sub.attributeValue("value"));
                        }
                    }
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("解析配置文件出错",e);
        }
        return configuration;
    }



}
