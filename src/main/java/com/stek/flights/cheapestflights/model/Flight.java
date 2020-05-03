package com.stek.flights.cheapestflights.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String airlineCode;

	private Long airlineId;

	@ManyToOne
	@JoinColumn(name = "source_airport_id")
	private Airport from;

	@ManyToOne
	@JoinColumn(name = "destination_airport_id")
	private Airport to;

	private String codeShare;

	private int numOfStops;

	private String equipmentCode;

	private Double flightCost;

	public Flight(String airlineCode, Long airlineId, Airport from, Airport to, String codeShare, int numOfStops, String equipmentCode, Double flightCost) {
		this.airlineCode = airlineCode;
		this.airlineId = airlineId;
		this.from = from;
		this.to = to;
		this.codeShare = codeShare;
		this.numOfStops = numOfStops;
		this.equipmentCode = equipmentCode;
		this.flightCost = flightCost;
	}

}
