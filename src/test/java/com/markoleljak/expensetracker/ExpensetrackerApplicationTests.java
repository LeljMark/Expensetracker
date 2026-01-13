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

		String token = testUtil.RegisterLoginAndGetToken("testUser525287@gmail.com", "psswd1234");

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		CreateExpenseRequest request = new CreateExpenseRequest(
				new BigDecimal("12.50"),
				LocalDate.now(),
				"Food",
				"Lobster"
		);

		HttpEntity<CreateExpenseRequest> entity = new HttpEntity<>(request, headers);
		ResponseEntity<ExpenseResponse> response =
				rest.postForEntity(
						"/api/expenses/create",
						entity,
						ExpenseResponse.class
				);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().category()).isEqualTo("Food");
	}

	@Test
	void createExpenseWithInvalidCategory() {

	}

}
