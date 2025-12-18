package com.markoleljak.expensetracker.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String category) {
        super(category);
    }
}
