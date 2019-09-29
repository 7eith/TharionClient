package com.synezia.client.components.buttons.actions;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@AllArgsConstructor
public class DisplayScreenAction extends Action {

    private GuiScreen target;

    @Override
    public void execute() {
    	Minecraft.getMinecraft().displayGuiScreen(target);
    }

}
