package com.stek.flights.cheapestflights.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.stek.flights.cheapestflights.model.Airport;
import com.stek.flights.cheapestflights.model.City;
import com.stek.flights.cheapestflights.model.Country;
import com.stek.flights.cheapestflights.model.DST;
import com.stek.flights.cheapestflights.model.Flight;
import com.stek.flights.cheapestflights.service.exception.FileProcessingException;
import com.stek.flights.cheapestflights.service.repository.AirportRepository;
import com.stek.flights.cheapestflights.service.repository.CityRepository;
import com.stek.flights.cheapestflights.service.repository.CountryRepository;
import com.stek.flights.cheapestflights.service.repository.FlightRepository;
import com.stek.flights.cheapestflights.util.FileStorageProperties;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@Service
public class FileServiceImpl implements FileService {

	private final Path fileStorageLocation;

	private final CountryRepository countryRepository;

	private final AirportRepository airportRepository;

	private final AirportService airportService;

	private final FlightRepository flightRepository;

	private final CityService cityService;

	private final CityRepository cityRepository;

	private final FileStorageProperties fileStorageProperties;

	private final ResourceLoader resourceLoader;

	@Autowired
	public FileServiceImpl(CountryRepository countryRepository, AirportRepository airportRepository, AirportService airportService, FlightRepository flightRepository, CityService cityService,
			CityRepository cityRepository, FileStorageProperties fileStorageProperties, ResourceLoader resourceLoader) {
		this.countryRepository = countryRepository;
		this.airportRepository = airportRepository;
		this.airportService = airportService;
		this.flightRepository = flightRepository;
		this.cityService = cityService;
		this.cityRepository = cityRepository;
		this.fileStorageProperties = fileStorageProperties;
		this.resourceLoader = resourceLoader;
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
	}

	@Override
	public void loadCountriesFromResource(Resource resource) throws IOException {
		// this method will load content from airports.txt, extract countries and save them in db on app startup
		List<String> lines = Files.readAllLines(Paths.get(resource.getURI()), StandardCharsets.UTF_8);
		final Set<Country> countriesToSave = new HashSet<>();

		lines.forEach(line -> {
			String[] airportData = line.split(",");
			String countryName = airportData[3].replace("\"", "");
			countriesToSave.add(new Country(countryName));
		});

		countryRepository.saveAll(countriesToSave);
	}

	@Override
	public String uploadFile(MultipartFile file) {
		try {
			Files.createDirectories(fileStorageLocation);

			String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

			Path targetLocation = fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException e) {
			throw new FileProcessingException("Something went wrong");
		}
	}

	@Override
	public void loadAirportsFromResource(String fileName) throws IOException {
		Map<String, City> allCitiesByNames = cityService.getAllCitiesLookupByNames();

		List<String> extractedFileLines = extractFileIntoLines(fileName);

		extractedFileLines.forEach(line -> {
			String[] airportData = line.split(",");
			String cityName = trimUnnecessaryChars(airportData[2]);

			if (allCitiesByNames.containsKey(cityName)) {
				Long id = Long.valueOf(airportData[0]);
				String name = trimUnnecessaryChars(airportData[1]);
				String iata = trimUnnecessaryChars(airportData[4]);
				String icao = trimUnnecessaryChars(airportData[5]);
				Double latitude = Double.valueOf(airportData[6]);
				Double longitude = Double.valueOf(airportData[7]);
				Integer feet = null;
				if (isNumeric(airportData[8])) {
					feet = Integer.parseInt(airportData[8]);
				}

				Integer timezoneOffset = null;
				if (isNumeric(airportData[9])) {
					timezoneOffset = Integer.parseInt(airportData[9]);
				}
				DST dst = EnumUtils.isValidEnum(DST.class, airportData[10]) ? DST.valueOf(trimUnnecessaryChars(airportData[10])) : DST.U;
				String timezone = trimUnnecessaryChars(airportData[11]);
				String type = trimUnnecessaryChars(airportData[12]);
				String source = trimUnnecessaryChars(airportData[13]);
				airportRepository.save(new Airport(id, name, allCitiesByNames.get(cityName), iata, icao, latitude, longitude, feet, timezoneOffset, dst, timezone, type, source));
			}
		});
	}

	private String trimUnnecessaryChars(String field) {
		return field.replace("\"", "");
	}

	private List<String> extractFileIntoLines(String fileName) throws IOException {
		String routeFileLocation = String.format("%s/%s", fileStorageProperties.getUploadDir(), fileName);
		return Files.readAllLines(Paths.get(routeFileLocation));
	}

	@Override
	public void loadRoutesFomResource(String fileName) throws IOException {
		Map<String, Airport> airportsWithCodeLookup = airportService.findAllAirportsWithCodeLookup();
		Map<Long, Airport> airportsWithIdLookup = airportService.findAllAirportsWithIdLookup();

		List<String> extractedFileLines = extractFileIntoLines(fileName);

		extractedFileLines.forEach(line -> {
			String[] routeData = line.split(",");

			String airlineCode = routeData[0];
			Long airlineId = null;
			if (isNumeric(routeData[1])) {
				airlineId = Long.valueOf(routeData[1]);
			}
			String sourceAirportCode = routeData[2];

			// default can be zero (so we don't need null check later if ID can't be extracted)
			Long sourceAirportId = 0L;
			if (isNumeric(routeData[3])) {
				sourceAirportId = Long.valueOf(routeData[3]);
			}

			String destinationAirportCode = routeData[4];

			Long destinationAirportId = 0L;
			if (isNumeric(routeData[5])) {
				destinationAirportId = Long.valueOf(routeData[5]);
			}

			String codeShare = routeData[6];
			int numOfStops = Integer.parseInt(routeData[7]);
			String equipmentCode = routeData[8];
			Double flightCost = Double.valueOf(routeData[9]);

			Airport sourceAirport = findAirport(sourceAirportId, sourceAirportCode, airportsWithIdLookup, airportsWithCodeLookup);
			Airport destinationAirport = findAirport(destinationAirportId, destinationAirportCode, airportsWithIdLookup, airportsWithCodeLookup);

			if (sourceAirport != null && destinationAirport != null) {
				flightRepository.save(new Flight(airlineCode, airlineId, sourceAirport, destinationAirport, codeShare, numOfStops, equipmentCode, flightCost));
			}

		});
	}

	private Airport findAirport(Long airportId, String airportCode, Map<Long, Airport> airportMapWithIdLookup, Map<String, Airport> airportMapWithCodeLookup) {
		Airport airport = null;

		if (airportMapWithIdLookup.containsKey(airportId)) {
			airport = airportMapWithIdLookup.get(airportId);
		} else if (airportMapWithCodeLookup.containsKey(airportCode)) {
			airport = airportMapWithCodeLookup.get(airportCode);
		}

		return airport;
	}
}
