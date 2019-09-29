package com.synezia.client.utilities;

import lombok.Getter;

/**
 * @author Snkh
 *	26 jul. 2018
 */

public class MousePosition {
	
	@Getter private static Integer posX;
	@Getter private static Integer posY;
	
	public static void set(Integer X, Integer Y) {
		posX = X;
		posY = Y;
	}
}
