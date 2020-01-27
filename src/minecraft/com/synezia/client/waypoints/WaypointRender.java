package com.synezia.client.waypoints;

import org.lwjgl.opengl.GL11;

import com.synezia.client.components.texts.Text;
import com.synezia.client.components.texts.TextSize;
import com.synezia.client.utilities.Utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author Snkh
 *	29 sept. 2019
 */

public class WaypointRender extends Render {
	
    private Minecraft minecraft = Minecraft.getMinecraft();
    private FontRenderer fontRenderer;
    private Tessellator tessellator;
    private long time;
    private boolean animation;
    
    public WaypointRender() {
    	animation = true;
    	this.fontRenderer = minecraft.fontRenderer;
	}

    @Override
    public void doRender(Entity entity, double var2, double var4, double var6, float var8, float var9) {
        for (Waypoint waypoint : WaypointManager.getInstance().getWaypoints()) {
            double posX = (double)waypoint.getPosX() - RenderManager.renderPosX;
            double posY = (double)waypoint.getPosY() - RenderManager.renderPosY;
            double posZ = (double)waypoint.getPosZ() - RenderManager.renderPosZ;
            double distanceAtPlayer = waypoint.getDistanceToAnEntity(this.renderManager.livingPlayer);
            double maxDistance = (double)this.minecraft.gameSettings.getOptionFloatValue(GameSettings.Options.RENDER_DISTANCE) * 12.0;
            String name = Utilities.color("&7" + waypoint.getTitle() + " &8[&7" + (int)distanceAtPlayer + "m&8]");
            if (waypoint.isActive()) {
                RenderItem itemRenderer;
                ItemStack item;
                if (distanceAtPlayer > maxDistance) {
                    posX = posX / distanceAtPlayer * maxDistance;
                    posY = posY / distanceAtPlayer * maxDistance;
                    posZ = posZ / distanceAtPlayer * maxDistance;
                    distanceAtPlayer = maxDistance;
                }
                GL11.glAlphaFunc((int)516, (float)0.1f);
                int widthModifier = 0;
                int widthPos = 0;
                int width = this.fontRenderer.getStringWidth(name) / 2;
                float size = ((float)distanceAtPlayer * 0.09f + 1.0f) * 0.0166f;
                if (waypoint.getType() == WaypointType.CLAN) {
                    width = 8;
                    widthPos = 4;
                    if (System.currentTimeMillis() - this.time > 5000) {
                        this.animation = !this.animation;
                        this.time = System.currentTimeMillis();
                    }
                    if (distanceAtPlayer >= 25.0) {
                        waypoint.setActive(false);
                    } else if (!waypoint.isActive()) {
                        waypoint.setActive(true);
                    }
                } else if (waypoint.getType() == WaypointType.PLAYER) {
                    widthModifier = 10;
                    widthPos = 17;
                }
                width += widthModifier;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)((float)posX + 0.5f), (float)((float)posY + 1.3f), (float)((float)posZ + 0.5f));
                GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)(- RenderManager.instance.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)RenderManager.instance.playerViewX, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glRotated((double)180.0, (double)0.0, (double)0.0, (double)0.0);
                GL11.glScalef((float)size, (float)size, (float)size);
                GL11.glDisable((int)2896);
                GL11.glDisable((int)2912);
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glDepthMask((boolean)false);
                GL11.glBlendFunc((int)770, (int)771);
                Tessellator tessellator = Tessellator.instance;
                GL11.glDisable((int)3553);
                GL11.glEnable((int)2929);
                if (distanceAtPlayer < maxDistance) {
                    GL11.glDepthMask((boolean)true);
                }
                int r = waypoint.getColor().getLightColor() >> 16 & 255;
                int g = waypoint.getColor().getLightColor() >> 8 & 255;
                int b = waypoint.getColor().getLightColor() & 255;
                
                GL11.glEnable((int)32823);
                GL11.glPolygonOffset((float)1.0f, (float)3.0f);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(r, g, b, 150);
                tessellator.addVertex(- width - 2, -5.0, 0.0);
                tessellator.addVertex(- width - 2, 13.0, 0.0);
                tessellator.addVertex(width + 2, 13.0, 0.0);
                tessellator.addVertex(width + 2, -5.0, 0.0);
                tessellator.draw();
                
                GL11.glPolygonOffset((float)1.0f, (float)1.0f);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.15f);
                tessellator.addVertex(- width - 1, -4.0, 0.0);
                tessellator.addVertex(- width - 1, 12.0, 0.0);
                tessellator.addVertex(width + 1, 12.0, 0.0);
                tessellator.addVertex(width + 1, -4.0, 0.0);
                tessellator.draw();
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                
                GL11.glPolygonOffset((float)1.0f, (float)7.0f);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(r, g, b, 40);
                tessellator.addVertex(- width - 2, -4.0, 0.0);
                tessellator.addVertex(- width - 2, 12.0, 0.0);
                tessellator.addVertex(width + 2, 12.0, 0.0);
                tessellator.addVertex(width + 2, -4.0, 0.0);
                tessellator.draw();
                
                GL11.glPolygonOffset((float)1.0f, (float)5.0f);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.15f);
                tessellator.addVertex(- width - 1, -4.0, 0.0);
                tessellator.addVertex(- width - 1, 12.0, 0.0);
                tessellator.addVertex(width + 1, 12.0, 0.0);
                tessellator.addVertex(width + 1, -4.0, 0.0);
                tessellator.draw();
                
                GL11.glDisable((int)32823);
                GL11.glEnable((int)3553);
                if (waypoint.getType() == WaypointType.CLAN) {
                    if (this.animation) {
                        String info = Utilities.color("&e&l!");
                        new Text(info, widthPos + (- width), -2).withSize(TextSize.LARGE).draw();
                    } else {
                        item = new ItemStack(Items.paper);
                        itemRenderer = new RenderItem();
                        itemRenderer.renderItemIntoGUI(this.fontRenderer, this.minecraft.getTextureManager(), item, - width, -4);
                        new Text(waypoint.getTitle(), widthPos + (- width), -2).withSize(TextSize.LARGE).draw();
                    }
                } else if (waypoint.getType() == WaypointType.PLAYER) {
                    new Text(name, widthPos + (- width), 0).draw();
                    item = new ItemStack(Items.skull);
                    itemRenderer = new RenderItem();
                    itemRenderer.renderItemIntoGUI(this.fontRenderer, this.minecraft.getTextureManager(), item, - width, -4);
                } else {
                    new Text(name, widthPos + (- width), 0).draw();
                }
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
                GL11.glEnable((int)2912);
                GL11.glEnable((int)2896);
                GL11.glDisable((int)3042);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glPopMatrix();
                continue;
            }
            if (waypoint.getType() != WaypointType.TRADE) continue;
            if (distanceAtPlayer >= 25.0) {
                waypoint.setActive(false);
                continue;
            }
            if (waypoint.isActive()) continue;
            waypoint.setActive(true);
        }
    }
    @Override
    protected ResourceLocation getEntityTexture(Entity var1) {
        return null;
    }
}

