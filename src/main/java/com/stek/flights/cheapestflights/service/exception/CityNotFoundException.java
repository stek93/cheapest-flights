package com.stek.flights.cheapestflights.service.exception;

public class CityNotFoundException extends RuntimeException {

	public CityNotFoundException(String name) {
		super(String.format("City not found for parameter {name=%s}", name));
	}

	public CityNotFoundException(Long id) {
		super(String.format("City not found for parameter {id=%d}", id));
	}
}
