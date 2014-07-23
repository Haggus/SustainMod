package org.theya.sustain.item;

import net.minecraft.client.renderer.texture.IconRegister;

import org.theya.sustain.Sustain;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBatteryPack extends ItemPowerStorage{

	public ItemBatteryPack(int id, int maxPower) {
		super(id);

		this.setMaxDamage(maxPower);
		this.maxStackSize = 1;
		this.setCreativeTab(Sustain.sustainTab);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Sustain.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}

