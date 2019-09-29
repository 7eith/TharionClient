package com.synezia.client;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synezia.client.settings.SettingsManager;

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
	
	/**
	 * Client constructor
	 */
	
	public Client()
	{
		i = this;
		this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).create();
		this.settingsManager = new SettingsManager();
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
	}
	
}
