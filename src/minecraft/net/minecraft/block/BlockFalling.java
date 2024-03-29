package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;
    private static final String __OBFID = "CL_00000240";

    public BlockFalling()
    {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public BlockFalling(Material p_i45405_1_)
    {
        super(p_i45405_1_);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, this, this.tickRate(p_149726_1_));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this, this.tickRate(p_149695_1_));
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (!p_149674_1_.isRemote)
        {
            this.func_149830_m(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
        }
    }

    private void func_149830_m(World p_149830_1_, int p_149830_2_, int p_149830_3_, int p_149830_4_)
    {
        if (func_149831_e(p_149830_1_, p_149830_2_, p_149830_3_ - 1, p_149830_4_) && p_149830_3_ >= 0)
        {
            byte var8 = 32;

            if (!fallInstantly && p_149830_1_.checkChunksExist(p_149830_2_ - var8, p_149830_3_ - var8, p_149830_4_ - var8, p_149830_2_ + var8, p_149830_3_ + var8, p_149830_4_ + var8))
            {
                if (!p_149830_1_.isRemote)
                {
                    EntityFallingBlock var9 = new EntityFallingBlock(p_149830_1_, (double)((float)p_149830_2_ + 0.5F), (double)((float)p_149830_3_ + 0.5F), (double)((float)p_149830_4_ + 0.5F), this, p_149830_1_.getBlockMetadata(p_149830_2_, p_149830_3_, p_149830_4_));
                    this.func_149829_a(var9);
                    p_149830_1_.spawnEntityInWorld(var9);
                }
            }
            else
            {
                p_149830_1_.setBlockToAir(p_149830_2_, p_149830_3_, p_149830_4_);

                while (func_149831_e(p_149830_1_, p_149830_2_, p_149830_3_ - 1, p_149830_4_) && p_149830_3_ > 0)
                {
                    --p_149830_3_;
                }

                if (p_149830_3_ > 0)
                {
                    p_149830_1_.setBlock(p_149830_2_, p_149830_3_, p_149830_4_, this);
                }
            }
        }
    }

    protected void func_149829_a(EntityFallingBlock p_149829_1_) {}

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World p_149738_1_)
    {
        return 2;
    }

    public static boolean func_149831_e(World p_149831_0_, int p_149831_1_, int p_149831_2_, int p_149831_3_)
    {
        Block var4 = p_149831_0_.getBlock(p_149831_1_, p_149831_2_, p_149831_3_);

        if (var4.blockMaterial == Material.air)
        {
            return true;
        }
        else if (var4 == Blocks.fire)
        {
            return true;
        }
        else
        {
            Material var5 = var4.blockMaterial;
            return var5 == Material.water ? true : var5 == Material.lava;
        }
    }

    public void func_149828_a(World p_149828_1_, int p_149828_2_, int p_149828_3_, int p_149828_4_, int p_149828_5_) {}
}
