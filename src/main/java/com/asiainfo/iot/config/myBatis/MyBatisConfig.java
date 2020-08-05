package com.asiainfo.iot.config.myBatis;//package com.asiainfo.iot.config.myBatis;

import com.asiainfo.iot.config.dataSource.DataSourceConfig;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.*;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * MyBatis基础配置
 * @author zhouzy3
 */
@Configuration
@AutoConfigureAfter(DataSourceConfig.class)
public class MyBatisConfig implements TransactionManagementConfigurer {

    private final DataSource dataSource;

    @Autowired
    public MyBatisConfig(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlsession = new SqlSessionFactoryBean();
        /** 设置datasource */
        sqlsession.setDataSource(dataSource);
        /** 设置typeAlias 包扫描路径 扫描entity包 使用别名*/
        sqlsession.setTypeAliasesPackage("com.asiainfo.iot.bean.mybatais");
        /** 设置mybatis configuration 扫描路径 */
        sqlsession.setConfigLocation(new ClassPathResource("myBatis/mybatis-config.xml"));
        sqlsession.setFailFast(true);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlsession.setMapperLocations(resolver.getResources("classpath:mapper/**/**Mapper.xml"));
            //添加分页插件、打印sql插件
            Interceptor[] plugins = new Interceptor[]{(Interceptor) pageHelper(),sqlPrintInterceptor()};
            sqlsession.setPlugins(plugins);
            return sqlsession.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean("transactionManager")
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 1、execution(): 表达式主体。
     *  2、第一个*号：表示返回类型，*号表示所有的类型。
     *  3、包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包，com.asiainfo.alarm.service.impl包、子孙包下所有类的方法。
     *  4、第二个*号：表示类名，*号表示所有的类。
     *  5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数。
     */
    private static final int TX_METHOD_TIMEOUT = 50000;
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.asiainfo.iot.service.impl..*.*(..))";

    // 事务的实现Advice
    @Bean
    public TransactionInterceptor txAdvice(@Qualifier("transactionManager") PlatformTransactionManager m) {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setTimeout(TX_METHOD_TIMEOUT);
        Map<String, TransactionAttribute> txMap = new HashMap<>();
        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("get*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        source.setNameMap(txMap);
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(m, source);
        return transactionInterceptor;
    }

    // 切面的定义,pointcut及advice
    @Bean
    public Advisor txAdviceAdvisor(@Qualifier("txAdvice") TransactionInterceptor txAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }

    //将要执行的sql进行日志打印(不想拦截，就把这方法注释掉)
    @Bean
    public SqlPrintInterceptor sqlPrintInterceptor(){
        return new SqlPrintInterceptor();
    }

    /**
     * 分页插件
     *
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
//        p.setProperty("dialect","mysql");
        p.setProperty("dialect","postgresql");
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        p.setProperty("returnPageInfo", "check");
        p.setProperty("params", "count=countSql");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
