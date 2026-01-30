package com.markoleljak.expensetracker.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        log.warn(
                "Unauthorized access attempt to {} - reason={}",
                request.getRequestURI(),
                authException.getClass().getSimpleName()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String errorMessage = "Unauthorized.";

        String json = """
                {
                    "error": "%s",
                    "message": "%s"
                }
                """.formatted(authException.getClass().getSimpleName(), errorMessage);

        response.getWriter().write(json);
    }
}
