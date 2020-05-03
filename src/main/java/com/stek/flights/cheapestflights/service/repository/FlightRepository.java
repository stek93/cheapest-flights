package com.stek.flights.cheapestflights.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stek.flights.cheapestflights.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
}
