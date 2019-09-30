package com.synezia.client.resources;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;

/**
 * @author Snkh
 *	22 jul. 2017
 */

@Getter
public enum Resource {
	
	LOGO("logo", ResourceType.INTERFACE),
	BACKGROUND("background", ResourceType.INTERFACE),
    
	HOME("home", ResourceType.ICON),
	ENVELOPE("envelope", ResourceType.ICON),
	WHITE_CLOCK("white-clock", ResourceType.ICON),
	
	AUDIO("audio", ResourceType.ICON),
	MONITOR("monitor", ResourceType.ICON),
	KEYBOARD("keyboard", ResourceType.ICON),
	COG("cog", ResourceType.ICON),
	
	
	PLACEHOLDER("placeholder", ResourceType.ICON),
	PLUS("plus", ResourceType.ICON);

	private String name;
	private ResourceType type;
	private ResourceLocation location;
	
	Resource(String name, ResourceType type) {
		this.name = name;
		this.type = type;
		this.location = this.addResource(name, type);
	}
	
    public ResourceLocation addResource(String name, ResourceType type) {
        return new ResourceLocation(type.getPath() + name + type.getFileExtension());
    }
}
