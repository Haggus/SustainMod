package org.theya.sustain.gui;

import org.theya.sustain.OreRecipes;
import org.theya.sustain.Sustain;
import org.theya.sustain.block.TileEntityRecycler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerRecycler extends Container{

	private TileEntityRecycler recyclerB;
	private int lastCookTime;
	private int lastBurnTime;

	public ContainerRecycler(InventoryPlayer par1InventoryPlayer, TileEntityRecycler par2TileEntityFurnace)
	{
		this.recyclerB = par2TileEntityFurnace;
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 44, 35));
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 1, 8, 56));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 2, 98, 17));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 3, 116, 17));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 4, 134, 17));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 5, 98, 35));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 6, 116, 35));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 7, 134, 35));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 8, 98, 53));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 9, 116, 53));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 10, 134, 53));
		
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
		par1ICrafting.sendProgressBarUpdate(this, 0, this.recyclerB.cookTime);
		par1ICrafting.sendProgressBarUpdate(this, 1, this.recyclerB.power);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.lastCookTime != this.recyclerB.cookTime)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.recyclerB.cookTime);
			}

			if (this.lastBurnTime != this.recyclerB.power)
			{
				icrafting.sendProgressBarUpdate(this, 1, this.recyclerB.power);
			}
		}

		this.lastCookTime = this.recyclerB.cookTime;
		this.lastBurnTime = this.recyclerB.power;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			this.recyclerB.cookTime = par2;
		}

		if (par1 == 1)
		{
			this.recyclerB.power = par2;
		}
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.recyclerB.isUseableByPlayer(par1EntityPlayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 >= 2 && par2 < 11)
			{
				if (!this.mergeItemStack(itemstack1, 11, 47, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 != 1 && par2 != 0)
			{
				if (itemstack1.itemID == Sustain.blockTrash.blockID)
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if (TileEntityRecycler.isItemFuel(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, 1, 2, false))
					{
						return null;
					}
				}
				else if (par2 >= 11 && par2 < 38)
				{
					if (!this.mergeItemStack(itemstack1, 38, 47, false))
					{
						return null;
					}
				}
				else if (par2 >= 38 && par2 < 47 && !this.mergeItemStack(itemstack1, 11, 38, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 11, 47, false))
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
