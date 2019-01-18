#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
package ${package}.config;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;


@Configuration
@EnableCaching(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)
public class CacheConfig extends CachingConfigurerSupport {

  private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @PreDestroy
  public void destroy() {
    logger.info("Closing Cache Manager");
  }

  @Override
  @Bean(name = "cacheManager")
  public CacheManager cacheManager() {
    logger.debug("CaffeineCacheManager");
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    simpleCacheManager.setCaches(Arrays.asList(enumCache(), userIdTokenResetPassword(), errors()));
    return simpleCacheManager;
  }

  @Bean
  CaffeineCache enumCache() {
    CaffeineCache cache = new CaffeineCache("enumCache", Caffeine.newBuilder().build());
    return cache;
  }

  @Bean
  CaffeineCache userIdTokenResetPassword() {
    CaffeineCache cache = new CaffeineCache("userIdTokenResetPassword", Caffeine.newBuilder()
        .maximumSize(100).expireAfterWrite(2, TimeUnit.HOURS)
        .build());
    return cache;
  }
  
  @Bean
  CaffeineCache errors() {
    CaffeineCache cache = new CaffeineCache("errors", Caffeine.newBuilder()
        .maximumSize(1000).expireAfterWrite(5, TimeUnit.MINUTES)
        .build());
    return cache;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.cache.annotation.CachingConfigurerSupport${pound}keyGenerator()
   * @see org.springframework.cache.interceptor.CacheExpressionRootObject
   * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/${pound}cache-annotations
   */
  @Override
  @Bean
  public KeyGenerator keyGenerator() {
    return new SimpleKeyGenerator();
  }
}
