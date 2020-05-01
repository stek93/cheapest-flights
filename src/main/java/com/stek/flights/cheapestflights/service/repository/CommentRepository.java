package com.stek.flights.cheapestflights.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stek.flights.cheapestflights.model.City;
import com.stek.flights.cheapestflights.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findAllByCityOrderByCreatedDateTimeDesc(City city, Pageable pageable);

	List<Comment> findAllByCity(City city);

	Optional<Comment> findByIdAndCityId(Long id, Long cityId);

}
