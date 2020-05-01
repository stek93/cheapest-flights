package com.stek.flights.cheapestflights.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.stek.flights.cheapestflights.model.City;
import com.stek.flights.cheapestflights.model.Comment;
import com.stek.flights.cheapestflights.model.request.CreateCity;
import com.stek.flights.cheapestflights.model.request.CreateComment;
import com.stek.flights.cheapestflights.model.response.CityResponse;
import com.stek.flights.cheapestflights.model.response.CommentResponse;
import com.stek.flights.cheapestflights.service.exception.CountryNotFoundException;

public interface CityService {

	City saveCity(CreateCity createCity) throws CountryNotFoundException;

	List<CityResponse> findAllCities();

	List<CityResponse> findAllCities(Pageable pageable);

	Optional<CityResponse> findCityByName(String name);

	Optional<CityResponse> findCityByName(String name, Pageable pageable);

	Optional<CityResponse> findCityById(Long id);

	Comment saveComment(Long cityId, CreateComment createComment);

	Optional<CommentResponse> findCommentDtoByCityIdAndCommentId(Long cityId, Long commentId);

	Map<String, City> getAllCitiesLookupByNames();

}
