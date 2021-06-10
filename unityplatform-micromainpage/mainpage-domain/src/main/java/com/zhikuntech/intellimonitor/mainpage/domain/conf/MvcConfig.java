//package com.zhikuntech.intellimonitor.mainpage.domain.conf;
//
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @Author 杨锦程
// * @Date 2021/6/10 1:31
// * @Description
// * @Version 1.0
// */
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//    /**
//     * json组件替换为fastjson
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
//        FastJsonConfig config = new FastJsonConfig();
//        config.setSerializerFeatures(SerializerFeature.BrowserCompatible,
//                SerializerFeature.DisableCircularReferenceDetect,
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteDateUseDateFormat,
//                SerializerFeature.SkipTransientField);
//        converter.setFastJsonConfig(config);
//        converter.setSupportedMediaTypes(
//                Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
//        converters.add(0, converter);
//    }
//}
