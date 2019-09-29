package com.synezia.client.components.buttons.type;

import com.synezia.client.components.buttons.Button;

import lombok.Data;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Data
public abstract class Type {
	
	/**
	 * Button
	 */
	
	private Button button; 
	
	/**
	 * Draw
	 */
	
	public abstract void draw(); 

}
