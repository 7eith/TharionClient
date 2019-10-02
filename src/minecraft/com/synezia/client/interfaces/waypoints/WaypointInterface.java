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
import com.synezia.client.components.buttons.informations.EscapeButtonInformations;
import com.synezia.client.components.buttons.type.IconType;
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
	 * TODO: Stop display waypoints if gui scale is not good (response fuck), par example, on get la taille de l'ecran, on enleve la taille de la barre du title, 
	 * et on enleve la taille du button d'en bas, puis on calcule combien d'item on peut poser dedans, par example pour du 1600 * 900 = 15 avant un breakpoint
	 * 
	 * TODO: Systeme de tri, on doit pouvoir trier les waypoints par type, player event clan factions trade others, 
	 * 
	 * TODO: Systeme de clique, on doit en cliquant sur un waypoints pouvoir afficher les informations du waypoint, pos type color etc...
	 * on doit aussi pouvoir en fesant CTRL Click donc keyEnter (event), activer et desactiver le waypoints, qu'il soit possible ou pas de le voir.
	 * en appuyant sur la croix on supprime le waypoints, (on quitte l'interface ?) et on dis dans le chat que ce waypoint est bien supprimer.
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
	
	public WaypointInterface() 
	{
		this.waypointsManager = Client.i.getWaypointsManager();
		this.waypoints = waypointsManager.getWaypoints();
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
        addWaypoint.setTitle("Add Waypoint");
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
        
        addWaypoint.enable();
        
        this.addComponent(home);
        this.addComponent(addWaypoint);
	}

	@Override
	public void drawComponents() {
		
        new ColoredBackground().withColor(new Colors(new Color(12, 25, 52), new Color(8, 3, 37))).withTransparency(transparency).draw();
        
        /**
         * Calculs helpful
         */
        
        Integer width = Interface.getWidth();
        Integer height = Interface.getHeight();
        
        /** 
         * Sidebar
         */
        
        new ColoredBackground().withSize(new Size(35, height)).withColor(new Colors(new Color(12, 25, 52), new Color(8, 3, 37))).withTransparency(0.9F).draw();
        
        // Siderbar right 
        
        new ColoredBackground(180, 0).withSize(new Size(width, 15)).withColor(new Colors(new Color(12, 25, 52), new Color(8, 3, 37))).withTransparency(0.6F).draw(); // Background
//        new Text("&7• &cOptions > Mods > Waypoints", 183, 4).draw();
//        new Text("X", width - 10, 4).draw();
        
        new ColoredBackground(35, 0).withSize(new Size(145, height)).withColor(new Colors(Color.GRAY, Color.BLACK)).withBorders(true).withTransparency(0.35F).draw(); // Background
        new ColoredBackground(35, 0).withSize(new Size(145, 15)).withColor(new Colors(new Color(44,56,126), Color.BLACK)).withTransparency(0.85F).draw(); // Title? 
        new Text("&7• &9Waypoints &4[" + this.waypoints.size() + "]", 38, 4).draw();
        
        // Integer nb = 0; nb < 12; nb++
        Integer nb = 0;
        for(Waypoint waypoint : waypoints) {
        	
            int NotifX = 110;
            int NotifY = (15) + 26 * nb; // ?
            int NotifW = 145;
            int NotifH = 25;
            
            new ColoredBackground(NotifX - 75, NotifY).withSize(new Size(NotifW, NotifH)).withColor(new Colors(new Color(12, 25, 52), Color.BLACK)).withTransparency(0.4f).withBorders(true).draw();
            new TexturedBackground(NotifX - 73, NotifY + 3).withSize(new Size(20, 20)).setResource(Resource.PLACEHOLDER).draw();
            new Text(waypoint.getTitle(), NotifX - 50, NotifY + 7).withSize(TextSize.SMALL).draw();
            new Text("&a" + waypoint.getType(), NotifX - 47, NotifY + 16).withSize(TextSize.SMALL).draw();
            new Text("X", NotifX + 63, NotifY + 2).withSize(TextSize.SMALL).draw();
            
            nb++;
        }
        
        new ColoredBackground(40, this.getHeight() - 25).withSize(new Size(135, 20)).withColor(new Colors(new Color(12, 25, 52))).withBorders(true).withTransparency(0.9F).draw();
        new Text("&cFactions", 45, this.getHeight() - 19).draw();
        
        new ColoredBackground(200, this.getSplitHeight() - 100).withSize(new Size(135, 200)).withColor(Colors.DARK_RED).withBorders(true).withTransparency(0.7F).draw();
        
	}

	@Override
	public void updateInterface() {

		if (this.transparency < 0.7F)
			this.transparency = transparency + 0.05F; 
		
	}

}
