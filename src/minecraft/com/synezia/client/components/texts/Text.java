package com.synezia.client.components.texts;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.synezia.client.components.Component;
import com.synezia.client.utilities.Colors;
import com.synezia.client.utilities.Utilities;

import lombok.Getter;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Getter 
public class Text extends Component {
	
	private String text;
	private TextSize size;
	private Colors color;

	public Text(String text, Integer posX, Integer posY) {
		super(posX, posY);
		
		this.text = text;
		this.size = TextSize.DEFAULT;
		this.color = new Colors(new Color(255, 255, 255));
	}
	
	public Text withSize(TextSize size) {this.size = size; return this;}
	public Text withColor(Colors color) {this.color = color; return this;}

	@Override
	public void draw() {
		
		Float textSize = this.size.getSize();
		
		/**
		 * Draw
		 */
		
		GL11.glPushMatrix();
		GL11.glScalef(textSize, textSize, textSize);
		
		Utilities.displayString(this.text, (int)((float)this.getPosX() / textSize), (int)((float)this.getPosY() / textSize), color);
		
		GL11.glPopMatrix();
	}

}
