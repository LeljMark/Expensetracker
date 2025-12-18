package com.markoleljak.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(
        BigDecimal amount,
        LocalDate date,
        String category,
        String description
) {}
