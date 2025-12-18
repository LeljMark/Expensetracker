package com.markoleljak.expensetracker.service;

import com.markoleljak.expensetracker.dto.CreateExpenseRequest;
import com.markoleljak.expensetracker.exception.CategoryNotFoundException;
import com.markoleljak.expensetracker.model.Category;
import com.markoleljak.expensetracker.model.Expense;
import com.markoleljak.expensetracker.model.User;
import com.markoleljak.expensetracker.repository.CategoryRepository;
import com.markoleljak.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public Expense createExpense(User user, CreateExpenseRequest request) {
        Category category = categoryRepository
                .findByName(request.category())
                .orElseThrow(() ->
                        new CategoryNotFoundException(request.category())
                );

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setAmount(request.amount());
        expense.setCategory(category);
        expense.setDescription(request.description());
        expense.setDate(request.date());
        expense.setCreatedAt(Instant.now());

        return expenseRepository.save(expense);
    }

}
