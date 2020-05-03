package com.stek.flights.cheapestflights.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stek.flights.cheapestflights.model.response.FileUploadResponse;
import com.stek.flights.cheapestflights.service.FileService;
import com.stek.flights.cheapestflights.service.exception.FileProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/files")
public class FileUploadController {

	private final FileService fileService;

	@Autowired
	public FileUploadController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping(path = "/upload")
	public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		log.info("POST /files/upload");

		String fileName = fileService.uploadFile(file);

		try {
			if (fileName.equalsIgnoreCase("airports.txt")) {
				fileService.loadAirportsFromResource(fileName);
			} else if (fileName.equalsIgnoreCase("routes.txt")) {
				fileService.loadRoutesFomResource(fileName);
			}
		} catch (IOException e) {
			throw new FileProcessingException(fileName);
		}

		return ResponseEntity.ok(new FileUploadResponse(fileName, file.getContentType(), file.getSize()));
	}

}
