package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.optifine.Config;
import net.minecraft.shader.client.Shaders;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSpider extends RenderLiving
{
    public static final ResourceLocation spiderEyesTextures = new ResourceLocation("textures/entity/spider_eyes.png");
    public static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");
    private static final String __OBFID = "CL_00001027";

    public RenderSpider()
    {
        super(new ModelSpider(), 1.0F);
        this.setRenderPassModel(new ModelSpider());
    }

    public float getDeathMaxRotation(EntitySpider par1EntityLivingBase)
    {
        return 180.0F;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    public int shouldRenderPass(EntitySpider par1EntityLivingBase, int par2, float par3)
    {
        if (par2 != 0)
        {
            return -1;
        }
        else
        {
            this.bindTexture(spiderEyesTextures);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (par1EntityLivingBase.isInvisible())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            char var4 = 61680;
            int var5 = var4 % 65536;
            int var6 = var4 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var5 / 1.0F, (float)var6 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (Config.isShaders())
            {
                Shaders.beginSpiderEyes();
            }

            return 1;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySpider par1Entity)
    {
        return spiderTextures;
    }

    public float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase)
    {
        return this.getDeathMaxRotation((EntitySpider)par1EntityLivingBase);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    public int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return this.shouldRenderPass((EntitySpider)par1EntityLivingBase, par2, par3);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getEntityTexture((EntitySpider)par1Entity);
    }
}
