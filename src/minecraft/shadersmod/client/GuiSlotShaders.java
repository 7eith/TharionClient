package shadersmod.client;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.Lang;

class GuiSlotShaders extends GuiSlot
{
    public ArrayList shaderslist;
    public int selectedIndex;
    public long lastClickedCached = 0L;
    public final GuiShaders shadersGui;

    public GuiSlotShaders(GuiShaders par1GuiShaders, int width, int height, int top, int bottom, int slotHeight)
    {
        super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
        this.shadersGui = par1GuiShaders;
        this.updateList();
        int posYSelected = this.selectedIndex * slotHeight;
        int wMid = (bottom - top) / 2;

        if (posYSelected > wMid)
        {
            this.scrollBy(posYSelected - wMid);
        }
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return this.width - 20;
    }

    public void updateList()
    {
        this.shaderslist = Shaders.listOfShaders();
        this.selectedIndex = 0;
        int i = 0;

        for (int n = this.shaderslist.size(); i < n; ++i)
        {
            if (((String)this.shaderslist.get(i)).equals(Shaders.currentshadername))
            {
                this.selectedIndex = i;
                break;
            }
        }
    }

    public int getSize()
    {
        return this.shaderslist.size();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    public void elementClicked(int index, boolean doubleClicked, int mouseX, int mouseY)
    {
        if (index != this.selectedIndex || this.lastClickedCached != System.currentTimeMillis())
        {
            this.selectedIndex = index;
            this.lastClickedCached = System.currentTimeMillis();
            Shaders.setShaderPack((String)this.shaderslist.get(index));
            Shaders.uninit();
            this.shadersGui.updateButtons();
        }
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    public boolean isSelected(int index)
    {
        return index == this.selectedIndex;
    }

    public int getScrollBarX()
    {
        return this.width - 6;
    }

    /**
     * Return the height of the content being scrolled
     */
    public int getContentHeight()
    {
        return this.getSize() * 18;
    }

    public void drawBackground() {}

    public void drawSlot(int index, int posX, int posY, int contentY, Tessellator tess, int mouseX, int mouseY)
    {
        String label = (String)this.shaderslist.get(index);

        if (label.equals(Shaders.packNameNone))
        {
            label = Lang.get("of.options.shaders.packNone");
        }
        else if (label.equals(Shaders.packNameDefault))
        {
            label = Lang.get("of.options.shaders.packDefault");
        }

        this.shadersGui.drawCenteredString(label, this.width / 2, posY + 1, 16777215);
    }

    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }
}
