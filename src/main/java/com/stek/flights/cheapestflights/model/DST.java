package com.stek.flights.cheapestflights.model;

public enum DST {
	E("Europe"), A("US/Canada"), S("South America"), O("Australia"), Z("New Zealand"), N("None"), U("Unknown");

	public final String value;

	DST(String value) {
		this.value = value;
	}
}
