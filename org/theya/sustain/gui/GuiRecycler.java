package org.theya.sustain.gui;

import org.lwjgl.opengl.GL11;
import org.theya.sustain.Sustain;
import org.theya.sustain.block.TileEntityRecycler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiRecycler extends GuiContainer{

public static final ResourceLocation texture = new ResourceLocation(Sustain.modid, "textures/gui/recycler.png");
	
	public TileEntityRecycler recyclerB;
	
	public GuiRecycler(InventoryPlayer invPlayer, TileEntityRecycler entity) {
		super(new ContainerRecycler(invPlayer, entity));
		
		this.recyclerB = entity;

		this.xSize = 176;
		this.ySize = 165;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2){
		String s = this.recyclerB.isInvNameLocalized() ? this.recyclerB.getInvName() : I18n.getString(this.recyclerB.getInvName());
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 5, 4210752);
	}
	
	public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().
		bindTexture(texture);
		
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int i1;

		if(this.recyclerB.hasPower()){
			i1 = this.recyclerB.getPowerRemainingScaled(45);
			this.drawTexturedModalRect(guiLeft + 8, guiTop + 53 - i1, 176, 62 - i1, 16, i1);
		}
		
		i1 = this.recyclerB.getCookProgressScaled(24);
		this.drawTexturedModalRect(guiLeft + 67, guiTop + 34, 176, 0, i1 + 1, 16);
	}
}
