package com.stek.flights.cheapestflights.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "countries")
@Getter
@Setter
@ToString
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
	private List<City> cityList;

	public Country() {
		this.cityList = new ArrayList<>();
	}

	public Country(String name) {
		this();
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Country country = (Country) o;

		return name.equals(country.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
