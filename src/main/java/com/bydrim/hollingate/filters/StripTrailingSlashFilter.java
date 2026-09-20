package com.bydrim.hollingate.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class StripTrailingSlashFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // this doesn't include query params
        String reqPath = request.getRequestURI();
        // there is a single slash after host, which is ok
        if (reqPath.lastIndexOf('/') == reqPath.indexOf('/')) {
            filterChain.doFilter(request, response);
            return;
        }
        // last slash is not the last char in the path
        if (reqPath.charAt(reqPath.length() - 1) != '/') {
            filterChain.doFilter(request, response);
            return;
        }

        // last slash is the last char and needs to be removed
        StringBuffer urlBuff = request.getRequestURL();
        urlBuff.deleteCharAt(urlBuff.length() - 1);
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(urlBuff);
        String queryStr = request.getQueryString();
        if (null != queryStr) {
            urlBuilder.append('?');
            urlBuilder.append(queryStr);
        }
        response.sendRedirect(urlBuilder.toString());
        filterChain.doFilter(request, response);
    }
}
