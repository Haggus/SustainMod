package org.theya.sustain.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import org.theya.sustain.Sustain;
import org.theya.sustain.block.TileEntityWaterFilter;
import org.theya.sustain.block.TileEntityWaterTurbine;
import org.theya.sustain.util.Position;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PollutionBarData implements IExtendedEntityProperties{
	
	public static final String POLLUTION_DATA_NAME = "PollutionBarData";
	
	private static final int FURNACE_POLLUTION = 40;
	private static final int TREE_POLLUTION = -5;
	private static final int WTURBINE_POLLUTION = 40;
	private static final int WFILTER_POLLUTION = -40;
	
	public static final int TRASH_WATCHER_ID = 20;
	public static final int AIR_POLLUTION_ID = 19;
	public static final int SOIL_POLLUTION_ID = 15;
	public static final int WATER_POLLUTION_ID = 14;
	
	private final EntityPlayer player;
	
	private int airToHit = 0;
	private int airChange = 0;
	private int airSpeed = 120;
	
	private int soilToHit = 0;
	private int soilChange = 0;
	private int soilSpeed = 960;
	
	private int waterToHit = 0;
	private int waterChange = 0;
	private int waterSpeed = 240;

	private int maxTrashAmount = 30;
	private int trashChange = 0;
	private int trashSpeed = 120;
	
	private int changeCounter = 0;
	private int changeSpeed = 960;
	
	private int eventCounter = 0;
	private int eventSpeed = 36000; // <<< every 10 minutes
	private int poisonChange = 0;
	private int poisonSpeed = 2400;
	
	private ArrayList<Position> furnaces = new ArrayList<Position>();
	private ArrayList<Position> saplings = new ArrayList<Position>();
	private ArrayList<Position> water_turbines = new ArrayList<Position>();
	private ArrayList<Position> water_filters = new ArrayList<Position>();
	private ArrayList<Position> air_turbines = new ArrayList<Position>();
	private ArrayList<Position> recyclers = new ArrayList<Position>();
	
	private static Random random = new Random();
	
	public PollutionBarData(EntityPlayer player) {
		this.player = player;
		this.player.getDataWatcher().addObject(TRASH_WATCHER_ID, 0);
		System.out.println("Pollution Reset <<<<<<<<<<<<<<");
		int a = random.nextInt(30) + 20;
		int s = random.nextInt(30) + 20;
		int w = random.nextInt(30) + 20;
		if(!player.worldObj.isRemote) {			
			System.out.println("AIR RANDOM >>> " + a);
			System.out.println("SOL RANDOM >>> " + s);
			System.out.println("WTR RANDOM >>> " + w);
		}
		this.player.getDataWatcher().addObject(AIR_POLLUTION_ID, a);
		this.player.getDataWatcher().addObject(SOIL_POLLUTION_ID, s);
		this.player.getDataWatcher().addObject(WATER_POLLUTION_ID, w);
	}
	
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(PollutionBarData.POLLUTION_DATA_NAME, new PollutionBarData(player));
	}
	
	public static final PollutionBarData get(EntityPlayer player) {
		return (PollutionBarData)player.getExtendedProperties(POLLUTION_DATA_NAME);
	}
	
	public byte[] writeToByteArray(ArrayList<Position> array) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		for(Position element : array) {
			try{				
				out.writeInt(element.getX());
				out.writeInt(element.getY());
				out.writeInt(element.getZ());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}
	
	public void writeToArrayList(byte[] temp, ArrayList<Position> array) {
		ByteArrayInputStream bais = new ByteArrayInputStream(temp);
		DataInputStream in = new DataInputStream(bais);
		try {			
			while(in.available() > 0) {
				int tempX = in.readInt();
				int tempY = in.readInt();
				int tempZ = in.readInt();
				array.add(new Position(tempX, tempY, tempZ));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		if(!this.player.worldObj.isRemote) {
			NBTTagCompound properties = new NBTTagCompound();
			
			properties.setInteger("CurrentTrash", this.player.getDataWatcher().getWatchableObjectInt(TRASH_WATCHER_ID));
			byte[] test = writeToByteArray(furnaces);
			properties.setByteArray("CurrentFurnaces", test);
			byte[] test2 = writeToByteArray(saplings);
			properties.setByteArray("CurrentSaplings", test2);
			byte[] test3 = writeToByteArray(water_turbines);
			properties.setByteArray("CurrentWaterTurbines", test3);
			byte[] test4 = writeToByteArray(water_filters);
			properties.setByteArray("CurrentWaterFilters", test4);
			byte[] test5 = writeToByteArray(air_turbines);
			properties.setByteArray("CurrentAirTurbines", test5);
			byte[] test6 = writeToByteArray(recyclers);
			properties.setByteArray("CurrentRecyclers", test6);
			properties.setInteger("CurrentAirPollution", this.player.getDataWatcher().getWatchableObjectInt(AIR_POLLUTION_ID));
			properties.setInteger("CurrentSoilPollution", this.player.getDataWatcher().getWatchableObjectInt(SOIL_POLLUTION_ID));
			properties.setInteger("CurrentWaterPollution", this.player.getDataWatcher().getWatchableObjectInt(WATER_POLLUTION_ID));
			properties.setInteger("CurrentAirToHit", airToHit);
			properties.setInteger("CurrentSoilToHit", soilToHit);
			properties.setInteger("CurrentWaterToHit", waterToHit);
			
			System.out.println("[NBT-SAVE] Trash amount: " + properties.getInteger("CurrentTrash"));
			for(int i=0; i<furnaces.size(); i++) {
				System.out.println("[SAVING FURNACE] @>> Mx: " + furnaces.get(i).getX() + ", My: " + furnaces.get(i).getY() + ", Mz: " + furnaces.get(i).getZ());
			}
			for(int i=0; i<saplings.size(); i++) {
				System.out.println("[SAVING SAPLING] @>> Mx: " + saplings.get(i).getX() + ", My: " + saplings.get(i).getY() + ", Mz: " + saplings.get(i).getZ());
			}
			for(int i=0; i<water_turbines.size(); i++) {
				System.out.println("[SAVING WATER TURBINES] @>> Mx: " + water_turbines.get(i).getX() + ", My: " + water_turbines.get(i).getY() + ", Mz: " + water_turbines.get(i).getZ());
			}
			for(int i=0; i<water_filters.size(); i++) {
				System.out.println("[SAVING WATER FILTERS] @>> Mx: " + water_filters.get(i).getX() + ", My: " + water_filters.get(i).getY() + ", Mz: " + water_filters.get(i).getZ());
			}
			for(int i=0; i<air_turbines.size(); i++) {
				System.out.println("[SAVING AIR TURBINES] @>> Mx: " + air_turbines.get(i).getX() + ", My: " + air_turbines.get(i).getY() + ", Mz: " + air_turbines.get(i).getZ());
			}
			for(int i=0; i<recyclers.size(); i++) {
				System.out.println("[SAVING RECYCLERS] @>> Mx: " + recyclers.get(i).getX() + ", My: " + recyclers.get(i).getY() + ", Mz: " + recyclers.get(i).getZ());
			}
			System.out.println("[SAVING HITS] To HIT: " + airToHit + ", " + soilToHit + ", " + waterToHit);
			
			compound.setTag(POLLUTION_DATA_NAME, properties);
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound)compound.getTag(POLLUTION_DATA_NAME);
		
		this.player.getDataWatcher().updateObject(TRASH_WATCHER_ID, properties.getInteger("CurrentTrash"));
		byte[] test = properties.getByteArray("CurrentFurnaces");
		writeToArrayList(test, furnaces);
		byte[] test2 = properties.getByteArray("CurrentSaplings");
		writeToArrayList(test2, saplings);
		byte[] test3 = properties.getByteArray("CurrentWaterTurbines");
		writeToArrayList(test3, water_turbines);
		byte[] test4 = properties.getByteArray("CurrentWaterFilters");
		writeToArrayList(test4, water_filters);
		byte[] test5 = properties.getByteArray("CurrentAirTurbines");
		writeToArrayList(test5, air_turbines);
		byte[] test6 = properties.getByteArray("CurrentRecyclers");
		writeToArrayList(test6, recyclers);
		this.player.getDataWatcher().updateObject(AIR_POLLUTION_ID, properties.getInteger("CurrentAirPollution"));
		this.player.getDataWatcher().updateObject(SOIL_POLLUTION_ID, properties.getInteger("CurrentSoilPollution"));
		this.player.getDataWatcher().updateObject(WATER_POLLUTION_ID, properties.getInteger("CurrentWaterPollution"));
		airToHit = properties.getInteger("CurrentAirToHit");
		soilToHit = properties.getInteger("CurrentSoilToHit");
		waterToHit = properties.getInteger("CurrentWaterToHit");
		
		System.out.println("[NBT-READ] Trash amount: " + properties.getInteger("CurrentTrash"));
		for(int i=0; i<furnaces.size(); i++) {
			System.out.println("FURNACE AT >> Mx: " + furnaces.get(i).getX() + ", My: " + furnaces.get(i).getY() + ", Mz: " + furnaces.get(i).getZ());
		}
		for(int i=0; i<saplings.size(); i++) {
			System.out.println("SAPLING AT >> Mx: " + saplings.get(i).getX() + ", My: " + saplings.get(i).getY() + ", Mz: " + saplings.get(i).getZ());
		}
		for(int i=0; i<water_turbines.size(); i++) {
			System.out.println("WATER TURBINE AT >> Mx: " + water_turbines.get(i).getX() + ", My: " + water_turbines.get(i).getY() + ", Mz: " + water_turbines.get(i).getZ());
		}
		for(int i=0; i<water_filters.size(); i++) {
			System.out.println("WATER FILTER AT >> Mx: " + water_filters.get(i).getX() + ", My: " + water_filters.get(i).getY() + ", Mz: " + water_filters.get(i).getZ());
		}
		for(int i=0; i<air_turbines.size(); i++) {
			System.out.println("AIR TURBINE AT >> Mx: " + air_turbines.get(i).getX() + ", My: " + air_turbines.get(i).getY() + ", Mz: " + air_turbines.get(i).getZ());
		}
		for(int i=0; i<recyclers.size(); i++) {
			System.out.println("AIR TURBINE AT >> Mx: " + recyclers.get(i).getX() + ", My: " + recyclers.get(i).getY() + ", Mz: " + recyclers.get(i).getZ());
		}
		System.out.println("[LOADING HITS] HITS: " + airToHit + ", " + soilToHit + ", " + waterToHit);
	}
	
	public final void sync() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(12); // <<< FOR EVERY VAR ADD 4
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try {
			outputStream.writeInt(this.maxTrashAmount);
			outputStream.writeInt(furnaces.size());
			outputStream.writeInt(saplings.size());
			outputStream.writeInt(water_turbines.size());
			outputStream.writeInt(water_filters.size());
			outputStream.writeInt(air_turbines.size());
			outputStream.writeInt(recyclers.size());
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload("SustainSync", bos.toByteArray());
		
		if(!player.worldObj.isRemote) {
			EntityPlayerMP player1 = (EntityPlayerMP) player;
			PacketDispatcher.sendPacketToPlayer(packet, (Player)player1);
		}
	}
	
	public void generateTrash() {
		if(getTrash() < maxTrashAmount) {
			trashChange++;
			if(trashChange == trashSpeed) {
				trashChange = 0;
				
				int radius = 60;
				int diameter = radius * 2;
				
				int px = (int) (player.posX + random.nextInt(diameter) - radius);
				int py = (int) (player.posY + 10);
				int pz = (int) (player.posZ + random.nextInt(diameter) - radius);
				
				//System.out.println("Player at: " + px + ", " + py + ", " + pz);
				
				if(player.worldObj.getBlockId(px, py, pz) == 0) {
					boolean isBlock = false;
					while(!isBlock) {
						py--;
						if(player.worldObj.getBlockId(px, py, pz) != 0) {
							if(player.worldObj.getBlockId(px, py, pz) == Sustain.blockTrash.blockID) {
								break;
							}
							if(player.worldObj.getBlockId(px, py, pz) == Block.snow.blockID) {
								isBlock = true;
								player.worldObj.setBlock(px, py, pz, Sustain.blockTrash.blockID);
								increaseTrash();
							}
							isBlock = true;
							py++;
							player.worldObj.setBlock(px, py, pz, Sustain.blockTrash.blockID);
							increaseTrash();
						}
					}
				}
			}
		}
	}
	
	public void worldEvent() {
		eventCounter++;
		if(eventCounter >= eventSpeed) {
			eventCounter = 0;
			System.out.println("Event Trigger!");
			//air pollution
			int temp = 0;
			player.addChatMessage(EnumChatFormatting.RED.toString() + "[----BREAKING NEWS!----]");
			switch(random.nextInt(3)) {
			case 0: // << reactor meltdown
				player.addChatMessage(EnumChatFormatting.GRAY.toString() + "Nuclear reactor exploded.");
				player.addChatMessage(EnumChatFormatting.GRAY.toString() + "Radioactive material released into the atmosphere");
				player.addChatMessage(EnumChatFormatting.WHITE.toString() + "Air pollution has increased");
				temp = player.getDataWatcher().getWatchableObjectInt(AIR_POLLUTION_ID);
				temp += 20;
				setPollution(temp, 1);
				break;
			case 1:
				player.addChatMessage(EnumChatFormatting.GRAY.toString() + "Wildfires started accidentally by campers");
				player.addChatMessage(EnumChatFormatting.GRAY.toString() + "Thousands acres of forest and vegetation burned.");
				player.addChatMessage(EnumChatFormatting.GREEN.toString() + "Soil pollution has increased");
				temp = player.getDataWatcher().getWatchableObjectInt(SOIL_POLLUTION_ID);
				temp += 20;
				setPollution(temp, 2);
				break;
			case 2: // << oil spill
				player.addChatMessage(EnumChatFormatting.GRAY.toString() + "Fuel barge collided with a coral reef.");
				player.addChatMessage(EnumChatFormatting.GRAY.toString() + "Few million gallons of oil spilled polluting the coastline");
				player.addChatMessage(EnumChatFormatting.AQUA.toString() + "Water pollution has increased");
				temp = player.getDataWatcher().getWatchableObjectInt(WATER_POLLUTION_ID);
				temp += 20;
				setPollution(temp, 3);
				break;
			default:
				System.out.println("Something went wrong!");
			}
			player.addChatMessage(EnumChatFormatting.RED.toString() + "[----------------------]");
		}
	}
	
	public void worldPollute() {
		changeCounter++;
		if(changeCounter >= changeSpeed) {
			changeCounter = 0;
			
			int radius = 20;
			int diameter = radius * 2;

			int px = (int) (player.posX + (random.nextInt(diameter) - radius));
			int py = (int) (player.posY + 10); // <<< count starts from 10 block above the player
			int pz = (int) (player.posZ + (random.nextInt(diameter) - radius));
			
			if(player.worldObj.getBlockId(px, py, pz) == 0) {
				boolean isBlock = false;
				while(!isBlock) {
					py--;
					if(player.worldObj.getBlockId(px, py, pz) != 0) {
						isBlock = true;
						polluteBlock(px, py, pz);
					}
				}
			} else {
				if(player.worldObj.getBlockId(px, py, pz) == Block.wood.blockID) {
					polluteBlock(px, py, pz);
				}
				if(player.worldObj.getBlockId(px, py, pz) == Sustain.blockPollutedWood.blockID) {
					System.out.println("Polluted Block HIT!");
					boolean isWood = false;
					while(!isWood) {
						py--;
						if(player.worldObj.getBlockId(px, py, pz) == Block.wood.blockID) {
							System.out.println("Polluting block below!");
							isWood = true;
							polluteBlock(px, py, pz);
						} else if(player.worldObj.getBlockId(px, py, pz) == Sustain.blockPollutedWood.blockID) {
							continue;
						} else {
							break;
						}
					}
				}
				boolean isAir = false;
				while(!isAir) {
					py++;
					if(player.worldObj.getBlockId(px, py, pz) == 0) {
						isAir = true;
						py--;
						polluteBlock(px, py, pz);
					}
				}
			}
		}
	}
	
	public void polluteBlock(int x, int y, int z) {
		int blockid = player.worldObj.getBlockId(x, y, z);
		if(blockid == Block.grass.blockID) {
			player.worldObj.setBlock(x, y, z, Block.dirt.blockID);
		} else if(blockid == Block.leaves.blockID) {
			player.worldObj.setBlockToAir(x, y, z);
		} else if(blockid == Block.wood.blockID) {
			player.worldObj.setBlock(x, y, z, Sustain.blockPollutedWood.blockID);
		} else if(blockid == Block.tallGrass.blockID) {
			player.worldObj.setBlockToAir(x, y, z);
		} else if(blockid == Block.plantRed.blockID) {
			player.worldObj.setBlockToAir(x, y, z);
		} else if(blockid == Block.plantYellow.blockID) {
			player.worldObj.setBlockToAir(x, y, z);
		} else if(blockid == Block.mushroomBrown.blockID) {
			player.worldObj.setBlockToAir(x, y, z);
		} else if(blockid == Block.mushroomRed.blockID) {
			player.worldObj.setBlockToAir(x, y, z);
		}
	}
	
	public void updatePollution() {
		int countSmeltingFurnaces = 0;
		int countGrownTrees = 0;
		int countWorkingTurbines = 0;
		int countWorkingFilters = 0;
		if(furnaces.size() > 0) {
			for(int i=0; i<furnaces.size(); i++) {
				Position temp = new Position(furnaces.get(i).getX(), furnaces.get(i).getY(), furnaces.get(i).getZ());
				if(isSmelting(temp)) {
					countSmeltingFurnaces++;
				}
			}
		}
		if(saplings.size() > 0) {
			for(int i=0; i<saplings.size(); i++) {
				Position temp = new Position(saplings.get(i).getX(), saplings.get(i).getY(), saplings.get(i).getZ());
				if(doesTreeExist(temp)) {
					countGrownTrees++;
				}
			}
		}
		if(water_turbines.size() > 0) {
			for(int i=0; i<water_turbines.size(); i++) {
				Position temp = new Position(water_turbines.get(i).getX(), water_turbines.get(i).getY(), water_turbines.get(i).getZ());
				if(isGeneratingPower(temp)) {
					countWorkingTurbines++;
				}
			}
		}
		if(water_filters.size() > 0) {
			for(int i=0; i<water_filters.size(); i++) {
				Position temp = new Position(water_filters.get(i).getX(), water_filters.get(i).getY(), water_filters.get(i).getZ());
				if(isFiltering(temp)) {
					countWorkingFilters++;
				}
			}
		}
		
		airToHit = (countSmeltingFurnaces * FURNACE_POLLUTION) + (countGrownTrees * TREE_POLLUTION);
		waterToHit = (countWorkingTurbines * WTURBINE_POLLUTION) + (countWorkingFilters * WFILTER_POLLUTION);
		soilToHit = (airToHit + waterToHit) / 2;
		
		int temp = 0;
		airChange++;
		if(airChange == airSpeed) {
			airChange = 0;
			temp = player.getDataWatcher().getWatchableObjectInt(AIR_POLLUTION_ID);
			if(airToHit != 0) {				
				if(airToHit > temp) {
					temp++;
					setPollution(temp, 1);
				} else if(airToHit < temp) {
					temp--;
					setPollution(temp, 1);
				}
			}
		}
		soilChange++;
		if(soilChange == soilSpeed) {
			soilChange = 0;
			temp = player.getDataWatcher().getWatchableObjectInt(SOIL_POLLUTION_ID);
			if(soilToHit != 0) {				
				if(soilToHit > temp) {
					temp++;
					setPollution(temp, 2);
				} else if(soilToHit < temp) {
					temp--;
					setPollution(temp, 2);
				}
			}
		}
		waterChange++;
		if(waterChange == waterSpeed) {
			waterChange = 0;
			temp = player.getDataWatcher().getWatchableObjectInt(WATER_POLLUTION_ID);
			if(waterToHit != 0) {				
				if(waterToHit > temp) {
					temp++;
					setPollution(temp, 3);
				} else if(waterToHit < temp) {
					temp--;
					setPollution(temp, 3);
				}
			}
		}
		
		int aair = player.getDataWatcher().getWatchableObjectInt(AIR_POLLUTION_ID);
		if(aair > 0 && aair < 20) {
			changeSpeed = 240;
		} else if(aair > 20 && aair < 40) {
			changeSpeed = 180;
		} else if(aair > 40 && aair < 60) {
			changeSpeed = 120;
		} else if(aair > 60 && aair < 80) {
			player.addPotionEffect(new PotionEffect(Potion.weakness.id, 200));
			changeSpeed = 60;
		} else if(aair > 80) {
			player.addPotionEffect(new PotionEffect(Potion.weakness.id, 200));
			poisonChange++;
			if(poisonChange >= poisonSpeed) {
				poisonChange = 0;
				player.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
			}
			changeSpeed = 10;
		}
		
		worldEvent();
		generateTrash();
		worldPollute();
	}
	
	public void setPollution(int value, int type) {
		if(value < -100) {
			value = -100;
		} else if(value > 100) {
			value = 100;
		}
		if(type == 1) {
			this.player.getDataWatcher().updateObject(AIR_POLLUTION_ID, value);
		} else if(type == 2) {
			this.player.getDataWatcher().updateObject(SOIL_POLLUTION_ID, value);
		} else if(type == 3) {
			this.player.getDataWatcher().updateObject(WATER_POLLUTION_ID, value);
		}
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public void increaseTrash() {
		int temp = this.player.getDataWatcher().getWatchableObjectInt(TRASH_WATCHER_ID);
		temp++;
		this.player.getDataWatcher().updateObject(TRASH_WATCHER_ID, temp);
		System.out.println("INCREASE TRASH! Current value: " + getTrash());
		this.sync();
	}
	
	public void decreaseTrash() {
		int temp = this.player.getDataWatcher().getWatchableObjectInt(TRASH_WATCHER_ID);
		temp--;
		this.player.getDataWatcher().updateObject(TRASH_WATCHER_ID, temp);	
		//System.out.println("DECREASE TRASH!");
		this.sync();
	}
	
	public int getTrash() {
		return this.player.getDataWatcher().getWatchableObjectInt(TRASH_WATCHER_ID);
	}
	
	public boolean hasDuplicates(Position temp, ArrayList<Position> array) {
		for(int i=0; i<array.size(); i++) {
			if(array.get(i).getX() == temp.getX()) {
				if(array.get(i).getY() == temp.getY()) {
					if(array.get(i).getZ() == temp.getZ()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean doesFurnaceExist(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Block.furnaceIdle.blockID || player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Block.furnaceBurning.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean doesSaplingExist(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Block.sapling.blockID || player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Block.wood.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean doesTreeExist(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Block.wood.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean doesWaterTurbineExist(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Sustain.blockWaterTurbine.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean doesWaterFilterExist(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Sustain.blockWaterFilter.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean doesAirTurbineExist(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Sustain.blockWindTurbine.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean doesRecyclerExist(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Sustain.blockRecycler.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isSmelting(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Block.furnaceBurning.blockID) {
			return true;
		} else if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Sustain.blockElectricFurnaceActive.blockID) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFiltering(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Sustain.blockWaterFilter.blockID) {			
			TileEntityWaterFilter filter = (TileEntityWaterFilter)player.worldObj.getBlockTileEntity(temp.getX(), temp.getY(), temp.getZ());
			if(filter != null) {			
				if(filter.isFilteringWater()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean isGeneratingPower(Position temp) {
		if(player.worldObj.getBlockId(temp.getX(), temp.getY(), temp.getZ()) == Sustain.blockWaterTurbine.blockID) {			
			TileEntityWaterTurbine turbine = (TileEntityWaterTurbine)player.worldObj.getBlockTileEntity(temp.getX(), temp.getY(), temp.getZ());
			if(turbine != null) {			
				if(turbine.hasWaterAround()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void addFurnace(int x, int y, int z) {
		Position temp = new Position(x, y, z);
		if(hasDuplicates(temp, furnaces)) {
			System.out.println("SERVER, Duplicate Furnace Entry");
		} else {			
			furnaces.add(temp);
		}
	}
	
	public void removeEmptyFurnaces() {
		for(int i=0; i<furnaces.size(); i++) {
			Position pos = new Position(furnaces.get(i).getX(), furnaces.get(i).getY(), furnaces.get(i).getZ());
			if(doesFurnaceExist(pos)) {
				System.out.println("Furnace exists");
			} else {
				furnaces.remove(i);
			}
		}
	}
	
	public void addSapling(int x, int y, int z) {
		Position temp = new Position(x, y, z);
		if(hasDuplicates(temp, saplings)) {
			System.out.println("SERVER, Duplicate Sapling Entry");
		} else {
			saplings.add(temp);
		}
	}
	
	public void removeSaplings() {
		for(int i=0; i<saplings.size(); i++) {
			Position pos = new Position(saplings.get(i).getX(), saplings.get(i).getY(), saplings.get(i).getZ());
			if(doesSaplingExist(pos)) {
				System.out.println("Sapling exists");
			} else {
				saplings.remove(i);
			}
		}
	}
	
	public void addWaterTurbine(int x, int y, int z) {
		Position temp = new Position(x, y, z);
		if(hasDuplicates(temp, water_turbines)) {
			System.out.println("SERVER, Duplicate Water Turbine Entry");
		} else {
			water_turbines.add(temp);
		}
	}
	
	public void removeWaterTurbines() {
		for(int i=0; i<water_turbines.size(); i++) {
			Position pos = new Position(water_turbines.get(i).getX(), water_turbines.get(i).getY(), water_turbines.get(i).getZ());
			if(doesWaterTurbineExist(pos)) {
				System.out.println("Water Turbine exists");
			} else {
				water_turbines.remove(i);
			}
		}
	}
	
	public void addWaterFilter(int x, int y, int z) {
		Position temp = new Position(x, y, z);
		if(hasDuplicates(temp, water_filters)) {
			System.out.println("SERVER, Duplicate Water Filter Entry");
		} else {
			water_filters.add(temp);
		}
	}
	
	public void removeWaterFilters() {
		for(int i=0; i<water_filters.size(); i++) {
			Position pos = new Position(water_filters.get(i).getX(), water_filters.get(i).getY(), water_filters.get(i).getZ());
			if(doesWaterFilterExist(pos)) {
				System.out.println("Water Filter exists");
			} else {
				water_filters.remove(i);
			}
		}
	}
	
	public void addAirTurbine(int x, int y, int z) {
		Position temp = new Position(x, y, z);
		if(hasDuplicates(temp, air_turbines)) {
			System.out.println("SERVER, Duplicate Air Turbine Entry");
		} else {
			air_turbines.add(temp);
		}
	}
	
	public void removeAirTurbines() {
		for(int i=0; i<air_turbines.size(); i++) {
			Position pos = new Position(air_turbines.get(i).getX(), air_turbines.get(i).getY(), air_turbines.get(i).getZ());
			if(doesAirTurbineExist(pos)) {
				System.out.println("Air Turbine exists");
			} else {
				air_turbines.remove(i);
			}
		}
	}
	
	public void addRecycler(int x, int y, int z) {
		Position temp = new Position(x, y, z);
		if(hasDuplicates(temp, recyclers)) {
			System.out.println("SERVER, Duplicate Recycler Entry");
		} else {
			recyclers.add(temp);
		}
	}
	
	public void removeRecyclers() {
		for(int i=0; i<recyclers.size(); i++) {
			Position pos = new Position(recyclers.get(i).getX(), recyclers.get(i).getY(), recyclers.get(i).getZ());
			if(doesRecyclerExist(pos)) {
				System.out.println("Recycler exists");
			} else {
				recyclers.remove(i);
			}
		}
	}
	
	public void destroyMachines() {
		int replacementBlock = Block.cobblestoneMossy.blockID;
		for(int i=0; i<furnaces.size(); i++) {
			player.getEntityWorld().setBlock(furnaces.get(i).getX(), furnaces.get(i).getY(), furnaces.get(i).getZ(), replacementBlock);
		}
		for(int i=0; i<water_turbines.size(); i++) {
			player.getEntityWorld().setBlock(water_turbines.get(i).getX(), water_turbines.get(i).getY(), water_turbines.get(i).getZ(), replacementBlock);
		}
		for(int i=0; i<water_filters.size(); i++) {
			player.getEntityWorld().setBlock(water_filters.get(i).getX(), water_filters.get(i).getY(), water_filters.get(i).getZ(), replacementBlock);
		}
		for(int i=0; i<air_turbines.size(); i++) {
			player.getEntityWorld().setBlock(air_turbines.get(i).getX(), air_turbines.get(i).getY(), air_turbines.get(i).getZ(), replacementBlock);
		}
		for(int i=0; i<recyclers.size(); i++) {
			player.getEntityWorld().setBlock(recyclers.get(i).getX(), recyclers.get(i).getY(), recyclers.get(i).getZ(), replacementBlock);
		}
		furnaces.clear();
		saplings.clear();
		water_turbines.clear();
		water_filters.clear();
		air_turbines.clear();
		recyclers.clear();
		System.out.println("SKY NET IS DOWN!");
	}
	
	public int getAirPollution() {
		return this.player.getDataWatcher().getWatchableObjectInt(AIR_POLLUTION_ID);
	}
	
	public int getSoilPollution() {
		return this.player.getDataWatcher().getWatchableObjectInt(SOIL_POLLUTION_ID);
	}
	
	public int getWaterPollution() {
		return this.player.getDataWatcher().getWatchableObjectInt(WATER_POLLUTION_ID);
	}
}
