package com.synezia.client.components;

import lombok.Data;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@Data
public abstract class Component {

	private String id;
	
	private Integer posX;
	private Integer posY;
	
	private boolean active;
	private boolean visible;
	private boolean selected;
	
	public Component(Integer posX, Integer posY) {
		this.posX = posX;
		this.posY = posY;
		
		this.active = true;
		this.visible = true;
		this.selected = false;
	}
	
	public abstract void draw();
	
}