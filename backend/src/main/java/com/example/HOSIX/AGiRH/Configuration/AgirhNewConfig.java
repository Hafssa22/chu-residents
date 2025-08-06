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
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.HOSIX.AGiRH.Repository.AgirhNew",
        entityManagerFactoryRef = "newEntityManagerFactory",
        transactionManagerRef = "newTransactionManager"
)
public class AgirhNewConfig {

    @Bean(name = "newDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.agirhnew")
    public DataSource newDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "newEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean newEntityManagerFactory(
            @Qualifier("newDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.HOSIX.AGiRH.Entity.AGiRHNew");
        emf.setPersistenceUnitName("new");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(adapter);

        // Ajout des propriétés JPA/Hibernate
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); // Ajustez selon votre DB
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.format_sql", "true");
        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    @Bean(name = "newTransactionManager")
    public PlatformTransactionManager newTransactionManager(
            @Qualifier("newEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}