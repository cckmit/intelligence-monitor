package com.zhikuntech.intellimonitor.core.commons.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author 杨锦程
 * @Date 2021/6/19 0:05
 * @Description sftp的配置项
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties("sftp")
@PropertySource(value = {"classpath:application.yml"})
@Data
public class SFTPConfig {
    private String ip;
    private Integer port;
    private String username;
    private String password;
    private String dst;
}
