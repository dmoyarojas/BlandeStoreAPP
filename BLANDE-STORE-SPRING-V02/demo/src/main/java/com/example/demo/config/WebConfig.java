package com.example.demo.config;

import com.example.demo.interceptor.AdminAuthInterceptor;
import com.example.demo.interceptor.CajeroAuthInterceptor;
import com.example.demo.interceptor.NoCacheInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminAuthInterceptor adminAuthInterceptor;

    @Autowired
    private CajeroAuthInterceptor cajeroAuthInterceptor;

    @Autowired
    private NoCacheInterceptor noCacheInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor para evitar caché en páginas protegidas
        registry.addInterceptor(noCacheInterceptor)
                .addPathPatterns("/admin/**", "/cajero/**");

        // Interceptores de autorización
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**");
        
        registry.addInterceptor(cajeroAuthInterceptor)
                .addPathPatterns("/cajero/**");
    }
}
