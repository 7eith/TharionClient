package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.synezia.client.components.Size;
import com.synezia.client.components.backgrounds.ColoredBackground;
import com.synezia.client.components.texts.Text;
import com.synezia.client.utilities.Colors;

import java.awt.Color;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tv.twitch.chat.ChatUserInfo;

public class GuiChat extends GuiScreen implements GuiYesNoCallback
{
    private static final Set field_152175_f = Sets.newHashSet(new String[] {"http", "https"});
    private static final Logger logger = LogManager.getLogger();
    private String historyBuffer = "";

    /**
     * keeps position of which chat message you will select when you press up, (does not increase for duplicated
     * messages sent immediately after each other)
     */
    private int sentHistoryCursor = -1;
    private boolean playerNamesFound;
    private boolean waitingOnAutocomplete;
    private int autocompleteIndex;
    private List foundPlayerNames = new ArrayList();

    /** used to pass around the URI to various dialogues and to the host os */
    private URI clickedURI;

    /** Chat entry field */
    protected GuiTextField inputField;

    /**
     * is the text that appears when you press the chat key and the input box appears pre-filled
     */
    private String defaultInputFieldText = "";

    public GuiChat() {}

    public GuiChat(String defaultInput)
    {
        this.defaultInputFieldText = defaultInput;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.inputField.updateCursorCounter();
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char typedChar, int keyCode)
    {
        this.waitingOnAutocomplete = false;

        if (keyCode == 15)
        {
            this.autocompletePlayerNames();
        }
        else
        {
            this.playerNamesFound = false;
        }

        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 200)
            {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208)
            {
                this.getSentHistory(1);
            }
            else if (keyCode == 201)
            {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209)
            {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else
            {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else
        {
            String var3 = this.inputField.getText().trim();

            if (var3.length() > 0)
            {
                this.submitChatMessage(var3);
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    public void submitChatMessage(String str)
    {
        this.mc.ingameGUI.getChatGUI().addToSentMessages(str);
        this.mc.thePlayer.sendChatMessage(str);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0)
        {
            if (var1 > 1)
            {
                var1 = 1;
            }

            if (var1 < -1)
            {
                var1 = -1;
            }

            if (!isShiftKeyDown())
            {
                var1 *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(var1);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        if (p_73864_3_ == 0 && this.mc.gameSettings.chatLinks)
        {
            IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

            if (var4 != null)
            {
                ClickEvent var5 = var4.getChatStyle().getChatClickEvent();

                if (var5 != null)
                {
                    if (isShiftKeyDown())
                    {
                        this.inputField.writeText(var4.getUnformattedTextForChat());
                    }
                    else
                    {
                        URI var6;

                        if (var5.getAction() == ClickEvent.Action.OPEN_URL)
                        {
                            try
                            {
                                var6 = new URI(var5.getValue());

                                if (!field_152175_f.contains(var6.getScheme().toLowerCase()))
                                {
                                    throw new URISyntaxException(var5.getValue(), "Unsupported protocol: " + var6.getScheme().toLowerCase());
                                }

                                if (this.mc.gameSettings.chatLinksPrompt)
                                {
                                    this.clickedURI = var6;
                                    this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, var5.getValue(), 0, false));
                                }
                                else
                                {
                                    this.func_146407_a(var6);
                                }
                            }
                            catch (URISyntaxException var7)
                            {
                                logger.error("Can\'t open url for " + var5, var7);
                            }
                        }
                        else if (var5.getAction() == ClickEvent.Action.OPEN_FILE)
                        {
                            var6 = (new File(var5.getValue())).toURI();
                            this.func_146407_a(var6);
                        }
                        else if (var5.getAction() == ClickEvent.Action.SUGGEST_COMMAND)
                        {
                            this.inputField.setText(var5.getValue());
                        }
                        else if (var5.getAction() == ClickEvent.Action.RUN_COMMAND)
                        {
                            this.submitChatMessage(var5.getValue());
                        }
                        else if (var5.getAction() == ClickEvent.Action.TWITCH_USER_INFO)
                        {
                            ChatUserInfo var8 = this.mc.func_152346_Z().func_152926_a(var5.getValue());

                            if (var8 != null)
                            {
                                this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.func_152346_Z(), var8));
                            }
                            else
                            {
                                logger.error("Tried to handle twitch user but couldn\'t find them!");
                            }
                        }
                        else
                        {
                            logger.error("Don\'t know how to handle " + var5);
                        }
                    }

                    return;
                }
            }
        }

        this.inputField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    public void confirmClicked(boolean p_73878_1_, int p_73878_2_)
    {
        if (p_73878_2_ == 0)
        {
            if (p_73878_1_)
            {
                this.func_146407_a(this.clickedURI);
            }

            this.clickedURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void func_146407_a(URI p_146407_1_)
    {
        try
        {
            Class var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            var2.getMethod("browse", new Class[] {URI.class}).invoke(var3, new Object[] {p_146407_1_});
        }
        catch (Throwable var4)
        {
            logger.error("Couldn\'t open link", var4);
        }
    }

    public void autocompletePlayerNames()
    {
        String currentText;

		if (this.playerNamesFound)
        {
            this.inputField.deleteFromCursor(this.inputField.getNthWordFromPosWS(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

            if (this.autocompleteIndex >= this.foundPlayerNames.size())
            {
                this.autocompleteIndex = 0;
            }
        }
        else
        {
            int numberOfWordsByPosition = this.inputField.getNthWordFromPosWS(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            String textField = this.inputField.getText().substring(numberOfWordsByPosition).toLowerCase();
            currentText = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.sendAutocompleteRequest(currentText, textField);

            if (this.foundPlayerNames.isEmpty())
            {
                return;
            }

            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(numberOfWordsByPosition - this.inputField.getCursorPosition());
        }

        if (this.foundPlayerNames.size() > 1)
        {
            StringBuilder foundPlayerText = new StringBuilder();

            for (Iterator iterator = this.foundPlayerNames.iterator(); iterator.hasNext(); foundPlayerText.append(currentText))
            {
            	currentText = (String)iterator.next();

                if (foundPlayerText.length() > 0)
                {
                	foundPlayerText.append(", ");
                }
            }
            System.out.println(foundPlayerText.toString());
            new ColoredBackground(0, 0).withSize(new Size(300, 20)).withColor(Colors.LIGHT_MIDNIGHT).withTransparency(0.5F).draw();
//            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(foundPlayerText.toString()), 1);
        }
        
        this.inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
    }

    private void sendAutocompleteRequest(String str, String textField)
    {
        if (str.length() >= 1)
        {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(str));
            this.waitingOnAutocomplete = true;
        }
    }

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int p_146402_1_)
    {
        int var2 = this.sentHistoryCursor + p_146402_1_;
        int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();

        if (var2 < 0)
        {
            var2 = 0;
        }

        if (var2 > var3)
        {
            var2 = var3;
        }

        if (var2 != this.sentHistoryCursor)
        {
            if (var2 == var3)
            {
                this.sentHistoryCursor = var3;
                this.inputField.setText(this.historyBuffer);
            }
            else
            {
                if (this.sentHistoryCursor == var3)
                {
                    this.historyBuffer = this.inputField.getText();
                }

                this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
                this.sentHistoryCursor = var2;
            }
        }
    }
    
    private void completeNewChatComponent()
    {
    	Integer size = this.foundPlayerNames.size();
    	Integer posY = 0;
    	
    	new ColoredBackground(0, this.getHeight() - 20 - this.mc.fontRenderer.FONT_HEIGHT * size).withSize(new Size(90, this.mc.fontRenderer.FONT_HEIGHT * size)).withColor(Colors.GRAY).withTransparency(0.9F).draw();
    	for (Iterator iterator = this.foundPlayerNames.iterator(); iterator.hasNext(); posY += this.mc.fontRenderer.FONT_HEIGHT)
    	{
    		new Text(iterator.next().toString(), 0, this.getHeight() - 73 - this.mc.fontRenderer.FONT_HEIGHT + posY).draw();
    	}
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
    	if (!this.foundPlayerNames.isEmpty())
    		this.completeNewChatComponent(); 
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE); // where taping text
        this.inputField.drawTextBox();
        IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (var4 != null && var4.getChatStyle().getChatHoverEvent() != null)
        {
            HoverEvent var5 = var4.getChatStyle().getChatHoverEvent();

            if (var5.getAction() == HoverEvent.Action.SHOW_ITEM)
            {
                ItemStack var6 = null;

                try
                {
                    NBTBase var7 = JsonToNBT.func_150315_a(var5.getValue().getUnformattedText());

                    if (var7 != null && var7 instanceof NBTTagCompound)
                    {
                        var6 = ItemStack.loadItemStackFromNBT((NBTTagCompound)var7);
                    }
                }
                catch (NBTException var11)
                {
                    ;
                }

                if (var6 != null)
                {
                    this.renderToolTip(var6, p_73863_1_, p_73863_2_);
                }
                else
                {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", p_73863_1_, p_73863_2_);
                }
            }
            else if (var5.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                this.func_146283_a(Splitter.on("\n").splitToList(var5.getValue().getFormattedText()), p_73863_1_, p_73863_2_);
            }
            else if (var5.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT)
            {
                StatBase var12 = StatList.func_151177_a(var5.getValue().getUnformattedText());

                if (var12 != null)
                {
                    IChatComponent var13 = var12.func_150951_e();
                    ChatComponentTranslation var8 = new ChatComponentTranslation("stats.tooltip.type." + (var12.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                    var8.getChatStyle().setItalic(Boolean.valueOf(true));
                    String var9 = var12 instanceof Achievement ? ((Achievement)var12).getDescription() : null;
                    ArrayList var10 = Lists.newArrayList(new String[] {var13.getFormattedText(), var8.getFormattedText()});

                    if (var9 != null)
                    {
                        var10.addAll(this.fontRendererObj.listFormattedStringToWidth(var9, 150));
                    }

                    this.func_146283_a(var10, p_73863_1_, p_73863_2_);
                }
                else
                {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", p_73863_1_, p_73863_2_);
                }
            }

            GL11.glDisable(GL11.GL_LIGHTING);
        }

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public void func_146406_a(String[] p_146406_1_)
    {
        if (this.waitingOnAutocomplete)
        {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();
            String[] var2 = p_146406_1_;
            int var3 = p_146406_1_.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                String var5 = var2[var4];

                if (var5.length() > 0)
                {
                    this.foundPlayerNames.add(var5);
                }
            }

            String var6 = this.inputField.getText().substring(this.inputField.getNthWordFromPosWS(-1, this.inputField.getCursorPosition(), false));
            String var7 = StringUtils.getCommonPrefix(p_146406_1_);

            if (var7.length() > 0 && !var6.equalsIgnoreCase(var7))
            {
                this.inputField.deleteFromCursor(this.inputField.getNthWordFromPosWS(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(var7);
            }
            else if (this.foundPlayerNames.size() > 0)
            {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
