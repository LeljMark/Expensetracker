package com.markoleljak.expensetracker.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger("RequestResponseLoggingInterceptor");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info(String.format("Request received: method=%s, path=%s",
                request.getMethod(), request.getRequestURI()));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info(String.format("Sending response: method=%s, path=%s, status=%s",
                request.getMethod(), request.getRequestURI(), response.getStatus()));
    }
}
