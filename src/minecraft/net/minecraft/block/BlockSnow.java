package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow extends Block
{
    private static final String __OBFID = "CL_00000309";

    protected BlockSnow()
    {
        super(Material.snow);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.func_150154_b(0);
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("snow");
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        int var5 = p_149668_1_.getBlockMetadata(p_149668_2_, p_149668_3_, p_149668_4_) & 7;
        float var6 = 0.125F;
        return AxisAlignedBB.getBoundingBox((double)p_149668_2_ + this.minX, (double)p_149668_3_ + this.minY, (double)p_149668_4_ + this.minZ, (double)p_149668_2_ + this.maxX, (double)((float)p_149668_3_ + (float)var5 * var6), (double)p_149668_4_ + this.maxZ);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.func_150154_b(0);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        this.func_150154_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));
    }

    protected void func_150154_b(int p_150154_1_)
    {
        int var2 = p_150154_1_ & 7;
        float var3 = (float)(2 * (1 + var2)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        Block var5 = p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_);
        return var5 != Blocks.ice && var5 != Blocks.packed_ice ? (var5.getMaterial() == Material.leaves ? true : (var5 == this && (p_149742_1_.getBlockMetadata(p_149742_2_, p_149742_3_ - 1, p_149742_4_) & 7) == 7 ? true : var5.isOpaqueCube() && var5.blockMaterial.blocksMovement())) : false;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        this.func_150155_m(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
    }

    private boolean func_150155_m(World p_150155_1_, int p_150155_2_, int p_150155_3_, int p_150155_4_)
    {
        if (!this.canPlaceBlockAt(p_150155_1_, p_150155_2_, p_150155_3_, p_150155_4_))
        {
            this.dropBlockAsItem(p_150155_1_, p_150155_2_, p_150155_3_, p_150155_4_, p_150155_1_.getBlockMetadata(p_150155_2_, p_150155_3_, p_150155_4_), 0);
            p_150155_1_.setBlockToAir(p_150155_2_, p_150155_3_, p_150155_4_);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_)
    {
        int var7 = p_149636_6_ & 7;
        this.dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, new ItemStack(Items.snowball, var7 + 1, 0));
        p_149636_1_.setBlockToAir(p_149636_3_, p_149636_4_, p_149636_5_);
        p_149636_2_.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Items.snowball;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (p_149674_1_.getSavedLightValue(EnumSkyBlock.Block, p_149674_2_, p_149674_3_, p_149674_4_) > 11)
        {
            this.dropBlockAsItem(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_), 0);
            p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_, p_149674_4_);
        }
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        return p_149646_5_ == 1 ? true : super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
    }
}
