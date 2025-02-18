package com.example.cachecontrol;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
public class CacheWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        final CacheControl cacheControl = CacheControl.noCache().cachePrivate();
        interceptor.setCacheControl(cacheControl);
        registry.addInterceptor(interceptor)
                .excludePathPatterns("/resources/**");;
    }
}
