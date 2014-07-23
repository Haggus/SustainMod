package org.theya.sustain;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
		netHandler.getPlayer().addChatMessage("Welcome to " + EnumChatFormatting.GREEN.toString() + "SustainMod" + 
				EnumChatFormatting.WHITE.toString() + ".");
		netHandler.getPlayer().addChatMessage("Please read the " + EnumChatFormatting.DARK_PURPLE.toString() + "Manual " +
				EnumChatFormatting.WHITE.toString() + "first!");
//		netHandler.getPlayer().addChatMessage("Welcome " + netHandler.getPlayer().getEntityName() + " to this world");
//		netHandler.getPlayer().addChatMessage(EnumChatFormatting.GOLD + "Welcome " + netHandler.getPlayer().getEntityName() + " to this world");
//		netHandler.getPlayer().addChatMessage(EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD.toString() + "Welcome " + netHandler.getPlayer().getEntityName() + " to this world");
//		netHandler.getPlayer().addChatMessage(EnumChatFormatting.GREEN.toString() + EnumChatFormatting.ITALIC.toString() + "Welcome " + netHandler.getPlayer().getEntityName() + " to this world");
//		netHandler.getPlayer().addChatMessage(EnumChatFormatting.BLUE.toString() + EnumChatFormatting.UNDERLINE.toString() + "Welcome " + netHandler.getPlayer().getEntityName() + " to this world");
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
	}

}
