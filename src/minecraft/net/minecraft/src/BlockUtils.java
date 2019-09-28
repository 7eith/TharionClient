package net.minecraft.src;

import net.minecraft.block.Block;

public class BlockUtils
{
    public static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
    public static ReflectorMethod ForgeBlock_setLightOpacity = new ReflectorMethod(ForgeBlock, "setLightOpacity");
    public static boolean directAccessValid = true;

    public static void setLightOpacity(Block block, int opacity)
    {
        if (directAccessValid)
        {
            try
            {
                block.setLightOpacity(opacity);
                return;
            }
            catch (IllegalAccessError var3)
            {
                directAccessValid = false;

                if (!ForgeBlock_setLightOpacity.exists())
                {
                    throw var3;
                }
            }
        }

        Reflector.callVoid(block, ForgeBlock_setLightOpacity, new Object[] {Integer.valueOf(opacity)});
    }
}
