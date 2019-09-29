package com.synezia.client.components.buttons.informations;

import com.synezia.client.components.texts.Text;
import com.synezia.client.components.texts.TextSize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@Getter @Setter @AllArgsConstructor 
public class EscapeButtonInformations extends Informations {
	
	private Integer posX; 
	private Integer posY;

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
			new Text(title, posX, posY)
				.withSize(TextSize.EXTRA_SMALL)
				.withColor(this.getButton().getTitleColor())
				.draw();
	}
}