//package com.example.hw8.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpStatus;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//
//public class PubFilter implements Filter
//{
//    public void init(FilterConfig filterConfig)throws ServletException
//    {
//        Filter.super.init(filterConfig);
//    }
//
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)throws ServletException, IOException
//    {
//        String ageString=servletRequest.getParameter("age");
//        HttpServletResponse httpServletResponse=(HttpServletResponse)servletResponse;
//
//        System.out.println("age is: "+ageString);
//        int age=Integer.parseInt(ageString);
//
//        if(age<18)
//        {
//            System.err.print("You are too young, under 18! ");
//
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            httpServletResponse.setContentType("application/json");
//            ObjectMapper mapper=new ObjectMapper();
//            PrintWriter out=httpServletResponse.getWriter();
//            out.print(mapper.writeValueAsString("You are too young! "));
//            out.flush();
//        }
//        else
//        {
//            filterChain.doFilter(servletRequest,servletResponse);
//        }
//    }
//
//    public void destroy()
//    {
//        Filter.super.destroy();
//    }
//}
