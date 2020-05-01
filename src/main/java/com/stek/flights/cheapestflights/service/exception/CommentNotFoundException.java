package com.stek.flights.cheapestflights.service.exception;

public class CommentNotFoundException extends RuntimeException {

	public CommentNotFoundException(Long id, Long cityId) {
		super(String.format("Comment not found for the combination of parameters {cityId=%d} and {commentId=%d}", cityId, id));
	}

}
