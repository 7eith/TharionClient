package com.synezia.client.components.backgrounds;

import org.lwjgl.opengl.GL11;

import com.synezia.client.components.Size;
import com.synezia.client.interfaces.Interface;
import com.synezia.client.utilities.Colors;
import com.synezia.client.utilities.Utilities;

import lombok.Getter;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@Getter
public class ColoredBackground extends Background {
	
	private Colors color;
	private float transparency = 1.0f;
	private boolean borders = false;
	
	public ColoredBackground() {
		super();
	}

	public ColoredBackground(Integer posX, Integer posY) {
		super(posX, posY);
	}
	
	public ColoredBackground withSize(Size size) {this.size = size; return this;}
	public ColoredBackground withColor(Colors colors) {this.color = colors; return this;}
	public ColoredBackground withTransparency(Float transparency) {this.transparency = transparency; return this;}
	public ColoredBackground withBorders(Boolean borders) {this.borders = borders; return this;}

	@Override
	public void draw() {
		
		if(this.size == Size.FULLSCREEN) {
			this.size = new Size(Interface.getWidth(), Interface.getHeight());
		}
		
        Tessellator tessellator = Tessellator.instance;
        
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        
        Utilities.setGlColor(this.color.getLightColor(), this.transparency);
        
        tessellator.startDrawingQuads();
        tessellator.addVertex(this.getPosX(), this.getPosY() + this.getSize().getHeight(), 0.0);
        tessellator.addVertex(this.getPosX() + this.getSize().getWidth(), this.getPosY() + this.getSize().getHeight(), 0.0);
        tessellator.addVertex(this.getPosX() + this.getSize().getWidth(), this.getPosY(), 0.0);
        tessellator.addVertex(this.getPosX(), this.getPosY(), 0.0);
        tessellator.draw();
        
        
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)this.transparency);
        
        if (this.borders) {
            Integer color = Utilities.convertGlColor(this.color.getDarkColor(), this.transparency).getRGB();
            Utilities.displayGradientColor(this.getPosX() - 1, this.getPosY() - 1, this.getPosX(), this.getPosY() + this.getSize().getHeight() + 1, color, color);
            Utilities.displayGradientColor(this.getPosX() + this.getSize().getWidth(), this.getPosY(), this.getPosX() + this.getSize().getWidth() + 1, this.getPosY() + this.getSize().getHeight(), color, color);
            Utilities.displayGradientColor(this.getPosX(), this.getPosY() - 1, this.getPosX() + this.getSize().getWidth() + 1, this.getPosY(), color, color);
            Utilities.displayGradientColor(this.getPosX(), this.getPosY() + this.getSize().getHeight(), this.getPosX() + this.getSize().getWidth() + 1, this.getPosY() + this.getSize().getHeight() + 1, color, color);
        }
        
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
		
	}
}
