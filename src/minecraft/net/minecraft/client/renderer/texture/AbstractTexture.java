package net.minecraft.client.renderer.texture;

import shadersmod.client.MultiTexID;
import shadersmod.client.ShadersTex;

public abstract class AbstractTexture implements ITextureObject {
	public int glTextureId = -1;
	public MultiTexID multiTex;
	private static final String __OBFID = "CL_00001047";

	public int getGlTextureId() {
		if (this.glTextureId == -1) {
			this.glTextureId = TextureUtil.glGenTextures();
		}

		return this.glTextureId;
	}

	public void deleteGlTexture() {
		ShadersTex.deleteTextures(this, this.glTextureId);

		if (this.glTextureId != -1) {
			TextureUtil.deleteTexture(this.glTextureId);
			this.glTextureId = -1;
		}
	}

	public MultiTexID getMultiTexID() {
		return ShadersTex.getMultiTexID(this);
	}
}
