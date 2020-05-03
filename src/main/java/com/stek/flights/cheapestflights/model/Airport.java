package com.stek.flights.cheapestflights.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "airports")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
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

	@OneToMany(mappedBy = "from")
	private List<Flight> departures;

	@OneToMany(mappedBy = "to")
	private List<Flight> arrivals;

	public Airport(Long id, String name, City city, String iata, String icao, Double latitude, Double longitude, Integer feet, Integer timezoneOffset, DST dst, String timezone, String type,
			String source) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.iata = iata;
		this.icao = icao;
		this.latitude = latitude;
		this.longitude = longitude;
		this.feet = feet;
		this.timezoneOffset = timezoneOffset;
		this.dst = dst;
		this.timezone = timezone;
		this.type = type;
		this.source = source;
	}

}
