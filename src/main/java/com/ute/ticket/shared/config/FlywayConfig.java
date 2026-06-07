package com.ute.ticket.shared.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }

    @Bean
    public static BeanFactoryPostProcessor ensureFlywayRunsBeforeJpa() {
        return beanFactory -> {
            String[] jpaBeanNames = {"entityManagerFactory"};
            for (String name : jpaBeanNames) {
                if (beanFactory.containsBeanDefinition(name)) {
                    beanFactory.getBeanDefinition(name)
                            .setDependsOn("flyway");
                }
            }
        };
    }
}
