package org.theya.sustain.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import org.theya.sustain.OreRecipes;
import org.theya.sustain.block.TileEntityWaterTurbine;
import org.theya.sustain.item.ItemPowerStorage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerWaterTurbine extends Container {
	
	private TileEntityWaterTurbine waterTurbine;

	public ContainerWaterTurbine(InventoryPlayer par1InventoryPlayer, TileEntityWaterTurbine par2TileEntityFurnace)
	{
		this.waterTurbine = par2TileEntityFurnace;
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 80, 35));
		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	public void addCraftingToCrafters(ICrafting par1ICrafting)
	{
		super.addCraftingToCrafters(par1ICrafting);
	}

	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.waterTurbine.isUseableByPlayer(par1EntityPlayer);
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedSlotNumber)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(clickedSlotNumber);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (clickedSlotNumber == 0)
			{
				if (!this.mergeItemStack(itemstack1, 1, 37, true))
				{
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (clickedSlotNumber != 0)
			{
				if(slot.getStack().getItem() instanceof ItemPowerStorage) {
					if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				}
			}
			else if (!this.mergeItemStack(itemstack1, 1, 37, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
		return itemstack;
	}
}
