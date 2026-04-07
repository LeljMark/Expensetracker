package com.markoleljak.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for representing an expense response. Contains details about the amount, date, category, and description of the expense.
 * @param amount
 * @param date
 * @param category
 * @param description
 */
public record ExpenseResponse(
        BigDecimal amount,
        LocalDate date,
        String category,
        String description
) {}
