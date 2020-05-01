package com.stek.flights.cheapestflights.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "airports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Airport {

	@Id
	private Long id;

	private String name;

	@ManyToOne
	private City city;

	@Column(length = 3)
	private String iata;

	@Column(length = 4)
	private String icao;

	private Double latitude;

	private Double longitude;

	private Integer feet;

	private Integer timezoneOffset;

	@Enumerated(value = EnumType.STRING)
	private DST dst;

	private String timezone;

	private String type;

	private String source;

}
