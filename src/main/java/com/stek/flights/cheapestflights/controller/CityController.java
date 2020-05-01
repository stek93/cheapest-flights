package com.stek.flights.cheapestflights.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stek.flights.cheapestflights.model.City;
import com.stek.flights.cheapestflights.model.Comment;
import com.stek.flights.cheapestflights.model.request.CreateCity;
import com.stek.flights.cheapestflights.model.request.CreateComment;
import com.stek.flights.cheapestflights.model.response.CityResponse;
import com.stek.flights.cheapestflights.model.response.CommentResponse;
import com.stek.flights.cheapestflights.service.CityService;
import com.stek.flights.cheapestflights.service.exception.CityNotFoundException;
import com.stek.flights.cheapestflights.service.exception.CommentNotFoundException;
import com.stek.flights.cheapestflights.service.repository.CommentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/cities")
public class CityController {

	private final CityService cityService;

	private final CommentRepository commentRepository;

	@Autowired
	public CityController(CityService cityService, CommentRepository commentRepository) {
		this.cityService = cityService;
		this.commentRepository = commentRepository;
	}

	@GetMapping
	public ResponseEntity<?> getAllCities(@RequestParam(required = false, defaultValue = "0") Integer comments) {
		log.info("GET /cities");
		if (comments > 0) {
			Pageable numberOfDesiredComments = PageRequest.of(0, comments);
			return ResponseEntity.ok(cityService.findAllCities(numberOfDesiredComments));
		} else {
			return ResponseEntity.ok(cityService.findAllCities());
		}
	}

	@GetMapping(params = { "name" })
	public ResponseEntity<?> getCitiesByName(@RequestParam(required = false, defaultValue = "0") Integer comments, @RequestParam String name) {
		log.info("GET /cities/{}", name);

		final Optional<CityResponse> cityResponseOptional;

		if (comments > 0) {
			Pageable numberOfDesiredComments = PageRequest.of(0, comments);
			cityResponseOptional = cityService.findCityByName(name, numberOfDesiredComments);
		} else {
			cityResponseOptional = cityService.findCityByName(name);
		}

		return cityResponseOptional.map(ResponseEntity::ok).orElseThrow(() -> new CityNotFoundException(name));
	}

	@PostMapping
	public ResponseEntity<Void> createCity(@Valid @RequestBody CreateCity createCity) {
		log.info("POST /cities (name = {}, description = {})", createCity.getName(), createCity.getDescription());

		City savedCity = cityService.saveCity(createCity);
		URI locationURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCity.getId()).toUri();
		return ResponseEntity.created(locationURI).build();
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<CityResponse> getCityById(@PathVariable Long id) {
		log.info("GET /cities/{}", id);
		return cityService.findCityById(id).map(ResponseEntity::ok).orElseThrow(() -> new CityNotFoundException(id));
	}

	@PostMapping(path = "/{cityId}/comments")
	public ResponseEntity<Void> createCommentForCity(@PathVariable Long cityId, @Valid @RequestBody CreateComment createComment) {
		log.info("POST /cities/{}/comments (description = {})", cityId, createComment.getDescription());

		Optional<CityResponse> optionalCity = cityService.findCityById(cityId);
		if (optionalCity.isEmpty())
			throw new CityNotFoundException(cityId);

		Comment savedComment = cityService.saveComment(cityId, createComment);
		URI locationURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{commentId}").buildAndExpand(cityId, savedComment.getId()).toUri();
		return ResponseEntity.created(locationURI).build();
	}

	@GetMapping(path = "/{cityId}/comments/{commentId}")
	public ResponseEntity<CommentResponse> getCommentForCity(@PathVariable Long cityId, @PathVariable Long commentId) {
		log.info("GET /cities/{}/comments/{})", cityId, commentId);
		return cityService.findCommentDtoByCityIdAndCommentId(cityId, commentId).map(ResponseEntity::ok).orElseThrow(() -> new CommentNotFoundException(commentId, cityId));
	}

	@DeleteMapping(path = "/{cityId}/comments/{commentId}")
	public ResponseEntity<Void> deleteCommentForCity(@PathVariable Long cityId, @PathVariable Long commentId) {
		log.info("DELETE /cities/{}/comments/{})", cityId, commentId);

		Optional<Comment> comment = commentRepository.findByIdAndCityId(commentId, cityId);
		if (comment.isEmpty()) {
			throw new CommentNotFoundException(commentId, cityId);
		} else {
			commentRepository.deleteById(commentId);
		}

		return ResponseEntity.noContent().build();
	}

	@PutMapping(path = "/{cityId}/comments/{commentId}")
	public ResponseEntity<Void> updateCommentForCity(@PathVariable Long cityId, @PathVariable Long commentId, @Valid @RequestBody CreateComment createComment) {
		log.info("PUT /cities/{}/comments/{})", cityId, commentId);

		Optional<Comment> optionalComment = commentRepository.findByIdAndCityId(commentId, cityId);

		// resource already exists, we want to update it and to return 204 - no content
		if (optionalComment.isPresent()) {
			Comment comment = optionalComment.get();
			comment.setDescription(createComment.getDescription());
			commentRepository.save(comment);
			return ResponseEntity.noContent().build();
		} else {
			// we want to return 201 - new resource created
			Optional<CityResponse> optionalCity = cityService.findCityById(cityId);
			if (optionalCity.isEmpty())
				throw new CityNotFoundException(cityId);

			Comment savedComment = cityService.saveComment(cityId, createComment);
			URI locationURI = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(savedComment.getId()).toUri();

			return ResponseEntity.created(locationURI).build();
		}

	}

}
