package com.stek.flights.cheapestflights.model.response;

import java.util.List;

import lombok.Data;

@Data
public class RouteSuggestion {

	private List<AirportResponse> airports;

	private Double totalFlightPrice;

	private int numberOfAirports;

	public RouteSuggestion(List<AirportResponse> airports, Double totalFlightPrice) {
		this.airports = airports;
		this.totalFlightPrice = totalFlightPrice;
		this.numberOfAirports = airports.size();
	}

}
