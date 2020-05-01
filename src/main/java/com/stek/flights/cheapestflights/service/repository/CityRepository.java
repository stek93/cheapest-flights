package com.stek.flights.cheapestflights.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stek.flights.cheapestflights.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	Optional<City> findByName(String name);

}
