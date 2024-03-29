package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButtonLanguage extends GuiButton
{
    private static final String __OBFID = "CL_00000672";

    public GuiButtonLanguage(int p_i1041_1_, int p_i1041_2_, int p_i1041_3_)
    {
        super(p_i1041_1_, p_i1041_2_, p_i1041_3_, 20, 20, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {
            p_146112_1_.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var4 = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int var5 = 106;

            if (var4)
            {
                var5 += this.height;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, var5, this.width, this.height);
        }
    }
}
