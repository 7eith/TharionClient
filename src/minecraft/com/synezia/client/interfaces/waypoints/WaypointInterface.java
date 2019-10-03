package com.synezia.client.interfaces.waypoints;

import java.awt.Color;
import java.util.List;

import com.synezia.client.Client;
import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.components.backgrounds.TexturedBackground;
import com.synezia.client.components.buttons.Button;
import com.synezia.client.components.buttons.actions.Action;
import com.synezia.client.components.buttons.actions.DisplayScreenAction;
import com.synezia.client.components.buttons.actions.WaypointClickAction;
import com.synezia.client.components.buttons.informations.EscapeButtonInformations;
import com.synezia.client.components.buttons.informations.WaypointInformations;
import com.synezia.client.components.buttons.type.IconType;
import com.synezia.client.components.buttons.type.WaypointButtonType;
import com.synezia.client.components.texts.Text;
import com.synezia.client.components.texts.TextSize;
import com.synezia.client.interfaces.Interface;
import com.synezia.client.resources.Resource;
import com.synezia.client.utilities.Colors;
import com.synezia.client.waypoints.Waypoint;
import com.synezia.client.waypoints.WaypointManager;
import com.synezia.client.waypoints.WaypointType;

import net.minecraft.util.MathHelper;

/**
 * @author Snkh
 *	29 sept. 2019
 */

public class WaypointInterface extends Interface {
	
	/**
	 * 
	 * TODO: Systeme de tri, on doit pouvoir trier les waypoints par type, player event clan factions trade others, 
	 * 
	 * TODO: Systeme d'ajout de waypoints, on doit pouvoir ajouter un waypoint avec une couleur predefini pour chaque type, factions par exemple en bleu, et pouvoir la modifier
	 * via un systeme RGB qui creer une nouvelle colors. Pour ce systeme d'ajout creer un mini menu sur la droite, avec le titre 'Ajout d'un Waypoint', 
	 * le nom du waypoints en disant qu'on supporte les codes couleurs '123456789abcdef', la posX Y et Z par defaut la position du Joueur.
	 * 
	 * TODO: Systeme de pagination, le top serais avec la possibiliter de scroll mais bon, c'est chaud j'sais aps faire mais ca peut etre cool,
	 * vu qu'on peut afficher peu de waypoints sur les petits ecran par example, trouver un meilleur icon aussi et le foutre en blanc? et pourquoi pas changer
	 * la couleur du background?
	 * 
	 * TODO: Creer un systeme de coordonner reader via le chat pour lire les coordonnees et creer un waypoint, mais j'pense que c'possible ig 
	 * via un combo commande qui send un packet waypoint et un tellraw api (chat component ou on peut cliquer), mais c'est toujours cool comme nouveautes!
	 * 
	 * TODO: after, BONUS; faire un packet de waypoints pour par example recuperer les waypoints in game en se connectant, par example un waypoint f home, ap etc defini 
	 * par le chef de factions ou quoi
	 */
	
	private WaypointManager waypointsManager;
	private List<Waypoint> waypoints;
	
	private float transparency = 0.0F;
	private String message;
	
	public WaypointInterface() 
	{
		this.waypointsManager = Client.i.getWaypointsManager();
		this.waypoints = waypointsManager.getWaypoints();
		this.message = "&8[&9Informations&8] &9Cliquer sur un waypoint pour le desactiver et cliquer en maintenant CTRL pour le supprimer!";
	}
	
	public WaypointInterface(String message)
	{
		this();
		this.message = message;
		this.transparency = 0.7F;
	}

	@Override
	public void initializeInterface() 
	{
        Button home = new Button(7, 15);
        
        home.setSize(20, 20);
        home.setType(new IconType(Resource.HOME));
        home.setInformations(new EscapeButtonInformations(9, 38));
        home.setTitle("Accueil");
        home.addAction(new DisplayScreenAction(null));
        
        home.enable();
        
        Button addWaypoint = new Button(7, 50);
        
        addWaypoint.setSize(20, 20);
        addWaypoint.setType(new IconType(Resource.HOME));
        addWaypoint.setInformations(new EscapeButtonInformations(9, 73));
        addWaypoint.setTitle("Cr\u00e9er un waypoint");
        addWaypoint.addAction(new Action() {
			
			@Override
			public void execute() {
				
		        int x = MathHelper.floor_double(mc.thePlayer.posX);
		        int y = MathHelper.floor_double(mc.thePlayer.posY);
		        int z = MathHelper.floor_double(mc.thePlayer.posZ);
				waypoints.add(new Waypoint(WaypointType.PLAYER, "&eCaserne de clan", x, y, z));
				mc.displayGuiScreen(null);
			}
		});

        Integer count = 0;
        Integer supportedWaypoint = (this.getHeight() - 15) / 25; // taille total diviser par la taille d'un waypoint
        
        for (Waypoint waypoint : this.waypointsManager.getWaypoints())
        {
            Button component = new Button(35, (15) + 26 * count);
            
            component.setSize(145, 25);
            component.setType(new WaypointButtonType());
            component.setInformations(new WaypointInformations(waypoints.get(count)));
            component.addAction(new WaypointClickAction(count));
            
            if (supportedWaypoint > count)
            	this.addComponent("waypoint" + count, component);
        	
        	count++;
        }
        
        this.addComponent(home);
	}

	@Override
	public void drawComponents() {
        new ColoredBackground().withColor(new Colors(new Color(12, 25, 52), new Color(8, 3, 37))).withTransparency(transparency).draw();
        
        /** 
         * Sidebar
         */
        
        new ColoredBackground().withSize(new Size(35, this.getHeight())).withColor(new Colors(new Color(12, 25, 52), new Color(8, 3, 37))).withTransparency(0.9F).draw();
        
        /**
         * Waypoints 
         */
        
        new ColoredBackground(35, 15).withSize(new Size(145, this.getHeight() - 15)).withColor(new Colors(Color.GRAY, Color.BLACK)).withBorders(true).withTransparency(0.35F).draw(); // Background
        new ColoredBackground(35, 0).withSize(new Size(145, 15)).withColor(new Colors(new Color(44,56,126), Color.BLACK)).withTransparency(0.85F).draw(); // Title? 
        new Text("&7• &9Waypoints &7[&9" + this.waypoints.size() + "&7]", 38, 4).draw();
        
        /**
         * TopBar
         */
        
        new ColoredBackground(180, 0).withSize(new Size(width, 15)).withColor(new Colors(new Color(12, 25, 52), new Color(8, 3, 37))).withTransparency(0.6F).draw(); // Background
        new Text(message, 183, 4).draw();
	}

	@Override
	public void updateInterface() {

		if (this.transparency < 0.7F)
			this.transparency = transparency + 0.05F; 
		
	}

}
