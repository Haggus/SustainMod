package org.theya.sustain.client;

import org.theya.sustain.CommonProxy;
import org.theya.sustain.Sustain;
import org.theya.sustain.block.TileEntityTrash;
import org.theya.sustain.renderer.RendererTrash;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy{

	@Override
	public void registerRenderers() {
		//sd
	}
	
	public void registerTrashRender() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrash.class, new RendererTrash());
		LanguageRegistry.instance().addStringLocalization(Sustain.sustainTab.getTranslatedTabLabel(), "Sustain");
	}
}
