package com.markoleljak.expensetracker.auth;

import com.markoleljak.expensetracker.TestUtil;
import com.markoleljak.expensetracker.dto.LoginResponse;
import com.markoleljak.expensetracker.dto.LoginRequest;
import com.markoleljak.expensetracker.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("Register with new email → 200 OK")
    void testRegisterNewEmail() {

        RegisterRequest req = new RegisterRequest("newTestEmail@test.com", "testPassword123");

        ResponseEntity<String> response = rest.postForEntity(
                url("/auth/register"),
                req,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Register with existing email → 400 bad request")
    void testRegisterExistingEmail() {

        RegisterRequest req = new RegisterRequest("email12345@example.com", "password123");

        // First registration should pass
        rest.postForEntity(
                url("/auth/register"),
                req,
                String.class
        );

        // Second registration should fail
        ResponseEntity<String> response = rest.postForEntity(
                url("/auth/register"),
                req,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("Email already registered");
    }

    @Test
    @DisplayName("Login with correct credentials → 200 + JWT token")
    void testLoginSuccess() {

        // Register first
        RegisterRequest register = new RegisterRequest("login@test.com", "abc12345");
        rest.postForEntity(
                url("/auth/register"),
                register,
                String.class
        );

        // Login
        LoginRequest login = new LoginRequest("login@test.com", "abc12345");

        ResponseEntity<LoginResponse> response = rest.postForEntity(
                url("/auth/login"),
                login,
                LoginResponse.class
        );

        LoginResponse body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getToken()).isNotNull();
        assertThat(body.getToken()).isNotEmpty();
    }

    @Test
    @DisplayName("Login with wrong password → 401")
    void testLoginWrongPassword() {

        // Register first
        RegisterRequest register = new RegisterRequest("password@test.com", "correct");
        rest.postForEntity(
                url("/auth/register"),
                register,
                String.class
        );

        // Wrong password
        LoginRequest login = new LoginRequest("password@test.com", "WRONG");

        ResponseEntity<LoginResponse> response = rest.postForEntity(
                url("/auth/login"),
                login,
                LoginResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Login with non-existent email -> 401")
    void testLoginUnknownEmail() {

        LoginRequest login = new LoginRequest("ghostEmail@test.com", "password42523");

        ResponseEntity<String> response = rest.postForEntity(
                url("/auth/login"),
                login,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Access protected endpoint with valid token -> 200")
    void testProtectedEndpointAuthorized() {

        TestUtil testUtil = new TestUtil(rest, port);

        String token = testUtil.RegisterLoginAndGetToken("protectedTestEmail@test.com", "pass47282");

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity entity = new org.springframework.http.HttpEntity<>(headers);

        // call protected endpoint
        ResponseEntity<String> response = rest.exchange(
                url("/api/test/protected"),
                org.springframework.http.HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("protected endpoint ok");
    }

    @Test
    @DisplayName("Access protected endpoint without token → 401")
    void testProtectedEndpointNoToken() {

        ResponseEntity<String> response = rest.getForEntity(
                url("/api/test/protected"),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Access protected endpoint with invalid token → 401")
    void testProtectedEndpointInvalidToken() {

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer invalid.token");

        HttpEntity entity = new org.springframework.http.HttpEntity<>(headers);

        ResponseEntity<String> response = rest.exchange(
                url("/api/test/protected"),
                org.springframework.http.HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
