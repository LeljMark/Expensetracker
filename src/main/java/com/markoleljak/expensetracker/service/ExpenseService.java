package com.markoleljak.expensetracker.service;

import com.markoleljak.expensetracker.dto.CreateExpenseRequest;
import com.markoleljak.expensetracker.exception.CategoryNotFoundException;
import com.markoleljak.expensetracker.exception.InvalidDatesException;
import com.markoleljak.expensetracker.exception.ResourceNotFoundException;
import com.markoleljak.expensetracker.model.Category;
import com.markoleljak.expensetracker.model.Expense;
import com.markoleljak.expensetracker.model.User;
import com.markoleljak.expensetracker.repository.CategoryRepository;
import com.markoleljak.expensetracker.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public Expense createExpense(User user, CreateExpenseRequest request) {
        log.info("Creating expense for userId={}", user.getId());
        log.debug("CreateExpenseRequest={}", request);

        Category category = categoryRepository
                .findByName(request.category())
                .orElseThrow(() -> {
                            log.warn("Category not found: {}", request.category());
                            return new ResourceNotFoundException("Category not found.");
                });

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setAmount(request.amount());
        expense.setCategory(category);
        expense.setDescription(request.description());
        expense.setDate(request.date());
        expense.setCreatedAt(Instant.now());

        Expense savedExpense = expenseRepository.save(expense);
        log.info("Expense created with id={} for userId={}", savedExpense.getId(), user.getId());

        return savedExpense;
    }

    public List<Expense> getExpensesForUser(User user, LocalDate dateFrom, LocalDate dateUntil) {
        log.info("Fetching expenses for userId={}", user.getId());
        log.debug("Filters: dateFrom={}, dateUntil={}", dateFrom, dateUntil);

        List<Expense> fetchedExpenses;
        if (dateFrom != null && dateUntil != null) {
            if (dateFrom.isAfter(dateUntil)) {
                log.warn("Invalid date range: dateFrom={} dateUntil={}", dateFrom, dateUntil);
                throw new InvalidDatesException("dateFrom cannot be after dateUntil!");
            }
            fetchedExpenses = expenseRepository.findByUserAndDateBetweenOrderByDateDesc(user, dateFrom, dateUntil);
        } else if (dateFrom != null) {
            fetchedExpenses = expenseRepository.findByUserAndAfterDateOrderByDateDesc(user, dateFrom);
        } else if (dateUntil != null) {
            fetchedExpenses = expenseRepository.findByUserAndBeforeDateOrderByDateDesc(user, dateUntil);
        } else {
            fetchedExpenses = expenseRepository.findByUserOrderByDateDesc(user);
        }

        log.info("Fetched expenses={}", fetchedExpenses);
        return fetchedExpenses;
    }

    public void deleteExpense(Long expenseId, Long userId) {
        log.info("Deleting expense id={} for userId={}", expenseId, userId);

        Expense expense = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> {
                    log.warn("Expense not found: id={} userId={}", expenseId, userId);
                    return new ResourceNotFoundException("Expense not found.");
                });

        expenseRepository.delete(expense);
        log.info("Expense deleted id={}", expenseId);
    }
}
