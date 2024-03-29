package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends Block
{
    /** This piston is the sticky one? */
    private final boolean isSticky;

    /** Only visible when piston is extended */
    private IIcon innerTopIcon;

    /** Bottom side texture */
    private IIcon bottomIcon;

    /** Top icon of piston depends on (either sticky or normal) */
    private IIcon topIcon;
    private static final String __OBFID = "CL_00000366";

    public BlockPistonBase(boolean p_i45443_1_)
    {
        super(Material.piston);
        this.isSticky = p_i45443_1_;
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public IIcon getPistonExtensionTexture()
    {
        return this.topIcon;
    }

    public void func_150070_b(float p_150070_1_, float p_150070_2_, float p_150070_3_, float p_150070_4_, float p_150070_5_, float p_150070_6_)
    {
        this.setBlockBounds(p_150070_1_, p_150070_2_, p_150070_3_, p_150070_4_, p_150070_5_, p_150070_6_);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        int var3 = getPistonOrientation(p_149691_2_);
        return var3 > 5 ? this.topIcon : (p_149691_1_ == var3 ? (!isExtended(p_149691_2_) && this.minX <= 0.0D && this.minY <= 0.0D && this.minZ <= 0.0D && this.maxX >= 1.0D && this.maxY >= 1.0D && this.maxZ >= 1.0D ? this.topIcon : this.innerTopIcon) : (p_149691_1_ == Facing.oppositeSide[var3] ? this.bottomIcon : this.blockIcon));
    }

    public static IIcon getPistonBaseIcon(String p_150074_0_)
    {
        return p_150074_0_ == "piston_side" ? Blocks.piston.blockIcon : (p_150074_0_ == "piston_top_normal" ? Blocks.piston.topIcon : (p_150074_0_ == "piston_top_sticky" ? Blocks.sticky_piston.topIcon : (p_150074_0_ == "piston_inner" ? Blocks.piston.innerTopIcon : null)));
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("piston_side");
        this.topIcon = p_149651_1_.registerIcon(this.isSticky ? "piston_top_sticky" : "piston_top_normal");
        this.innerTopIcon = p_149651_1_.registerIcon("piston_inner");
        this.bottomIcon = p_149651_1_.registerIcon("piston_bottom");
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 16;
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
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return false;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int var7 = determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, var7, 2);

        if (!p_149689_1_.isRemote)
        {
            this.updatePistonState(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!p_149695_1_.isRemote)
        {
            this.updatePistonState(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        if (!p_149726_1_.isRemote && p_149726_1_.getTileEntity(p_149726_2_, p_149726_3_, p_149726_4_) == null)
        {
            this.updatePistonState(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        }
    }

    /**
     * handles attempts to extend or retract the piston.
     */
    private void updatePistonState(World p_150078_1_, int p_150078_2_, int p_150078_3_, int p_150078_4_)
    {
        int var5 = p_150078_1_.getBlockMetadata(p_150078_2_, p_150078_3_, p_150078_4_);
        int var6 = getPistonOrientation(var5);

        if (var6 != 7)
        {
            boolean var7 = this.isIndirectlyPowered(p_150078_1_, p_150078_2_, p_150078_3_, p_150078_4_, var6);

            if (var7 && !isExtended(var5))
            {
                if (canExtend(p_150078_1_, p_150078_2_, p_150078_3_, p_150078_4_, var6))
                {
                    p_150078_1_.addBlockEvent(p_150078_2_, p_150078_3_, p_150078_4_, this, 0, var6);
                }
            }
            else if (!var7 && isExtended(var5))
            {
                p_150078_1_.setBlockMetadataWithNotify(p_150078_2_, p_150078_3_, p_150078_4_, var6, 2);
                p_150078_1_.addBlockEvent(p_150078_2_, p_150078_3_, p_150078_4_, this, 1, var6);
            }
        }
    }

    private boolean isIndirectlyPowered(World p_150072_1_, int p_150072_2_, int p_150072_3_, int p_150072_4_, int p_150072_5_)
    {
        return p_150072_5_ != 0 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ - 1, p_150072_4_, 0) ? true : (p_150072_5_ != 1 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_, 1) ? true : (p_150072_5_ != 2 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ - 1, 2) ? true : (p_150072_5_ != 3 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ + 1, 3) ? true : (p_150072_5_ != 5 && p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_, p_150072_4_, 5) ? true : (p_150072_5_ != 4 && p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_, p_150072_4_, 4) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_, 0) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 2, p_150072_4_, 1) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ - 1, 2) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ + 1, 3) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_ + 1, p_150072_4_, 4) ? true : p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_ + 1, p_150072_4_, 5)))))))))));
    }

    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        if (!p_149696_1_.isRemote)
        {
            boolean var7 = this.isIndirectlyPowered(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_);

            if (var7 && p_149696_5_ == 1)
            {
                p_149696_1_.setBlockMetadataWithNotify(p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_ | 8, 2);
                return false;
            }

            if (!var7 && p_149696_5_ == 0)
            {
                return false;
            }
        }

        if (p_149696_5_ == 0)
        {
            if (!this.tryExtend(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_))
            {
                return false;
            }

            p_149696_1_.setBlockMetadataWithNotify(p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_ | 8, 2);
            p_149696_1_.playSoundEffect((double)p_149696_2_ + 0.5D, (double)p_149696_3_ + 0.5D, (double)p_149696_4_ + 0.5D, "tile.piston.out", 0.5F, p_149696_1_.rand.nextFloat() * 0.25F + 0.6F);
        }
        else if (p_149696_5_ == 1)
        {
            TileEntity var16 = p_149696_1_.getTileEntity(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_], p_149696_3_ + Facing.offsetsYForSide[p_149696_6_], p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);

            if (var16 instanceof TileEntityPiston)
            {
                ((TileEntityPiston)var16).clearPistonTileEntity();
            }

            p_149696_1_.setBlock(p_149696_2_, p_149696_3_, p_149696_4_, Blocks.piston_extension, p_149696_6_, 3);
            p_149696_1_.setTileEntity(p_149696_2_, p_149696_3_, p_149696_4_, BlockPistonMoving.getTileEntity(this, p_149696_6_, p_149696_6_, false, true));

            if (this.isSticky)
            {
                int var8 = p_149696_2_ + Facing.offsetsXForSide[p_149696_6_] * 2;
                int var9 = p_149696_3_ + Facing.offsetsYForSide[p_149696_6_] * 2;
                int var10 = p_149696_4_ + Facing.offsetsZForSide[p_149696_6_] * 2;
                Block var11 = p_149696_1_.getBlock(var8, var9, var10);
                int var12 = p_149696_1_.getBlockMetadata(var8, var9, var10);
                boolean var13 = false;

                if (var11 == Blocks.piston_extension)
                {
                    TileEntity var14 = p_149696_1_.getTileEntity(var8, var9, var10);

                    if (var14 instanceof TileEntityPiston)
                    {
                        TileEntityPiston var15 = (TileEntityPiston)var14;

                        if (var15.getPistonOrientation() == p_149696_6_ && var15.isExtending())
                        {
                            var15.clearPistonTileEntity();
                            var11 = var15.getStoredBlockID();
                            var12 = var15.getBlockMetadata();
                            var13 = true;
                        }
                    }
                }

                if (!var13 && var11.getMaterial() != Material.air && canPushBlock(var11, p_149696_1_, var8, var9, var10, false) && (var11.getMobilityFlag() == 0 || var11 == Blocks.piston || var11 == Blocks.sticky_piston))
                {
                    p_149696_2_ += Facing.offsetsXForSide[p_149696_6_];
                    p_149696_3_ += Facing.offsetsYForSide[p_149696_6_];
                    p_149696_4_ += Facing.offsetsZForSide[p_149696_6_];
                    p_149696_1_.setBlock(p_149696_2_, p_149696_3_, p_149696_4_, Blocks.piston_extension, var12, 3);
                    p_149696_1_.setTileEntity(p_149696_2_, p_149696_3_, p_149696_4_, BlockPistonMoving.getTileEntity(var11, var12, p_149696_6_, false, false));
                    p_149696_1_.setBlockToAir(var8, var9, var10);
                }
                else if (!var13)
                {
                    p_149696_1_.setBlockToAir(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_], p_149696_3_ + Facing.offsetsYForSide[p_149696_6_], p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);
                }
            }
            else
            {
                p_149696_1_.setBlockToAir(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_], p_149696_3_ + Facing.offsetsYForSide[p_149696_6_], p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);
            }

            p_149696_1_.playSoundEffect((double)p_149696_2_ + 0.5D, (double)p_149696_3_ + 0.5D, (double)p_149696_4_ + 0.5D, "tile.piston.in", 0.5F, p_149696_1_.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        int var5 = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);

        if (isExtended(var5))
        {
            float var6 = 0.25F;

            switch (getPistonOrientation(var5))
            {
                case 0:
                    this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 1:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                    break;

                case 2:
                    this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                    break;

                case 3:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                    break;

                case 4:
                    this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 5:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        this.setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
        return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public static int getPistonOrientation(int p_150076_0_)
    {
        return p_150076_0_ & 7;
    }

    /**
     * Determine if the metadata is related to something powered.
     */
    public static boolean isExtended(int p_150075_0_)
    {
        return (p_150075_0_ & 8) != 0;
    }

    /**
     * gets the way this piston should face for that entity that placed it.
     */
    public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_)
    {
        if (MathHelper.abs((float)p_150071_4_.posX - (float)p_150071_1_) < 2.0F && MathHelper.abs((float)p_150071_4_.posZ - (float)p_150071_3_) < 2.0F)
        {
            double var5 = p_150071_4_.posY + 1.82D - (double)p_150071_4_.yOffset;

            if (var5 - (double)p_150071_2_ > 2.0D)
            {
                return 1;
            }

            if ((double)p_150071_2_ - var5 > 0.0D)
            {
                return 0;
            }
        }

        int var7 = MathHelper.floor_double((double)(p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return var7 == 0 ? 2 : (var7 == 1 ? 5 : (var7 == 2 ? 3 : (var7 == 3 ? 4 : 0)));
    }

    /**
     * returns true if the piston can push the specified block
     */
    private static boolean canPushBlock(Block p_150080_0_, World p_150080_1_, int p_150080_2_, int p_150080_3_, int p_150080_4_, boolean p_150080_5_)
    {
        if (p_150080_0_ == Blocks.obsidian)
        {
            return false;
        }
        else
        {
            if (p_150080_0_ != Blocks.piston && p_150080_0_ != Blocks.sticky_piston)
            {
                if (p_150080_0_.getBlockHardness(p_150080_1_, p_150080_2_, p_150080_3_, p_150080_4_) == -1.0F)
                {
                    return false;
                }

                if (p_150080_0_.getMobilityFlag() == 2)
                {
                    return false;
                }

                if (p_150080_0_.getMobilityFlag() == 1)
                {
                    if (!p_150080_5_)
                    {
                        return false;
                    }

                    return true;
                }
            }
            else if (isExtended(p_150080_1_.getBlockMetadata(p_150080_2_, p_150080_3_, p_150080_4_)))
            {
                return false;
            }

            return !(p_150080_0_ instanceof ITileEntityProvider);
        }
    }

    /**
     * checks to see if this piston could push the blocks in front of it.
     */
    private static boolean canExtend(World p_150077_0_, int p_150077_1_, int p_150077_2_, int p_150077_3_, int p_150077_4_)
    {
        int var5 = p_150077_1_ + Facing.offsetsXForSide[p_150077_4_];
        int var6 = p_150077_2_ + Facing.offsetsYForSide[p_150077_4_];
        int var7 = p_150077_3_ + Facing.offsetsZForSide[p_150077_4_];
        int var8 = 0;

        while (true)
        {
            if (var8 < 13)
            {
                if (var6 <= 0 || var6 >= 255)
                {
                    return false;
                }

                Block var9 = p_150077_0_.getBlock(var5, var6, var7);

                if (var9.getMaterial() != Material.air)
                {
                    if (!canPushBlock(var9, p_150077_0_, var5, var6, var7, true))
                    {
                        return false;
                    }

                    if (var9.getMobilityFlag() != 1)
                    {
                        if (var8 == 12)
                        {
                            return false;
                        }

                        var5 += Facing.offsetsXForSide[p_150077_4_];
                        var6 += Facing.offsetsYForSide[p_150077_4_];
                        var7 += Facing.offsetsZForSide[p_150077_4_];
                        ++var8;
                        continue;
                    }
                }
            }

            return true;
        }
    }

    /**
     * attempts to extend the piston. returns false if impossible.
     */
    private boolean tryExtend(World p_150079_1_, int p_150079_2_, int p_150079_3_, int p_150079_4_, int p_150079_5_)
    {
        int var6 = p_150079_2_ + Facing.offsetsXForSide[p_150079_5_];
        int var7 = p_150079_3_ + Facing.offsetsYForSide[p_150079_5_];
        int var8 = p_150079_4_ + Facing.offsetsZForSide[p_150079_5_];
        int var9 = 0;

        while (true)
        {
            if (var9 < 13)
            {
                if (var7 <= 0 || var7 >= 255)
                {
                    return false;
                }

                Block var10 = p_150079_1_.getBlock(var6, var7, var8);

                if (var10.getMaterial() != Material.air)
                {
                    if (!canPushBlock(var10, p_150079_1_, var6, var7, var8, true))
                    {
                        return false;
                    }

                    if (var10.getMobilityFlag() != 1)
                    {
                        if (var9 == 12)
                        {
                            return false;
                        }

                        var6 += Facing.offsetsXForSide[p_150079_5_];
                        var7 += Facing.offsetsYForSide[p_150079_5_];
                        var8 += Facing.offsetsZForSide[p_150079_5_];
                        ++var9;
                        continue;
                    }

                    var10.dropBlockAsItem(p_150079_1_, var6, var7, var8, p_150079_1_.getBlockMetadata(var6, var7, var8), 0);
                    p_150079_1_.setBlockToAir(var6, var7, var8);
                }
            }

            var9 = var6;
            int var19 = var7;
            int var11 = var8;
            int var12 = 0;
            Block[] var13;
            int var14;
            int var15;
            int var16;

            for (var13 = new Block[13]; var6 != p_150079_2_ || var7 != p_150079_3_ || var8 != p_150079_4_; var8 = var16)
            {
                var14 = var6 - Facing.offsetsXForSide[p_150079_5_];
                var15 = var7 - Facing.offsetsYForSide[p_150079_5_];
                var16 = var8 - Facing.offsetsZForSide[p_150079_5_];
                Block var17 = p_150079_1_.getBlock(var14, var15, var16);
                int var18 = p_150079_1_.getBlockMetadata(var14, var15, var16);

                if (var17 == this && var14 == p_150079_2_ && var15 == p_150079_3_ && var16 == p_150079_4_)
                {
                    p_150079_1_.setBlock(var6, var7, var8, Blocks.piston_extension, p_150079_5_ | (this.isSticky ? 8 : 0), 4);
                    p_150079_1_.setTileEntity(var6, var7, var8, BlockPistonMoving.getTileEntity(Blocks.piston_head, p_150079_5_ | (this.isSticky ? 8 : 0), p_150079_5_, true, false));
                }
                else
                {
                    p_150079_1_.setBlock(var6, var7, var8, Blocks.piston_extension, var18, 4);
                    p_150079_1_.setTileEntity(var6, var7, var8, BlockPistonMoving.getTileEntity(var17, var18, p_150079_5_, true, false));
                }

                var13[var12++] = var17;
                var6 = var14;
                var7 = var15;
            }

            var6 = var9;
            var7 = var19;
            var8 = var11;

            for (var12 = 0; var6 != p_150079_2_ || var7 != p_150079_3_ || var8 != p_150079_4_; var8 = var16)
            {
                var14 = var6 - Facing.offsetsXForSide[p_150079_5_];
                var15 = var7 - Facing.offsetsYForSide[p_150079_5_];
                var16 = var8 - Facing.offsetsZForSide[p_150079_5_];
                p_150079_1_.notifyBlocksOfNeighborChange(var14, var15, var16, var13[var12++]);
                var6 = var14;
                var7 = var15;
            }

            return true;
        }
    }
}
