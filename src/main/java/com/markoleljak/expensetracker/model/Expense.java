package com.markoleljak.expensetracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Expense entity representing an expense record in the application. Contains details about the user, category, amount,
 * date, and description of the expense.
 */
@Entity
@Table(name = "expenses")
@Getter
@Setter
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate date;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    private String description;

    public String toString() {
        return String.format("[user=%s, amount=%s, date=%s, category=%s, description=%s]",
                user.getEmail(), amount, date, category.getName(), description);
    }
}
