package com.stek.flights.cheapestflights.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String airlineCode;

	private Long airlineId;

	@ManyToOne
	@JoinColumn(name = "source_airport_id")
	private Airport sourceAirport;

	@ManyToOne
	@JoinColumn(name = "destination_airport_id")
	private Airport destinationAirport;

	private String codeShare;

	private int numOfStops;

	private String equipmentCode;

	private Double flightCost;

	public Route(String airlineCode, Long airlineId, Airport sourceAirport, Airport destinationAirport, String codeShare, int numOfStops, String equipmentCode, Double flightCost) {
		this.airlineCode = airlineCode;
		this.airlineId = airlineId;
		this.sourceAirport = sourceAirport;
		this.destinationAirport = destinationAirport;
		this.codeShare = codeShare;
		this.numOfStops = numOfStops;
		this.equipmentCode = equipmentCode;
		this.flightCost = flightCost;
	}

}
