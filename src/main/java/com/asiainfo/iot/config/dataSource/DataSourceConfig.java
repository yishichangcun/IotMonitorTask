package com.asiainfo.iot.config.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Configuration
public class DataSourceConfig {


    @Autowired
    private Environment environment;

    @Bean(name = "datasource")
    @Primary
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(environment.getProperty("spring.datasource.url"));
        datasource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        datasource.setUsername(environment.getProperty("spring.datasource.username"));
        datasource.setPassword(environment.getProperty("spring.datasource.password"));
        datasource.setInitialSize(Integer.valueOf(Objects.requireNonNull(environment.getProperty("spring.datasource.druid.initial-size"))));
        datasource.setMinIdle(Integer.valueOf(Objects.requireNonNull(environment.getProperty("spring.datasource.druid.min-idle"))));
        datasource.setMaxWait(Long.valueOf(Objects.requireNonNull(environment.getProperty("spring.datasource.druid.max-wait"))));
        datasource.setMaxActive(Integer.valueOf(Objects.requireNonNull(environment.getProperty("spring.datasource.druid.max-active"))));
//        /**setMinEvictableIdleTimeMillis需要先设置*/
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(Objects.requireNonNull(environment.getProperty("spring.datasource.druid.min-evictable-idle-time-millis"))));
        datasource.setMaxEvictableIdleTimeMillis(Long.valueOf(Objects.requireNonNull(environment.getProperty("spring.master.max-evictable-idle-time-millis"))));

//        datasource.setInitialSize(Integer.valueOf(Objects.requireNonNull(environment.getProperty("spring.master.initial-size"))));
//        datasource.setMinIdle(Integer.valueOf(Objects.requireNonNull(environment.getProperty("spring.master.min-idle"))));
//        datasource.setMaxWait(Long.valueOf(Objects.requireNonNull(environment.getProperty("spring.master.max-wait"))));
//        datasource.setMaxActive(Integer.valueOf(Objects.requireNonNull(environment.getProperty("spring.master.max-active"))));
////        /**setMinEvictableIdleTimeMillis需要先设置*/
//        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(Objects.requireNonNull(environment.getProperty("spring.master.min-evictable-idle-time-millis"))));
//        datasource.setMaxEvictableIdleTimeMillis(Long.valueOf(Objects.requireNonNull(environment.getProperty("spring.master.max-evictable-idle-time-millis"))));
        //超过时间限制是否回收
        datasource.setRemoveAbandoned(true);
        //超时时间；单位为秒。180秒=3分钟
        datasource.setRemoveAbandonedTimeout(360);
        //关闭abanded连接时输出错误日志
        //当连接超过3分钟后会强制进行回收，并输出异常日志
        datasource.setLogAbandoned(true);
        try {
            datasource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    /**
     * 注册DruidServlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        return servletRegistrationBean;
    }

    /**
     * 注册DruidFilter拦截
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean druidFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<String, String>();
        //设置忽略请求
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
