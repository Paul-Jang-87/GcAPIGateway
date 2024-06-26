package gc.apiClient.configDB;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "gc.apiClient.repository.oracleH", // Reference to repository package
        entityManagerFactoryRef = "oracleHEntityManagerFactory",
        transactionManagerRef = "oracleHTransactionManager"
)
@Profile("oracleH")
public class OracleDataSourceHconfig {

    @Autowired
    private EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleHEntityManagerFactory(
            @Qualifier("oracleHDataSource") DataSource dataSource) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("gc.apiClient.entity.oracleH") // Reference to entity package
                .persistenceUnit("oracleH")
                .properties(hibernateProperties()) // Apply Hibernate properties here
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.orclh")
    public DataSource oracleHDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager oracleHTransactionManager(
            @Qualifier("oracleHEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "none");
        // hibernateProperties.put("hibernate.show_sql", true);
        // hibernateProperties.put("hibernate.format_sql", true);
        return hibernateProperties;
    }
}
