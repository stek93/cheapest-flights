package com.stek.flights.cheapestflights.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stek.flights.cheapestflights.core.FlightAdvisor;
import com.stek.flights.cheapestflights.model.City;
import com.stek.flights.cheapestflights.model.response.RouteSuggestion;
import com.stek.flights.cheapestflights.service.exception.CityNotFoundException;
import com.stek.flights.cheapestflights.service.repository.CityRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/flights")
public class FlightController {

	private final FlightAdvisor flightAdvisor;

	private final CityRepository cityRepository;

	@Autowired
	public FlightController(FlightAdvisor flightAdvisor, CityRepository cityRepository) {
		this.flightAdvisor = flightAdvisor;
		this.cityRepository = cityRepository;
	}

	@GetMapping
	public ResponseEntity<?> getCheapestFlightBetweenTwoCities(@RequestParam String from, @RequestParam String to, @RequestParam(required = false, defaultValue = "false") boolean showMin) {
		log.info("GET /flights/min-cost?from={}&to={}", from, to);

		List<RouteSuggestion> routeSuggestions = new ArrayList<>();

		Optional<City> optionalSourceCity = cityRepository.findByName(from);
		Optional<City> optionalDestinationCity = cityRepository.findByName(to);

		if (optionalSourceCity.isEmpty() || optionalDestinationCity.isEmpty()) {
			throw new CityNotFoundException(from, to);
		}

		// we want to iterate through all possible airports (from both cities) combinations in order to find
		// all flight routes and to highlight best of them
		optionalSourceCity.get().getAirports().forEach(sourceAirport -> {
			optionalDestinationCity.get().getAirports().forEach(destinationAirport -> {
				flightAdvisor.findCheapFlights(sourceAirport);
				RouteSuggestion routeSuggestion = flightAdvisor.getRouteSuggestion(destinationAirport);

				if (routeSuggestion != null) {
					routeSuggestions.add(routeSuggestion);
				}
			});
		});

		routeSuggestions.sort(Comparator.comparingDouble(RouteSuggestion::getTotalFlightPrice));

		if (showMin && routeSuggestions.size() > 0) {
			return ResponseEntity.ok(routeSuggestions.get(0));
		}

		return ResponseEntity.ok(routeSuggestions);
	}

}
