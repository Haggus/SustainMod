package org.theya.sustain.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import org.theya.sustain.block.TileEntityQuartzFurnace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerQuartzFurnace extends Container {
	
	private TileEntityQuartzFurnace quartzFurnace;
	
	public int lastBurnTime;
	public int lastItemBurnTime;
	public int lastCookTime;
	
	public ContainerQuartzFurnace(InventoryPlayer inventoryPlayer, TileEntityQuartzFurnace tileentity) {
		this.quartzFurnace = tileentity;
		
		this.addSlotToContainer(new Slot(tileentity, 0, 56, 17));
		this.addSlotToContainer(new Slot(tileentity, 1, 56, 53));
		this.addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tileentity, 2, 116, 35));
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i*9 + 9, 8 + j*18, 84 + i*18));
			}
		}
		
		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i*18, 142));
		}
	}
	
	public void addCraftingToCrafters(ICrafting icrafting) {
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.quartzFurnace.cookTime);
		icrafting.sendProgressBarUpdate(this, 1, this.quartzFurnace.burnTime);
		icrafting.sendProgressBarUpdate(this, 2, this.quartzFurnace.currentItemBurnTime);
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for(int i=0; i<this.crafters.size(); i++) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			
			if(this.lastCookTime != this.quartzFurnace.cookTime) {
				icrafting.sendProgressBarUpdate(this, 0, this.quartzFurnace.cookTime);
			}
			
			if(this.lastBurnTime != this.quartzFurnace.burnTime) {
				icrafting.sendProgressBarUpdate(this, 1, this.quartzFurnace.burnTime);
			}
			
			if(this.lastItemBurnTime != this.quartzFurnace.currentItemBurnTime) {
				icrafting.sendProgressBarUpdate(this, 2, this.quartzFurnace.currentItemBurnTime);
			}
		}
		
		this.lastCookTime = this.quartzFurnace.cookTime;
		this.lastBurnTime = this.quartzFurnace.burnTime;
		this.lastItemBurnTime = this.quartzFurnace.currentItemBurnTime;
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int value) {
		if(slot == 0) this.quartzFurnace.cookTime = value;
		if(slot == 1) this.quartzFurnace.burnTime = value;
		if(slot == 2) this.quartzFurnace.currentItemBurnTime = value;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlotNumber) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(clickedSlotNumber);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(clickedSlotNumber == 2) {
				if(!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			} else if(clickedSlotNumber != 1 && clickedSlotNumber != 0) {
				if(FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
					if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				} else if(TileEntityQuartzFurnace.isItemFuel(itemstack1)) {
					if(!this.mergeItemStack(itemstack1, 1, 2, false)) {
						return null;
					}
				} else if(clickedSlotNumber >= 3 && clickedSlotNumber < 30) {
					if(!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return null;
					}
				} else if(clickedSlotNumber >= 30 && clickedSlotNumber < 39) {
					if(!this.mergeItemStack(itemstack1, 3, 29, false)) {
						return null;
					}
				}
			} else if(!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return null;
			}
			
			if(itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			} else {
				slot.onSlotChanged();
			}
			
			if(itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		
		return itemstack;
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.quartzFurnace.isUseableByPlayer(entityplayer);
	}

}
