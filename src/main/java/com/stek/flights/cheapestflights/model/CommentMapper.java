package com.stek.flights.cheapestflights.model;

import org.mapstruct.Mapper;

import com.stek.flights.cheapestflights.model.response.CommentResponse;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	Comment toEntity(CommentResponse commentResponse);

	CommentResponse toDto(Comment comment);

}
