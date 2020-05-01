package com.stek.flights.cheapestflights.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.stek.flights.cheapestflights.service.FileService;

@Component
public class FileLoadRunner implements CommandLineRunner {

	@Value("classpath:airports.txt")
	private Resource resource;

	private final FileService fileService;

	@Autowired
	public FileLoadRunner(FileService fileService) {
		this.fileService = fileService;
	}

	@Override
	public void run(String... args) throws Exception {
		fileService.loadCountriesFromResource(resource);
	}
}
