package com.markoleljak.expensetracker.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
