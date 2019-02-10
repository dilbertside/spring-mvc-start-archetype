package ${package}.config;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import ${package}.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Application.class)
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
class JpaConfig {

  @Configuration
  @Profile({"!test"})
  @PropertySource("classpath:persistence.properties")
  static class StandardConfig {
    
  }
  
  @Autowired
  Environment environment;

  @Bean
  @Profile({"!test"})
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(environment.getProperty("dataSource.driverClassName"));
    config.setJdbcUrl(environment.getProperty("dataSource.url"));
    config.setUsername(environment.getProperty("dataSource.username", "sa"));
    config.setPassword(environment.getProperty("dataSource.password",""));
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    config.addDataSourceProperty("useServerPrepStmts", "true");

    return new HikariDataSource(config);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);

    String entities = ClassUtils.getPackageName(Application.class);
    String converters = ClassUtils.getPackageName(Jsr310JpaConverters.class);
    entityManagerFactoryBean.setPackagesToScan(entities, converters);

    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());

    Map<String, Object> jpaProperties = entityManagerFactoryBean.getJpaPropertyMap();
    jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, environment.getProperty("hibernate.hbm2ddl.auto", "update"));
    jpaProperties.put(org.hibernate.cfg.Environment.FORMAT_SQL, environment.getProperty("hibernate.format_sql", "false"));
    jpaProperties.put(org.hibernate.cfg.Environment.USE_SQL_COMMENTS, environment.getProperty("hibernate.use_sql_comments", "false"));
    return entityManagerFactoryBean;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
    hjva.setDatabase(resolveDatabase(environment.getProperty("dataSource.url")));
    hjva.setDatabasePlatform(environment.getProperty("hibernate.dialect"));
    hjva.setShowSql(environment.getProperty("hibernate.showSql", Boolean.class, Boolean.FALSE));
    hjva.setGenerateDdl(environment.getProperty("hibernate.generateDdl", Boolean.class, Boolean.FALSE));
    return hjva;
  }

  @Bean
  public JpaDialect jpaDialect() {
    return new HibernateJpaDialect();
  }

  private Database resolveDatabase(String url) {
    Database db = null;
    if (StringUtils.hasLength(url)) {
      Assert.isTrue(url.startsWith("jdbc"), "URL must start with 'jdbc'");
      String dbType = StringUtils.split(url, ":")[1];
      for (Database elt : Database.values()) {
        if (StringUtils.startsWithIgnoreCase(dbType, elt.name())) {
          db = elt;
          break;
        }
      }
    }
    return db;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
