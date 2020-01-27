package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.components.texts.Text;
import com.synezia.client.utilities.Colors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiNewChat extends Gui
{
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;

    /** A list of messages previously sent through the chat GUI */
    private final List sentMessages = new ArrayList();

    /** Chat lines to be displayed in the chat box */
    private final List chatLines = new ArrayList();
    private final List drawnChatLines = new ArrayList();
    private int scrollPos;
    private boolean isScrolled;

    public GuiNewChat(Minecraft instance)
    {
        this.mc = instance;
    }

    public void drawChat(int updateCounter)
    {
//        new ColoredBackground(0, 0).withSize(new Size(getChatWidth(), getChatHeight())).withColor(Colors.FRUIT_RED).withTransparency(0.9F).draw(); // Background
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            int lineCount = this.getLineCount();
            boolean chatOpened = false;
            int var4 = 0;
            int drawnChatLines = this.drawnChatLines.size();
            float opacity = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (drawnChatLines > 0)
            {
                if (this.getChatOpen())
                {
                    chatOpened = true;
                }

                float chatScale = this.getChatScale();
                int chatSize = MathHelper.ceiling_float_int((float)this.getChatWidth() / chatScale);
                GL11.glPushMatrix();
                GL11.glTranslatef(2.0F, 20.0F, 0.0F);
                GL11.glScalef(chatScale, chatScale, 1.0F);
                int var9;
                int var11;
                int updatedOpacity;

                for (var9 = 0; var9 + this.scrollPos < this.drawnChatLines.size() && var9 < lineCount; ++var9)
                {
                    ChatLine chatLine = (ChatLine)this.drawnChatLines.get(var9 + this.scrollPos);
                    if (chatLine != null)
                    {
                        var11 = updateCounter - chatLine.getUpdatedCounter();

                        if (var11 < 200 || chatOpened)
                        {
                            double var12 = (double)var11 / 200.0D;
                            var12 = 1.0D - var12;
                            var12 *= 10.0D;

                            if (var12 < 0.0D)
                            {
                                var12 = 0.0D;
                            }

                            if (var12 > 1.0D)
                            {
                                var12 = 1.0D;
                            }

                            var12 *= var12;
                            updatedOpacity = (int)(255.0D * var12);

                            if (chatOpened)
                            {
                            	updatedOpacity = 255;
                            }

                            updatedOpacity = (int)((float)updatedOpacity * opacity);
                            ++var4;

                            if (updatedOpacity > 3)
                            {
                                byte var15 = 0;
                                int var16 = -var9 * 9;
                                drawRect(0, var16 - 9, var15 + chatSize + 4, var16, updatedOpacity / 2 << 24);
                                String str = chatLine.getChatComponent().getFormattedText();
                                this.mc.fontRenderer.drawStringWithShadow(str, var15, var16 - 8, 16777215 + (updatedOpacity << 24));
                                GL11.glDisable(GL11.GL_ALPHA_TEST);
                            }
                        }
                    }
                }
                if (chatOpened)
                {
                    var9 = this.mc.fontRenderer.FONT_HEIGHT;
                    GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
                    int var18 = drawnChatLines * var9 + drawnChatLines;
                    var11 = var4 * var9 + var4;
                    int var19 = this.scrollPos * var11 / drawnChatLines;
                    int var13 = var11 * var11 / var18;

                    if (var18 != var11)
                    {
                    	updatedOpacity = var19 > 0 ? 170 : 96;
                        int var20 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -var19, 2, -var19 - var13, var20 + (updatedOpacity << 24));
                        drawRect(2, -var19, 1, -var19 - var13, 13421772 + (updatedOpacity << 24));
                    }
                }
                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Clears the chat.
     */
    public void clearChatMessages()
    {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent p_146227_1_)
    {
        this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
    }

    /**
     * prints the ChatComponent to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_)
    {
        this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
    }

    private String formatColors(String par1Str)
    {
        return Minecraft.getMinecraft().gameSettings.chatColours ? par1Str : EnumChatFormatting.getTextWithoutFormattingCodes(par1Str);
    }

    private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_)
    {
        if (p_146237_2_ != 0)
        {
            this.deleteChatLine(p_146237_2_);
        }

        int var5 = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
        int var6 = 0;
        ChatComponentText var7 = new ChatComponentText("");
        ArrayList var8 = Lists.newArrayList();
        ArrayList var9 = Lists.newArrayList(p_146237_1_);

        for (int var10 = 0; var10 < var9.size(); ++var10)
        {
            IChatComponent var11 = (IChatComponent)var9.get(var10);
            String var12 = this.formatColors(var11.getChatStyle().getFormattingCode() + var11.getUnformattedTextForChat());
            int var13 = this.mc.fontRenderer.getStringWidth(var12);
            ChatComponentText var14 = new ChatComponentText(var12);
            var14.setChatStyle(var11.getChatStyle().createShallowCopy());
            boolean var15 = false;

            if (var6 + var13 > var5)
            {
                String var16 = this.mc.fontRenderer.trimStringToWidth(var12, var5 - var6, false);
                String var17 = var16.length() < var12.length() ? var12.substring(var16.length()) : null;

                if (var17 != null && var17.length() > 0)
                {
                    int var18 = var16.lastIndexOf(" ");

                    if (var18 >= 0 && this.mc.fontRenderer.getStringWidth(var12.substring(0, var18)) > 0)
                    {
                        var16 = var12.substring(0, var18);
                        var17 = var12.substring(var18);
                    }

                    ChatComponentText var19 = new ChatComponentText(var17);
                    var19.setChatStyle(var11.getChatStyle().createShallowCopy());
                    var9.add(var10 + 1, var19);
                }

                var13 = this.mc.fontRenderer.getStringWidth(var16);
                var14 = new ChatComponentText(var16);
                var14.setChatStyle(var11.getChatStyle().createShallowCopy());
                var15 = true;
            }

            if (var6 + var13 <= var5)
            {
                var6 += var13;
                var7.appendSibling(var14);
            }
            else
            {
                var15 = true;
            }

            if (var15)
            {
                var8.add(var7);
                var6 = 0;
                var7 = new ChatComponentText("");
            }
        }

        var8.add(var7);
        boolean var20 = this.getChatOpen();
        IChatComponent var22;

        for (Iterator var21 = var8.iterator(); var21.hasNext(); this.drawnChatLines.add(0, new ChatLine(p_146237_3_, var22, p_146237_2_)))
        {
            var22 = (IChatComponent)var21.next();

            if (var20 && this.scrollPos > 0)
            {
                this.isScrolled = true;
                this.scroll(1);
            }
        }

        while (this.drawnChatLines.size() > 100)
        {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }

        if (!p_146237_4_)
        {
            this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));

            while (this.chatLines.size() > 100)
            {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void refreshChat()
    {
        this.drawnChatLines.clear();
        this.resetScroll();

        for (int var1 = this.chatLines.size() - 1; var1 >= 0; --var1)
        {
            ChatLine var2 = (ChatLine)this.chatLines.get(var1);
            this.setChatLine(var2.getChatComponent(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
        }
    }

    /**
     * Gets the list of messages previously sent through the chat GUI
     */
    public List getSentMessages()
    {
        return this.sentMessages;
    }

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     */
    public void addToSentMessages(String p_146239_1_)
    {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_))
        {
            this.sentMessages.add(p_146239_1_);
        }
    }

    /**
     * Resets the chat scroll (executed when the GUI is closed, among others)
     */
    public void resetScroll()
    {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    /**
     * Scrolls the chat by the given number of lines.
     */
    public void scroll(int amount)
    {
        this.scrollPos += amount;
        int var2 = this.drawnChatLines.size();

        if (this.scrollPos > var2 - this.getLineCount())
        {
            this.scrollPos = var2 - this.getLineCount();
        }

        if (this.scrollPos <= 0)
        {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_)
    {
        if (!this.getChatOpen())
        {
            return null;
        }
        else
        {
            ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int var4 = var3.getScaleFactor();
            float var5 = this.getChatScale();
            int var6 = p_146236_1_ / var4 - 3;
            int var7 = p_146236_2_ / var4 - 27;
            var6 = MathHelper.floor_float((float)var6 / var5);
            var7 = MathHelper.floor_float((float)var7 / var5);

            if (var6 >= 0 && var7 >= 0)
            {
                int var8 = Math.min(this.getLineCount(), this.drawnChatLines.size());

                if (var6 <= MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale()) && var7 < this.mc.fontRenderer.FONT_HEIGHT * var8 + var8)
                {
                    int var9 = var7 / this.mc.fontRenderer.FONT_HEIGHT + this.scrollPos;

                    if (var9 >= 0 && var9 < this.drawnChatLines.size())
                    {
                        ChatLine var10 = (ChatLine)this.drawnChatLines.get(var9);
                        int var11 = 0;
                        Iterator var12 = var10.getChatComponent().iterator();

                        while (var12.hasNext())
                        {
                            IChatComponent var13 = (IChatComponent)var12.next();

                            if (var13 instanceof ChatComponentText)
                            {
                                var11 += this.mc.fontRenderer.getStringWidth(this.formatColors(((ChatComponentText)var13).getChatComponentText_TextValue()));

                                if (var11 > var6)
                                {
                                    return var13;
                                }
                            }
                        }
                    }

                    return null;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Returns true if the chat GUI is open
     */
    public boolean getChatOpen()
    {
        return this.mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     */
    public void deleteChatLine(int id)
    {
        Iterator var2 = this.drawnChatLines.iterator();
        ChatLine var3;

        while (var2.hasNext())
        {
            var3 = (ChatLine)var2.next();

            if (var3.getChatLineID() == id)
            {
                var2.remove();
            }
        }

        var2 = this.chatLines.iterator();

        while (var2.hasNext())
        {
            var3 = (ChatLine)var2.next();

            if (var3.getChatLineID() == id)
            {
                var2.remove();
                break;
            }
        }
    }

    public int getChatWidth()	// getChatWidth
    {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight() // calculateChatboxWidth
    {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float getChatScale()
    {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float scale)
    {
        short var1 = 320;
        byte var2 = 40;
        return MathHelper.floor_float(scale * (float)(var1 - var2) + (float)var2);
    }

    public static int calculateChatboxHeight(float scale)
    {
        short var1 = 180;
        byte var2 = 20;
        return MathHelper.floor_float(scale * (float)(var1 - var2) + (float)var2);
    }

    public int getLineCount()
    {
        return this.getChatHeight() / 9;
    }
}
