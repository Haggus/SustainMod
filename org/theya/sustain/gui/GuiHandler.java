package org.theya.sustain.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.theya.sustain.Sustain;
import org.theya.sustain.block.TileEntityElectricFurnace;
import org.theya.sustain.block.TileEntityQuartzFurnace;
import org.theya.sustain.block.TileEntityRecycler;
import org.theya.sustain.block.TileEntityWaterFilter;
import org.theya.sustain.block.TileEntityWaterTurbine;
import org.theya.sustain.block.TileEntityWindTurbine;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler{
	
	public GuiHandler() {
		NetworkRegistry.instance().registerGuiHandler(Sustain.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getBlockTileEntity(x, y ,z);
		
		if(entity != null) {
			switch(ID) {
			case Sustain.guiIdQuartzFurnace:
				if(entity instanceof TileEntityQuartzFurnace) {
					return new ContainerQuartzFurnace(player.inventory, (TileEntityQuartzFurnace) entity);
				}
				return null;
				
			case Sustain.guiIdElectricFurnace:
				if (entity instanceof TileEntityElectricFurnace) {
					return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace) entity);
				} 
				return null;
				
			case Sustain.guiIdWaterTurbine:
				if(entity instanceof TileEntityWaterTurbine) {
					return new ContainerWaterTurbine(player.inventory, (TileEntityWaterTurbine) entity);
				}
				return null;
				
			case Sustain.guiIdWindTurbine:
				if(entity instanceof TileEntityWindTurbine) {
					return new ContainerWindTurbine(player.inventory, (TileEntityWindTurbine) entity);
				}
				return null;
				
			case Sustain.guiIdWaterFilter:
				if(entity instanceof TileEntityWaterFilter) {
					return new ContainerWaterFilter(player.inventory, (TileEntityWaterFilter) entity);
				}
				return null;
				
			case Sustain.guiIdRecycler:
				if(entity instanceof TileEntityRecycler) {
					return new ContainerRecycler(player.inventory, (TileEntityRecycler) entity);
				}
				return null;
				
			default:
				return null;
			}
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getBlockTileEntity(x, y ,z);
		
		if(entity != null) {
			switch(ID) {
			case Sustain.guiIdQuartzFurnace:
				if(entity instanceof TileEntityQuartzFurnace) {
					return new GuiQuartzFurnace(player.inventory, (TileEntityQuartzFurnace) entity);
				}
				return null;
				
			case Sustain.guiIdElectricFurnace:
				if(entity instanceof TileEntityElectricFurnace) {
					return new GuiElectricFurnace(player.inventory, (TileEntityElectricFurnace) entity);
				} 
				return null;
				
			case Sustain.guiIdWaterTurbine:
				if(entity instanceof TileEntityWaterTurbine) {
					return new GuiWaterTurbine(player.inventory, (TileEntityWaterTurbine) entity);
				}
				return null;
				
			case Sustain.guiIdWindTurbine:
				if(entity instanceof TileEntityWindTurbine) {
					return new GuiWindTurbine(player.inventory, (TileEntityWindTurbine) entity);
				}
				return null;
				
			case Sustain.guiIdWaterFilter:
				if(entity instanceof TileEntityWaterFilter) {
					return new GuiWaterFilter(player.inventory, (TileEntityWaterFilter) entity);
				}
				return null;
				
			case Sustain.guiIdRecycler:
				if(entity instanceof TileEntityRecycler) {
					return new GuiRecycler(player.inventory, (TileEntityRecycler) entity);
				}
				return null;
				
			default:
				return null;
			}
		}
		
		return null;
	}

}
