package org.theya.sustain.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.theya.sustain.Sustain;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockQuartzFurnace extends BlockContainer{
	
	private final boolean isActive;
	
	private Random rand = new Random();
	
	@SideOnly(Side.CLIENT)
	private Icon iconFront;
	
	private static boolean keepInventory;

	public BlockQuartzFurnace(int id, boolean isActive) {
		super(id, Material.rock);
		this.isActive = isActive;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(Sustain.modid + ":" + "quartzFurnace_side");
		this.iconFront = iconRegister.registerIcon(Sustain.modid + ":" + (this.isActive ? "quartzFurnace_active" : "quartzFurnace_idle"));
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		return metadata == 0 && side == 3 ? this.iconFront : (side == metadata ? this.iconFront : this.blockIcon);
	}
	
	public int idDropped(int par1, Random random, int par3) {
		return Sustain.blockQuartzFurnaceIdle.blockID;
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}
	
	public void setDefaultDirection(World world, int x, int y, int z) {
		if(!world.isRemote) {
			int l = world.getBlockId(x, y, z - 1);
			int il = world.getBlockId(x, y, z + 1);
			int jl = world.getBlockId(x - 1, y, z);
			int kl = world.getBlockId(x + 1, y, z);
			byte b0 = 3;
			
			if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[il]) {
				b0 = 3;
			}
			
			if(Block.opaqueCubeLookup[il] && !Block.opaqueCubeLookup[l]) {
				b0 = 2;
			}
			
			if(Block.opaqueCubeLookup[kl] && !Block.opaqueCubeLookup[jl]) {
				b0 = 5;
			}
			
			if(Block.opaqueCubeLookup[jl] && !Block.opaqueCubeLookup[kl]) {
				b0 = 4;
			}
			
			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			FMLNetworkHandler.openGui(player, Sustain.instance, Sustain.guiIdQuartzFurnace, world, x, y, z);
		}
		return true;
	}

	public TileEntity createNewTileEntity(World world) {
		return new TileEntityQuartzFurnace();
	}
	
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if(this.isActive) {
			int direction = world.getBlockMetadata(x, y, z);
			
			float x1 = (float)x + 0.5f;
			float y1 = (float)y + random.nextFloat();
			float z1 = (float)z + 0.5f;
			
			float f = 0.52f;
			float f1 = random.nextFloat() * 0.6f - 0.3f;
			
			if(direction == 4) {
				world.spawnParticle("smoke", (double)(x1 - f), (double)(y1), (double)(z1 + f1), 0d, 0d, 0d);
				world.spawnParticle("flame", (double)(x1 - f), (double)(y1), (double)(z1 + f1), 0d, 0d, 0d);
			} else if(direction == 5) {
				world.spawnParticle("smoke", (double)(x1 + f), (double)(y1), (double)(z1 + f1), 0d, 0d, 0d);
				world.spawnParticle("flame", (double)(x1 + f), (double)(y1), (double)(z1 + f1), 0d, 0d, 0d);
			} else if(direction == 2) {
				world.spawnParticle("smoke", (double)(x1 + f1), (double)(y1), (double)(z1 - f), 0d, 0d, 0d);
				world.spawnParticle("flame", (double)(x1 + f1), (double)(y1), (double)(z1 - f), 0d, 0d, 0d);
			} else if(direction == 3) {
				world.spawnParticle("smoke", (double)(x1 + f1), (double)(y1), (double)(z1 + f), 0d, 0d, 0d);
				world.spawnParticle("flame", (double)(x1 + f1), (double)(y1), (double)(z1 + f), 0d, 0d, 0d);
			}
		}
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
		int l = MathHelper.floor_double((double)(entityLivingBase.rotationYaw * 4.0f / 360.0f) + 0.5d) & 3;
		
		if(l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		
		if(l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		
		if(l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		
		if(l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		
		if(itemStack.hasDisplayName()) {
			((TileEntityQuartzFurnace)world.getBlockTileEntity(x, y, z)).setGuiDisplayName(itemStack.getDisplayName());
		}
	}

	public static void updateQuartzFurnaceBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		TileEntity tileentity = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
		keepInventory = true;
		
		if(active) {
			worldObj.setBlock(xCoord, yCoord, zCoord, Sustain.blockQuartzFurnaceActive.blockID);
		} else {
			worldObj.setBlock(xCoord, yCoord, zCoord, Sustain.blockQuartzFurnaceIdle.blockID);
		}
		
		keepInventory = false;
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		
		if(tileentity != null) {
			tileentity.validate();
			worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
	}
	
	public void breakBlock(World world, int x, int y, int z, int oldBlockID, int oldMetaData) {
		if(!keepInventory) {
			TileEntityQuartzFurnace tileentity = (TileEntityQuartzFurnace)world.getBlockTileEntity(x, y, z);
			
			if(tileentity != null) {
				for(int i=0; i<tileentity.getSizeInventory(); i++) {
					ItemStack itemstack = tileentity.getStackInSlot(i);
					
					if(itemstack != null) {
						float f = this.rand.nextFloat() * 0.8f + 0.1f;
						float f1 = this.rand.nextFloat() * 0.8f + 0.1f;
						float f2 = this.rand.nextFloat() * 0.8f + 0.1f;
						
						while(itemstack.stackSize > 0) {
							int j = this.rand.nextInt(21) + 10;
							
							if(j > itemstack.stackSize) {
								j = itemstack.stackSize;
							}
							
							itemstack.stackSize -= j;
							
							EntityItem item = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.itemID, j, itemstack.getItemDamage()));
							
							if(itemstack.hasTagCompound()) {
								item.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
							}
							
							float f3 = 0.05f;
							item.motionX = (double)((float)this.rand.nextGaussian() * f3);
							item.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2f);
							item.motionZ = (double)((float)this.rand.nextGaussian() * f3);
							
							world.spawnEntityInWorld(item);
						}
					}
				}
				
				world.func_96440_m(x, y, z, oldBlockID);
			}
		}
		
		super.breakBlock(world, x, y, z, oldBlockID, oldMetaData);
	}
	
	public boolean hasComparatorInputOverride() {
		return true;
	}
	
	public int getComparatorInputOverride(World world, int x, int y, int z, int i) {
		return Container.calcRedstoneFromInventory((IInventory)world.getBlockTileEntity(x, y, z));
	}
	
	@SideOnly(Side.CLIENT)
	public int idPicked(World world, int x, int y, int z) {
		return Sustain.blockQuartzFurnaceIdle.blockID;
	}
}
