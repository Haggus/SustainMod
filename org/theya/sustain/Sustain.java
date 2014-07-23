package org.theya.sustain;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.theya.sustain.block.BlockAluminumOre;
import org.theya.sustain.block.BlockBioFluid;
import org.theya.sustain.block.BlockElectricFurnace;
import org.theya.sustain.block.BlockPollutedWood;
import org.theya.sustain.block.BlockQuartzFurnace;
import org.theya.sustain.block.BlockRecycler;
import org.theya.sustain.block.BlockTrash;
import org.theya.sustain.block.BlockVoid;
import org.theya.sustain.block.BlockWaterFilter;
import org.theya.sustain.block.BlockWaterTurbine;
import org.theya.sustain.block.BlockWindTurbine;
import org.theya.sustain.block.TileEntityElectricFurnace;
import org.theya.sustain.block.TileEntityQuartzFurnace;
import org.theya.sustain.block.TileEntityRecycler;
import org.theya.sustain.block.TileEntityTrash;
import org.theya.sustain.block.TileEntityWaterFilter;
import org.theya.sustain.block.TileEntityWaterTurbine;
import org.theya.sustain.block.TileEntityWindTurbine;
import org.theya.sustain.gui.GuiHandler;
import org.theya.sustain.gui.PollutionBar;
import org.theya.sustain.gui.PollutionHandler;
import org.theya.sustain.item.ItemAluminumIngot;
import org.theya.sustain.item.ItemAxeVoid;
import org.theya.sustain.item.ItemBasic;
import org.theya.sustain.item.ItemBattery;
import org.theya.sustain.item.ItemBatteryPack;
import org.theya.sustain.item.ItemHoeVoid;
import org.theya.sustain.item.ItemPickVoid;
import org.theya.sustain.item.ItemShovelVoid;
import org.theya.sustain.item.ItemSwordVoid;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=Sustain.modid, name="Sustain", version="3.14.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={
		"SustainSync", "SustainPollution", "RecAdd", "RecRemove",
		"SustainTrash", "FurnacesAdd", "FurnacesRemove", 
		"SaplingsAdd", "SaplingsRemove", "WTurbineAdd",
		"WTurbineRmv", "WFilterAdd", "WFilterRmv",
		"ATurbineAdd", "ATurbineRmv", "SustainDestroy"
		}, packetHandler=PacketHandler.class)
public class Sustain {
	
	public static final String modid = "sustain";
	
	public static Block blockVoid;
	public static Block blockBioFluid;
	public static Block blockQuartzFurnaceIdle;
	public static Block blockQuartzFurnaceActive;
	public static Block blockPollutedWood;
	public static Block blockTrash;
	public static Block blockElectricFurnaceIdle;
	public static Block blockElectricFurnaceActive;
	public static Block blockWaterTurbine;
	public static Block blockWindTurbine;
	public static Block blockWaterFilter;
	public static Block blockRecycler;
	public static Block blockAluminumOre;
	
	public static Item itemCobblestoneNugget;
	public static Item itemAxeVoid;
	public static Item itemPickVoid;
	public static Item itemSwordVoid;
	public static Item itemShovelVoid;
	public static Item itemHoeVoid;
	public static Item itemBattery;
	public static Item itemBatteryPack;
	public static Item itemAluminumIngot;
	
	public static Fluid fluidBio;
	
	public static Material materialBio;
	
	public static final int idFluidBio = 687;
	
	public static final int guiIdQuartzFurnace = 0;
	public static final int guiIdElectricFurnace = 8;
	public static final int guiIdWaterTurbine = 7;
	public static final int guiIdWindTurbine = 6;
	public static final int guiIdWaterFilter = 5;
	public static final int guiIdRecycler = 4;
	
	public static EnumToolMaterial toolMaterialVoid;
	
	public static CreativeTabs sustainTab;
	
	public static Achievement achievementWaterFilter;
	public static Achievement achievementWaterTurbine;
	
	@Instance("sustain")
	public static Sustain instance;

	@SidedProxy(clientSide="org.theya.sustain.client.ClientProxy", serverSide="org.theya.sustain.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		sustainTab = new CreativeTabs("sustaintab") {
			@SideOnly(Side.CLIENT)
			public int getTabIconItemIndex() {
				return Sustain.blockVoid.blockID;
			}
		};
		
		fluidBio = new Fluid("liquidBio").setBlockID(idFluidBio).setUnlocalizedName("liquidBio");
		FluidRegistry.registerFluid(fluidBio);
		
		materialBio = new MaterialLiquid(MapColor.grassColor);
		
		toolMaterialVoid = EnumHelper.addToolMaterial("voidMaterialTool", 3, 2345, 9.0f, 3.5f, 15);
		
		blockVoid = new BlockVoid(1000, Material.ground).setUnlocalizedName("blockVoid");
		blockBioFluid = new BlockBioFluid(idFluidBio).setUnlocalizedName("fluidBio");
		blockQuartzFurnaceIdle = new BlockQuartzFurnace(710, false).setUnlocalizedName("quartzFurnaceIdle").setHardness(3.5f).setCreativeTab(this.sustainTab);
		blockQuartzFurnaceActive = new BlockQuartzFurnace(711, true).setUnlocalizedName("quartzFurnaceActive").setHardness(3.5f).setLightValue(0.9f);
		blockPollutedWood = new BlockPollutedWood(712, Material.wood).setUnlocalizedName("blockPollutedWood");
		blockTrash = new BlockTrash(713).setUnlocalizedName("blockTrash");
		blockElectricFurnaceIdle = new BlockElectricFurnace(910, false).setUnlocalizedName("electricFurnaceIdle").setHardness(3.7f).setCreativeTab(this.sustainTab);
		blockElectricFurnaceActive = new BlockElectricFurnace(911, true).setUnlocalizedName("electricFurnaceActive").setHardness(3.7f).setLightValue(0.9f);
		blockWaterTurbine = new BlockWaterTurbine(912).setUnlocalizedName("waterTurbine").setHardness(3.7f).setCreativeTab(this.sustainTab);
		blockWindTurbine = new BlockWindTurbine(913).setUnlocalizedName("windTurbine").setHardness(3.7f).setCreativeTab(this.sustainTab);
		blockWaterFilter = new BlockWaterFilter(914).setUnlocalizedName("waterFilter").setHardness(3.7f).setCreativeTab(this.sustainTab);
		blockRecycler = new BlockRecycler(915).setUnlocalizedName("recycler").setHardness(3.7f).setCreativeTab(this.sustainTab);
		blockAluminumOre = new BlockAluminumOre(916, Material.rock).setUnlocalizedName("blockAluminumOre");

		itemCobblestoneNugget = new ItemBasic(700).setUnlocalizedName("cobblestoneNugget");
		itemAxeVoid = new ItemAxeVoid(702, toolMaterialVoid).setUnlocalizedName("axeVoid");
		itemPickVoid = new ItemPickVoid(703, toolMaterialVoid).setUnlocalizedName("pickVoid");
		itemSwordVoid = new ItemSwordVoid(704, toolMaterialVoid).setUnlocalizedName("swordVoid");
		itemShovelVoid = new ItemShovelVoid(705, toolMaterialVoid).setUnlocalizedName("shovelVoid");
		itemHoeVoid = new ItemHoeVoid(706, toolMaterialVoid).setUnlocalizedName("hoeVoid");
		itemBattery = new ItemBattery(810, 1500).setUnlocalizedName("battery");
		itemBatteryPack = new ItemBatteryPack(811, 10000).setUnlocalizedName("batteryPack");
		itemAluminumIngot = new ItemAluminumIngot(812).setUnlocalizedName("ingotAluminum");
		
		GameRegistry.addRecipe(new ItemStack(itemCobblestoneNugget, 9), new Object[] {
			"X", 
			'X', Block.cobblestone
		});
		GameRegistry.addRecipe(new ItemStack(Block.cobblestone), new Object[] {
			"XXX",
			"XXX",
			"XXX",
			'X', itemCobblestoneNugget
		});
		
		GameRegistry.addRecipe(new ItemStack(blockWindTurbine), new Object[] {
			"BAB",
			"AIA",
			"BAB",
			'B', Block.blockIron,
			'A', itemAluminumIngot,
			'I', Item.ingotIron
		});
		GameRegistry.addRecipe(new ItemStack(blockWaterTurbine), new Object[] {
			"BAB",
			"AIA",
			"BAB",
			'B', Block.cobblestone,
			'A', itemAluminumIngot,
			'I', Item.ingotIron
		});
		GameRegistry.addRecipe(new ItemStack(blockWaterFilter), new Object[] {
			"BAB",
			"AIA",
			"BAB",
			'B', Block.blockIron,
			'A', itemAluminumIngot,
			'I', Block.coalBlock
		});
		GameRegistry.addRecipe(new ItemStack(blockElectricFurnaceIdle), new Object[] {
			"III",
			"IFI",
			"III",
			'I', Item.ingotIron,
			'F', Block.furnaceIdle
		});
		GameRegistry.addRecipe(new ItemStack(itemBattery, 1, itemBattery.getMaxDamage()), new Object[] {
			"XIX",
			"XRX",
			"XRX",
			'X', itemAluminumIngot,
			'R', Block.coalBlock,
			'I', Item.ingotIron
		});
		
		OreDictionary.registerOre("oreAluminum", Sustain.blockAluminumOre);
		GameRegistry.addSmelting(blockAluminumOre.blockID, new ItemStack(itemAluminumIngot), 0.2f);
		GameRegistry.addSmelting(itemCobblestoneNugget.itemID, new ItemStack(Block.cobblestone), 0.2f);
		GameRegistry.registerFuelHandler(new FuelHandler());
		
		GameRegistry.registerTileEntity(TileEntityQuartzFurnace.class, "QuartzFurnace");
		GameRegistry.registerTileEntity(TileEntityTrash.class, "Trash");
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "Electric Furnace");
		GameRegistry.registerTileEntity(TileEntityWaterTurbine.class, "Water Turbine");
		GameRegistry.registerTileEntity(TileEntityWindTurbine.class, "Wind Turbine");
		GameRegistry.registerTileEntity(TileEntityWaterFilter.class, "Water Filter");
		GameRegistry.registerTileEntity(TileEntityRecycler.class, "Recycler");
		
		NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandler());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		registerBlock(blockVoid, "Void");
		registerBlock(blockBioFluid, "Bio Fuel");
		registerBlock(blockQuartzFurnaceIdle, "Brick Furnace");
		registerBlock(blockQuartzFurnaceActive, "Brick Furnace Active");
		registerBlock(blockPollutedWood, "Polluted Wood");
		registerBlock(blockTrash, "Trash");
		registerBlock(blockElectricFurnaceIdle, "Electric Furnace");
		registerBlock(blockElectricFurnaceActive, "Electric Furnace");
		registerBlock(blockWaterTurbine, "Water Turbine");
		registerBlock(blockWindTurbine, "Wind Turbine");
		registerBlock(blockWaterFilter, "Water Filter");
		registerBlock(blockRecycler, "Recycler");
		registerBlock(blockAluminumOre, "Aluminum Ore");
		
		registerItem(itemCobblestoneNugget, "Cobblestone Nugget");
		registerItem(itemAxeVoid, "Void Axe");
		registerItem(itemPickVoid, "Void Pickaxe");
		registerItem(itemSwordVoid, "Void Sword");
		registerItem(itemShovelVoid, "Void Shovel");
		registerItem(itemHoeVoid, "Void Hoe");
		registerItem(itemBattery, "Battery");
		registerItem(itemBatteryPack, "Battery Pack");
		registerItem(itemAluminumIngot, "Aluminum Ingot");
		
		LanguageRegistry.instance().addStringLocalization("container.quartzFurnace", "Brick Furnace");
		LanguageRegistry.instance().addStringLocalization("container.electricFurnace", "Electric Furnace");
		LanguageRegistry.instance().addStringLocalization("container.waterTurbine", "Water Turbine");
		LanguageRegistry.instance().addStringLocalization("container.windTurbine", "Wind Turbine");
		LanguageRegistry.instance().addStringLocalization("container.waterFilter", "Water Filter");
		LanguageRegistry.instance().addStringLocalization("container.recycler", "Recycler");
		
		proxy.registerTrashRender();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new PollutionHandler());
		achievementWaterFilter = new Achievement(2001, "achievementWaterFilter", -3, -2, Sustain.blockWaterFilter, AchievementList.buildWorkBench).registerAchievement();
		this.addAchievementName("achievementWaterFilter", "Got Time Achieve!");
		this.addAchievementDesc("achievementWaterFilter", "You built a Time Machine!");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {			
			MinecraftForge.EVENT_BUS.register(new PollutionBar(Minecraft.getMinecraft()));
		}
	}
	
	public void registerBlock(Block block, String name) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName());
		LanguageRegistry.addName(block, name);
	}
	
	public void registerItem(Item item, String name) {
		GameRegistry.registerItem(item, item.getUnlocalizedName());
		LanguageRegistry.addName(item, name);
	}
	
	private void addAchievementName(String ach, String name)
	{
	        LanguageRegistry.instance().addStringLocalization("achievement." + ach, "en_US", name);
	}

	private void addAchievementDesc(String ach, String desc)
	{
	        LanguageRegistry.instance().addStringLocalization("achievement." + ach + ".desc", "en_US", desc);
	}
}
