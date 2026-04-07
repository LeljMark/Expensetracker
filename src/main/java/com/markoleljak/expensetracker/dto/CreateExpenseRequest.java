package com.markoleljak.expensetracker.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating a new expense. Contains validation annotations to ensure that the input data is valid before processing.
 * @param amount
 * @param date
 * @param category
 * @param description
 */
public record CreateExpenseRequest(

        @NotNull
        @DecimalMin(value = "0.01", inclusive = true)
        BigDecimal amount,

        @NotNull
        @PastOrPresent
        LocalDate date,

        @NotBlank
        String category,

        @Size(max = 255)
        String description
) {}
