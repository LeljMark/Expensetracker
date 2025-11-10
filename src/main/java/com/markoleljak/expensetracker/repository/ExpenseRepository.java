package com.markoleljak.expensetracker.repository;

import com.markoleljak.expensetracker.model.Expense;
import com.markoleljak.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
}
