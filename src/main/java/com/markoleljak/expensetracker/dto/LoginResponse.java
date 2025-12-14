package com.markoleljak.expensetracker.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private String token;
    private String email;

    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}