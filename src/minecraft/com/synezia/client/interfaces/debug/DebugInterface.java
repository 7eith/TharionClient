package com.synezia.client.interfaces.debug;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.interfaces.Interface;
import com.synezia.client.utilities.Colors;

/**
 * @author Snkh
 *	25 jul. 2018
 */

public class DebugInterface extends Interface {
	
	private ColoredBackground background = new ColoredBackground(500, 200).withSize(new Size(20, 20)).withColor(new Colors(new Color(0))).withTransparency(0.8f);
	private Color color;

	@Override
	public void initializeInterface() {
		
		
		
	}

	@Override
	public void drawComponents() {
		
		this.background.draw();
		
	}

	@Override
	public void updateInterface() {
		Random random = new Random();
		
		final float hue = random.nextFloat();
		// Saturation between 0.1 and 0.3
		final float saturation = (random.nextInt(2000) + 1000) / 10000f;
		final float luminance = 0.9f;
		final Color color = Color.getHSBColor(hue, saturation, luminance);
		
		this.color = new Color(background.getColor().getLightColor() + 1);
		this.background.withColor(new Colors(color));
		
	}
	
	List<Integer> getUniqueColors(int amount) {
	    final int lowerLimit = 0x101010;
	    final int upperLimit = 0xE0E0E0;
	    final int colorStep = (upperLimit-lowerLimit)/amount;

	    final List<Integer> colors = new ArrayList<Integer>(amount);
	    for (int i=0;i<amount;i++) {
	        int color = lowerLimit+colorStep*i;
	        colors.add(color);
	    }
	    return colors;
	}

}
