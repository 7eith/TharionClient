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
	
	public static Colors LIGHT_RED = new Colors(new Color(231, 76, 60), new Color(203, 67, 53));
	public static Colors DARK_RED = new Colors(new Color(192, 57, 43), new Color(169, 50, 38));
	public static Colors RUBY_RED = new Colors(new Color(248, 57, 66), new Color(237, 75, 81));
	public static Colors FRUIT_RED = new Colors(new Color(237, 85, 101), new Color(218, 68, 83));
	
	public static Colors MINT_GREEN = new Colors(new Color(72, 207, 173), new Color(55, 188, 155));
	public static Colors GREEN = new Colors(new Color(160, 212, 104), new Color(140, 193, 82));
	public static Colors LIGHT_GREEN = new Colors(new Color(46, 204, 113), new Color(40, 180, 99));
	public static Colors DARK_GREEN = new Colors(new Color(39, 174, 96), new Color(34, 153, 84));
	
	public static Colors LIGHT_MIDNIGHT = new Colors(new Color(86, 101, 115), new Color(44, 62, 80));
	public static Colors DARK_MIDNIGHT = new Colors(new Color(93, 109, 126), new Color(52, 73, 94));
	public static Colors MIDNIGHT = new Colors(new Color(56, 56, 56), new Color(34, 32, 43));
	
	public static Colors ORANGE = new Colors(new Color(252, 110, 81), new Color(233, 87, 63));
	public static Colors YELLOW = new Colors(new Color(244, 208, 63), new Color(241, 196, 15));
	public static Colors LIGHT_YELLOW = new Colors(new Color(255, 206, 84), new Color(246, 187, 66));
	
	public static Colors BLUE = new Colors(new Color(93, 156, 236), new Color(74, 137, 220));
	public static Colors LIGHT_BLUE = new Colors(new Color(79, 193, 233), new Color(59, 175, 218));
	
	public static Colors PURPLE = new Colors(new Color(155, 89, 182), new Color(142, 68, 173));
	public static Colors DARK_PURPLE = new Colors(new Color(46, 38, 48), new Color(42, 36, 44));
	public static Colors CLOUD = new Colors(new Color(249, 249, 249), new Color(228, 228, 228));
	public static Colors LIGHT_GRAY = new Colors(new Color(204, 209, 217), new Color(170, 178, 189));
	public static Colors GRAY = new Colors(new Color(47, 47, 47), new Color(41, 41, 41));
	public static Colors PINK = new Colors(new Color(236, 135, 192), new Color(215, 112, 173));
	public static Colors BROWN = new Colors(new Color(210, 192, 170), new Color(202, 180, 148));
	public static Colors LIGHT_BROWN = new Colors(new Color(232, 206, 177), new Color(193, 165, 132));
	public static Colors BLACK = new Colors(new Color(48, 48, 48), new Color(27, 26, 26));
	public static Colors BEIGE = new Colors(new Color(254, 254, 254), new Color(207, 186, 165));
	public static Colors WHITE = new Colors(new Color(253, 254, 254), new Color(236, 240, 241));
	
	public static Colors COBALT = new Colors(new Color(12, 25, 52), new Color(8, 3, 37));
    
}
