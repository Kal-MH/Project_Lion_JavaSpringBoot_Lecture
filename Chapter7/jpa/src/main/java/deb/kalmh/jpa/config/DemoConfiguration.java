package deb.kalmh.jpa.config;

import deb.kalmh.jpa.filter.TransactionLogFilter;
import deb.kalmh.jpa.interceptor.HeaderLogginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DemoConfiguration implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(DemoConfiguration.class);

    private final HeaderLogginInterceptor headerLogginInterceptor;

    public DemoConfiguration(
            @Autowired HeaderLogginInterceptor headerLogginInterceptor
    ) {
        this.headerLogginInterceptor = headerLogginInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //interceptor등록을 위한 함수
        registry
                .addInterceptor(headerLogginInterceptor)
                .addPathPatterns("/post/**")
                .excludePathPatterns("/except/**");
    }
}
