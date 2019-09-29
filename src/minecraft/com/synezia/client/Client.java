package com.synezia.client;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synezia.client.settings.SettingsManager;
import com.synezia.client.utilities.Colors;
import com.synezia.client.utilities.ColorsAdapter;
import com.synezia.client.waypoints.WaypointManager;

import lombok.Getter;

/**
 * @author Snkh
 *	29 sept. 2019
 */

public class Client {
	
	/**
	 * Client instance
	 */
	
	@Getter public static Client i;
	
	/**
	 * Gson Instance
	 */
	
	@Getter private Gson gson;
	
	/**
	 * 	Managers
	 */
	
	@Getter private SettingsManager settingsManager;
	@Getter private WaypointManager waypointsManager;
	
	/**
	 * Client constructor
	 */
	
	public Client()
	{
		i = this;
		this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).registerTypeAdapter(Colors.class, new ColorsAdapter()).create();
		
		this.settingsManager = new SettingsManager();
		this.waypointsManager = new WaypointManager();
	}
	
	public void start()
	{
		
	}
	
	public void runTick()
	{
		
	}
	
	public void shutdown()
	{
		this.settingsManager.save();
		this.waypointsManager.save();
		
	}
	
}
