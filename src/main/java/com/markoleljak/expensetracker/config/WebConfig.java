package com.markoleljak.expensetracker.config;

import com.markoleljak.expensetracker.logging.RequestResponseLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class that implements WebMvcConfigurer to add the RequestResponseLoggingInterceptor to the application's interceptor registry.
 * This allows for logging of incoming requests and outgoing responses for debugging and monitoring purposes.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestResponseLoggingInterceptor interceptor;

    public WebConfig(RequestResponseLoggingInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
