package com.markoleljak.expensetracker;

import com.markoleljak.expensetracker.dto.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility class for integration tests. Provides methods for common test operations like registering, logging in, and creating expenses.
 */
public class TestUtil {

    private TestRestTemplate rest;
    private int port;

    public TestUtil(TestRestTemplate rest, int port) {
        this.rest = rest;
        this.port = port;
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    public String RegisterLoginAndGetToken(String email, String password) {

        RegisterRequest registerRequest = new RegisterRequest(email, password);
        rest.postForEntity(
                url("/auth/register"),
                registerRequest,
                String.class
        );

        LoginRequest loginRequest = new LoginRequest(email, password);
        ResponseEntity<LoginResponse> response = rest.postForEntity(
                url("/auth/login"),
                loginRequest,
                LoginResponse.class
        );

        LoginResponse body = response.getBody();

        assertThat(body.getToken()).isNotNull();
        assertThat(body.getToken()).isNotEmpty();

        return body.getToken();
    }

    public ResponseEntity<ExpenseResponse> createExpense(String token, BigDecimal amount, LocalDate date, String category) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        CreateExpenseRequest request = new CreateExpenseRequest(
                amount,
                date,
                category,
                "Test"
        );

        HttpEntity<CreateExpenseRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ExpenseResponse> response = rest.postForEntity(
                "/api/expenses",
                entity,
                ExpenseResponse.class
        );

        return response;
    }
}
