package org.theya.sustain.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.theya.sustain.Sustain;
import org.theya.sustain.block.TileEntityQuartzFurnace;

public class GuiQuartzFurnace extends GuiContainer{
	
	public static final ResourceLocation texture = new ResourceLocation(Sustain.modid, "textures/gui/quartzFurnace.png");
	
	public TileEntityQuartzFurnace quartzFurnace;

	public GuiQuartzFurnace(InventoryPlayer inventoryPlayer, TileEntityQuartzFurnace entity) {
		super(new ContainerQuartzFurnace(inventoryPlayer, entity));
		
		this.quartzFurnace = entity;
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = this.quartzFurnace.isInvNameLocalized() ? this.quartzFurnace.getInvName() : I18n.getString(this.quartzFurnace.getInvName());
		
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(this.quartzFurnace.isBurning()) {
			int k = this.quartzFurnace.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(guiLeft + 56, guiTop + 36 + 12 - k, 176, 12 - k, 14, k + 2);
		}
		
		int k = this.quartzFurnace.getCookProgressScaled(24);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, k + 1, 16);
	}
}
