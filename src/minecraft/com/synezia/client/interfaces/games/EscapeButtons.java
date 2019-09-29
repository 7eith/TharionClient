package com.synezia.client.interfaces.games;

import java.util.List;

import com.google.common.collect.Lists;
import com.synezia.client.components.Size;
import com.synezia.client.components.buttons.Button;
import com.synezia.client.components.buttons.actions.Action;
import com.synezia.client.components.buttons.actions.DisplayScreenAction;
import com.synezia.client.components.buttons.informations.EscapeButtonInformations;
import com.synezia.client.components.buttons.informations.Informations;
import com.synezia.client.components.buttons.type.IconType;
import com.synezia.client.resources.Resource;

import lombok.Data;
import net.minecraft.client.Minecraft;

/**
 * @author Snkh
 *	25 jul. 2018
 */

@Data
public class EscapeButtons {
	
	private List<Button> buttons = Lists.newArrayList();
	private Size defaultSize = new Size(20, 20);
	
	public EscapeButtons() {
		
        Button home = new Button(7, 15);
        
        home.setSize(this.getDefaultSize());
        home.setType(new IconType(Resource.HOME));
        home.setInformations(new EscapeButtonInformations(9, 38));
        home.setTitle("Accueil");
        home.addAction(new DisplayScreenAction(null));
        
        home.enable();
        
        this.buttons.add(home);
		
	}
}
