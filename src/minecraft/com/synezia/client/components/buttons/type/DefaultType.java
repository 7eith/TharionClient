package com.synezia.client.components.buttons.type;

import java.awt.Color;

import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.utilities.Colors;

/**
 * @author Snkh
 *	26 jul. 2018
 */

public class DefaultType extends Type {

	@Override
	public void draw() {
    	Integer color = this.getButton().isActive() ? (this.getButton().isHovered() ? this.getButton().getColor().getLightColor() : this.getButton().getColor().getDarkColor()) : Color.LIGHT_GRAY.getRGB();
    	
        ColoredBackground rect = new ColoredBackground(this.getButton().getPosX(), this.getButton().getPosY());
        
        rect.withColor(new Colors(color));
        rect.withSize(this.getButton().getSize());
        rect.draw();
	}

}
