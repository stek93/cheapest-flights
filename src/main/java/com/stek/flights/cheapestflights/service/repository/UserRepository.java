package com.stek.flights.cheapestflights.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stek.flights.cheapestflights.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
