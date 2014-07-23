package org.theya.sustain.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.theya.sustain.Sustain;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTrash extends BlockContainer{

	public BlockTrash(int id) {
		super(id, Material.ground);
		
		this.setBlockBounds(1f/16f, 0f, 1f/16f, 1f-1f/16f, 1f-1f/16f * 12, 1f-1f/16f);
		this.setCreativeTab(Sustain.sustainTab);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTrash();
	}
	
	public int getRenderType() {
		return -1;
	}
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		this.blockIcon = icon.registerIcon(Sustain.modid + ":" + "blockTrash");
	}
}
