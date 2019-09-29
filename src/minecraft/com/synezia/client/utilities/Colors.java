package com.synezia.client.utilities;

import java.awt.Color;

import lombok.Data;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Data
public class Colors {
	
	private Integer lightColor;
	private Integer darkColor;
	
	public Colors(Color color) {
		this.lightColor = color.getRGB();
		this.darkColor  = Color.BLACK.getRGB();
	}
	
	public Colors(Color light, Color dark) {
		this.lightColor = light.getRGB();
		this.darkColor = dark.getRGB();
	}
	
	public Colors(Integer color) {
		this.lightColor = color; 
		this.darkColor = Color.BLACK.getRGB();
	}
	
	public Colors(Integer lightColor, Integer darkColor) {
		this.lightColor = lightColor;
		this.darkColor = darkColor;
	}

	public static Colors YELLOW = new Colors(new Color(231, 76, 60), new Color(203, 67, 53));
	public static Colors DARK_PURPLE = new Colors(new Color(46, 38, 48), new Color(42, 36, 44));
}
