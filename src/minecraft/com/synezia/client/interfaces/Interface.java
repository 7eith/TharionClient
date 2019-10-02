package com.synezia.client.interfaces;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.synezia.client.components.Component;
import com.synezia.client.components.SizedComponent;
import com.synezia.client.components.buttons.Button;
import com.synezia.client.components.buttons.actions.Action;
import com.synezia.client.components.texts.fields.ActionField;
import com.synezia.client.components.texts.fields.TextFieldComponent;

import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

/**
 * @author Snkh
 *	25 jul. 2018
 */

public abstract class Interface extends GuiScreen {
	
	private boolean canClose = true;
	
	/**
	 * Components
	 */
	
	@Getter private List<Component> components = Lists.newArrayList();
	@Getter private Button currentButton;
	@Getter private TextFieldComponent currentTextField;
	
	@Override
	public void initGui() {
		super.initGui();
		
		if(!this.components.isEmpty()) 
			this.components.clear();
		
		Keyboard.enableRepeatEvents((boolean)true);
		
		this.initializeInterface();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float updateTicks) {
		super.drawScreen(mouseX, mouseY, updateTicks);
		
		this.drawComponents();
		
		for(Component current: components)
			if(current.isVisible())
				current.draw();
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		for (Component component : this.components) {
            if (!(component instanceof TextFieldComponent)) continue;
            TextFieldComponent componentField = (TextFieldComponent)component;
            GuiTextField field = componentField.getField();
            field.updateCursorCounter();
        }
		
		this.updateInterface();
	}
	
    @Override
    public void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        if (this.currentButton != null && state == 0) 
            this.currentButton = null;

        super.mouseMovedOrUp(mouseX, mouseY, state);
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    	for (Component item : components)
    		if (item instanceof TextFieldComponent) 
    			((TextFieldComponent) item).getField().setFocused(false);

        for (Component component : components) {
            SizedComponent sized;
            if (!component.isActive() || !component.isVisible() || !(component instanceof SizedComponent) || !(sized = (SizedComponent)component).isPressed() || mouseButton != 0) 
            	continue;
            
            if (sized instanceof Button) {
                Button button;
                this.currentButton = button = (Button)sized;
                List<Action> actions = button.getActions();
                if (actions.isEmpty()) continue;
                for (Action action : actions) {
                    action.execute();
                }
                continue;
            }
            
            if (component instanceof TextFieldComponent) {
                TextFieldComponent componentField = (TextFieldComponent)component;
                GuiTextField field = componentField.getField();
                field.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    @Override
    protected void keyTyped(char carracter, int value) {
    	if (this.canClose) 
            super.keyTyped(carracter, value);
        
        for (Component component : this.components) {
            if (!(component instanceof TextFieldComponent)) 
            	continue;
            TextFieldComponent componentField = (TextFieldComponent)component;
            GuiTextField field = componentField.getField();
            
            if (field.isFocused())
            	field.textboxKeyTyped(carracter, value);
            
            ActionField actionField = componentField.getAction();
            if (actionField == null) 
            	continue;
            actionField.textUpdate(field.getText());
        }
    }
	
	/**
	 * Methods
	 */
	
	public abstract void initializeInterface();
	public abstract void drawComponents();
	public abstract void updateInterface();
	
	/**
	 * 
	 */
	
	public void addComponent(Component component) {
		this.addComponent(component.toString(), component);
	}
	
	public void addComponent(String id, Component component) {
		if(this.getComponent(id) != null) return;
		
		component.setId(id);
		this.components.add(component);
	}
	
	public Component getComponent(String id) {
		for (Component current : components) 
            if(current.getId().equalsIgnoreCase(id)) return current;
		
        return null;
	}
	
	public void removeComponent(String id) {
		for(Component component: components) 
			if(component.getId() == id) components.remove(component);
	}
	
	
	/**
	 *  Helpers
	 */
	
	@Getter private Integer splitWidth = this.getWidth() / 2;
	@Getter private Integer splitHeight = this.getHeight() / 2;

}