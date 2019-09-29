package com.synezia.client.utilities;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.synezia.client.resources.Resource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Snkh
 *	26 jul. 2018
 */

public class Utilities {
	
	/**
	 * Components - Color
	 */
	
    public static void setGlColor(int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        setGlColor(color, alpha);
    }

    public static void setGlColor(int color, float transparency) {
        float red = (float)(color >> 16 & 255) / 255.0f;
        float blue = (float)(color >> 8 & 255) / 255.0f;
        float green = (float)(color & 255) / 255.0f;
        GL11.glColor4f((float)red, (float)blue, (float)green, (float)transparency);
    }

    public static Color convertGlColor(int color, float transparency) {
        float red = (float)(color >> 16 & 255) / 255.0f;
        float blue = (float)(color >> 8 & 255) / 255.0f;
        float green = (float)(color & 255) / 255.0f;
        return new Color(red, blue, green, transparency);
    }
    
    /**
     * Components - Draw
     */
    
    public static void displayImageSized(float posX, float posY, int u, int v, float width, float height) {
        float scaledX = 1.0f / width;
        float scaledY = 1.0f / height;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(posX, posY + height, 0.0, (float)u * scaledX, ((float)v + height) * scaledY);
        tessellator.addVertexWithUV(posX + width, posY + height, 0.0, ((float)u + width) * scaledX, ((float)v + height) * scaledY);
        tessellator.addVertexWithUV(posX + width, posY, 0.0, ((float)u + width) * scaledX, (float)v * scaledY);
        tessellator.addVertexWithUV(posX, posY, 0.0, (float)u * scaledX, (float)v * scaledY);
        tessellator.draw();
    }

    public static void displayGradientColor(float posX, float posY, float width, float height, int firstColor, int secondColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3008);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel((int)7425);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        setGlColor(firstColor);
        tessellator.addVertex(width, posY, 90.0);
        tessellator.addVertex(posX, posY, 90.0);
        setGlColor(secondColor);
        tessellator.addVertex(posX, height, 90.0);
        tessellator.addVertex(width, height, 90.0);
        tessellator.draw();
        GL11.glShadeModel((int)7424);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3553);
    }

    public static void displayEntity(EntityLivingBase entity, int posX, int poxY, int entitySize, float posViewX, float posViewY) {
        GL11.glEnable((int)2903);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)posX, (float)poxY, (float)50.0f);
        GL11.glScalef((float)(- entitySize), (float)entitySize, (float)entitySize);
        GL11.glRotatef((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float yawOffset = entity.renderYawOffset;
        float yaw = entity.rotationYaw;
        float pitch = entity.rotationPitch;
        float rotationYawHead = entity.prevRotationYawHead;
        float yawHead = entity.rotationYawHead;
        GL11.glRotatef((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)((- (float)Math.atan(posViewY / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        entity.renderYawOffset = (float)Math.atan(posViewX / 40.0f) * 20.0f;
        entity.rotationYaw = (float)Math.atan(posViewX / 40.0f) * 40.0f;
        entity.rotationPitch = (- (float)Math.atan(posViewY / 40.0f)) * 20.0f;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager.instance.playerViewY = 180.0f;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        entity.renderYawOffset = yawOffset;
        entity.rotationYaw = yaw;
        entity.rotationPitch = pitch;
        entity.prevRotationYawHead = rotationYawHead;
        entity.rotationYawHead = yawHead;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable((int)32826);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable((int)3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    public static void displayLogo(Integer width, Integer height, Integer posX, Integer posY) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
        Minecraft.getMinecraft().getTextureManager().bindTexture(Resource.LOGO.getLocation()); // TO REPLACE
        displayImageSized(posX, posY, 0, 0, width, height);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
    }
    
    /**
     * Component - Text
     */
    
    public static String color(String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        int i = 0;
        
        while (i < b.length - 1) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
            ++i;
        }
        
        return new String(b);
    }

    public static void displayString(String value, float posX, float posY) {
    	Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(color(value), (int)posX, (int)posY, 16777215);
    }
    
    public static void displayString(String value, float posX, float posY, Colors color) {
    	Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(color(value), (int)posX, (int)posY, color.getLightColor());
    }
    
    public static void displayString(String value, float posX, float posY, Integer color) {
    	Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(color(value), (int)posX, (int)posY, color);
    }
    
    public static int getStringWidth(String title) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(color(title));
    }

}
