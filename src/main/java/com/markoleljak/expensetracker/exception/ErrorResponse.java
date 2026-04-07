package com.markoleljak.expensetracker.exception;

import java.time.LocalDateTime;

/**
 * A record representing the structure of an error response returned by the API when an exception occurs.
 * It contains the timestamp of the error, the HTTP status code, a brief error description, and a detailed message.
 *
 * @param timestamp The date and time when the error occurred.
 * @param status    The HTTP status code associated with the error.
 * @param error     A brief description of the error type.
 * @param message   A detailed message providing more information about the error.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message) {}
