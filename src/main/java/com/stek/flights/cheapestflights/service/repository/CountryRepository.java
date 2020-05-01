package com.stek.flights.cheapestflights.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stek.flights.cheapestflights.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	Optional<Country> findByName(String name);

}
