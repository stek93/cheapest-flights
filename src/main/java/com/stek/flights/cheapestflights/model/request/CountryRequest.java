package com.stek.flights.cheapestflights.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CountryRequest {

	@NotBlank(message = "Country name is mandatory")
	private String name;

}
