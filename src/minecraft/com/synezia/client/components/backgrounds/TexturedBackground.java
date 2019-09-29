package com.synezia.client.components.backgrounds;

import org.lwjgl.opengl.GL11;

import com.synezia.client.components.Size;
import com.synezia.client.interfaces.Interface;
import com.synezia.client.resources.Resource;
import com.synezia.client.utilities.Utilities;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Getter 
public class TexturedBackground extends Background {
	
	private Resource resource;
	
	public TexturedBackground() {
		super();
	}
	
	public TexturedBackground(Integer posX, Integer posY) {
		super(posX, posY);
	}
	
	public TexturedBackground withSize(Size size) {this.size = size; return this;}
	public TexturedBackground setResource(Resource resource) {this.resource = resource; return this;}

	@Override
	public void draw() {
		
		if(resource == null)
			return;
		
		if(this.size == Size.FULLSCREEN) 
			this.size = new Size(Interface.getWidth(), Interface.getHeight());
		
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource.getLocation());
        
        Utilities.displayImageSized(this.getPosX(), this.getPosY(), 0, 0, this.getSize().getWidth(), this.getSize().getHeight());
        
        GL11.glDisable((int)3042);
        GL11.glDisable((int)3008);
		
	}
}
