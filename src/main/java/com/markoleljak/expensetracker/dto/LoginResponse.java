package com.markoleljak.expensetracker.dto;

import lombok.Getter;

/**
 * DTO for representing a login response. Contains the JWT token and the email of the authenticated user.
 */
@Getter
public class LoginResponse {

    private String token;
    private String email;

    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}