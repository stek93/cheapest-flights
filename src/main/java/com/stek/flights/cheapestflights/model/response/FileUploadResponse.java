package com.stek.flights.cheapestflights.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse {

	private String fileName;

	private String fileType;

	private Long size;

}
