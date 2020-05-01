package com.stek.flights.cheapestflights.model;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.stek.flights.cheapestflights.model.request.CreateCity;
import com.stek.flights.cheapestflights.model.response.CityResponse;

@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public abstract class CityMapper {

	@Autowired
	private CountryMapper countryMapper;

	@Autowired
	private CommentMapper commentMapper;

	public abstract City toEntity(CreateCity createCity);

	public abstract CityResponse toDto(City city);

}
