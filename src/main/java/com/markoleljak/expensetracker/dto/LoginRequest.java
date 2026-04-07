package com.markoleljak.expensetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for handling login requests. Contains validation annotations to ensure that the email is in a valid format and the password is not blank.
 * @param email
 * @param password
 */
public record LoginRequest(
        @Email String email,
        @NotBlank String password
) {}
