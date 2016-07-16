package hu.hevi.havesomerest.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    HandlerInterceptor yourInjectedInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("ADD INTERCEPTOR");
        registry.addInterceptor(yourInjectedInterceptor);
    }
}