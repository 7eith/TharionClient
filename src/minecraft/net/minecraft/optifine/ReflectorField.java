package net.minecraft.optifine;

import java.lang.reflect.Field;

public class ReflectorField
{
    public IFieldLocator fieldLocator;
    public boolean checked;
    public Field targetField;

    public ReflectorField(ReflectorClass reflectorClass, String targetFieldName)
    {
        this((IFieldLocator)(new FieldLocatorName(reflectorClass, targetFieldName)));
    }

    public ReflectorField(ReflectorClass reflectorClass, Class targetFieldType)
    {
        this(reflectorClass, targetFieldType, 0);
    }

    public ReflectorField(ReflectorClass reflectorClass, Class targetFieldType, int targetFieldIndex)
    {
        this((IFieldLocator)(new FieldLocatorType(reflectorClass, targetFieldType, targetFieldIndex)));
    }

    public ReflectorField(Field field)
    {
        this((IFieldLocator)(new FieldLocatorFixed(field)));
    }

    public ReflectorField(IFieldLocator fieldLocator)
    {
        this.fieldLocator = null;
        this.checked = false;
        this.targetField = null;
        this.fieldLocator = fieldLocator;
        this.getTargetField();
    }

    public Field getTargetField()
    {
        if (this.checked)
        {
            return this.targetField;
        }
        else
        {
            this.checked = true;
            this.targetField = this.fieldLocator.getField();

            if (this.targetField != null)
            {
                this.targetField.setAccessible(true);
            }

            return this.targetField;
        }
    }

    public Object getValue()
    {
        return Reflector.getFieldValue((Object)null, this);
    }

    public void setValue(Object value)
    {
        Reflector.setFieldValue((Object)null, this, value);
    }

    public void setValue(Object obj, Object value)
    {
        Reflector.setFieldValue(obj, this, value);
    }

    public boolean exists()
    {
        return this.getTargetField() != null;
    }
}
