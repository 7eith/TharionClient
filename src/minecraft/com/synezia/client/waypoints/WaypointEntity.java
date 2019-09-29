package com.synezia.client.waypoints;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author Snkh
 *	29 sept. 2019
 */

public class WaypointEntity extends Entity  
{
	
    public WaypointEntity(World world) 
    {
        super(world);
        this.ignoreFrustumCheck = true;
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) 
    {
        return true;
    }

    @Override
    protected void entityInit() {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {}

    @Override
    public int getBrightnessForRender(float f) 
    {
        return 15728880;
    }

    @Override
    public float getBrightness(float f) 
    {
        return 1.0f;
    }
}
	