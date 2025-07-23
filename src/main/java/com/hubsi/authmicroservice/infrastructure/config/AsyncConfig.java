package com.hubsi.authmicroservice.infrastructure.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    private final GlobalAsyncExceptionHandler globalAsyncExceptionHandler;

    public AsyncConfig(GlobalAsyncExceptionHandler globalAsyncExceptionHandler) {
        this.globalAsyncExceptionHandler = globalAsyncExceptionHandler;
    }

    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor= new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(10);
        threadPoolExecutor.setMaxPoolSize(12);
        threadPoolExecutor.setQueueCapacity(50);
        threadPoolExecutor.setThreadNamePrefix("AsyncExecutor-");
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return globalAsyncExceptionHandler;
    }
}
