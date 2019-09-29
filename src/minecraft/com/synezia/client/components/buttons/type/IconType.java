package com.synezia.client.components.buttons.type;

import java.awt.Color;

import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.components.backgrounds.TexturedBackground;
import com.synezia.client.resources.Resource;
import com.synezia.client.utilities.Colors;
import com.synezia.client.utilities.Utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@Getter @Setter @AllArgsConstructor
public class IconType extends Type {
	
	//TODO: FadeInLeft gauche to droite w/ timer
	
	private Resource icon;

	@Override
	public void draw() {
		
		TexturedBackground texture = new TexturedBackground(this.getButton().getPosX(), this.getButton().getPosY());
		texture.withSize(this.getButton().getSize());
		texture.setResource(this.getIcon());
		
		Utilities.setGlColor(this.getButton().getColor().getLightColor());
		
		if(this.getButton().isHovered()) {
			
			new ColoredBackground(0, this.getButton().getPosY() - 1).withSize(new Size(34, 30)).withColor(new Colors(Color.WHITE)).withTransparency(0.5f).draw();
			Utilities.setGlColor(Color.BLACK.getRGB());
		}
		
		texture.draw();
	}
}