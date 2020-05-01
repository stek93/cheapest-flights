package com.stek.flights.cheapestflights.model.response;

import java.util.List;

import lombok.Data;

@Data
public class CityResponse {

	private Long id;

	private String name;

	private String description;

	private CountryResponse country;

	private List<CommentResponse> comments;

}
