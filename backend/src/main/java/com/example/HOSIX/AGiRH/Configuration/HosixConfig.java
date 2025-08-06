package com.example.HOSIX.AGiRH.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
        basePackages = "com.example.HOSIX.AGiRH.Repository.HosixRepo",
        entityManagerFactoryRef = "hosixEntityManagerFactory",
        transactionManagerRef = "hosixTransactionManager"
)
public class HosixConfig {

    @Bean(name = "hosixDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hosix")
    public DataSource hosixDataSource() {
        return DataSourceBuilder
                .create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "hosixEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean hosixEntityManagerFactory(
            @Qualifier("hosixDataSource") DataSource hosixDataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.format_sql", true);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(hosixDataSource);
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setPackagesToScan("com.example.HOSIX.AGiRH.Entity.HOSIXRes");
        emf.setPersistenceUnitName("hosixResident");
        emf.setJpaPropertyMap(props);
        return emf;
    }

    @Bean(name = "hosixTransactionManager")
    public PlatformTransactionManager hosixTransactionManager(
            @Qualifier("hosixEntityManagerFactory") LocalContainerEntityManagerFactoryBean hosixEmf) {
        return new JpaTransactionManager(hosixEmf.getObject());
    }
}