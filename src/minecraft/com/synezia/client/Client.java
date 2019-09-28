package com.synezia.client;

import org.apache.logging.log4j.LogManager;

import lombok.Getter;
import net.minecraft.client.Minecraft;

public class Client {
	
	/**
	 * Client instance
	 */
	
	@Getter private static Client i;
	
	/**
	 * Debug Mode
	 */

	@Getter private boolean debug = false;
	
	/**
	 * Client constructor
	 */
	
	public Client()
	{
		i = this;
		LogManager.getLogger().debug("Launching Client");
	}
	
	public void start()
	{
		
	}
	
	public void runTick()
	{
		
	}
	
	public void shutdown()
	{
		
	}

}
