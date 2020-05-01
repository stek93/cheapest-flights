package com.stek.flights.cheapestflights.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private LocalDateTime createdDateTime;

	private LocalDateTime modifiedDateTime;

	@ManyToOne
	private City city;

	@PrePersist
	public void prePersist() {
		this.createdDateTime = LocalDateTime.now();
		this.modifiedDateTime = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.modifiedDateTime = LocalDateTime.now();
	}

	public Comment(String description, City city) {
		this.description = description;
		this.city = city;
	}

}
