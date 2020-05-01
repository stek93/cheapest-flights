package com.stek.flights.cheapestflights.controller.validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiValidationError {

	private String object;
	private String field;
	private Object rejectedValue;
	private String message;

	public ApiValidationError(String object, String message) {
		this.object = object;
		this.message = message;
	}

	public ApiValidationError(String object, String field, Object rejectedValue, String message) {
		this(object, message);
		this.field = field;
		this.rejectedValue = rejectedValue;
	}
}
