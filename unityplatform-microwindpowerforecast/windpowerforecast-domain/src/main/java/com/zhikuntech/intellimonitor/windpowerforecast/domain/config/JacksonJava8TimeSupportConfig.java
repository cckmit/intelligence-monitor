package com.zhikuntech.intellimonitor.windpowerforecast.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author liukai
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonJava8TimeSupportConfig {


    // TODO

    // https://blog.csdn.net/qq724737991/article/details/105857621


    static class Java8TimeModule extends SimpleModule {

        public Java8TimeModule() {
//            this.addSerializer()
//            this.addDeserializer()
        }
    }
}
