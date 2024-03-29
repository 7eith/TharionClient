package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.optifine.Config;
import net.minecraft.optifine.Reflector;
import net.minecraft.shader.client.Shaders;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class RendererLivingEntity extends Render
{
    public static final Logger logger = LogManager.getLogger();
    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public ModelBase mainModel;

    /** The model to be used during the render passes. */
    public ModelBase renderPassModel;
    public static float NAME_TAG_RANGE = 64.0F;
    public static float NAME_TAG_RANGE_SNEAK = 32.0F;
    private static final String __OBFID = "CL_00001012";

    public RendererLivingEntity(ModelBase par1ModelBase, float par2)
    {
        this.mainModel = par1ModelBase;
        this.shadowSize = par2;
    }

    /**
     * Sets the model to be used in the current render pass (the first render pass is done after the primary model is
     * rendered) Args: model
     */
    public void setRenderPassModel(ModelBase par1ModelBase)
    {
        this.renderPassModel = par1ModelBase;
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    public float interpolateRotation(float par1, float par2, float par3)
    {
        float var4;

        for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, new Object[] {par1Entity, this, Double.valueOf(par2), Double.valueOf(par4), Double.valueOf(par6)}))
        {
            boolean isShaders = Config.isShaders();
            float var25;
            float var26;
            float var14;
            float var15;
            float var16;

            if (isShaders && Shaders.useEntityColor)
            {
                if (par1Entity.hurtTime <= 0 && par1Entity.deathTime <= 0)
                {
                    var25 = par1Entity.getBrightness(par9);
                    int var11 = this.getColorMultiplier(par1Entity, var25, par9);
                    boolean var13 = (var11 >> 24 & 255) > 0;

                    if (var13)
                    {
                        var26 = (float)(var11 >> 24 & 255) / 255.0F;
                        var14 = (float)(var11 >> 16 & 255) / 255.0F;
                        var15 = (float)(var11 >> 8 & 255) / 255.0F;
                        var16 = (float)(var11 & 255) / 255.0F;
                        Shaders.setEntityColor(var14, var15, var16, 1.0F - var26);
                    }
                }
                else
                {
                    Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F);
                }
            }

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            this.mainModel.onGround = this.renderSwingProgress(par1Entity, par9);

            if (this.renderPassModel != null)
            {
                this.renderPassModel.onGround = this.mainModel.onGround;
            }

            this.mainModel.isRiding = par1Entity.isRiding();

            if (this.renderPassModel != null)
            {
                this.renderPassModel.isRiding = this.mainModel.isRiding;
            }

            this.mainModel.isChild = par1Entity.isChild();

            if (this.renderPassModel != null)
            {
                this.renderPassModel.isChild = this.mainModel.isChild;
            }

            try
            {
                var25 = this.interpolateRotation(par1Entity.prevRenderYawOffset, par1Entity.renderYawOffset, par9);
                float var271 = this.interpolateRotation(par1Entity.prevRotationYawHead, par1Entity.rotationYawHead, par9);
                float var28;

                if (par1Entity.isRiding() && par1Entity.ridingEntity instanceof EntityLivingBase)
                {
                    EntityLivingBase var291 = (EntityLivingBase)par1Entity.ridingEntity;
                    var25 = this.interpolateRotation(var291.prevRenderYawOffset, var291.renderYawOffset, par9);
                    var28 = MathHelper.wrapAngleTo180_float(var271 - var25);

                    if (var28 < -85.0F)
                    {
                        var28 = -85.0F;
                    }

                    if (var28 >= 85.0F)
                    {
                        var28 = 85.0F;
                    }

                    var25 = var271 - var28;

                    if (var28 * var28 > 2500.0F)
                    {
                        var25 += var28 * 0.2F;
                    }
                }

                var26 = par1Entity.prevRotationPitch + (par1Entity.rotationPitch - par1Entity.prevRotationPitch) * par9;
                this.renderLivingAt(par1Entity, par2, par4, par6);
                var28 = this.handleRotationFloat(par1Entity, par9);
                this.rotateCorpse(par1Entity, var28, var25, par9);
                var14 = 0.0625F;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glScalef(-1.0F, -1.0F, 1.0F);
                this.preRenderCallback(par1Entity, par9);
                GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
                var15 = par1Entity.prevLimbSwingAmount + (par1Entity.limbSwingAmount - par1Entity.prevLimbSwingAmount) * par9;
                var16 = par1Entity.limbSwing - par1Entity.limbSwingAmount * (1.0F - par9);

                if (par1Entity.isChild())
                {
                    var16 *= 3.0F;
                }

                if (var15 > 1.0F)
                {
                    var15 = 1.0F;
                }

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                this.mainModel.setLivingAnimations(par1Entity, var16, var15, par9);
                this.renderModel(par1Entity, var16, var15, var28, var271 - var25, var26, var14);
                int var18;
                float var19;
                float var20;
                int var29;
                float var22;

                for (int var27 = 0; var27 < 4; ++var27)
                {
                    var18 = this.shouldRenderPass(par1Entity, var27, par9);

                    if (var18 > 0)
                    {
                        this.renderPassModel.setLivingAnimations(par1Entity, var16, var15, par9);
                        this.renderPassModel.render(par1Entity, var16, var15, var28, var271 - var25, var26, var14);

                        if ((var18 & 240) == 16)
                        {
                            this.func_82408_c(par1Entity, var27, par9);
                            this.renderPassModel.render(par1Entity, var16, var15, var28, var271 - var25, var26, var14);
                        }

                        if ((var18 & 15) == 15)
                        {
                            var19 = (float)par1Entity.ticksExisted + par9;
                            this.bindTexture(RES_ITEM_GLINT);
                            GL11.glEnable(GL11.GL_BLEND);
                            var20 = 0.5F;
                            GL11.glColor4f(var20, var20, var20, 1.0F);
                            GL11.glDepthFunc(GL11.GL_EQUAL);
                            GL11.glDepthMask(false);

                            for (var29 = 0; var29 < 2; ++var29)
                            {
                                GL11.glDisable(GL11.GL_LIGHTING);
                                var22 = 0.76F;
                                GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                                GL11.glMatrixMode(GL11.GL_TEXTURE);
                                GL11.glLoadIdentity();
                                float var30 = var19 * (0.001F + (float)var29 * 0.003F) * 20.0F;
                                float var24 = 0.33333334F;
                                GL11.glScalef(var24, var24, var24);
                                GL11.glRotatef(30.0F - (float)var29 * 60.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslatef(0.0F, var30, 0.0F);
                                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                                this.renderPassModel.render(par1Entity, var16, var15, var28, var271 - var25, var26, var14);
                            }

                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glDepthMask(true);
                            GL11.glLoadIdentity();
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            GL11.glEnable(GL11.GL_LIGHTING);
                            GL11.glDisable(GL11.GL_BLEND);
                            GL11.glDepthFunc(GL11.GL_LEQUAL);
                        }

                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                    }
                }

                GL11.glDepthMask(true);

                if (isShaders && Shaders.useEntityColor)
                {
                    Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
                }

                this.renderEquippedItems(par1Entity, par9);

                if (!isShaders || !Shaders.useEntityColor)
                {
                    float var301 = par1Entity.getBrightness(par9);
                    var18 = this.getColorMultiplier(par1Entity, var301, par9);
                    OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

                    if (isShaders)
                    {
                        Shaders.disableLightmap();
                    }

                    if ((var18 >> 24 & 255) > 0 || par1Entity.hurtTime > 0 || par1Entity.deathTime > 0)
                    {
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glDepthFunc(GL11.GL_EQUAL);

                        if (isShaders)
                        {
                            Shaders.beginLivingDamage();
                        }

                        if (par1Entity.hurtTime > 0 || par1Entity.deathTime > 0)
                        {
                            GL11.glColor4f(var301, 0.0F, 0.0F, 0.4F);
                            this.mainModel.render(par1Entity, var16, var15, var28, var271 - var25, var26, var14);

                            for (var29 = 0; var29 < 4; ++var29)
                            {
                                if (this.inheritRenderPass(par1Entity, var29, par9) >= 0)
                                {
                                    GL11.glColor4f(var301, 0.0F, 0.0F, 0.4F);
                                    this.renderPassModel.render(par1Entity, var16, var15, var28, var271 - var25, var26, var14);
                                }
                            }
                        }

                        if ((var18 >> 24 & 255) > 0)
                        {
                            var19 = (float)(var18 >> 16 & 255) / 255.0F;
                            var20 = (float)(var18 >> 8 & 255) / 255.0F;
                            float var31 = (float)(var18 & 255) / 255.0F;
                            var22 = (float)(var18 >> 24 & 255) / 255.0F;
                            GL11.glColor4f(var19, var20, var31, var22);
                            this.mainModel.render(par1Entity, var16, var15, var28, var271 - var25, var26, var14);

                            for (int var32 = 0; var32 < 4; ++var32)
                            {
                                if (this.inheritRenderPass(par1Entity, var32, par9) >= 0)
                                {
                                    GL11.glColor4f(var19, var20, var31, var22);
                                    this.renderPassModel.render(par1Entity, var16, var15, var28, var271 - var25, var26, var14);
                                }
                            }
                        }

                        GL11.glDepthFunc(GL11.GL_LEQUAL);

                        if (isShaders)
                        {
                            Shaders.endLivingDamage();
                        }

                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                    }
                }

                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            }
            catch (Exception var261)
            {
                logger.error("Couldn\'t render entity", var261);
            }

            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if (isShaders)
            {
                Shaders.enableLightmap();
            }

            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
            this.passSpecialRender(par1Entity, par2, par4, par6);

            if (Reflector.RenderLivingEvent_Post_Constructor.exists())
            {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, new Object[] {par1Entity, this, Double.valueOf(par2), Double.valueOf(par4), Double.valueOf(par6)});
            }
        }
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.bindEntityTexture(par1EntityLivingBase);

        if (!par1EntityLivingBase.isInvisible())
        {
            this.mainModel.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
        }
        else if (!par1EntityLivingBase.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            this.mainModel.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
        }
        else
        {
            this.mainModel.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLivingBase);
        }
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    {
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
    }

    protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
    {
        GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);

        if (par1EntityLivingBase.deathTime > 0)
        {
            float var6 = ((float)par1EntityLivingBase.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
            var6 = MathHelper.sqrt_float(var6);

            if (var6 > 1.0F)
            {
                var6 = 1.0F;
            }

            GL11.glRotatef(var6 * this.getDeathMaxRotation(par1EntityLivingBase), 0.0F, 0.0F, 1.0F);
        }
        else
        {
            String var61 = EnumChatFormatting.getTextWithoutFormattingCodes(par1EntityLivingBase.getCommandSenderName());

            if ((var61.equals("Dinnerbone") || var61.equals("Grumm")) && (!(par1EntityLivingBase instanceof EntityPlayer) || !((EntityPlayer)par1EntityLivingBase).getHideCape()))
            {
                GL11.glTranslatef(0.0F, par1EntityLivingBase.height + 0.1F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    public float renderSwingProgress(EntityLivingBase par1EntityLivingBase, float par2)
    {
        return par1EntityLivingBase.getSwingProgress(par2);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2)
    {
        return (float)par1EntityLivingBase.ticksExisted + par2;
    }

    protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2) {}

    /**
     * renders arrows the Entity has been attacked with, attached to it
     */
    public void renderArrowsStuckInEntity(EntityLivingBase par1EntityLivingBase, float par2)
    {
        int var3 = par1EntityLivingBase.getArrowCountInEntity();

        if (var3 > 0)
        {
            EntityArrow var4 = new EntityArrow(par1EntityLivingBase.worldObj, par1EntityLivingBase.posX, par1EntityLivingBase.posY, par1EntityLivingBase.posZ);
            Random var5 = new Random((long)par1EntityLivingBase.getEntityId());
            RenderHelper.disableStandardItemLighting();

            for (int var6 = 0; var6 < var3; ++var6)
            {
                GL11.glPushMatrix();
                ModelRenderer var7 = this.mainModel.getRandomModelBox(var5);
                ModelBox var8 = (ModelBox)var7.cubeList.get(var5.nextInt(var7.cubeList.size()));
                var7.postRender(0.0625F);
                float var9 = var5.nextFloat();
                float var10 = var5.nextFloat();
                float var11 = var5.nextFloat();
                float var12 = (var8.posX1 + (var8.posX2 - var8.posX1) * var9) / 16.0F;
                float var13 = (var8.posY1 + (var8.posY2 - var8.posY1) * var10) / 16.0F;
                float var14 = (var8.posZ1 + (var8.posZ2 - var8.posZ1) * var11) / 16.0F;
                GL11.glTranslatef(var12, var13, var14);
                var9 = var9 * 2.0F - 1.0F;
                var10 = var10 * 2.0F - 1.0F;
                var11 = var11 * 2.0F - 1.0F;
                var9 *= -1.0F;
                var10 *= -1.0F;
                var11 *= -1.0F;
                float var15 = MathHelper.sqrt_float(var9 * var9 + var11 * var11);
                var4.prevRotationYaw = var4.rotationYaw = (float)(Math.atan2((double)var9, (double)var11) * 180.0D / Math.PI);
                var4.prevRotationPitch = var4.rotationPitch = (float)(Math.atan2((double)var10, (double)var15) * 180.0D / Math.PI);
                double var16 = 0.0D;
                double var18 = 0.0D;
                double var20 = 0.0D;
                float var22 = 0.0F;
                this.renderManager.renderEntityWithPosYaw(var4, var16, var18, var20, var22, par2);
                GL11.glPopMatrix();
            }

            RenderHelper.enableStandardItemLighting();
        }
    }

    protected int inheritRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return this.shouldRenderPass(par1EntityLivingBase, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return -1;
    }

    protected void func_82408_c(EntityLivingBase par1EntityLivingBase, int par2, float par3) {}

    protected float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase)
    {
        return 90.0F;
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLivingBase par1EntityLivingBase, float par2, float par3)
    {
        return 0;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {}

    /**
     * Passes the specialRender and renders it
     */
    public void passSpecialRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    {
        if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, new Object[] {par1EntityLivingBase, this, Double.valueOf(par2), Double.valueOf(par4), Double.valueOf(par6)}))
        {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

            if (this.func_110813_b(par1EntityLivingBase))
            {
                float var8 = 1.6F;
                float var9 = 0.016666668F * var8;
                double var10 = par1EntityLivingBase.getDistanceSqToEntity(this.renderManager.livingPlayer);
                float var12 = par1EntityLivingBase.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

                if (var10 < (double)(var12 * var12))
                {
                    String var13 = par1EntityLivingBase.func_145748_c_().getFormattedText();

                    if (par1EntityLivingBase.isSneaking())
                    {
                        FontRenderer var14 = this.getFontRendererFromRenderManager();
                        GL11.glPushMatrix();
                        GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + par1EntityLivingBase.height + 0.5F, (float)par6);
                        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                        GL11.glScalef(-var9, -var9, var9);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
                        GL11.glDepthMask(false);
                        GL11.glEnable(GL11.GL_BLEND);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                        Tessellator var15 = Tessellator.instance;
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        var15.startDrawingQuads();
                        int var16 = var14.getStringWidth(var13) / 2;
                        var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                        var15.addVertex((double)(-var16 - 1), -1.0D, 0.0D);
                        var15.addVertex((double)(-var16 - 1), 8.0D, 0.0D);
                        var15.addVertex((double)(var16 + 1), 8.0D, 0.0D);
                        var15.addVertex((double)(var16 + 1), -1.0D, 0.0D);
                        var15.draw();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glDepthMask(true);
                        var14.drawString(var13, -var14.getStringWidth(var13) / 2, 0, 553648127);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glPopMatrix();
                    }
                    else
                    {
                        this.func_96449_a(par1EntityLivingBase, par2, par4, par6, var13, var9, var10);
                    }
                }
            }

            if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists())
            {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, new Object[] {par1EntityLivingBase, this, Double.valueOf(par2), Double.valueOf(par4), Double.valueOf(par6)});
            }
        }
    }

    protected boolean func_110813_b(EntityLivingBase par1EntityLivingBase)
    {
        return Minecraft.isGuiEnabled() && par1EntityLivingBase != this.renderManager.livingPlayer && !par1EntityLivingBase.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && par1EntityLivingBase.riddenByEntity == null;
    }

    protected void func_96449_a(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, String par8Str, float par9, double par10)
    {
        if (par1EntityLivingBase.isPlayerSleeping())
        {
            this.func_147906_a(par1EntityLivingBase, par8Str, par2, par4 - 1.5D, par6, 64);
        }
        else
        {
            this.func_147906_a(par1EntityLivingBase, par8Str, par2, par4, par6, 64);
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRender((EntityLivingBase)par1Entity, par2, par4, par6, par8, par9);
    }

    public ModelBase getMainModel()
    {
        return this.mainModel;
    }
}
