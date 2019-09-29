package com.synezia.client.components.buttons.informations;

import com.synezia.client.components.buttons.Button;

import lombok.Data;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Data
public abstract class Informations {

	private Button button;
	
	public abstract void draw();
	
}