package com.epam.model;

import javax.validation.constraints.Size;

public class ImageModel {
	@Size(min = 3, max = 12, message = "Description should be 3-12 char long")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}