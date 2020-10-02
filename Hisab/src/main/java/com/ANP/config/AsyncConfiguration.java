package com.ANP.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
@Configuration
@EnableAsync
public class AsyncConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Value("${AsyncConfiguration.corePoolSize}")
    private Integer corePoolSize;

    @Value("${AsyncConfiguration.MaxPoolSize}")
    private Integer maxPoolSize;

    @Value("${AsyncConfiguration.QCapacity}")
    private Integer qCapacity;

    @Value("${AsyncConfiguration.ThreadNamePrefix}")
    private String threadNamePrefix;


    @Bean (name = "taskExecutor")
    public Executor taskExecutor() {
        logger.info("Creating Async Task Executor, core poolsize[" + corePoolSize + "] MaxPoolSize["
                + maxPoolSize + "] QCapacity[" + qCapacity + "] ThreadNamePrefix[" + threadNamePrefix + "]");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(qCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }
}

