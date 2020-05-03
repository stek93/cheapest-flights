package com.stek.flights.cheapestflights.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.stek.flights.cheapestflights.model.response.AirportResponse;

@Mapper(componentModel = "spring")
public interface AirportMapper {

	@Mappings({ @Mapping(source = "city.name", target = "city"), @Mapping(source = "city.country.name", target = "country") })
	AirportResponse toDto(Airport airport);

}
