package com.stek.flights.cheapestflights.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateCity {

	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Description is mandatory")
	private String description;

	@Valid
	private CountryRequest country;

}
