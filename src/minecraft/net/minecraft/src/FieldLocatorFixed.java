package net.minecraft.src;

import java.lang.reflect.Field;

public class FieldLocatorFixed implements IFieldLocator
{
    public Field field;

    public FieldLocatorFixed(Field field)
    {
        this.field = field;
    }

    public Field getField()
    {
        return this.field;
    }
}
