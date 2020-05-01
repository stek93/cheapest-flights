package com.stek.flights.cheapestflights.service.exception;

public class FileProcessingException extends RuntimeException {

	public FileProcessingException(String message) {
		super(String.format("There is an error in file {name=%s}", message));
	}

}
