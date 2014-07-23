package org.theya.sustain.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

import org.theya.sustain.Sustain;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPollutedWood extends Block{

	public BlockPollutedWood(int id, Material material) {
		super(id, material);

		this.setHardness(2.0f);
		this.setResistance(5f);
		this.setStepSound(soundWoodFootstep);
		this.setCreativeTab(Sustain.sustainTab);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(Sustain.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}
