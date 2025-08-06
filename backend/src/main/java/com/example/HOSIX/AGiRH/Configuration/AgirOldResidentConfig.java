package com.example.HOSIX.AGiRH.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.HOSIX.AGiRH.Repository.AgirhOld",
        entityManagerFactoryRef = "oldEntityManagerFactory",
        transactionManagerRef = "oldTransactionManager"
)
public class AgirOldResidentConfig {

    @Primary
    @Bean(name = "oldDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.agirhold")
    public DataSource oldDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "oldEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oldEntityManagerFactory(
            @Qualifier("oldDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.HOSIX.AGiRH.Entity.AGiRHOld");
        emf.setPersistenceUnitName("old");

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

    @Primary
    @Bean(name = "oldTransactionManager")
    public PlatformTransactionManager oldTransactionManager(
            @Qualifier("oldEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}
