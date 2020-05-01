package com.stek.flights.cheapestflights.controller.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.stek.flights.cheapestflights.service.exception.CityNotFoundException;
import com.stek.flights.cheapestflights.service.exception.CommentNotFoundException;
import com.stek.flights.cheapestflights.service.exception.CountryNotFoundException;
import com.stek.flights.cheapestflights.service.exception.FileProcessingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = "Validation errors";
		List<ApiValidationError> validationErrors = new ArrayList<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			String fieldName = error.getField();
			Object rejectedValue = error.getRejectedValue();
			String defaultMessage = error.getDefaultMessage();
			String objectName = error.getObjectName();
			validationErrors.add(new ApiValidationError(objectName, fieldName, rejectedValue, defaultMessage));
		});

		return buildResponseEntity(new ApiError(status, ex, message, validationErrors));
	}

	@ExceptionHandler(value = { CityNotFoundException.class, CountryNotFoundException.class, CommentNotFoundException.class })
	public ResponseEntity<?> handleEntityNotFound(Exception ex) {
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
	}

	@ExceptionHandler(value = { FileProcessingException.class })
	public ResponseEntity<?> handleFileUploadFailed(FileProcessingException ex) {
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
