package com.stek.flights.cheapestflights.model;

import org.mapstruct.Mapper;

import com.stek.flights.cheapestflights.model.request.CountryRequest;

@Mapper(componentModel = "spring")
public interface CountryMapper {

	Country toEntity(CountryRequest countryRequest);

}
