package org.theya.sustain.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.theya.sustain.Sustain;
import org.theya.sustain.model.ModelTrash;

public class RendererTrash extends TileEntitySpecialRenderer {
	
	private static final ResourceLocation texture = new ResourceLocation(Sustain.modid, "textures/blocks/trash.png");
	
	private ModelTrash model;
	
	public RendererTrash() {
		this.model = new ModelTrash();
	}

	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5f, (float)y + 1.5f, (float)z + 0.5f);
		GL11.glRotatef(180, 0f, 0f, 1f);
		this.bindTexture(texture);
		GL11.glPushMatrix();
		this.model.renderModel(0.0625f);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
