package com.natwest.transfer;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TransferServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	public void testApplicationStart() {
		int statusCode = RestAssured.get("http://localhost:" + port + "/api").statusCode();
		assertEquals(HttpStatus.OK.value(), statusCode);
	}

}
