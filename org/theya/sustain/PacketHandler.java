package org.theya.sustain;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.theya.sustain.gui.PollutionBarData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler{
	
	private EntityPlayer player;

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		this.player = (EntityPlayer) player;
		if(packet.channel.equals("SustainSync")) {
			handleSync(packet, this.player);
		}
		if(packet.channel.equals("SustainPollution")) {
			handlePollution(packet, this.player);
		}
		if(packet.channel.equals("SustainTrash")) {
			handleTrash(packet, this.player);
		}
		if(packet.channel.equals("FurnacesAdd")) {
			handleFurnacesAdd(packet, this.player);
		}
		if(packet.channel.equals("FurnacesRemove")) {
			handleFurnacesRemove(packet, this.player);
		}
		if(packet.channel.equals("SaplingsAdd")) {
			handleSaplingsAdd(packet, this.player);
		}
		if(packet.channel.equals("SaplingsRemove")) {
			handleSaplingsRemove(packet, this.player);
		}
		if(packet.channel.equals("WTurbineAdd")) {
			handleWaterTurbineAdd(packet, this.player);
		}
		if(packet.channel.equals("WTurbineRmv")) {
			handleWaterTurbineRemove(packet, this.player);
		}
		if(packet.channel.equals("WFilterAdd")) {
			handleWaterFilterAdd(packet, this.player);
		}
		if(packet.channel.equals("WFilterRmv")) {
			handleWaterFilterRemove(packet, this.player);
		}
		if(packet.channel.equals("ATurbineAdd")) {
			handleAirTurbineAdd(packet, this.player);
		}
		if(packet.channel.equals("ATurbineRmv")) {
			handleAirTurbineRemove(packet, this.player);
		}
		if(packet.channel.equals("SustainDestroy")) {
			handleMachineDestroy(packet, this.player);
		}
		if(packet.channel.equals("RecAdd")) {
			handleRecyclerAdd(packet, this.player);
		}
		if(packet.channel.equals("RecRemove")) {
			handleRecyclerRemove(packet, this.player);
		}
	}
	
	private void handleTrash(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		boolean isIncrease = false;
		
		try {
			isIncrease = inputStream.readBoolean();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if(isIncrease) {
			props.increaseTrash();
		} else {
			props.decreaseTrash();
		}
	}
	
	private void handleSync(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			System.out.println("[PACKET] Max trash     : " + inputStream.readInt());
			System.out.println("[PACKET] Furnaces      : " + inputStream.readInt());
			System.out.println("[PACKET] Saplings      : " + inputStream.readInt());
			System.out.println("[PACKET] Water Turbines: " + inputStream.readInt());
			System.out.println("[PACKET] Water Filters : " + inputStream.readInt());
			System.out.println("[PACKET] Air Turbines  : " + inputStream.readInt());
			System.out.println("[PACKET] Recyclers     : " + inputStream.readInt());
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void handlePollution(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			//System.out.println("[POLLUTION] updating..." + inputStream.readBoolean());
			inputStream.readBoolean();
			props.updatePollution();
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void handleFurnacesAdd(Packet250CustomPayload packet, EntityPlayer player2) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.addFurnace(x, y, z);
			System.out.println("Packet Recieved, Furnace added at: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleFurnacesRemove(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.removeEmptyFurnaces();
			System.out.println("Packet Recieved, Furnace removed from: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleSaplingsAdd(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.addSapling(x, y, z);
			System.out.println("Packet Recieved, Sapling added at: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleSaplingsRemove(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.removeSaplings();
			System.out.println("Packet Recieved, Sapling removed from: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleWaterTurbineAdd(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.addWaterTurbine(x, y, z);
			System.out.println("Packet Recieved, Water Turbine added at: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleWaterTurbineRemove(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.removeWaterTurbines();
			System.out.println("Packet Recieved, Water Turbine removed from: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleWaterFilterAdd(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.addWaterFilter(x, y, z);
			System.out.println("Packet Recieved, Water Filter added at: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleWaterFilterRemove(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.removeWaterFilters();
			System.out.println("Packet Recieved, Water Filter removed from: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleAirTurbineAdd(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.addAirTurbine(x, y, z);
			System.out.println("Packet Recieved, Air Turbine added at: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleAirTurbineRemove(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.removeAirTurbines();
			System.out.println("Packet Recieved, Air Turbine removed from: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleMachineDestroy(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			inputStream.readBoolean();
			props.destroyMachines();
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void handleRecyclerAdd(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.addRecycler(x, y, z);
			System.out.println("Packet Recieved, Recycler added at: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleRecyclerRemove(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		PollutionBarData props = PollutionBarData.get((EntityPlayer)player);
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			props.removeRecyclers();
			System.out.println("Packet Recieved, Recycler removed from: " + x + ", " + y + ", " + z);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
