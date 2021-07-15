package generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 滕楠
 * @className SqlserverGenerator
 * @create 2021/7/14 17:40
 **/

public class SqlserverGenerator {
    /**
     * RUN THIS
     */
    //生成文件所在项目路径
    private static String baseProjectPath = "D:\\workcode\\intelligence-monitor\\unityplatform-microstructuredata\\structuredata-domain";

    //基础包名
    private static String basePackage = "com.zhikuntech.intellimonitor.structuredata.domain";
    //设置作者
    private static String authorName = "tn";
    //这里是要生成的表名（如果全部要生成的话，这里注释掉）
    //private static String[] tables= {"t_role","t_resource","t_role_resource","t_user_role"};
    //可以设置table前缀
    //private static String prefix = "t_";

    //数据库配置四要素
    private static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String url = "jdbc:sqlserver://192.168.30.60:1433;databaseName=振动监测数据库";
    private static String username = "sa";
    private static String password = "develop";


    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(baseProjectPath + "/src/main/java");
        // TODO 设置用户名
        gc.setAuthor("tn");
        gc.setOpen(true);
        // service 命名方式
        gc.setServiceName("%sService");
        // service impl 命名方式
        gc.setServiceImplName("%sServiceImpl");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        // TODO 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // TODO 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        //父包
        pc.setParent("com.zhikuntech.intellimonitor.structuredata.domain");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        mpg.setPackageInfo(pc);

        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        // 设置逻辑删除键（这个是逻辑删除的操作）
        strategy.setLogicDeleteFieldName("deleted");
        // TODO 指定生成的bean的数据库表名（如果全部生成，这里要注释掉）
        strategy.setInclude("国电普陀风电场_1号风机_塔筒振动");
        //strategy.setSuperEntityColumns("id");
        // 驼峰转连字符
        //strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}

