package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityBeacon extends TileEntity implements IInventory
{
    /** List of effects that Beacon can apply */
    public static final Potion[][] effectsList = new Potion[][] {{Potion.moveSpeed, Potion.digSpeed}, {Potion.resistance, Potion.jump}, {Potion.damageBoost}, {Potion.regeneration}};
    private long field_146016_i;
    private float field_146014_j;
    private boolean field_146015_k;

    /** Level of this beacon's pyramid. */
    private int levels = -1;

    /** Primary potion effect given by this beacon. */
    private int primaryEffect;

    /** Secondary potion effect given by this beacon. */
    private int secondaryEffect;

    /** Item given to this beacon as payment. */
    private ItemStack payment;
    private String field_146008_p;
    private static final String __OBFID = "CL_00000339";

    public void updateEntity()
    {
        if (this.worldObj.getTotalWorldTime() % 80L == 0L)
        {
            this.func_146003_y();
            this.func_146000_x();
        }
    }

    private void func_146000_x()
    {
        if (this.field_146015_k && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0)
        {
            double var1 = (double)(this.levels * 10 + 10);
            byte var3 = 0;

            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect)
            {
                var3 = 1;
            }

            AxisAlignedBB var4 = AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(var1, var1, var1);
            var4.maxY = (double)this.worldObj.getHeight();
            List var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var4);
            Iterator var6 = var5.iterator();
            EntityPlayer var7;

            while (var6.hasNext())
            {
                var7 = (EntityPlayer)var6.next();
                var7.addPotionEffect(new PotionEffect(this.primaryEffect, 180, var3, true));
            }

            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0)
            {
                var6 = var5.iterator();

                while (var6.hasNext())
                {
                    var7 = (EntityPlayer)var6.next();
                    var7.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true));
                }
            }
        }
    }

    private void func_146003_y()
    {
        int var1 = this.levels;

        if (!this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord))
        {
            this.field_146015_k = false;
            this.levels = 0;
        }
        else
        {
            this.field_146015_k = true;
            this.levels = 0;

            for (int var2 = 1; var2 <= 4; this.levels = var2++)
            {
                int var3 = this.yCoord - var2;

                if (var3 < 0)
                {
                    break;
                }

                boolean var4 = true;

                for (int var5 = this.xCoord - var2; var5 <= this.xCoord + var2 && var4; ++var5)
                {
                    for (int var6 = this.zCoord - var2; var6 <= this.zCoord + var2; ++var6)
                    {
                        Block var7 = this.worldObj.getBlock(var5, var3, var6);

                        if (var7 != Blocks.emerald_block && var7 != Blocks.gold_block && var7 != Blocks.diamond_block && var7 != Blocks.iron_block)
                        {
                            var4 = false;
                            break;
                        }
                    }
                }

                if (!var4)
                {
                    break;
                }
            }

            if (this.levels == 0)
            {
                this.field_146015_k = false;
            }
        }

        if (!this.worldObj.isRemote && this.levels == 4 && var1 < this.levels)
        {
            Iterator var8 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)this.xCoord, (double)(this.yCoord - 4), (double)this.zCoord).expand(10.0D, 5.0D, 10.0D)).iterator();

            while (var8.hasNext())
            {
                EntityPlayer var9 = (EntityPlayer)var8.next();
                var9.triggerAchievement(AchievementList.field_150965_K);
            }
        }
    }

    public float func_146002_i()
    {
        if (!this.field_146015_k)
        {
            return 0.0F;
        }
        else
        {
            int var1 = (int)(this.worldObj.getTotalWorldTime() - this.field_146016_i);
            this.field_146016_i = this.worldObj.getTotalWorldTime();

            if (var1 > 1)
            {
                this.field_146014_j -= (float)var1 / 40.0F;

                if (this.field_146014_j < 0.0F)
                {
                    this.field_146014_j = 0.0F;
                }
            }

            this.field_146014_j += 0.025F;

            if (this.field_146014_j > 1.0F)
            {
                this.field_146014_j = 1.0F;
            }

            return this.field_146014_j;
        }
    }

    /**
     * Return the primary potion effect given by this beacon.
     */
    public int getPrimaryEffect()
    {
        return this.primaryEffect;
    }

    /**
     * Return the secondary potion effect given by this beacon.
     */
    public int getSecondaryEffect()
    {
        return this.secondaryEffect;
    }

    /**
     * Return the levels of this beacon's pyramid.
     */
    public int getLevels()
    {
        return this.levels;
    }

    public void func_146005_c(int p_146005_1_)
    {
        this.levels = p_146005_1_;
    }

    public void setPrimaryEffect(int p_146001_1_)
    {
        this.primaryEffect = 0;

        for (int var2 = 0; var2 < this.levels && var2 < 3; ++var2)
        {
            Potion[] var3 = effectsList[var2];
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                Potion var6 = var3[var5];

                if (var6.id == p_146001_1_)
                {
                    this.primaryEffect = p_146001_1_;
                    return;
                }
            }
        }
    }

    public void setSecondaryEffect(int p_146004_1_)
    {
        this.secondaryEffect = 0;

        if (this.levels >= 4)
        {
            for (int var2 = 0; var2 < 4; ++var2)
            {
                Potion[] var3 = effectsList[var2];
                int var4 = var3.length;

                for (int var5 = 0; var5 < var4; ++var5)
                {
                    Potion var6 = var3[var5];

                    if (var6.id == p_146004_1_)
                    {
                        this.secondaryEffect = p_146004_1_;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, var1);
    }

    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.primaryEffect = p_145839_1_.getInteger("Primary");
        this.secondaryEffect = p_145839_1_.getInteger("Secondary");
        this.levels = p_145839_1_.getInteger("Levels");
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("Primary", this.primaryEffect);
        p_145841_1_.setInteger("Secondary", this.secondaryEffect);
        p_145841_1_.setInteger("Levels", this.levels);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 1;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return p_70301_1_ == 0 ? this.payment : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (p_70298_1_ == 0 && this.payment != null)
        {
            if (p_70298_2_ >= this.payment.stackSize)
            {
                ItemStack var3 = this.payment;
                this.payment = null;
                return var3;
            }
            else
            {
                this.payment.stackSize -= p_70298_2_;
                return new ItemStack(this.payment.getItem(), p_70298_2_, this.payment.getItemDamage());
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        if (p_70304_1_ == 0 && this.payment != null)
        {
            ItemStack var2 = this.payment;
            this.payment = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
        if (p_70299_1_ == 0)
        {
            this.payment = p_70299_2_;
        }
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.field_146008_p : "container.beacon";
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName()
    {
        return this.field_146008_p != null && this.field_146008_p.length() > 0;
    }

    public void func_145999_a(String p_145999_1_)
    {
        this.field_146008_p = p_145999_1_;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 1;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return p_94041_2_.getItem() == Items.emerald || p_94041_2_.getItem() == Items.diamond || p_94041_2_.getItem() == Items.gold_ingot || p_94041_2_.getItem() == Items.iron_ingot;
    }
}
