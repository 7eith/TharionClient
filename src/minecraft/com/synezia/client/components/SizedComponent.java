package com.synezia.client.components;

import com.synezia.client.utilities.MousePosition;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@Getter @Setter 
public abstract class SizedComponent extends Component {
	
	public Size size;

	public SizedComponent(Integer posX, Integer posY) {
		super(posX, posY);
	}
	
    public boolean isHovered() {
        if (MousePosition.getPosX() >= this.getPosX() && MousePosition.getPosY() >= this.getPosY() && MousePosition.getPosX() < this.getPosX() + this.size.getWidth()  && MousePosition.getPosY() < this.getPosY() + this.size.getHeight()) 
            return true;
        return false;
    }
	
    public boolean isPressed() {
        if (this.isActive() && this.isHovered()) 
            return true;
        return false;
    }
}
