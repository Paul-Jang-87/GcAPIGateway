package gc.apiClient.configDB;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "gc.apiClient.repository.postgresql", // 참고할 repository
		entityManagerFactoryRef = "postgresqlEntityManagerFactory", transactionManagerRef = "postgresqlTransactionManager")



@Profile("postgres")
public class PostgresqlDataSourceConfig {
	
	
	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("postgresqlDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("gc.apiClient.entity.postgresql")// 참고할 엔티티
				.persistenceUnit("postgres").properties(hibernateProperties()) // Apply Hibernate properties here
				.build();

	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.postgresql")
	public DataSource postgresqlDataSource() {// DB커넥션을 위한 정보들. application.properties에서 확인할 수 있다.
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	public PlatformTransactionManager postgresqlTransactionManager(
			@Qualifier("postgresqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	private Map<String, Object> hibernateProperties() {// Hibernate 옵션들 설정.
		Map<String, Object> hibernateProperties = new HashMap<>();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
//		hibernateProperties.put("hibernate.show_sql", true);
//		hibernateProperties.put("hibernate.format_sql", true);
		return hibernateProperties;
	}

}