package com.markoleljak.expensetracker.controller;

import com.markoleljak.expensetracker.dto.CreateExpenseRequest;
import com.markoleljak.expensetracker.dto.ExpenseResponse;
import com.markoleljak.expensetracker.model.Expense;
import com.markoleljak.expensetracker.model.User;
import com.markoleljak.expensetracker.repository.UserRepository;
import com.markoleljak.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody CreateExpenseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Expense expense = expenseService.createExpense(user, request);

        ExpenseResponse response = new ExpenseResponse(
                expense.getAmount(),
                expense.getDate(),
                expense.getCategory().getName(),
                expense.getDescription()
        );

        return ResponseEntity.ok(response); // TODO ?
    }

    @GetMapping("/user/all")
    public List<ExpenseResponse> getUserExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        // TODO
        //  there isn't really a need to fetch the user - we are fetching expenses of the user
        //  who is currently logged in, and all of his information should be in the @AuthenticationPrincipal
        //  but I'm not sure if it will work because the UserDetails is Spring's object - maybe configure
        //  my own @AuthenticationPrincipal that corresponds (or is the same) as the User entity?
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return expenseService.getExpensesForUser(user)
                .stream()
                .map(expense -> new ExpenseResponse(
                        expense.getAmount(),
                        expense.getDate(),
                        expense.getCategory().getName(),
                        expense.getDescription()
                )).toList();
    }
}
