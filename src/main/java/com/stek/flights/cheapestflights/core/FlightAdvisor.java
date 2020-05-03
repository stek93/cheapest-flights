package com.stek.flights.cheapestflights.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stek.flights.cheapestflights.model.Airport;
import com.stek.flights.cheapestflights.model.AirportMapper;
import com.stek.flights.cheapestflights.model.Flight;
import com.stek.flights.cheapestflights.model.response.AirportResponse;
import com.stek.flights.cheapestflights.model.response.RouteSuggestion;
import com.stek.flights.cheapestflights.service.repository.FlightRepository;

@Component
public class FlightAdvisor {

	private AirportMapper airportMapper;

	private List<Flight> allFlights;

	private Set<Airport> checkedAirports;

	private Set<Airport> uncheckedAirports;

	private Map<Airport, Airport> predecessors;

	private Map<Airport, Double> flightCost;

	private FlightRepository flightRepository;

	@Autowired
	public FlightAdvisor(AirportMapper airportMapper, FlightRepository flightRepository) {
		this.airportMapper = airportMapper;
		this.flightRepository = flightRepository;
	}

	public void findCheapFlights(Airport sourceAirport) {
		allFlights = flightRepository.findAll();
		checkedAirports = new HashSet<>();
		uncheckedAirports = new HashSet<>();
		flightCost = new HashMap<>();
		predecessors = new HashMap<>();

		flightCost.put(sourceAirport, 0.0);
		uncheckedAirports.add(sourceAirport);

		while (uncheckedAirports.size() > 0) {
			Airport airport = findSmallerPriceAirport();
			checkedAirports.add(airport);
			uncheckedAirports.remove(airport);
			findMinimalFlightCost(airport);
		}
	}

	private void findMinimalFlightCost(Airport currentAirport) {
		Map<Flight, Airport> connectedAirports = getConnectedAirports(currentAirport);

		for (Map.Entry<Flight, Airport> connections : connectedAirports.entrySet()) {
			Flight connectedFlight = connections.getKey();
			Airport connectedAirport = connections.getValue();
			Double priceWithConnection = getSmallestPrice(currentAirport) + getPrice(currentAirport, connectedAirport, connectedFlight);

			if (getSmallestPrice(connectedAirport) > priceWithConnection) {
				flightCost.put(connectedAirport, priceWithConnection);
				predecessors.put(connectedAirport, currentAirport);
				uncheckedAirports.add(connectedAirport);
			}
		}
	}

	private Double getPrice(Airport srcAirport, Airport dstAirport, Flight associatedFlight) {
		for (Flight flight : allFlights) {
			if (flight.getFrom().equals(srcAirport) && flight.getTo().equals(dstAirport) && flight.equals(associatedFlight)) {
				return flight.getFlightCost();
			}
		}

		throw new RuntimeException("Something went wrong");
	}

	private Map<Flight, Airport> getConnectedAirports(Airport airport) {
		Map<Flight, Airport> connectedAirports = new HashMap<>();
		for (Flight flight : allFlights) {
			if (flight.getFrom().equals(airport) && !isAlreadyChecked(flight.getTo())) {
				connectedAirports.put(flight, flight.getTo());
			}
		}

		return connectedAirports;
	}

	private Airport findSmallerPriceAirport() {
		Airport smallerPriceAirport = null;
		for (Airport airport : uncheckedAirports) {
			if (smallerPriceAirport == null) {
				smallerPriceAirport = airport;
			} else {
				if (getSmallestPrice(airport) < getSmallestPrice(smallerPriceAirport)) {
					smallerPriceAirport = airport;
				}
			}
		}

		return smallerPriceAirport;
	}

	private boolean isAlreadyChecked(Airport airport) {
		return checkedAirports.contains(airport);
	}

	private Double getSmallestPrice(Airport airport) {
		Double price = flightCost.get(airport);
		if (price == null) {
			return Double.MAX_VALUE;
		} else {
			return price;
		}
	}

	public RouteSuggestion getRouteSuggestion(Airport targetAirport) {
		List<AirportResponse> flightRoute = new ArrayList<>();

		Airport airport = targetAirport;
		if (predecessors.get(airport) == null) {
			return null;
		}

		flightRoute.add(airportMapper.toDto(airport));
		while (predecessors.get(airport) != null) {
			airport = predecessors.get(airport);
			flightRoute.add(airportMapper.toDto(airport));
		}

		Collections.reverse(flightRoute);

		return new RouteSuggestion(flightRoute, flightCost.get(targetAirport));
	}

}
