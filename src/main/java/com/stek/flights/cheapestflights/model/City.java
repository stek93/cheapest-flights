package com.stek.flights.cheapestflights.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "cities")
@Data
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToOne
	private Country country;

	private String description;

	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	private List<Comment> comments;

	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	private List<Airport> airports;

	public City() {
		this.comments = new ArrayList<>();
	}
}
