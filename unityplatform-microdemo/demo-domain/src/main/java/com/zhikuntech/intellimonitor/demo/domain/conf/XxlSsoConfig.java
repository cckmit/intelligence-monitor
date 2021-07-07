package com.zhikuntech.intellimonitor.demo.domain.conf;

import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.filter.XxlSsoTokenFilter;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 杨锦程
 * @Date 2021/6/24 11:02
 * @Description 登录验证过滤器
 * @Version 1.0
 */
//@Configuration
public class XxlSsoConfig implements InitializingBean, DisposableBean {
    @Value("${xxl.sso.redis.address}")
    private String redisAddress;

    @Value("${xxl.sso.redis.expire.minute}")
    private int redisExpireMinute;

    @Value("${xxl.sso.need-reset-pwd}")
    private String needResetPwd;

    @Value("${xxl.sso.excluded-paths}")
    private String excludedPaths;

    @Override
    public void afterPropertiesSet() {
        SsoLoginStore.setDefaultRedisExpireMinute(redisExpireMinute);
        JedisUtil.init(redisAddress);

    }

    @Bean
    public FilterRegistrationBean<XxlSsoTokenFilter> xxlSsoFilterRegistration() {
        // xxl-sso, filter init
        FilterRegistrationBean<XxlSsoTokenFilter> registration = new FilterRegistrationBean<>();

        registration.setName("XxlSsoTokenFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setFilter(new XxlSsoTokenFilter());
        registration.addInitParameter(Conf.NEED_RESET_PWD, needResetPwd);
        registration.addInitParameter(Conf.SSO_EXCLUDED_PATHS, excludedPaths);
        return registration;
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }
}
