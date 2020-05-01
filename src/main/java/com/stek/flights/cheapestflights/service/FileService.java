package com.stek.flights.cheapestflights.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	void loadCountriesFromResource(Resource resource) throws IOException;

	void loadAirportsFromResource(String fileName) throws IOException;

	void loadRoutesFomResource(String fileName) throws IOException;

	String uploadFile(MultipartFile file);

}
