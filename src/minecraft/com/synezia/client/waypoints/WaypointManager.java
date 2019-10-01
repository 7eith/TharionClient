package com.synezia.client.waypoints;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.synezia.client.Client;
import com.synezia.client.utilities.Colors;
import com.synezia.client.utilities.Disk;

import lombok.Getter;
import net.minecraft.client.Minecraft;

/**
 * @author Snkh
 *	29 sept. 2019
 */

public class WaypointManager 
{
	
	@Getter private static WaypointManager instance;
	@Getter private List<Waypoint> waypoints = Lists.newArrayList();
	@Getter private File File = new File(Minecraft.getMinecraft().mcDataDir + "waypoints.json");
	
	public WaypointManager()
	{
		this.instance = this;
		
		/**
		 * Loading from Disk
		 */
		
		String content = Disk.readCatch(this.getFile());
		
		if(content == null)
			return;
		
		List waypointsList = (List)Client.i.getGson().fromJson(content, new TypeToken<List<Waypoint>>(){}.getType());
		waypoints.clear();
		waypoints.addAll(waypointsList);

		waypoints.add(new Waypoint(WaypointType.FACTION, "&eFaction Waypoint", 0, 150, 0).withColor(Colors.DARK_PURPLE));
		waypoints.add(new Waypoint(WaypointType.CLAN, "&cEnculer de clan Waypoint", 0, 30, 0).withColor(Colors.MIDNIGHT));
	}
	
    public Waypoint getWaypointAt(Integer posX, Integer posY, Integer posZ) {
    	return waypoints.stream()
    			.filter(w -> w.getPosX() == posX && w.getPosY() == posY && w.getPosZ() == posZ)
    			.findFirst()
                .get();
    }

	
	public void save()
	{
		List<Waypoint> playerWaypoints = waypoints
				.stream()
				.filter(w -> w.getType() == WaypointType.PLAYER)
				.collect(Collectors.toList());
		
		Disk.writeCatch(this.getFile(), Client.i.getGson().toJson(playerWaypoints));
	}

}
