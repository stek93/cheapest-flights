package com.stek.flights.cheapestflights.service;

import java.util.Map;

import com.stek.flights.cheapestflights.model.Airport;

public interface AirportService {

	Map<Long, Airport> findAllAirportsWithIdLookup();

	Map<String, Airport> findAllAirportsWithCodeLookup();

}
