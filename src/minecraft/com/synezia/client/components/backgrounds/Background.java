package com.synezia.client.components.backgrounds;

import com.synezia.client.components.Size;
import com.synezia.client.components.SizedComponent;

/**
 * @author Snkh
 *	26 jul. 2018
 */

public abstract class Background extends SizedComponent  {
	
	public Background() {
		super(0, 0);
		
		this.size = Size.FULLSCREEN;
	}

	public Background(Integer posX, Integer posY) {
		super(posX, posY);
	}
	
	public abstract void draw();
}
