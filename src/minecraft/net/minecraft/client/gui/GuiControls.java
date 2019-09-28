package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GuiControls extends GuiScreen
{
    private static final GameSettings.Options[] field_146492_g = new GameSettings.Options[] {GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN};

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private GuiScreen parentScreen;
    protected String field_146495_a = "Controls";

    /** Reference to the GameSettings object. */
    private GameSettings options;

    /** The ID of the button that has been pressed. */
    public KeyBinding buttonId = null;
    public long field_152177_g;
    private GuiKeyBindingList keyBindingList;
    private GuiButton field_146493_s;
    private static final String __OBFID = "CL_00000736";

    public GuiControls(GuiScreen p_i1027_1_, GameSettings p_i1027_2_)
    {
        this.parentScreen = p_i1027_1_;
        this.options = p_i1027_2_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.keyBindingList = new GuiKeyBindingList(this, this.mc);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.field_146493_s = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0])));
        this.field_146495_a = I18n.format("controls.title", new Object[0]);
        int var1 = 0;
        GameSettings.Options[] var2 = field_146492_g;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            GameSettings.Options var5 = var2[var4];

            if (var5.getEnumFloat())
            {
                this.buttonList.add(new GuiOptionSlider(var5.returnEnumOrdinal(), this.width / 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1), var5));
            }
            else
            {
                this.buttonList.add(new GuiOptionButton(var5.returnEnumOrdinal(), this.width / 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1), var5, this.options.getKeyBinding(var5)));
            }

            ++var1;
        }
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.id == 200)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (p_146284_1_.id == 201)
        {
            KeyBinding[] var2 = this.mc.gameSettings.keyBindings;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                KeyBinding var5 = var2[var4];
                var5.setKeyCode(var5.getKeyCodeDefault());
            }

            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (p_146284_1_.id < 100 && p_146284_1_ instanceof GuiOptionButton)
        {
            this.options.setOptionValue(((GuiOptionButton)p_146284_1_).returnEnumOptions(), 1);
            p_146284_1_.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(p_146284_1_.id));
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        if (this.buttonId != null)
        {
            this.options.setOptionKeyBinding(this.buttonId, -100 + p_73864_3_);
            this.buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (p_73864_3_ != 0 || !this.keyBindingList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_))
        {
            super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        if (p_146286_3_ != 0 || !this.keyBindingList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_))
        {
            super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (this.buttonId != null)
        {
            if (p_73869_2_ == 1)
            {
                this.options.setOptionKeyBinding(this.buttonId, 0);
            }
            else
            {
                this.options.setOptionKeyBinding(this.buttonId, p_73869_2_);
            }

            this.buttonId = null;
            this.field_152177_g = Minecraft.getSystemTime();
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else
        {
            super.keyTyped(p_73869_1_, p_73869_2_);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.keyBindingList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, this.field_146495_a, this.width / 2, 8, 16777215);
        boolean var4 = true;
        KeyBinding[] var5 = this.options.keyBindings;
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            KeyBinding var8 = var5[var7];

            if (var8.getKeyCode() != var8.getKeyCodeDefault())
            {
                var4 = false;
                break;
            }
        }

        this.field_146493_s.enabled = !var4;
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
