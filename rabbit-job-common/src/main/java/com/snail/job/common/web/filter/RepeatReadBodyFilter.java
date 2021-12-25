package com.snail.job.common.web.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WuQinglong created on 2021/12/25 18:47
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
