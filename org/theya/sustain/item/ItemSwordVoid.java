package org.theya.sustain.item;

import org.theya.sustain.Sustain;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class ItemSwordVoid extends ItemSword{

	public ItemSwordVoid(int id, EnumToolMaterial toolMaterial) {
		super(id, toolMaterial);
		
		this.setCreativeTab(Sustain.sustainTab);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Sustain.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}
