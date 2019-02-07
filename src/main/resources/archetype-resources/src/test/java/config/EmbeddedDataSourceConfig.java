package ${package}.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.util.ClassUtils;
import org.thymeleaf.exceptions.ConfigurationException;

import javax.sql.DataSource;

/**
 * The data source config that can be used in integration tests.
 */
@Configuration
@Profile("test")
public class EmbeddedDataSourceConfig {

  private static final boolean hsqldbPresent = ClassUtils.isPresent("org.hsqldb.jdbcDriver", WebAppConfigurationAware.class.getClassLoader());
  private static final boolean h2dbPresent = ClassUtils.isPresent("org.h2.Driver", WebAppConfigurationAware.class.getClassLoader());

  @Autowired
  Environment environment;
  
  
  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseType edt;
    if(hsqldbPresent) {
      edt = EmbeddedDatabaseType.HSQL;
    } else if (h2dbPresent) {
      edt = EmbeddedDatabaseType.H2;
    } else {
      throw new ConfigurationException("NO database driver available");
    }
    return new EmbeddedDatabaseBuilder()
            .setType(edt)
            .build();
  }
}
