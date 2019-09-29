package com.synezia.client.components.buttons;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;
import com.synezia.client.components.Size;
import com.synezia.client.components.SizedComponent;
import com.synezia.client.components.buttons.actions.Action;
import com.synezia.client.components.buttons.informations.DefaultInformations;
import com.synezia.client.components.buttons.informations.Informations;
import com.synezia.client.components.buttons.type.DefaultType;
import com.synezia.client.components.buttons.type.Type;
import com.synezia.client.settings.Settings;
import com.synezia.client.utilities.Colors;

import lombok.Getter;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Getter
public class Button extends SizedComponent {
	
	private String title;
	
	private Type type;
	private List<Action> actions;
	private Informations informations;
	
	private Colors color;
	private Colors titleColor;
	private boolean enabled;

	public Button(Integer posX, Integer posY) {
		super(posX, posY);
		
		this.setSize(new Size(150, 30));
		this.setType(new DefaultType());
		this.setInformations(new DefaultInformations());
		
		this.setColor(new Colors(Color.WHITE));
		this.actions = Lists.newArrayList();
		this.titleColor = new Colors(Color.WHITE);
		this.enabled = true;
	}
	
	public Button setSize(Integer xSize, Integer ySize) { this.setSize(new Size(xSize, ySize)); return this;}
	public Button setSize(Size size) {this.size = size; return this;}
	public Button setTitle(String title) {this.title = title; return this;}
	public Button setType(Type type) {this.type = type; this.type.setButton(this); return this;}
	public Button setInformations(Informations informations) {this.informations = informations; this.informations.setButton(this); return this;}
	
	public Button setColor(Colors color) {this.color = color; return this;}
	public Button setTitleColor(Colors color) {this.titleColor = color; return this;}
	
	public Button addAction(Action action) {this.actions.add(action); return this;}
	public Button enable() {this.enabled = true; return this;}
	public Button disable() {this.enabled = false; return this;}

	@Override
	public void draw() {
		
		if (!this.isEnabled())
			return;
		
		/**
		 * Draw Type
		 */
		
		if (this.type != null) 
			this.type.draw();
		
		/**
		 * Draw Informations
		 */
		
		if (this.informations != null) 
			this.informations.draw();
	}

}
