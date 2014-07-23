package org.theya.sustain.block;

import org.theya.sustain.Sustain;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockBioFluid extends BlockFluidClassic{

	public BlockBioFluid(int id) {
		super(id, Sustain.fluidBio, Sustain.materialBio);
		
		this.setCreativeTab(Sustain.sustainTab);
	}
	
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(Sustain.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}
