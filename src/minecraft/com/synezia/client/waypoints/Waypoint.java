package com.synezia.client.waypoints;

import java.util.List;

import com.synezia.client.utilities.Colors;

import lombok.Data;
import net.minecraft.entity.Entity;

/**
 * @author Snkh
 *	29 sept. 2019
 */

@Data 
public class Waypoint {
	
	private WaypointType type;
	private String title;
	private Integer posX;
	private Integer posY;
	private Integer posZ;
	private Colors color;
	private boolean active;
	
	public Waypoint(WaypointType type, String title, Integer posX, Integer posY, Integer posZ)
	{
		this.type = type;
		this.title = title;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.color = type.getColor();
		this.active = true;
	}
	
	public Waypoint withColor(Colors color) { this.color = color; return this; }
	
    public double getDistanceToAnEntity(Entity entity) {
        double distX = (double)this.posX - entity.posX;
        double distY = (double)this.posY - entity.posY;
        double distZ = (double)this.posZ - entity.posZ;
        return Math.sqrt(distX * distX + distY * distY + distZ * distZ);
    }
}
