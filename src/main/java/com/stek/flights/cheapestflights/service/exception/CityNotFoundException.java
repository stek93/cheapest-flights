package com.stek.flights.cheapestflights.service.exception;

public class CityNotFoundException extends RuntimeException {

	public CityNotFoundException(String name) {
		super(String.format("City not found for parameter {name=%s}", name));
	}

	public CityNotFoundException(String source, String destination) {
		super(String.format("City combination not found for parameters {source=%s}/{destination=%s}", source, destination));
	}

	public CityNotFoundException(Long id) {
		super(String.format("City not found for parameter {id=%d}", id));
	}
}
