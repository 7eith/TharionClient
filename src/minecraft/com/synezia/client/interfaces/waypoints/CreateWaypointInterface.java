package com.synezia.client.interfaces.waypoints;

import java.awt.Color;

import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.components.texts.Text;
import com.synezia.client.components.texts.TextSize;
import com.synezia.client.interfaces.Interface;
import com.synezia.client.utilities.Colors;
import com.synezia.client.waypoints.WaypointManager;

public class CreateWaypointInterface extends Interface {
	
	private WaypointManager manager;
	
	public CreateWaypointInterface() 
	{
		this.manager = WaypointManager.getInstance();
	}

	@Override
	public void initializeInterface() 
	{
		
		
	}

	@Override
	public void drawComponents() 
	{
		// Background
		new ColoredBackground().withColor(Colors.COBALT).withTransparency(0.6F).draw();
		
		// Waypoint Menu
		new ColoredBackground(this.getSplitWidth() - 70, this.getSplitHeight() - 100)
			.withSize(new Size(140, 190))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
		// Waypoint Title
		new ColoredBackground(this.getSplitWidth() - 70, this.getSplitHeight() - 100)
			.withSize(new Size(140, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
		new Text("&cCreate Waypoint", this.getSplitWidth() - 65, this.getSplitHeight() - 94).draw();
		
		// Waypoint : Name
		new Text("* Nom du waypoint", this.getSplitWidth() - 64, this.getSplitHeight() - 74).withSize(TextSize.SMALL).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() - 64)
			.withSize(new Size(130, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
		new Text("&cFactions LNZ", this.getSplitWidth() - 60, this.getSplitHeight() - 58).draw();
		
		// Waypoint : PosX
		new Text("* Position (X)", this.getSplitWidth() - 64, this.getSplitHeight() - 35).withSize(TextSize.SMALL).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() - 25)
			.withSize(new Size(130, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
		new Text("&90", this.getSplitWidth() - 60, this.getSplitHeight() - 20).draw();
		
		// Waypoint : PosY
		new Text("* Position (Y)", this.getSplitWidth() - 64, this.getSplitHeight()).withSize(TextSize.SMALL).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() + 10)
			.withSize(new Size(130, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
		new Text("&90", this.getSplitWidth() - 60, this.getSplitHeight() + 16).draw();
		
		// Waypoint : PosZ
		new Text("* Position (Z)", this.getSplitWidth() - 64, this.getSplitHeight() + 35).withSize(TextSize.SMALL).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() + 45)
			.withSize(new Size(130, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
		new Text("&90", this.getSplitWidth() - 60, this.getSplitHeight() + 51).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() + 70)
			.withSize(new Size(60, 15))
			.withColor(new Colors(Colors.DARK_RED.getLightColor()))
			.withBorders(true)
			.draw();
		
		new ColoredBackground(this.getSplitWidth() + 5, this.getSplitHeight() + 70)
			.withSize(new Size(60, 15))
			.withColor(new Colors(Colors.DARK_GREEN.getLightColor()))
			.withBorders(true)
			.draw();
		
	}

	@Override
	public void updateInterface() 
	{
		
		
		
	}

}
