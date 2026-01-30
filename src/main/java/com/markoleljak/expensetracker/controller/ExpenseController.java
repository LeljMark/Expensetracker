package com.markoleljak.expensetracker.controller;

import com.markoleljak.expensetracker.dto.CreateExpenseRequest;
import com.markoleljak.expensetracker.dto.ExpenseResponse;
import com.markoleljak.expensetracker.model.Expense;
import com.markoleljak.expensetracker.model.User;
import com.markoleljak.expensetracker.repository.UserRepository;
import com.markoleljak.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService, UserRepository userRepository) {
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody CreateExpenseRequest request,
            @AuthenticationPrincipal User user) {

        Expense expense = expenseService.createExpense(user, request);

        ExpenseResponse response = new ExpenseResponse(
                expense.getAmount(),
                expense.getDate(),
                expense.getCategory().getName(),
                expense.getDescription()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<ExpenseResponse> getUserExpenses(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateUntil
    ) {
        return expenseService.getExpensesForUser(user, dateFrom, dateUntil)
                .stream()
                .map(expense -> new ExpenseResponse(
                        expense.getAmount(),
                        expense.getDate(),
                        expense.getCategory().getName(),
                        expense.getDescription()
                )).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id, @AuthenticationPrincipal User user) {
        expenseService.deleteExpense(id, user.getId());
    }
}
