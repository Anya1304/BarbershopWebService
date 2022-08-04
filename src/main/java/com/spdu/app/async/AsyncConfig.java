package com.spdu.app.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "mailSenderExecutorService")
    public Executor mailSenderExecutorService(@Value("${mailExecutorService.corePoolSize}") int size,
                                              @Value("${mailExecutorService.maxPoolSize}") int maxPoolSize) {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(size);
        threadPoolExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolExecutor.initialize();

        return threadPoolExecutor;
    }
}
