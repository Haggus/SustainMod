package org.theya.sustain.block;

import org.theya.sustain.item.ItemPowerStorage;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWaterFilter extends TileEntity implements ISidedInventory{

	private static final int[] slots_top = new int[] {0};

	private ItemStack[] slots = new ItemStack[1];

	public int power = 1;
	
	public boolean isFiltering = false;

	private String waterFilterName;

	public int getSizeInventory()
	{
    	return this.slots.length;
	}

	public ItemStack getStackInSlot(int par1)
	{
    	return this.slots[par1];
	}

	public ItemStack decrStackSize(int par1, int par2)
	{
    	if (this.slots[par1] != null)
    	{
        		ItemStack itemstack;

        		if (this.slots[par1].stackSize <= par2)
       		{
            		itemstack = this.slots[par1];
            		this.slots[par1] = null;
           			return itemstack;
        		}
        		else
        		{
            		itemstack = this.slots[par1].splitStack(par2);

            		if (this.slots[par1].stackSize == 0)
            		{
                			this.slots[par1] = null;
           			}

            		return itemstack;
        		}
    	}
    	else
    	{
        		return null;
    	}
	}

	public ItemStack getStackInSlotOnClosing(int par1)
	{
    	if (this.slots[par1] != null)
    	{
        		ItemStack itemstack = this.slots[par1];
        		this.slots[par1] = null;
        		return itemstack;
    	}else{
        		return null;
    	}
	}

	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
    	this.slots[par1] = par2ItemStack;

    	if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
    	{
    		par2ItemStack.stackSize = this.getInventoryStackLimit();
    	}
	}

	public String getInvName()
	{
    	return this.isInvNameLocalized() ? this.waterFilterName : "container.waterFilter";
	}

	public boolean isInvNameLocalized()
	{
    	return this.waterFilterName != null && this.waterFilterName.length() > 0;
	}

	public void setGuiDisplayName(String par1Str)
	{
    	this.waterFilterName = par1Str;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
    	super.readFromNBT(par1NBTTagCompound);
    	NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
    	this.slots = new ItemStack[this.getSizeInventory()];

    	for (int i = 0; i < nbttaglist.tagCount(); ++i)
    	{
        		NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
        		byte b0 = nbttagcompound1.getByte("Slot");

        		if (b0 >= 0 && b0 < this.slots.length)
        		{
            		this.slots[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        		}
    	}

    	if (par1NBTTagCompound.hasKey("CustomName"))
    	{
        	this.waterFilterName = par1NBTTagCompound.getString("CustomName");
    	}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
    	super.writeToNBT(par1NBTTagCompound);
    	NBTTagList nbttaglist = new NBTTagList();

    	for (int i = 0; i < this.slots.length; ++i)
    	{	
        		if (this.slots[i] != null)
        		{
            		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            		nbttagcompound1.setByte("Slot", (byte)i);
            		this.slots[i].writeToNBT(nbttagcompound1);
            		nbttaglist.appendTag(nbttagcompound1);
        		}
    	}

    	par1NBTTagCompound.setTag("Items", nbttaglist);

    	if (this.isInvNameLocalized())
    	{
        		par1NBTTagCompound.setString("CustomName", this.waterFilterName);
    	}
	}

	public int getInventoryStackLimit()
	{
    	return 64;
	}

	public boolean hasPower()
	{
    	return this.power > 0;
	}
	
	public boolean hasWaterAround() {
		boolean temp = true;
		if(this.worldObj.getBlockId(xCoord + 1, yCoord, zCoord) != Block.waterStill.blockID && this.worldObj.getBlockId(xCoord + 1, yCoord, zCoord) != Block.waterMoving.blockID) {
			//System.out.println("NO WATER FRONT");
			temp = false;
		}
		if(this.worldObj.getBlockId(xCoord - 1, yCoord, zCoord) != Block.waterStill.blockID && this.worldObj.getBlockId(xCoord - 1, yCoord, zCoord) != Block.waterMoving.blockID) {
			//System.out.println("NO WATER BACK");
			temp = false;
		}
		if(this.worldObj.getBlockId(xCoord, yCoord, zCoord + 1) != Block.waterStill.blockID && this.worldObj.getBlockId(xCoord, yCoord, zCoord + 1) != Block.waterMoving.blockID) {
			//System.out.println("NO WATER LEFT");
			temp = false;
		}
		if(this.worldObj.getBlockId(xCoord, yCoord, zCoord - 1) != Block.waterStill.blockID && this.worldObj.getBlockId(xCoord, yCoord, zCoord - 1) != Block.waterMoving.blockID) {
			//System.out.println("NO WATER RIGHT");
			temp = false;
		}
		return temp;
	}

	public void updateEntity(){
    	if (!this.worldObj.isRemote){
    		if(isItemPowerStorage() && this.slots[0].getItemDamage() >= 0 && this.hasPower() && this.slots[0].getItemDamage() < this.slots[0].getMaxDamage()) {
    			this.slots[0].setItemDamage(this.slots[0].getItemDamage()+1);
    			isFiltering = true;
    		} else {
    			isFiltering = false;
    		}
    		if(hasWaterAround()) {
    			this.power = 1;
    		} else {
    			this.power = 0;
    		}
    	}
	}
	
	public boolean isFilteringWater() {
		if(hasWaterAround() && isFiltering) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isItemPowerStorage() {
		if(this.slots[0] != null) {
			return this.slots[0].getItem() instanceof ItemPowerStorage;
		} else {
			return false;
		}
	}

	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
    	return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}	

	public void openChest() {}

	public void closeChest() {}

	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
	{
		if(this.isItemPowerStorage()) {
			return true;
		} else {
			return false;
		}
	}

	public int[] getAccessibleSlotsFromSide(int par1)
	{
		return slots_top;
    	//return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
	}

	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
	{
	      return this.isItemValidForSlot(par1, par2ItemStack);
	}

	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3)
	{
   	 		return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
	}
}
