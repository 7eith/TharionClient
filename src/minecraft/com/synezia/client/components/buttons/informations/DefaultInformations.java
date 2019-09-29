package com.synezia.client.components.buttons.informations;

import com.synezia.client.components.texts.Text;
import com.synezia.client.utilities.Utilities;

/**
 * @author Snkh
 *	25 jul. 2018
 */

public class DefaultInformations extends Informations {

	@Override
	public void draw() {
		
		/**
		 * Title
		 */

		String title = getButton().getTitle();
		
		/**
		 * Draw
		 */
		
		if(title != null) 
			new Text(title, this.getButton().getPosX() + (this.getButton().getSize().getWidth() - Utilities.getStringWidth(title)) / 2, this.getButton().getPosY() + (this.getButton().getSize().getHeight() - 8) / 2)
			.withColor(this.getButton().getTitleColor())
			.draw();
	}

}
