package com.markoleljak.expensetracker.repository;

import com.markoleljak.expensetracker.model.Category;
import com.markoleljak.expensetracker.model.Expense;
import com.markoleljak.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserOrderByDateDesc(User user);

    List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

    List<Expense> findByUserAndCategory(User user, Category category);


}
