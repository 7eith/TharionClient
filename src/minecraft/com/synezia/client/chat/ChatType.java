package com.synezia.client.chat;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChatType {

	PUBLIC(0, "Publique"),
	
	FACTION(1, "Faction"),

	MARKET(2, "Commerce"),
	
	ANNONCE(3, "Annonces"),
	
	STAFF(4, "Staff");
	
	private Integer id;
	private String name;
	
}
