package com.synezia.client.components.texts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snkh
 *	26 jul. 2018
 */

@Getter @AllArgsConstructor
public enum TextSize {
	
    DEFAULT(1.0f),
    EXTANDED_DEFAULT(1.3f),
    LARGE(1.5f),
    EXTRA_LARGE(2.0f),
    EXTANDED_LARGE(3.0f),
    SMALL(0.8f),
    EXTRA_SMALL(0.5f);
    
    private float size;
}