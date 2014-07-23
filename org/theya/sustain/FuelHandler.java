package org.theya.sustain;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		
		if(fuel.itemID == Sustain.blockVoid.blockID) {
			return 200;
		}
		
		if(fuel.itemID == Sustain.itemCobblestoneNugget.itemID) {
			return 9999;
		}
		
		if(fuel.itemID == Block.blockDiamond.blockID) {
			return 5;
		}
		
		if(fuel.itemID == Item.slimeBall.itemID) {
			return 1;
		}
		
		if(fuel.itemID == Block.dirt.blockID) {
			return fuel.stackSize;
		}
		
		return 0;
	}

}
