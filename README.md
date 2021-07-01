# 一体化协同采集系统1.0

## TODO 打包需知
1.外部依赖发送更改时, install intelli-monitor-parent中的pom文件(必须)


## 项目依赖管理
所有外部依赖放在[intelli-monitor-parent]中

## 命名规范
服务命名: unityplatform-micro + 开发模块名称

例如模块名称为[demo]
那么名称就为[unityplatform-microdemo]

模块下的包有:app/(domain/data)/facade/prototype

1.app 模块中(仅)放启动类

2.(domain/data)命名没有强制要求, 也可以直接使用service
层进行替代.

3.facade中放提供给外部服务的接口(feign)

4.prototype中放提供给外部服务的实体,
一般来说facade引用prototype包,
外部服务直接引用facade包即可



## 项目结构
例如[unityplatform-microdemo]


## 编码注意⚠️
1.包名称均为小写

2.不要提交[target]文件中的内容


## 数据库链接
192.168.3.171:3306
db: monitor_sys

## 当前模块
1.unityplatform-micromainpage
2.unityplatform-microfanscada
3.unityplatform-microwindpowerforecast

TODO [代码提交规范]

## 外部依赖无法打包处理方法 （杨/代）
##golden
mvn install:install-file -Dfile=jar包本地路径 -DgroupId=com.golden -DartifactId=golden-java-sdk -Dversion=3.0.34 -Dpackaging=jar
##xxl-sso-core 1.1.1
mvn install:install-file -DgroupId=com.xuxueli -DartifactId=xxl-sso-core -Dversion=1.1.1 -Dfile=jar包位置 -Dpackaging=jar
