package org.theya.sustain.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

public class PollutionHandler {

	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event) {
		if(event.entity instanceof EntityPlayer && PollutionBarData.get((EntityPlayer)event.entity) == null) {
			PollutionBarData.register((EntityPlayer)event.entity);
		}
		if(event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(PollutionBarData.POLLUTION_DATA_NAME) == null) {
			event.entity.registerExtendedProperties(PollutionBarData.POLLUTION_DATA_NAME, new PollutionBarData((EntityPlayer)event.entity));
		}
	}
	
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			PollutionBarData.get((EntityPlayer)event.entity).sync();
		}
	}
}
