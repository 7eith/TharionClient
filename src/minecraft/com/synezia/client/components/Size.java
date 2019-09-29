package com.synezia.client.components;

import com.synezia.client.interfaces.Interface;

import lombok.Data;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@Data
public class Size {
	
	public static Size FULLSCREEN = new Size(0,0);
	
	private Integer width; 
	private Integer height;
	
	public Size(Integer width, Integer height) {
		this.width = width;
		this.height = height;
	}
}
