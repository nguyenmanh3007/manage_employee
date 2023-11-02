package com.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Bean
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
//        threadPoolTaskExecutor.setCorePoolSize(500); //số lượng thread tối thiểu được giữ trong thread pool
//        threadPoolTaskExecutor.setMaxPoolSize(100);// số lượng thread mới tối đa  được tạo ra, quá 10 thì requeest bị từ chối
//        threadPoolTaskExecutor.setQueueCapacity(5000); // só lượng tác vụ tối đa được giữ trong hàng đợi
//        threadPoolTaskExecutor.setThreadNamePrefix("Async-example-thread-");
//        threadPoolTaskExecutor.initialize(); // khởi tạo đối tượng thread pool
    }
}
