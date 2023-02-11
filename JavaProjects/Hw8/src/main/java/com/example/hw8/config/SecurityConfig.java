//package com.example.hw8.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.*;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter
//{
//    public void configure(HttpSecurity http)throws Exception
//    {
//        http.formLogin();
//
//        http.authorizeRequests()
//
//                .antMatchers("/club/**").permitAll()
//                .antMatchers("/student/**").permitAll()
//                .antMatchers("/**").hasRole("manager")
//                .antMatchers("/**").permitAll()
////                .antMatchers("/read/**").hasRole("manager")
////                .antMatchers("/create/**").hasRole("manager")
////                .antMatchers("/delete/**").hasRole("manager")
////                .antMatchers("/update/**").hasRole("manager")
//                .anyRequest().authenticated()
//                .and().csrf().disable();
//    }
//}
