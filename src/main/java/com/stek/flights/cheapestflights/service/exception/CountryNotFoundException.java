package com.stek.flights.cheapestflights.service.exception;

public class CountryNotFoundException extends RuntimeException {

	public CountryNotFoundException(String name) {
		super(String.format("Country not found for parameter {name=%s}", name));
	}

}
