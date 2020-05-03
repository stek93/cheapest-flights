package com.stek.flights.cheapestflights.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stek.flights.cheapestflights.model.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

	Optional<Airport> findByIcao(String icao);

	Optional<Airport> findByIata(String iata);

	List<Airport> findByCityName(String name);

}
