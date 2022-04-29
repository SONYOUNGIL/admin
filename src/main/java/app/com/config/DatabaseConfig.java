package app.com.config;
 
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
    
    Properties env;
    
    @Primary
    @Bean
    public DataSource dataSource() throws Exception {
        // -Dtys-msa.prod=local"
        String prod_nm = System.getProperty("spring.profiles.active");    	
		
		Resource resource = new ClassPathResource("/com/properties/app-"+prod_nm+".properties"); 
    	env = PropertiesLoaderUtils.loadProperties(resource);

    	HikariConfig hikariConfig = new HikariConfig();
    	hikariConfig.setDriverClassName(env.getProperty("db.driver-class-name"));
    	hikariConfig.setJdbcUrl(env.getProperty("db.jdbc-url"));
    	hikariConfig.setUsername(env.getProperty("db.username"));
    	hikariConfig.setPassword(env.getProperty("db.password"));
		hikariConfig.setPoolName(env.getProperty("db.pool-name"));
        hikariConfig.setConnectionTestQuery("SELECT 1");
    	hikariConfig.setMinimumIdle(10);
		hikariConfig.setMaximumPoolSize(10);
		//hikariConfig.setIdleTimeout(10000);
		hikariConfig.setConnectionTimeout(3000);
		hikariConfig.setValidationTimeout(3000);
		hikariConfig.setMaxLifetime(58000);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/com/sql/*.xml"));
        Properties mybatisProperties = new Properties();
        mybatisProperties.setProperty("mapUnderscoreToCamelCase", "true"); // CamelCase 자동맵핑
        sqlSessionFactoryBean.setConfigurationProperties(mybatisProperties);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("app");
        emf.setJpaVendorAdapter(jpaVendorAdapters());
        emf.setJpaProperties(jpaProperties());
        emf.afterPropertiesSet();
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

    private JpaVendorAdapter jpaVendorAdapters() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabasePlatform(env.getProperty("dialect"));
        return hibernateJpaVendorAdapter;
    }
    
    private Properties jpaProperties() {
        Properties jpaProperties = new Properties();
    
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hbm2ddl.auto"));
        jpaProperties.setProperty("hibernate.show_sql", env.getProperty("show_sql"));
        jpaProperties.setProperty("hibernate.format_sql", env.getProperty("format_sql"));
        jpaProperties.setProperty("hibernate.use_sql_comments", env.getProperty("use_sql_comments"));
        jpaProperties.setProperty("hibernate.globally_quoted_identifiers", env.getProperty("globally_quoted_identifiers"));
        jpaProperties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("temp.use_jdbc_metadata_defaults"));
        jpaProperties.setProperty("hibernate.physical_naming_strategy", env.getProperty("physical_naming_strategy"));
        jpaProperties.setProperty("hibernate.implicit_naming_strategy" , env.getProperty("implicit_naming_strategy"));

        jpaProperties.setProperty("logging.level.org.hibernate.type.descriptor.sql", env.getProperty("descriptor.sql"));
        return jpaProperties;
    }
}