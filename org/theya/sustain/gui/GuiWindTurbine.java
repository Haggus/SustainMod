package org.theya.sustain.gui;

import org.lwjgl.opengl.GL11;
import org.theya.sustain.Sustain;
import org.theya.sustain.block.TileEntityWindTurbine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiWindTurbine extends GuiContainer{
	public static final ResourceLocation texture = new ResourceLocation(Sustain.modid, "textures/gui/waterTurbine.png");
	
	public TileEntityWindTurbine windTurbine;
	
	public GuiWindTurbine(InventoryPlayer invPlayer, TileEntityWindTurbine entity) {
		super(new ContainerWindTurbine(invPlayer, entity));
		
		this.windTurbine = entity;

		this.xSize = 176;
		this.ySize = 165;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2){
		String s = this.windTurbine.isInvNameLocalized() ? this.windTurbine.getInvName() : I18n.getString(this.windTurbine.getInvName());
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 5, 4210752);
	}
	
	public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().
		bindTexture(texture);
		
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
