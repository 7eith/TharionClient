package com.synezia.client.interfaces.waypoints;

import java.awt.Color;

import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.components.buttons.Button;
import com.synezia.client.components.buttons.actions.Action;
import com.synezia.client.components.buttons.actions.DisplayScreenAction;
import com.synezia.client.components.buttons.type.DefaultType;
import com.synezia.client.components.texts.Text;
import com.synezia.client.components.texts.TextSize;
import com.synezia.client.components.texts.fields.TextFieldComponent;
import com.synezia.client.interfaces.Interface;
import com.synezia.client.utilities.Colors;
import com.synezia.client.utilities.Utilities;
import com.synezia.client.waypoints.Waypoint;
import com.synezia.client.waypoints.WaypointManager;
import com.synezia.client.waypoints.WaypointType;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

/**
 * @author Snkh
 *	30 sept. 2019
 */

public class CreateWaypointInterface extends Interface {
	
	private WaypointManager manager;
	
	private TextFieldComponent name;
	private TextFieldComponent posX;
	private TextFieldComponent posY;
	private TextFieldComponent posZ;
	
	public CreateWaypointInterface() 
	{
		this.manager = WaypointManager.getInstance();
	}

	@Override
	public void initializeInterface() 
	{
		
		// Cancel
		this.addComponent(
			new Button(this.getSplitWidth() - 65, this.getSplitHeight() + 70)
				.setSize(60, 15)
				.setType(new DefaultType().withBorder())
				.setTitle("Annuler")
				.setColor(Colors.LIGHT_RED)
				.addAction(new DisplayScreenAction(null))
		);
		
		// Create 
		this.addComponent(
			new Button(this.getSplitWidth() + 5, this.getSplitHeight() + 70)
				.setSize(60, 15)
				.setType(new DefaultType().withBorder())
				.setTitle("Cr\u00e9er")
				.setColor(Colors.LIGHT_GREEN)
				.addAction(new DisplayScreenAction(null))
				.addAction(new Action() {
					
					@Override
					public void execute() {
						Waypoint waypoint = new Waypoint(WaypointType.PLAYER, name.getField().getText(), Integer.parseInt(posX.getField().getText()), Integer.parseInt(posY.getField().getText()), Integer.parseInt(posZ.getField().getText()));
						
						System.out.println(Integer.parseInt(posX.getField().getText()));
						System.out.println(waypoint.toString());
						
						if (!manager.hasWaypointAt(waypoint.getPosX(), waypoint.getPosY(), waypoint.getPosZ())) 
						{
							manager.getWaypoints().add(waypoint);
							name.getField().setText("");
							mc.thePlayer.addChatMessage(new ChatComponentText(Utilities.color("&aWaypoint cr\u00e9er!")));
						}
						
						else 
						{
							mc.displayGuiScreen(null);
							mc.thePlayer.addChatMessage(new ChatComponentText(Utilities.color("&cCe Waypoint \u00e9xiste d\u00e9ja!")));
						}
						
						System.out.println(name.getField().getText());
						
					}
				})
		);
		
        this.name = new TextFieldComponent("&9Waypoint #" + (manager.getWaypoints().size() + 1), new Size(115, 25), this.getSplitWidth() - 60, this.getSplitHeight() - 58).setFieldSize(120, 25);
        this.posX = new TextFieldComponent("" + MathHelper.floor_double(mc.thePlayer.posX), new Size(115, 25), this.getSplitWidth() - 60, this.getSplitHeight() - 20).setFieldSize(120, 25);
        this.posY = new TextFieldComponent("" + MathHelper.floor_double(mc.thePlayer.posY), new Size(115, 25), this.getSplitWidth() - 60, this.getSplitHeight() + 16).setFieldSize(120, 25);
        this.posZ = new TextFieldComponent("" + MathHelper.floor_double(mc.thePlayer.posZ), new Size(115, 25), this.getSplitWidth() - 60, this.getSplitHeight() + 51).setFieldSize(120, 25);
        
        this.addComponent("name", name);
        this.addComponent("posX", posX);
        this.addComponent("posY", posY);
        this.addComponent("posZ", posZ);
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
		
//		new Text("&cFactions LNZ", this.getSplitWidth() - 60, this.getSplitHeight() - 58).draw();
		
		// Waypoint : PosX
		new Text("* Position (X)", this.getSplitWidth() - 64, this.getSplitHeight() - 35).withSize(TextSize.SMALL).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() - 25)
			.withSize(new Size(130, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
//		new Text("&90", this.getSplitWidth() - 60, this.getSplitHeight() - 20).draw();
		
		// Waypoint : PosY
		new Text("* Position (Y)", this.getSplitWidth() - 64, this.getSplitHeight()).withSize(TextSize.SMALL).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() + 10)
			.withSize(new Size(130, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
//		new Text("&90", this.getSplitWidth() - 60, this.getSplitHeight() + 16).draw();
		
		// Waypoint : PosZ
		new Text("* Position (Z)", this.getSplitWidth() - 64, this.getSplitHeight() + 35).withSize(TextSize.SMALL).draw();
		
		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() + 45)
			.withSize(new Size(130, 20))
			.withColor(new Colors(new Color(12, 25, 52)))
			.withBorders(true)
			.withTransparency(1F)
			.draw();
		
//		new Text("&90", this.getSplitWidth() - 60, this.getSplitHeight() + 51).draw();
		
//		new ColoredBackground(this.getSplitWidth() - 65, this.getSplitHeight() + 70)
//			.withSize(new Size(60, 15))
//			.withColor(new Colors(Colors.DARK_RED.getLightColor()))
//			.withBorders(true)
//			.draw();
		
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
