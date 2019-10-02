package com.synezia.client.components.texts.fields;

import com.synezia.client.components.Size;
import com.synezia.client.components.SizedComponent;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiTextField;

/**
 * @author Snkh
 *	2 oct. 2019
 */

@Getter @Setter
public class TextFieldComponent extends SizedComponent {
	
	private String defaultText;
	private GuiTextField field;
	private ActionField action;

	public TextFieldComponent(String defaultText, Size size, Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.defaultText = defaultText;
		this.size = size;
		this.field = new GuiTextField(defaultText, posX, posY, this.getSize().getWidth(), this.getSize().getHeight());
		this.field.setMaxStringLength(300);
		this.field.setEnableBackgroundDrawing(false);
		this.field.setFocused(false);
	}
	
	public TextFieldComponent setFieldSize(Integer width, Integer height)
	{
		this.field.setWidth(width);
		this.field.setHeight(height);
		return this;
	}

	@Override
	public void draw() 
	{
		this.field.drawTextBox();
	}

}
