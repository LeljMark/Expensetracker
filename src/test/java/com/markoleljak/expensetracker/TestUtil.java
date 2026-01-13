package com.markoleljak.expensetracker;

import com.markoleljak.expensetracker.dto.LoginRequest;
import com.markoleljak.expensetracker.dto.LoginResponse;
import com.markoleljak.expensetracker.dto.RegisterRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO
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
}
