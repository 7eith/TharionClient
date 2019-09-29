package com.synezia.client.resources;

import lombok.Getter;

/**
 * @author Snkh
 *	22 jul. 2017
 */

@Getter
public enum ResourceType {
	
    INTERFACE("textures/client/interfaces/"),
    ICON("textures/client/icons/"),
    ENTITY("textures/client/entities/"),
    SOUND("", ""),
    FONT("textures/client/fonts/", ".ttf");
	
	private String path; 
	private String fileExtension;

	ResourceType(String path, String fileExtension) {
		this.path = path;
		this.fileExtension = fileExtension;
	}
	
	ResourceType(String path) {
		this(path, ".png");
	}
}
