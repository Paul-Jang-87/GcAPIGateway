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
        basePackages = "gc.apiClient.repository.oracleH", // 레토지토리 패키지 참조
        entityManagerFactoryRef = "oracleHEntityManagerFactory",
        transactionManagerRef = "oracleHTransactionManager"
)

/*
 * 360view home 관련 db에 관려한 설정들을 정의해주는 클래스
 * 엔티티 매니저 팩토리, 트랜젝션 매니저 팩토리를 정의하고 프로젝트 전역에서 사용할 수 있도록 도와주는 역할의 클래스.
 */
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

    
    //프로젝트 내부에 있는 파일인 'application.properties'에서 'spring.datasource.orclh'로 시작하는 키들을 참조하여 값을 가지고와 'DataSourceBuilder'를 만든다.   
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