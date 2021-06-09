package com.zhikuntech.intellimonitor.mainpage.domain.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @Author 杨锦程
 * @Date 2021/6/9 11:23
 * @Description Swagger2API文档的配置
 * @Version 1.0
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("风功率曲线首页接口文档")
                        .description("风功率曲线首页接口文档")
                        .termsOfServiceUrl("http://www.xx.com/")
                        .contact("智鹍")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("1.0版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.zhikuntech.intellimonitor.mainpage.domain.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
