package com.stek.flights.cheapestflights.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateComment {

	@NotBlank(message = "Description is mandatory")
	private String description;

}
