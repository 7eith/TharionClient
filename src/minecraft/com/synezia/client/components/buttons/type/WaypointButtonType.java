package com.synezia.client.components.buttons.type;

import java.awt.Color;

import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.components.backgrounds.TexturedBackground;
import com.synezia.client.components.texts.Text;
import com.synezia.client.components.texts.TextSize;
import com.synezia.client.resources.Resource;
import com.synezia.client.utilities.Colors;

/**
 * @author Snkh
 *  03 oct. 2019
 */

public class WaypointButtonType extends Type {

	@Override
	public void draw() {
		Color color = new Color(12, 25, 52);
		Color hoveredColor = Color.WHITE;
		
		new ColoredBackground(this.getButton().getPosX(), this.getButton().getPosY())
			.withSize(this.getButton().getSize())
			.withColor(!this.getButton().isHovered() ? new Colors(color) : new Colors(hoveredColor))
			.withTransparency(!this.getButton().isHovered() ? 0.4F : 0.2F)
			.withBorders(true)
			.draw();

		new TexturedBackground(this.getButton().getPosX() + 2, this.getButton().getPosY() + 3).withSize(new Size(20, 20)).setResource(Resource.PLACEHOLDER).draw();
	}

}
