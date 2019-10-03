package com.synezia.client.components.buttons.informations;

import com.synezia.client.components.texts.Text;
import com.synezia.client.components.texts.TextSize;
import com.synezia.client.waypoints.Waypoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.entity.RenderManager;

/**
 * @author Snkh
 *  03 oct. 2019
 */

@Getter @Setter @AllArgsConstructor
public class WaypointInformations extends Informations {
	
	private Waypoint waypoint;

	@Override
	public void draw() {
		
		/**
		 * Draw new Text(this.getButton().getTitle(), this.getButton().getPosX() - 50, this.getButton().getPosY() + 7).withSize(TextSize.SMALL).draw();
		 */
		
		new Text(waypoint.getTitle(), this.getButton().getPosX() + 25, this.getButton().getPosY() + 6)
			.withSize(TextSize.SMALL)
			.draw();
		
		String isActive = waypoint.isActive() ? "&a* " : "&cx ";
		
		new Text(isActive + "Type: " + waypoint.getType() + " / X:" + waypoint.getPosX() + ", Y: " + waypoint.getPosY() + ", Z: " + waypoint.getPosZ(), this.getButton().getPosX() + 26, this.getButton().getPosY() + 14)
			.withSize(TextSize.EXTRA_SMALL)
			.draw();
		
		new Text("&9* Distance: " + (int)waypoint.getDistanceToAnEntity(RenderManager.instance.livingPlayer) + " blocks", this.getButton().getPosX() + 26, this.getButton().getPosY() + 20)
			.withSize(TextSize.EXTRA_SMALL)
			.draw();
		
	}

}
