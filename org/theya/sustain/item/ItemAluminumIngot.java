package org.theya.sustain.item;

import org.theya.sustain.Sustain;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemAluminumIngot extends Item{

	public ItemAluminumIngot(int id) {
		super(id);
		
		this.setCreativeTab(Sustain.sustainTab);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Sustain.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}
