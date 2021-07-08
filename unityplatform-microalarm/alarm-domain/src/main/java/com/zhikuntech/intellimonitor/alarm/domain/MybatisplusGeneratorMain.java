package com.zhikuntech.intellimonitor.alarm.domain;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     warning:
 *          不要随便执行, 除非知道在做什么！！！
 * </p>
 *
 * @author test
 */
public class MybatisplusGeneratorMain {

    public static void main(String[] args) {

//        if (true) {
//            // rem close
//            return;
//        }

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        // mybatis-plus-generator
        // "/src/main/java"
        // /Users/liukai/business
        // /unityplatform-microalarm/alarm-domain/src/main/java
        // /com/zhikuntech/intellimonitor/alarm/domain

        gc.setOutputDir(projectPath + "/unityplatform-microalarm/alarm-domain/src/main/java");
        gc.setAuthor("liukai");
        gc.setOpen(false);
        //xml
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);

        // 覆盖文件
//        gc.setFileOverride(true);


        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);


        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        // 116.63.173.57
        // 192.168.3.171
//        dsc.setUrl("jdbc:mysql://116.63.173.57:33061/monitor_sys?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&useSSL=false");
        dsc.setUrl("jdbc:mysql://192.168.3.171:3306/monitor_sys?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&useSSL=false");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("develop");
        dsc.setPassword("Aman@2020");

        mpg.setDataSource(dsc);

        // 包配置
        final PackageConfig pc = new PackageConfig();
//        pc.setModuleName("testuu");
        pc.setParent("com.zhikuntech.intellimonitor.alarm.domain");
        pc.setXml("mapper");
//        pc.setXml("com/zhikuntech/intellimonitor/fanscada/domain/mapper");
        mpg.setPackageInfo(pc);


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {

//                String outP = projectPath + "/mybatis-plus-generator/src/main/resources/com.zhikuntech.intellimonitor.fanscada.domain.mapper/"
                String outP = projectPath + "/mybatis-plus-generator/src/main/resources/mapper/"
                        + pc.getModuleName()
                        + "/"
                        + tableInfo.getEntityName()
                        + "Mapper"
                        + StringPool.DOT_XML
                        ;

//                outP = projectPath + "/unityplatform-microwindpowerforecast/windpowerforecast-domain/src/main/resources/com.zhikuntech.intellimonitor.fanscada.domain.mapper/"
                outP = projectPath + "/unityplatform-microalarm/alarm-domain/src/main/resources/mapper/"
                        + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                return outP;
            }
        });
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {

                if (fileType == FileType.SERVICE || fileType == FileType.SERVICE_IMPL || fileType == FileType.MAPPER) {
                    // 已经生成的service可以不重复生成
//                    return false;
                    return !new File(filePath).exists();
                }
                // 默认生成文件
                return true;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模版
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        //- 表名称
        /*
            alarm_config_level
            alarm_config_rule
            alarm_config_monitor
            alarm_produce_info
         */
        strategy.setInclude("alarm_config_monitor");
        strategy.setControllerMappingHyphenStyle(true);

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }

}
