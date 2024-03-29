package net.minecraft.shader.client;

import net.minecraft.optifine.MatchBlock;

public class BlockAlias
{
    public int blockId;
    public MatchBlock[] matchBlocks;

    public BlockAlias(int blockId, MatchBlock[] matchBlocks)
    {
        this.blockId = blockId;
        this.matchBlocks = matchBlocks;
    }

    public int getBlockId()
    {
        return this.blockId;
    }

    public boolean matches(int id, int metadata)
    {
        for (int i = 0; i < this.matchBlocks.length; ++i)
        {
            MatchBlock matchBlock = this.matchBlocks[i];

            if (matchBlock.matches(id, metadata))
            {
                return true;
            }
        }

        return false;
    }

    public int[] getMatchBlockIds()
    {
        int[] blockIds = new int[this.matchBlocks.length];

        for (int i = 0; i < blockIds.length; ++i)
        {
            blockIds[i] = this.matchBlocks[i].getBlockId();
        }

        return blockIds;
    }
}
