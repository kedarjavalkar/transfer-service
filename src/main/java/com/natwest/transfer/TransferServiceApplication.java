package com.natwest.transfer;

import static com.natwest.transfer.validation.StatusMessagDTO.SUCCESS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natwest.transfer.exception.ApiExceptionHandler;
import com.natwest.transfer.log.Loggable;

@SpringBootApplication
public class TransferServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransferServiceApplication.class, args);
	}

	@ControllerAdvice
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public class ExceptionHandler extends ApiExceptionHandler {
	}

	@RestController
	public class Hello {
		@Loggable
		@GetMapping
		public ResponseEntity<Object> world() {
			return ResponseEntity.ok(SUCCESS.setMessage("How you doing ... ?"));
		}
	}

}
