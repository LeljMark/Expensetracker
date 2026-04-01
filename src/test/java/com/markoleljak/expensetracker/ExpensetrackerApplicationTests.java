package com.markoleljak.expensetracker;

import com.markoleljak.expensetracker.dto.CreateExpenseRequest;
import com.markoleljak.expensetracker.dto.ExpenseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExpensetrackerApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate rest;

	@Test
	void createValidExpense() {
		TestUtil testUtil = new TestUtil(rest, port);
		String token = testUtil.RegisterLoginAndGetToken("test@test.com", "pass1234");

		ResponseEntity<ExpenseResponse> response = testUtil.createExpense(token, new BigDecimal("12.50"), LocalDate.now(), "Food");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().category()).isEqualTo("Food");
	}

	@Test
	void createExpenseWithInvalidCategory() {
		TestUtil testUtil = new TestUtil(rest, port);
		String token = testUtil.RegisterLoginAndGetToken("test1@test.com", "pass1234");

		ResponseEntity<ExpenseResponse> response = testUtil.createExpense(token, new BigDecimal("12.50"), LocalDate.now(), "NonExistentCategory");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void createExpenseWithNegativeAmount() {
		TestUtil testUtil = new TestUtil(rest, port);
		String token = testUtil.RegisterLoginAndGetToken("test2@test.com", "pass1234");

		ResponseEntity<ExpenseResponse> response = testUtil.createExpense(token, new BigDecimal("-10.00"), LocalDate.now(), "Food");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void createExpenseWithZeroAmount() {
		TestUtil testUtil = new TestUtil(rest, port);
		String token = testUtil.RegisterLoginAndGetToken("test3@test.com", "pass1234");

		ResponseEntity<ExpenseResponse> response = testUtil.createExpense(token, BigDecimal.ZERO, LocalDate.now(), "Food");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void createExpenseWithFutureDate() {
		TestUtil testUtil = new TestUtil(rest, port);
		String token = testUtil.RegisterLoginAndGetToken("test4@test.com", "pass1234");

		ResponseEntity<ExpenseResponse> response = testUtil.createExpense(token, new BigDecimal("12.50"), LocalDate.now().plusDays(5), "Food");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void testGetExpensesWithDateRange() {
		TestUtil testUtil = new TestUtil(rest, port);
		String token = testUtil.RegisterLoginAndGetToken("test5@test.com", "pass1234");

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		// Create multiple expenses with different dates
		testUtil.createExpense(token, new BigDecimal("5.00"), LocalDate.of(2024, 1, 1), "Food");
		testUtil.createExpense(token, new BigDecimal("10.00"), LocalDate.of(2024, 2, 1), "Transport");
		testUtil.createExpense(token, new BigDecimal("15.00"), LocalDate.of(2024, 3, 1), "Food");

		// Requst expenses from 15.01.2024 to 15.02.2024
		ResponseEntity<List> response = rest.exchange(
				"/api/expenses?dateFrom=2024-01-15&dateUntil=2024-02-15",
				org.springframework.http.HttpMethod.GET,
				new HttpEntity<>(headers),
				List.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(1);  // Only the February expense should be returned
	}
}
