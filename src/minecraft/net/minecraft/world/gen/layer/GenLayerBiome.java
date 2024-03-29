package net.minecraft.world.gen.layer;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerBiome extends GenLayer
{
    private BiomeGenBase[] field_151623_c;
    private BiomeGenBase[] field_151621_d;
    private BiomeGenBase[] field_151622_e;
    private BiomeGenBase[] field_151620_f;
    private static final String __OBFID = "CL_00000555";

    public GenLayerBiome(long p_i2122_1_, GenLayer p_i2122_3_, WorldType p_i2122_4_)
    {
        super(p_i2122_1_);
        this.field_151623_c = new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna, BiomeGenBase.savanna, BiomeGenBase.plains};
        this.field_151621_d = new BiomeGenBase[] {BiomeGenBase.forest, BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.plains, BiomeGenBase.birchForest, BiomeGenBase.swampland};
        this.field_151622_e = new BiomeGenBase[] {BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.taiga, BiomeGenBase.plains};
        this.field_151620_f = new BiomeGenBase[] {BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.coldTaiga};
        this.parent = p_i2122_3_;

        if (p_i2122_4_ == WorldType.DEFAULT_1_1)
        {
            this.field_151623_c = new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga};
        }
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_)
    {
        int[] var5 = this.parent.getInts(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
        int[] var6 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

        for (int var7 = 0; var7 < p_75904_4_; ++var7)
        {
            for (int var8 = 0; var8 < p_75904_3_; ++var8)
            {
                this.initChunkSeed((long)(var8 + p_75904_1_), (long)(var7 + p_75904_2_));
                int var9 = var5[var8 + var7 * p_75904_3_];
                int var10 = (var9 & 3840) >> 8;
                var9 &= -3841;

                if (isBiomeOceanic(var9))
                {
                    var6[var8 + var7 * p_75904_3_] = var9;
                }
                else if (var9 == BiomeGenBase.mushroomIsland.biomeID)
                {
                    var6[var8 + var7 * p_75904_3_] = var9;
                }
                else if (var9 == 1)
                {
                    if (var10 > 0)
                    {
                        if (this.nextInt(3) == 0)
                        {
                            var6[var8 + var7 * p_75904_3_] = BiomeGenBase.mesaPlateau.biomeID;
                        }
                        else
                        {
                            var6[var8 + var7 * p_75904_3_] = BiomeGenBase.mesaPlateau_F.biomeID;
                        }
                    }
                    else
                    {
                        var6[var8 + var7 * p_75904_3_] = this.field_151623_c[this.nextInt(this.field_151623_c.length)].biomeID;
                    }
                }
                else if (var9 == 2)
                {
                    if (var10 > 0)
                    {
                        var6[var8 + var7 * p_75904_3_] = BiomeGenBase.jungle.biomeID;
                    }
                    else
                    {
                        var6[var8 + var7 * p_75904_3_] = this.field_151621_d[this.nextInt(this.field_151621_d.length)].biomeID;
                    }
                }
                else if (var9 == 3)
                {
                    if (var10 > 0)
                    {
                        var6[var8 + var7 * p_75904_3_] = BiomeGenBase.megaTaiga.biomeID;
                    }
                    else
                    {
                        var6[var8 + var7 * p_75904_3_] = this.field_151622_e[this.nextInt(this.field_151622_e.length)].biomeID;
                    }
                }
                else if (var9 == 4)
                {
                    var6[var8 + var7 * p_75904_3_] = this.field_151620_f[this.nextInt(this.field_151620_f.length)].biomeID;
                }
                else
                {
                    var6[var8 + var7 * p_75904_3_] = BiomeGenBase.mushroomIsland.biomeID;
                }
            }
        }

        return var6;
    }
}
