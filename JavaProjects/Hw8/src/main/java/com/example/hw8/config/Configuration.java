//package com.example.hw8.config;
//
//import com.example.hw8.filter.PubFilter;
//import com.example.hw8.repository.JPAStudentRepository;
////import jakarta.servlet.Filter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.context.annotation.Bean;
//
//import javax.servlet.Filter;
//import java.util.ArrayList;
//import java.util.List;
//
//@org.springframework.context.annotation.Configuration
//public class Configuration
//{
//    @Bean
//    public FilterRegistrationBean<Filter> filterRegistrationBean()
//    {
//        FilterRegistrationBean<Filter> filterRegistrationBean=new FilterRegistrationBean<>();
//        List<String> patterns=new ArrayList<>();
//        patterns.add("/club");patterns.add("/club/two");patterns.add("/student/two");
//        filterRegistrationBean.setUrlPatterns(patterns);
//        filterRegistrationBean.setFilter(new PubFilter());
//
//        return filterRegistrationBean;
//    }
//}
