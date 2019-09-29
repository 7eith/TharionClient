package com.synezia.client.settings;

import java.io.File;

import com.google.common.reflect.TypeToken;
import com.synezia.client.Client;
import com.synezia.client.utilities.Disk;

import lombok.Getter;
import net.minecraft.client.Minecraft;

/**
 * @author Snkh
 *	29 sept. 2019
 */

public class SettingsManager {
	
	/**
	 * Instance
	 */
	
	public static SettingsManager i;
	
	/**
	 * Settings
	 */
	
	private Settings settings;
	
	/**
	 * File
	 */
	
	@Getter private File File = new File(Minecraft.getMinecraft().mcDataDir + "/settings/settings.json");
	
	
	public SettingsManager() {
		
		i = this;
		
		/**
		 * Create directory
		 */
		
		File directory = new File(Minecraft.getMinecraft().mcDataDir + "/settings/");
		
		if(!directory.exists())
			directory.mkdirs();
		
		/**
		 * Loading from Disk
		 */
		
		String content = Disk.readCatch(this.getFile());
		
		if(content == null)
			return;
		
		this.settings = Client.i.getGson().fromJson(content, new TypeToken<Settings>(){}.getType());
	}
	
	public Settings getSettings() {
		if(this.settings == null)  
			return settings = new Settings(); 
		
		return this.settings;
	}
	
	public void save() {
		
		Disk.writeCatch(this.getFile(), Client.i.getGson().toJson(this.getSettings()));
		
	}
}
