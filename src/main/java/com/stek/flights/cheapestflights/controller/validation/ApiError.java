package com.stek.flights.cheapestflights.controller.validation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

	private HttpStatus status;

	private int statusCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime timestamp;

	private String message;

	private String debugMessage;

	private List<ApiValidationError> validationErrors;

	private ApiError() {
		timestamp = LocalDateTime.now();
	}

	public ApiError(HttpStatus status) {
		this();
		this.status = status;
		this.statusCode = status.value();
	}

	public ApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.statusCode = status.value();
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	public ApiError(HttpStatus status, Throwable ex, String message, List<ApiValidationError> validationErrors) {
		this();
		this.status = status;
		this.statusCode = status.value();
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
		this.validationErrors = validationErrors;
	}

}
