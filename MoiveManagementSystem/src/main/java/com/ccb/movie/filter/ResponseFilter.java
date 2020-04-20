package com.ccb.movie.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter("respnose")
public class ResponseFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization, Cookie, Set-Cookie,X-Auth-Token");
        res.setHeader("Access-Control-Allow-Methods", "PUT,GET,POST,DELETE,OPTIONS");
        res.setHeader("Access-Control-Allow-Origin", "http://www.movie.com");
        res.setHeader("Allow", "PUT,GET,POST,DELETE,OPTIONS");
        filterChain.doFilter(servletRequest,res);
    }
}
