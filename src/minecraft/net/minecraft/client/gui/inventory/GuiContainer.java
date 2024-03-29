package net.minecraft.client.gui.inventory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainer extends GuiScreen
{
    protected static final ResourceLocation field_147001_a = new ResourceLocation("textures/gui/container/inventory.png");

    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;

    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    /** A list of the players inventory slots */
    public Container inventorySlots;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;
    private Slot theSlot;

    /** Used when touchscreen is enabled. */
    private Slot clickedSlot;

    /** Used when touchscreen is enabled. */
    private boolean isRightMouseClick;

    /** Used when touchscreen is enabled */
    private ItemStack draggedStack;
    private int field_147011_y;
    private int field_147010_z;
    private Slot returningStackDestSlot;
    private long returningStackTime;

    /** Used when touchscreen is enabled */
    private ItemStack returningStack;
    private Slot field_146985_D;
    private long field_146986_E;
    protected final Set field_147008_s = new HashSet();
    protected boolean field_147007_t;
    private int field_146987_F;
    private int field_146988_G;
    private boolean field_146995_H;
    private int field_146996_I;
    private long field_146997_J;
    private Slot field_146998_K;
    private int field_146992_L;
    private boolean field_146993_M;
    private ItemStack field_146994_N;
    private static final String __OBFID = "CL_00000737";

    public GuiContainer(Container p_i1072_1_)
    {
        this.inventorySlots = p_i1072_1_;
        this.field_146995_H = true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        int var4 = this.guiLeft;
        int var5 = this.guiTop;
        this.drawGuiContainerBackgroundLayer(p_73863_3_, p_73863_1_, p_73863_2_);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var4, (float)var5, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        this.theSlot = null;
        short var6 = 240;
        short var7 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var11;

        for (int var8 = 0; var8 < this.inventorySlots.inventorySlots.size(); ++var8)
        {
            Slot var9 = (Slot)this.inventorySlots.inventorySlots.get(var8);
            this.func_146977_a(var9);

            if (this.isMouseOverSlot(var9, p_73863_1_, p_73863_2_) && var9.func_111238_b())
            {
                this.theSlot = var9;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int var10 = var9.xDisplayPosition;
                var11 = var9.yDisplayPosition;
                GL11.glColorMask(true, true, true, false);
                this.drawGradientRect(var10, var11, var10 + 16, var11 + 16, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        this.drawGuiContainerForegroundLayer(p_73863_1_, p_73863_2_);
        InventoryPlayer var15 = this.mc.thePlayer.inventory;
        ItemStack var16 = this.draggedStack == null ? var15.getItemStack() : this.draggedStack;

        if (var16 != null)
        {
            byte var17 = 8;
            var11 = this.draggedStack == null ? 8 : 16;
            String var12 = null;

            if (this.draggedStack != null && this.isRightMouseClick)
            {
                var16 = var16.copy();
                var16.stackSize = MathHelper.ceiling_float_int((float)var16.stackSize / 2.0F);
            }
            else if (this.field_147007_t && this.field_147008_s.size() > 1)
            {
                var16 = var16.copy();
                var16.stackSize = this.field_146996_I;

                if (var16.stackSize == 0)
                {
                    var12 = "" + EnumChatFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(var16, p_73863_1_ - var4 - var17, p_73863_2_ - var5 - var11, var12);
        }

        if (this.returningStack != null)
        {
            float var18 = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (var18 >= 1.0F)
            {
                var18 = 1.0F;
                this.returningStack = null;
            }

            var11 = this.returningStackDestSlot.xDisplayPosition - this.field_147011_y;
            int var20 = this.returningStackDestSlot.yDisplayPosition - this.field_147010_z;
            int var13 = this.field_147011_y + (int)((float)var11 * var18);
            int var14 = this.field_147010_z + (int)((float)var20 * var18);
            this.drawItemStack(this.returningStack, var13, var14, (String)null);
        }

        GL11.glPopMatrix();

        if (var15.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack())
        {
            ItemStack var19 = this.theSlot.getStack();
            this.renderToolTip(var19, p_73863_1_, p_73863_2_);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack p_146982_1_, int p_146982_2_, int p_146982_3_, String p_146982_4_)
    {
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_);
        itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_ - (this.draggedStack == null ? 0 : 8), p_146982_4_);
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {}

    protected abstract void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_);

    private void func_146977_a(Slot p_146977_1_)
    {
        int var2 = p_146977_1_.xDisplayPosition;
        int var3 = p_146977_1_.yDisplayPosition;
        ItemStack var4 = p_146977_1_.getStack();
        boolean var5 = false;
        boolean var6 = p_146977_1_ == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
        ItemStack var7 = this.mc.thePlayer.inventory.getItemStack();
        String var8 = null;

        if (p_146977_1_ == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && var4 != null)
        {
            var4 = var4.copy();
            var4.stackSize /= 2;
        }
        else if (this.field_147007_t && this.field_147008_s.contains(p_146977_1_) && var7 != null)
        {
            if (this.field_147008_s.size() == 1)
            {
                return;
            }

            if (Container.func_94527_a(p_146977_1_, var7, true) && this.inventorySlots.canDragIntoSlot(p_146977_1_))
            {
                var4 = var7.copy();
                var5 = true;
                Container.func_94525_a(this.field_147008_s, this.field_146987_F, var4, p_146977_1_.getStack() == null ? 0 : p_146977_1_.getStack().stackSize);

                if (var4.stackSize > var4.getMaxStackSize())
                {
                    var8 = EnumChatFormatting.YELLOW + "" + var4.getMaxStackSize();
                    var4.stackSize = var4.getMaxStackSize();
                }

                if (var4.stackSize > p_146977_1_.getSlotStackLimit())
                {
                    var8 = EnumChatFormatting.YELLOW + "" + p_146977_1_.getSlotStackLimit();
                    var4.stackSize = p_146977_1_.getSlotStackLimit();
                }
            }
            else
            {
                this.field_147008_s.remove(p_146977_1_);
                this.func_146980_g();
            }
        }

        this.zLevel = 100.0F;
        itemRender.zLevel = 100.0F;

        if (var4 == null)
        {
            IIcon var9 = p_146977_1_.getBackgroundIconIndex();

            if (var9 != null)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
                this.drawTexturedModelRectFromIcon(var2, var3, var9, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                var6 = true;
            }
        }

        if (!var6)
        {
            if (var5)
            {
                drawRect(var2, var3, var2 + 16, var3 + 16, -2130706433);
            }

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var4, var2, var3);
            itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var4, var2, var3, var8);
        }

        itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    private void func_146980_g()
    {
        ItemStack var1 = this.mc.thePlayer.inventory.getItemStack();

        if (var1 != null && this.field_147007_t)
        {
            this.field_146996_I = var1.stackSize;
            ItemStack var4;
            int var5;

            for (Iterator var2 = this.field_147008_s.iterator(); var2.hasNext(); this.field_146996_I -= var4.stackSize - var5)
            {
                Slot var3 = (Slot)var2.next();
                var4 = var1.copy();
                var5 = var3.getStack() == null ? 0 : var3.getStack().stackSize;
                Container.func_94525_a(this.field_147008_s, this.field_146987_F, var4, var5);

                if (var4.stackSize > var4.getMaxStackSize())
                {
                    var4.stackSize = var4.getMaxStackSize();
                }

                if (var4.stackSize > var3.getSlotStackLimit())
                {
                    var4.stackSize = var3.getSlotStackLimit();
                }
            }
        }
    }

    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    private Slot getSlotAtPosition(int p_146975_1_, int p_146975_2_)
    {
        for (int var3 = 0; var3 < this.inventorySlots.inventorySlots.size(); ++var3)
        {
            Slot var4 = (Slot)this.inventorySlots.inventorySlots.get(var3);

            if (this.isMouseOverSlot(var4, p_146975_1_, p_146975_2_))
            {
                return var4;
            }
        }

        return null;
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        boolean var4 = p_73864_3_ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
        Slot var5 = this.getSlotAtPosition(p_73864_1_, p_73864_2_);
        long var6 = Minecraft.getSystemTime();
        this.field_146993_M = this.field_146998_K == var5 && var6 - this.field_146997_J < 250L && this.field_146992_L == p_73864_3_;
        this.field_146995_H = false;

        if (p_73864_3_ == 0 || p_73864_3_ == 1 || var4)
        {
            int var8 = this.guiLeft;
            int var9 = this.guiTop;
            boolean var10 = p_73864_1_ < var8 || p_73864_2_ < var9 || p_73864_1_ >= var8 + this.xSize || p_73864_2_ >= var9 + this.ySize;
            int var11 = -1;

            if (var5 != null)
            {
                var11 = var5.slotNumber;
            }

            if (var10)
            {
                var11 = -999;
            }

            if (this.mc.gameSettings.touchscreen && var10 && this.mc.thePlayer.inventory.getItemStack() == null)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
                return;
            }

            if (var11 != -1)
            {
                if (this.mc.gameSettings.touchscreen)
                {
                    if (var5 != null && var5.getHasStack())
                    {
                        this.clickedSlot = var5;
                        this.draggedStack = null;
                        this.isRightMouseClick = p_73864_3_ == 1;
                    }
                    else
                    {
                        this.clickedSlot = null;
                    }
                }
                else if (!this.field_147007_t)
                {
                    if (this.mc.thePlayer.inventory.getItemStack() == null)
                    {
                        if (p_73864_3_ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
                        {
                            this.handleMouseClick(var5, var11, p_73864_3_, 3);
                        }
                        else
                        {
                            boolean var12 = var11 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            byte var13 = 0;

                            if (var12)
                            {
                                this.field_146994_N = var5 != null && var5.getHasStack() ? var5.getStack() : null;
                                var13 = 1;
                            }
                            else if (var11 == -999)
                            {
                                var13 = 4;
                            }

                            this.handleMouseClick(var5, var11, p_73864_3_, var13);
                        }

                        this.field_146995_H = true;
                    }
                    else
                    {
                        this.field_147007_t = true;
                        this.field_146988_G = p_73864_3_;
                        this.field_147008_s.clear();

                        if (p_73864_3_ == 0)
                        {
                            this.field_146987_F = 0;
                        }
                        else if (p_73864_3_ == 1)
                        {
                            this.field_146987_F = 1;
                        }
                    }
                }
            }
        }

        this.field_146998_K = var5;
        this.field_146997_J = var6;
        this.field_146992_L = p_73864_3_;
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_)
    {
        Slot var6 = this.getSlotAtPosition(p_146273_1_, p_146273_2_);
        ItemStack var7 = this.mc.thePlayer.inventory.getItemStack();

        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen)
        {
            if (p_146273_3_ == 0 || p_146273_3_ == 1)
            {
                if (this.draggedStack == null)
                {
                    if (var6 != this.clickedSlot)
                    {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                }
                else if (this.draggedStack.stackSize > 1 && var6 != null && Container.func_94527_a(var6, this.draggedStack, false))
                {
                    long var8 = Minecraft.getSystemTime();

                    if (this.field_146985_D == var6)
                    {
                        if (var8 - this.field_146986_E > 500L)
                        {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            this.handleMouseClick(var6, var6.slotNumber, 1, 0);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            this.field_146986_E = var8 + 750L;
                            --this.draggedStack.stackSize;
                        }
                    }
                    else
                    {
                        this.field_146985_D = var6;
                        this.field_146986_E = var8;
                    }
                }
            }
        }
        else if (this.field_147007_t && var6 != null && var7 != null && var7.stackSize > this.field_147008_s.size() && Container.func_94527_a(var6, var7, true) && var6.isItemValid(var7) && this.inventorySlots.canDragIntoSlot(var6))
        {
            this.field_147008_s.add(var6);
            this.func_146980_g();
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        Slot var4 = this.getSlotAtPosition(p_146286_1_, p_146286_2_);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        boolean var7 = p_146286_1_ < var5 || p_146286_2_ < var6 || p_146286_1_ >= var5 + this.xSize || p_146286_2_ >= var6 + this.ySize;
        int var8 = -1;

        if (var4 != null)
        {
            var8 = var4.slotNumber;
        }

        if (var7)
        {
            var8 = -999;
        }

        Slot var10;
        Iterator var11;

        if (this.field_146993_M && var4 != null && p_146286_3_ == 0 && this.inventorySlots.func_94530_a((ItemStack)null, var4))
        {
            if (isShiftKeyDown())
            {
                if (var4 != null && var4.inventory != null && this.field_146994_N != null)
                {
                    var11 = this.inventorySlots.inventorySlots.iterator();

                    while (var11.hasNext())
                    {
                        var10 = (Slot)var11.next();

                        if (var10 != null && var10.canTakeStack(this.mc.thePlayer) && var10.getHasStack() && var10.inventory == var4.inventory && Container.func_94527_a(var10, this.field_146994_N, true))
                        {
                            this.handleMouseClick(var10, var10.slotNumber, p_146286_3_, 1);
                        }
                    }
                }
            }
            else
            {
                this.handleMouseClick(var4, var8, p_146286_3_, 6);
            }

            this.field_146993_M = false;
            this.field_146997_J = 0L;
        }
        else
        {
            if (this.field_147007_t && this.field_146988_G != p_146286_3_)
            {
                this.field_147007_t = false;
                this.field_147008_s.clear();
                this.field_146995_H = true;
                return;
            }

            if (this.field_146995_H)
            {
                this.field_146995_H = false;
                return;
            }

            boolean var9;

            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen)
            {
                if (p_146286_3_ == 0 || p_146286_3_ == 1)
                {
                    if (this.draggedStack == null && var4 != this.clickedSlot)
                    {
                        this.draggedStack = this.clickedSlot.getStack();
                    }

                    var9 = Container.func_94527_a(var4, this.draggedStack, false);

                    if (var8 != -1 && this.draggedStack != null && var9)
                    {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, p_146286_3_, 0);
                        this.handleMouseClick(var4, var8, 0, 0);

                        if (this.mc.thePlayer.inventory.getItemStack() != null)
                        {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, p_146286_3_, 0);
                            this.field_147011_y = p_146286_1_ - var5;
                            this.field_147010_z = p_146286_2_ - var6;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        }
                        else
                        {
                            this.returningStack = null;
                        }
                    }
                    else if (this.draggedStack != null)
                    {
                        this.field_147011_y = p_146286_1_ - var5;
                        this.field_147010_z = p_146286_2_ - var6;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }

                    this.draggedStack = null;
                    this.clickedSlot = null;
                }
            }
            else if (this.field_147007_t && !this.field_147008_s.isEmpty())
            {
                this.handleMouseClick((Slot)null, -999, Container.func_94534_d(0, this.field_146987_F), 5);
                var11 = this.field_147008_s.iterator();

                while (var11.hasNext())
                {
                    var10 = (Slot)var11.next();
                    this.handleMouseClick(var10, var10.slotNumber, Container.func_94534_d(1, this.field_146987_F), 5);
                }

                this.handleMouseClick((Slot)null, -999, Container.func_94534_d(2, this.field_146987_F), 5);
            }
            else if (this.mc.thePlayer.inventory.getItemStack() != null)
            {
                if (p_146286_3_ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
                {
                    this.handleMouseClick(var4, var8, p_146286_3_, 3);
                }
                else
                {
                    var9 = var8 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

                    if (var9)
                    {
                        this.field_146994_N = var4 != null && var4.getHasStack() ? var4.getStack() : null;
                    }

                    this.handleMouseClick(var4, var8, p_146286_3_, var9 ? 1 : 0);
                }
            }
        }

        if (this.mc.thePlayer.inventory.getItemStack() == null)
        {
            this.field_146997_J = 0L;
        }

        this.field_147007_t = false;
    }

    /**
     * Returns if the passed mouse position is over the specified slot.
     */
    private boolean isMouseOverSlot(Slot p_146981_1_, int p_146981_2_, int p_146981_3_)
    {
        return this.func_146978_c(p_146981_1_.xDisplayPosition, p_146981_1_.yDisplayPosition, 16, 16, p_146981_2_, p_146981_3_);
    }

    protected boolean func_146978_c(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_, int p_146978_5_, int p_146978_6_)
    {
        int var7 = this.guiLeft;
        int var8 = this.guiTop;
        p_146978_5_ -= var7;
        p_146978_6_ -= var8;
        return p_146978_5_ >= p_146978_1_ - 1 && p_146978_5_ < p_146978_1_ + p_146978_3_ + 1 && p_146978_6_ >= p_146978_2_ - 1 && p_146978_6_ < p_146978_2_ + p_146978_4_ + 1;
    }

    protected void handleMouseClick(Slot p_146984_1_, int p_146984_2_, int p_146984_3_, int p_146984_4_)
    {
        if (p_146984_1_ != null)
        {
            p_146984_2_ = p_146984_1_.slotNumber;
        }

        this.mc.playerController.windowClick(this.inventorySlots.windowId, p_146984_2_, p_146984_3_, p_146984_4_, this.mc.thePlayer);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            this.mc.thePlayer.closeScreen();
        }

        this.checkHotbarKeys(keyCode);

        if (this.theSlot != null && this.theSlot.getHasStack())
        {
            if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode())
            {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
            }
            else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode())
            {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
    }

    /**
     * This function is what controls the hotbar shortcut check when you press a number key when hovering a stack.
     */
    protected boolean checkHotbarKeys(int p_146983_1_)
    {
        if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null)
        {
            for (int var2 = 0; var2 < 9; ++var2)
            {
                if (p_146983_1_ == this.mc.gameSettings.keyBindsHotbar[var2].getKeyCode())
                {
                    this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, var2, 2);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        if (this.mc.thePlayer != null)
        {
            this.inventorySlots.onContainerClosed(this.mc.thePlayer);
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
        {
            this.mc.thePlayer.closeScreen();
        }
    }
}
