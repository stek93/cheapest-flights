package com.stek.flights.cheapestflights.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stek.flights.cheapestflights.model.Airport;
import com.stek.flights.cheapestflights.service.repository.AirportRepository;

@Service
public class AirportServiceImpl implements AirportService {

	private final AirportRepository airportRepository;

	@Autowired
	public AirportServiceImpl(AirportRepository airportRepository) {
		this.airportRepository = airportRepository;
	}

	@Override
	public Map<Long, Airport> findAllAirportsWithIdLookup() {
		return airportRepository.findAll().stream().collect(Collectors.toMap(Airport::getId, Function.identity()));
	}

	@Override
	public Map<String, Airport> findAllAirportsWithCodeLookup() {
		List<Airport> airportList = airportRepository.findAll();
		Map<String, Airport> airportHashMap = new HashMap<>();
		airportList.forEach(airport -> {
			airportHashMap.put(airport.getIata(), airport);
			airportHashMap.put(airport.getIcao(), airport);
		});

		return airportHashMap;
	}
}
