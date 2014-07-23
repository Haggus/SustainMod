package org.theya.sustain.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.lwjgl.opengl.GL11;
import org.theya.sustain.Sustain;
import org.theya.sustain.util.Position;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class PollutionBar extends Gui {
	
	private Minecraft mc;
	
	private int soilPollutionFlux = 0;
	private int airPollutionFlux = 0;
	private int waterPollutionFlux = 0;
	
	private boolean justDied = true;
	
	private static Random random = new Random();

	public PollutionBar(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	@ForgeSubscribe
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			EntityPlayer playa = (EntityPlayer)event.entity;
			playa.inventory.clearInventory(-1, -1);
			System.out.println("Player dies, destory da MACHINAS!");
			sendDestroyPacket();
			justDied = true;
		}
	}
	
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			System.out.println("PLAYA ENTERING, CHECKING FOR RECENT DEATHS...!");
			if(justDied) {
				System.out.println("Player respawned! GIVE HIM DA IDEMZ!");
				EntityPlayer playa = (EntityPlayer)event.entity;
				if(!playa.inventory.hasItem(Sustain.itemBatteryPack.itemID)) {
					//ITEMS
					ItemStack sustainManual = new ItemStack(Item.writableBook);
					NBTTagList bookPages = new NBTTagList("pages");
					bookPages.appendTag(new NBTTagString("1", "\n\n\n\n\n       Sustain Mod\n          Manual"));
					bookPages.appendTag(new NBTTagString("2", "This world is polluted and it is your job to save it!\n\nOn the top left you see 3 different types of pollution: Air, Soil and Water\n\nYou have to keep all of them as low as possible, to save this world!"));
					bookPages.appendTag(new NBTTagString("3", "This mod provides machines and electricity to help you reach your goals!\n\nYou have to generate power using turbines, which then can be used to power your recycler or furnaces!"));
					bookPages.appendTag(new NBTTagString("4", "However BE CAREFUL!\n\nToo much machinery can harm the environment and increase the pollution!\n\nTry to find the balance."));
					bookPages.appendTag(new NBTTagString("5", "Air pollution:\n\nIncreases by using furnaces.\n\nDecreases by planting trees."));
					bookPages.appendTag(new NBTTagString("6", "Water pollution:\n\nIncreases by using water turbines, which generate power.\n\nDecreases by using water, filters which clean the water."));
					bookPages.appendTag(new NBTTagString("7", "Soil pollution:\n\nIt is an average between Air and Water pollution. It takes a long time to bring it down. Make sure you keep and eye on this one!"));
					bookPages.appendTag(new NBTTagString("10", "This is an early development version of Sustain Mod. It may contain bugs and errors. This is NOT a final product."));
					
					sustainManual.setTagInfo("pages", bookPages);
					sustainManual.setTagInfo("author", new NBTTagString("author", "SustainMod"));
					sustainManual.setTagInfo("title", new NBTTagString("title", "Sustain Manual"));
					sustainManual.itemID = Item.writtenBook.itemID;
					
					playa.inventory.addItemStackToInventory(sustainManual);
					playa.inventory.addItemStackToInventory(new ItemStack(Sustain.blockRecycler));
					playa.inventory.addItemStackToInventory(new ItemStack(Sustain.itemBatteryPack));
					
					justDied = false;
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerPlaceBlock(PlayerInteractEvent event) {
		MovingObjectPosition m = this.mc.objectMouseOver;
		if(m != null) {			
			EntityPlayer p = event.entityPlayer;
			if(event.action == Action.RIGHT_CLICK_BLOCK) {
				if(p.inventory.getCurrentItem() != null) {					
					ItemStack s = p.inventory.getCurrentItem();
					//
					//		PLACING FURNACE
					//
					if(!p.worldObj.isRemote && s.itemID == Block.furnaceIdle.blockID) {
						Position temp = new Position(m.blockX, m.blockY, m.blockZ);
						sendPacketToArray(temp, "FurnacesAdd");
					}
					if(!p.worldObj.isRemote && s.itemID == Sustain.blockElectricFurnaceIdle.blockID) {
						Position temp = new Position(m.blockX, m.blockY, m.blockZ);
						sendPacketToArray(temp, "FurnacesAdd");
					}
					//
					//		PLACING SAPLING / TREE
					//
					if(!p.worldObj.isRemote && s.itemID == Block.sapling.blockID) {
						Position temp = new Position(m.blockX, m.blockY, m.blockZ);
						sendPacketToArray(temp, "SaplingsAdd");
					}
					//
					//		PLACING WATER MACHINES
					//
					if(!p.worldObj.isRemote && s.itemID == Sustain.blockWaterTurbine.blockID) {
						Position temp = new Position(m.blockX, m.blockY, m.blockZ);
						sendPacketToArray(temp, "WTurbineAdd");
					}
					if(!p.worldObj.isRemote && s.itemID == Sustain.blockWaterFilter.blockID) {
						Position temp = new Position(m.blockX, m.blockY, m.blockZ);
						sendPacketToArray(temp, "WFilterAdd");
					}
					//
					//		PLACING AIR TURIBNES
					//
					if(!p.worldObj.isRemote && s.itemID == Sustain.blockWindTurbine.blockID) {
						Position temp = new Position(m.blockX, m.blockY, m.blockZ);
						sendPacketToArray(temp, "ATurbineAdd");
					}
					//
					//		PLACING RECYCLER
					//
					if(!p.worldObj.isRemote && s.itemID == Sustain.blockRecycler.blockID) {
						Position temp = new Position(m.blockX, m.blockY, m.blockZ);
						sendPacketToArray(temp, "RecAdd");
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerDestroyBlock(BreakEvent event) {
		Block b = event.block;
		//
		//		FURNACES
		//
		if(b == Block.furnaceIdle || b == Block.furnaceBurning || b == Sustain.blockElectricFurnaceActive || b == Sustain.blockElectricFurnaceIdle) {
			sendPacketToArray(new Position(0, 0, 0), "FurnacesRemove");
		}
		//
		//		TREES / SAPLINGS
		//
		if(b == Block.sapling || b == Block.wood) {
			sendPacketToArray(new Position(0, 0, 0), "SaplingsRemove");
		}
		//
		//		TRASH BLOCKS
		//
		if(b == Sustain.blockTrash) {
			sendTrashUpdate(false);
		}
		//
		//		MACHINES
		//
		if(b == Sustain.blockWaterTurbine) {
			sendPacketToArray(new Position(0, 0, 0), "WTurbineRmv");
		}
		if(b == Sustain.blockWaterFilter) {
			sendPacketToArray(new Position(0, 0, 0), "WFilterRmv");
		}
		if(b == Sustain.blockWindTurbine) {
			sendPacketToArray(new Position(0, 0, 0), "ATurbineRmv");
		}
		if(b == Sustain.blockRecycler){
			sendPacketToArray(new Position(0, 0, 0), "RecRemove");
		}
	}
	
	public void sendPacketToArray(Position pos, String channel) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try {
			outputStream.writeInt(pos.getX());
			outputStream.writeInt(pos.getY());
			outputStream.writeInt(pos.getZ());
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = channel;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			EntityClientPlayerMP player = (EntityClientPlayerMP) this.mc.getNetHandler().getPlayer();
			player.sendQueue.addToSendQueue(packet);
		}
		if(side == Side.SERVER) {
			PacketDispatcher.sendPacketToServer(packet);
		}
	}
	
	public void sendTrashUpdate(boolean isIncrease) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4);
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try {
			outputStream.writeBoolean(isIncrease);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SustainTrash";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			EntityClientPlayerMP player = (EntityClientPlayerMP) this.mc.getNetHandler().getPlayer();
			player.sendQueue.addToSendQueue(packet);
		}
		if(side == Side.SERVER) {
			System.out.println("decreaseBlock");
			//EntityPlayerMP player = (EntityPlayerMP) this.mc.getNetHandler().getPlayer();
			PacketDispatcher.sendPacketToServer(packet);
		}
	}
	
	public void sendPollutionUpdate() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4);
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try {
			outputStream.writeBoolean(true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SustainPollution";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			EntityClientPlayerMP player = (EntityClientPlayerMP) this.mc.getNetHandler().getPlayer();
			player.sendQueue.addToSendQueue(packet);
		}
		if(side == Side.SERVER) {
			PacketDispatcher.sendPacketToServer(packet);
		}
	}
	
	public void sendDestroyPacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4);
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try {
			outputStream.writeBoolean(true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SustainDestroy";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			EntityClientPlayerMP player = (EntityClientPlayerMP) this.mc.getNetHandler().getPlayer();
			player.sendQueue.addToSendQueue(packet);
		}
		if(side == Side.SERVER) {
			PacketDispatcher.sendPacketToServer(packet);
		}
	}
	
	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderPollutionBar(RenderGameOverlayEvent event) {
		if(event.isCancelable() || event.type != ElementType.EXPERIENCE) {
			return;
		}
		
		sendPollutionUpdate();
		PollutionBarData props = PollutionBarData.get((EntityPlayer)this.mc.getNetHandler().getPlayer());

		airPollutionFlux = props.getAirPollution() / 2;
		soilPollutionFlux = props.getSoilPollution() / 2;
		waterPollutionFlux = props.getWaterPollution() / 2;
		
		//BAR POSITION
		int xPos = 3;
		int yPos = 3;
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.mc.renderEngine.bindTexture(new ResourceLocation(Sustain.modid, "textures/gui/pollutionBar.png"));

		//AIR POLLUTION
		if(airPollutionFlux >= 0) {
			drawTexturedModalRect(xPos + 113, yPos + 1 + 6, 50, 50, airPollutionFlux, 4);
		} else {
			drawTexturedModalRect(xPos + (113 + (airPollutionFlux)), yPos + 1 + 6, 50 - Math.abs(airPollutionFlux), 50, Math.abs(airPollutionFlux), 4); //bar
		}
		
		//SOIL POLLUTION
		if(soilPollutionFlux >= 0) {
			drawTexturedModalRect(xPos + 113, yPos + 10 + 6, 50, 50, soilPollutionFlux, 4);
		} else {
			drawTexturedModalRect(xPos + (113 + (soilPollutionFlux)), yPos + 10 + 6, 50 - Math.abs(soilPollutionFlux), 50, Math.abs(soilPollutionFlux), 4); //bar
		}

		//WATER POLLUTION
		if(waterPollutionFlux >= 0) {
			drawTexturedModalRect(xPos + 113, yPos + 19 + 6, 50, 50, waterPollutionFlux, 4);
		} else {
			drawTexturedModalRect(xPos + (113 + (waterPollutionFlux)), yPos + 19 + 6, 50 - Math.abs(waterPollutionFlux), 50, Math.abs(waterPollutionFlux), 4); //bar
		}
		
		//FRAME
		drawTexturedModalRect(xPos, yPos, 0, 0, 168, 50);
		
		//BADGE
		drawTexturedModalRect(xPos + 4, yPos + 4, 0, 54, 42, 42);
	}
}
