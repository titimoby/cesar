package org.mixit.cesar.site.repository;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = {
        "org.mixit.cesar.site.repository",
        "org.mixit.cesar.cfp.repository",
        "org.mixit.cesar.security.repository"
})
@PropertySource("classpath:application.properties")
public class DataSourceTestConfig {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username(env.getProperty("db.username"))
                .password(env.getProperty("db.password"))
                .url(env.getProperty("db.url"))
                .driverClassName(env.getProperty("db.driver"))
                .build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        // will set the provider to 'org.hibernate.ejb.HibernatePersistence'
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaDialect(new HibernateJpaDialect());
        factory.setPackagesToScan("org.mixit.cesar.site.model", "org.mixit.cesar.cfp.model", "org.mixit.cesar.security.model");
        factory.setDataSource(dataSource());
        // This will trigger the creation of the entity manager factory
        factory.afterPropertiesSet();

        return factory.getObject();
    }

}
