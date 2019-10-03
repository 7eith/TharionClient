package com.synezia.client.components.buttons.actions;

import com.synezia.client.interfaces.waypoints.WaypointInterface;
import com.synezia.client.utilities.Utilities;
import com.synezia.client.waypoints.Waypoint;
import com.synezia.client.waypoints.WaypointManager;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;

/**
 * @author Snkh
 *  03 oct. 2019
 */

@AllArgsConstructor
public class WaypointClickAction extends Action {
	
	private Integer waypointId;

	@Override
	public void execute() {
		Waypoint waypoint = WaypointManager.getInstance().getWaypoints().get(waypointId);
		
		if (GuiScreen.isCtrlKeyDown())
		{
			String name = waypoint.getTitle();
			WaypointManager.getInstance().getWaypoints().remove(waypoint);
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(Utilities.color("&cWaypoint supprimer")));
			Minecraft.getMinecraft().displayGuiScreen(new WaypointInterface("&cLe waypoint '" + name + "&c' supprimer!"));
		} else {
			waypoint.toggleActive();
		}

		
	}

}
