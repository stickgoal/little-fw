package me.maiz.app.little.config;

import lombok.Data;

/**
 * 配置信息
 */
@Data
public class Configuration {

    /**
     * 包扫描的基础包
     */
    private String basePackage;

    /**
     * 视图前缀
     */
    private String viewPrefix;

    /**
     * 视图后缀
     */
    private String viewSuffix;

}
