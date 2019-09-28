package com.synezia.client;

import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synezia.client.session.ClientSession;

import lombok.Getter;

public class Client {
	
	/**
	 * Client instance
	 */
	
	@Getter public static Client i;
	
	/**
	 * Client constructor
	 */
	
	public Client()
	{
		i = this;
		LogManager.getLogger().info("Launching Client");
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
	
	/***************************
	 * 
	 * Others Methods
	 * 
	 **************************/

	public Gson getGsonInstance() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).create();
	}
	
}
