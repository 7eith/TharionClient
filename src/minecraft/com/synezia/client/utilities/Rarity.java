package com.synezia.client.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Getter @AllArgsConstructor
public enum Rarity {
	
	COMMUN(1, EnumChatFormatting.WHITE, "Un objet commun"),
	UNCOMMUN(2, EnumChatFormatting.GRAY, "Un objet hors du commun"),
	RARE(3, EnumChatFormatting.AQUA, "Un objet rare!"),
	EPIC(4, EnumChatFormatting.LIGHT_PURPLE, "Cette objet est epic!"),
	LEGENDARY(5, EnumChatFormatting.GOLD, "Legendaire!");
	
	private Integer rank;
	private EnumChatFormatting color;
	private String text;
}
