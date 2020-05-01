package com.stek.flights.cheapestflights.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.stek.flights.cheapestflights.model.City;
import com.stek.flights.cheapestflights.model.CityMapper;
import com.stek.flights.cheapestflights.model.Comment;
import com.stek.flights.cheapestflights.model.CommentMapper;
import com.stek.flights.cheapestflights.model.Country;
import com.stek.flights.cheapestflights.model.request.CreateCity;
import com.stek.flights.cheapestflights.model.request.CreateComment;
import com.stek.flights.cheapestflights.model.response.CityResponse;
import com.stek.flights.cheapestflights.model.response.CommentResponse;
import com.stek.flights.cheapestflights.service.exception.CountryNotFoundException;
import com.stek.flights.cheapestflights.service.repository.CityRepository;
import com.stek.flights.cheapestflights.service.repository.CommentRepository;
import com.stek.flights.cheapestflights.service.repository.CountryRepository;

@Service
public class CityServiceImpl implements CityService {

	private final CityRepository cityRepository;

	private final CommentRepository commentRepository;

	private final CountryRepository countryRepository;

	private final CityMapper cityMapper;

	private final CommentMapper commentMapper;

	@Autowired
	public CityServiceImpl(CityRepository cityRepository, CommentRepository commentRepository, CountryRepository countryRepository, CityMapper cityMapper, CommentMapper commentMapper) {
		this.cityRepository = cityRepository;
		this.commentRepository = commentRepository;
		this.countryRepository = countryRepository;
		this.cityMapper = cityMapper;
		this.commentMapper = commentMapper;
	}

	@Override
	public City saveCity(CreateCity createCity) throws CountryNotFoundException {
		Optional<Country> optionalCountry = countryRepository.findByName(createCity.getCountry().getName());
		return optionalCountry.map(country -> {
			City city = cityMapper.toEntity(createCity);
			city.setCountry(country);
			return cityRepository.save(city);
		}).orElseThrow(() -> new CountryNotFoundException(createCity.getCountry().getName()));
	}

	@Override
	public List<CityResponse> findAllCities() {
		return cityRepository.findAll().stream().map(cityMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public List<CityResponse> findAllCities(Pageable pageable) {
		return cityRepository.findAll().stream().map(city -> {
			city.setComments(commentRepository.findAllByCityOrderByCreatedDateTimeDesc(city, pageable));
			return cityMapper.toDto(city);
		}).collect(Collectors.toList());
	}

	@Override
	public Optional<CityResponse> findCityByName(String name) {
		return cityRepository.findByName(name).map(cityMapper::toDto);
	}

	@Override
	public Optional<CityResponse> findCityByName(String name, Pageable pageable) {
		return cityRepository.findByName(name).map(city -> {
			city.setComments(commentRepository.findAllByCityOrderByCreatedDateTimeDesc(city, pageable));
			return cityMapper.toDto(city);
		});
	}

	@Override
	public Optional<CityResponse> findCityById(Long id) {
		return cityRepository.findById(id).map(cityMapper::toDto);
	}

	@Override
	public Comment saveComment(Long cityId, CreateComment createComment) {
		City city = cityRepository.getOne(cityId);
		return commentRepository.save(new Comment(createComment.getDescription(), city));
	}

	@Override
	public Optional<CommentResponse> findCommentDtoByCityIdAndCommentId(Long cityId, Long commentId) {
		return commentRepository.findByIdAndCityId(commentId, cityId).map(commentMapper::toDto);
	}

	@Override
	public Map<String, City> getAllCitiesLookupByNames() {
		return cityRepository.findAll().stream().collect(Collectors.toMap(City::getName, Function.identity()));
	}
}
