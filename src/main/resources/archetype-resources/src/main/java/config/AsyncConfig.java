package ${package}.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)
public class AsyncConfig implements AsyncConfigurer, SchedulingConfigurer {

  @Autowired
  private Environment environment;

  /**
   * for use with alert engine event processor and schedule tasks
   * @return {@link ThreadPoolTaskScheduler}
   */
  @Bean(destroyMethod = "shutdown")
  public ThreadPoolTaskScheduler scheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setThreadNamePrefix("sch-");
    threadPoolTaskScheduler.setThreadGroupName("scheduler-");
    threadPoolTaskScheduler.setPoolSize(environment.getProperty("poolSizeShedule", Integer.class, 50));
    threadPoolTaskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    if (this.environment.getProperty("WaitForTasksToCompleteOnShutdown") != null) {
      threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(this.environment.getProperty("WaitForTasksToCompleteOnShutdown", Boolean.class, false));
    }
    return threadPoolTaskScheduler;
  }

  /**
   * The asynchronous task executor used by the application.
   */
  @Override
  @Bean(name = { "taskExecutor" })
  public Executor getAsyncExecutor() {
    return Executors.newScheduledThreadPool(5, scheduler());
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(scheduler());
  }


  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
