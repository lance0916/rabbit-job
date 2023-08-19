package com.example.common;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WuQinglong
 */
public class RepeatReadBodyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ServletRequestWrapper wrapper;
        if (request instanceof ServletRequestWrapper) {
            wrapper = (ServletRequestWrapper) request;
        } else {
            wrapper = new ServletRequestWrapper(request);
        }
        filterChain.doFilter(wrapper, response);
    }
}
