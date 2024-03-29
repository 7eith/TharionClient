package net.minecraft.shader.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.optifine.Config;
import net.minecraft.optifine.GuiScreenOF;
import net.minecraft.optifine.Lang;
import net.minecraft.optifine.StrUtils;

public class GuiShaderOptions extends GuiScreenOF
{
    public GuiScreen prevScreen;
    public String title;
    public GameSettings settings;
    public int lastMouseX;
    public int lastMouseY;
    public long mouseStillTime;
    public String screenName;
    public String screenText;
    public boolean changed;
    public static final String OPTION_PROFILE = "<profile>";
    public static final String OPTION_EMPTY = "<empty>";
    public static final String OPTION_REST = "*";

    public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings)
    {
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.screenName = null;
        this.screenText = null;
        this.changed = false;
        this.title = "Shader Options";
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }

    public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings, String screenName)
    {
        this(guiscreen, gamesettings);
        this.screenName = screenName;

        if (screenName != null)
        {
            this.screenText = Shaders.translate("screen." + screenName, screenName);
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.title = I18n.format("of.options.shaderOptionsTitle", new Object[0]);
        byte baseId = 100;
        boolean baseX = false;
        byte baseY = 30;
        byte stepY = 20;
        int btnX = this.width - 130;
        byte btnWidth = 120;
        byte btnHeight = 20;
        int columns = 2;
        ShaderOption[] ops = Shaders.getShaderPackOptions(this.screenName);

        if (ops != null)
        {
            if (ops.length > 18)
            {
                columns = ops.length / 9 + 1;
            }

            for (int i = 0; i < ops.length; ++i)
            {
                ShaderOption so = ops[i];

                if (so != null && so.isVisible())
                {
                    int col = i % columns;
                    int row = i / columns;
                    int colWidth = Math.min(this.width / columns, 200);
                    int var21 = (this.width - colWidth * columns) / 2;
                    int x = col * colWidth + 5 + var21;
                    int y = baseY + row * stepY;
                    int w = colWidth - 10;
                    String text = this.getButtonText(so, w);
                    GuiButtonShaderOption btn = new GuiButtonShaderOption(baseId + i, x, y, w, btnHeight, so, text);
                    btn.enabled = so.isEnabled();
                    this.buttonList.add(btn);
                }
            }
        }

        this.buttonList.add(new GuiButton(201, this.width / 2 - btnWidth - 20, this.height / 6 + 168 + 11, btnWidth, btnHeight, I18n.format("controls.reset", new Object[0])));
        this.buttonList.add(new GuiButton(200, this.width / 2 + 20, this.height / 6 + 168 + 11, btnWidth, btnHeight, I18n.format("gui.done", new Object[0])));
    }

    public String getButtonText(ShaderOption so, int btnWidth)
    {
        String labelName = so.getNameText();

        if (so instanceof ShaderOptionScreen)
        {
            ShaderOptionScreen fr1 = (ShaderOptionScreen)so;
            return labelName + "...";
        }
        else
        {
            FontRenderer fr = this.fontRendererObj;

            for (int lenSuffix = fr.getStringWidth(": " + Lang.getOff()) + 5; fr.getStringWidth(labelName) + lenSuffix >= btnWidth && labelName.length() > 0; labelName = labelName.substring(0, labelName.length() - 1))
            {
                ;
            }

            String col = so.isChanged() ? so.getValueColor(so.getValue()) : "";
            String labelValue = so.getValueText(so.getValue());
            return labelName + ": " + col + labelValue;
        }
    }

    public void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.enabled)
        {
            if (guibutton.id < 200 && guibutton instanceof GuiButtonShaderOption)
            {
                GuiButtonShaderOption opts = (GuiButtonShaderOption)guibutton;
                ShaderOption i = opts.getShaderOption();

                if (i instanceof ShaderOptionScreen)
                {
                    String var8 = i.getName();
                    GuiShaderOptions scr = new GuiShaderOptions(this, this.settings, var8);
                    this.mc.displayGuiScreen(scr);
                    return;
                }

                if (isShiftKeyDown())
                {
                    i.resetValue();
                }
                else
                {
                    i.nextValue();
                }

                this.updateAllButtons();
                this.changed = true;
            }

            if (guibutton.id == 201)
            {
                ShaderOption[] var6 = Shaders.getChangedOptions(Shaders.getShaderPackOptions());

                for (int var7 = 0; var7 < var6.length; ++var7)
                {
                    ShaderOption opt = var6[var7];
                    opt.resetValue();
                    this.changed = true;
                }

                this.updateAllButtons();
            }

            if (guibutton.id == 200)
            {
                if (this.changed)
                {
                    Shaders.saveShaderPackOptions();
                    this.changed = false;
                    Shaders.uninit();
                }

                this.mc.displayGuiScreen(this.prevScreen);
            }
        }
    }

    public void actionPerformedRightClick(GuiButton btn)
    {
        if (btn instanceof GuiButtonShaderOption)
        {
            GuiButtonShaderOption btnSo = (GuiButtonShaderOption)btn;
            ShaderOption so = btnSo.getShaderOption();

            if (isShiftKeyDown())
            {
                so.resetValue();
            }
            else
            {
                so.prevValue();
            }

            this.updateAllButtons();
            this.changed = true;
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (this.changed)
        {
            Shaders.saveShaderPackOptions();
            this.changed = false;
            Shaders.uninit();
        }
    }

    public void updateAllButtons()
    {
        Iterator it = this.buttonList.iterator();

        while (it.hasNext())
        {
            GuiButton btn = (GuiButton)it.next();

            if (btn instanceof GuiButtonShaderOption)
            {
                GuiButtonShaderOption gbso = (GuiButtonShaderOption)btn;
                ShaderOption opt = gbso.getShaderOption();

                if (opt instanceof ShaderOptionProfile)
                {
                    ShaderOptionProfile optProf = (ShaderOptionProfile)opt;
                    optProf.updateProfile();
                }

                gbso.displayString = this.getButtonText(opt, gbso.getButtonWidth());
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y, float f)
    {
        this.drawDefaultBackground();

        if (this.screenText != null)
        {
            this.drawCenteredString(this.fontRendererObj, this.screenText, this.width / 2, 15, 16777215);
        }
        else
        {
            this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
        }

        super.drawScreen(x, y, f);

        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5)
        {
            this.drawTooltips(x, y, this.buttonList);
        }
        else
        {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    public void drawTooltips(int x, int y, List buttons)
    {
        short activateDelay = 700;

        if (System.currentTimeMillis() >= this.mouseStillTime + (long)activateDelay)
        {
            int x1 = this.width / 2 - 150;
            int y1 = this.height / 6 - 7;

            if (y <= y1 + 98)
            {
                y1 += 105;
            }

            int x2 = x1 + 150 + 150;
            int y2 = y1 + 84 + 10;
            GuiButton btn = getSelectedButton(buttons, x, y);

            if (btn instanceof GuiButtonShaderOption)
            {
                GuiButtonShaderOption btnSo = (GuiButtonShaderOption)btn;
                ShaderOption so = btnSo.getShaderOption();
                String[] lines = this.makeTooltipLines(so, x2 - x1);

                if (lines == null)
                {
                    return;
                }

                this.drawGradientRect(x1, y1, x2, y2, -536870912, -536870912);

                for (int i = 0; i < lines.length; ++i)
                {
                    String line = lines[i];
                    int col = 14540253;

                    if (line.endsWith("!"))
                    {
                        col = 16719904;
                    }

                    this.fontRendererObj.drawStringWithShadow(line, x1 + 5, y1 + 5 + i * 11, col);
                }
            }
        }
    }

    public String[] makeTooltipLines(ShaderOption so, int width)
    {
        if (so instanceof ShaderOptionProfile)
        {
            return null;
        }
        else
        {
            String name = so.getNameText();
            String desc = Config.normalize(so.getDescriptionText()).trim();
            String[] descs = this.splitDescription(desc);
            String id = null;

            if (!name.equals(so.getName()) && this.settings.advancedItemTooltips)
            {
                id = "\u00a78" + Lang.get("of.general.id") + ": " + so.getName();
            }

            String source = null;

            if (so.getPaths() != null && this.settings.advancedItemTooltips)
            {
                source = "\u00a78" + Lang.get("of.general.from") + ": " + Config.arrayToString((Object[])so.getPaths());
            }

            String def = null;

            if (so.getValueDefault() != null && this.settings.advancedItemTooltips)
            {
                String list = so.isEnabled() ? so.getValueText(so.getValueDefault()) : Lang.get("of.general.ambiguous");
                def = "\u00a78" + Lang.getDefault() + ": " + list;
            }

            ArrayList list1 = new ArrayList();
            list1.add(name);
            list1.addAll(Arrays.asList(descs));

            if (id != null)
            {
                list1.add(id);
            }

            if (source != null)
            {
                list1.add(source);
            }

            if (def != null)
            {
                list1.add(def);
            }

            String[] lines = this.makeTooltipLines(width, list1);
            return lines;
        }
    }

    public String[] splitDescription(String desc)
    {
        if (desc.length() <= 0)
        {
            return new String[0];
        }
        else
        {
            desc = StrUtils.removePrefix(desc, "//");
            String[] descs = desc.split("\\. ");

            for (int i = 0; i < descs.length; ++i)
            {
                descs[i] = "- " + descs[i].trim();
                descs[i] = StrUtils.removeSuffix(descs[i], ".");
            }

            return descs;
        }
    }

    public String[] makeTooltipLines(int width, List<String> args)
    {
        FontRenderer fr = this.fontRendererObj;
        ArrayList list = new ArrayList();

        for (int lines = 0; lines < args.size(); ++lines)
        {
            String arg = (String)args.get(lines);

            if (arg != null && arg.length() > 0)
            {
                List parts = fr.listFormattedStringToWidth(arg, width);
                Iterator it = parts.iterator();

                while (it.hasNext())
                {
                    String part = (String)it.next();
                    list.add(part);
                }
            }
        }

        String[] var10 = (String[])((String[])list.toArray(new String[list.size()]));
        return var10;
    }
}
