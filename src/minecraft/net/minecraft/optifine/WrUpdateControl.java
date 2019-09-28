package net.minecraft.optifine;

public class WrUpdateControl implements IWrUpdateControl
{
    public boolean hasForge;
    public int renderPass;

    public WrUpdateControl()
    {
        this.hasForge = Reflector.ForgeHooksClient.exists();
        this.renderPass = 0;
    }

    public void resume() {}

    public void pause() {}

    public void setRenderPass(int renderPass)
    {
        this.renderPass = renderPass;
    }
}
