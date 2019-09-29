package com.synezia.client.waypoints;

import com.synezia.client.utilities.Colors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snkh
 *	29 sept. 2019
 */

@Getter @AllArgsConstructor
public enum WaypointType {
	
    PLAYER(Colors.YELLOW),
    TRADE(Colors.YELLOW),
    EVENT(Colors.YELLOW),
    FACTION(Colors.YELLOW),
    CLAN(Colors.YELLOW),
    OTHERS(Colors.YELLOW);
		
	private Colors color;

}
