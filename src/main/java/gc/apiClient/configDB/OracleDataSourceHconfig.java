package gc.apiClient.configDB;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
        basePackages = "gc.apiClient.repository.oracleH", //참고할 repository
        entityManagerFactoryRef = "oracleHEntityManagerFactory",
        transactionManagerRef = "oracleHTransactionManager"
)

@Profile("oracleH")
public class OracleDataSourceHconfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean oracleHEntityManagerFactory(EntityManagerFactoryBuilder builder,
            @Qualifier("oracleHDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("gc.apiClient.entity.oracleH")//참고할 엔티티
                .persistenceUnit("oracleH")
                .properties(hibernateProperties()) // Apply Hibernate properties here
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.orclh")
    public DataSource oracleHDataSource() { // Fix the method name
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager oracleHTransactionManager(
            @Qualifier("oracleHEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    
    
    private Map<String, Object> hibernateProperties() {//Hibernate 옵션들 설정. 
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "none");
//        hibernateProperties.put("hibernate.show_sql", true);
//        hibernateProperties.put("hibernate.format_sql", true);
        return hibernateProperties;
    }
    
}
