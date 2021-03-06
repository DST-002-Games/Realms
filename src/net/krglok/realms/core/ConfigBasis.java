package net.krglok.realms.core;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.krglok.realms.Common.ItemGroup;
import net.krglok.realms.Common.ItemList;
import net.krglok.realms.builder.BuildPlanType;
import net.krglok.realms.data.ConfigInterface;
import net.krglok.realms.model.RealmModel;

/**
 * class for define static values (constants) 
 * 
 * @author Windu
 *
 */
public class ConfigBasis implements ConfigInterface
{
	protected static String PLUGIN_VER = "0.3.0";
	protected static final String CONFIG_SETTLEMENT_COUNTER = "settlementCounter";
	protected static final String CONFIG_REALM_COUNTER = "realmCounter";
	protected static final String CONFIG_PLUGIN_VER = "plugin_ver";
	public static final String CONFIG_PLUGIN_NAME = "plugin_name";
	public static final String PLUGIN_NAME = "Realms";
	public static final String NPC_0 = "NONE";
	public static final String NEW_SETTLEMENT = "New Settlement";

	
	public final static long dayNight = 24000; //2400 for test ; // serverTicks 
	public final static long RealmTick = 20L; 
	public final static long DelayTick = 200L; 
	public static long GameDay = ConfigBasis.dayNight / ConfigBasis.RealmTick; // 40; //
	
	public static double DISTANCE_1_DAY = 1200.0;
	
	public static double SETTLER_TAXE = 1.0;	/// Tax in money for every Settler , haedTax
	public static double SALES_TAX = 10.0;
	public static double TRADER_TAXE  = 5.0;	/// Tax for Trader 
	public static double TAVERNE_TAXE = 7.0;	/// Tax for Taverne will modified by settlerCount

	private static final int SETTLER_COUNT = 4; 
	public static final int BREEDING_DAYS = 67; // days from pregnant to child birth 
	public static final int BREEDING_DELAY = -70; 
	public static final int BREED_ADULT = 1264;  // days until child to adult
	public static final int ADULT_AGE = (14*12*30); // age of adult child
	public static final double ADULT_MONEY = 100.0; // money for children at change to adult    
	
	public static final int ENTERTAIN_SETTLERS = 50;	/// how many settlers happy thru Entertain

	public static final double MAX_HAPPINESS = 3.2;		// bei +-0.1 Modificator werden 32 day gebraucht bis 0  
	public static final double MIN_HAPPINESS = -3.2;		// bei +-0.1 Modificator werden 32 day gebraucht bis 0  
//	public static final double  FERTILITY = 1.5;   //  % satz // 07.01.2015 halbiert !!!
	public static final double  LETHALITY= 1.0;   //  % satz
	public static final double  HUNGER_LETHALITY = -6.0;   //  % satz
	public static final double  HUNGER_LETHALITY_CHILD = -4.0;   //  % satz
	public static final double  HUNGER_BEGGAR = -0.7;   //  % satz
	public static final int NORMAL_AGE = 60;	// Normales Alter ohn alters check
	
	public static final double NPC_HEALTH = 20.0;
	
	public static final int WAREHOUSE_CHEST_FACTOR = 9;
	public static final int TRADER_CHEST_FACTOR = 5;
	public static final int CHEST_STORE = 1728;
	public static final int WAREHOUSE_SPARE_STORE = 100;
	
	public static final int CHURCH_Settler = 2; 
	public static final int HALL_Settler = 5; 
	public static final int HALL_Power = 100; 
	public static final int TOWN_Power = 1500; 
	public static final int CITY_Power = 10000;
	public static final int METROPOL_Power = 30000;
	public static final int CASTLE_Power = 5000;
	public static final int CAMP_Power = 100; 
	public static final int LEHEN_1_Power = 100; 
	public static final int LEHEN_2_Power = 1500; 
	public static final int LEHEN_3_Power = 10000;
	public static final int LEHEN_4_Power = 30000;
	
	public static final int DONATION_POINT = 1;
	public static final int REQUIRED_POINT = 1;
	public static final int VALUABLE_POINT = 1;
	public static final int MONEY_POINT = 1;
	public static final int TRADE_POINT = 1;
	public static final int MEMBER_POINT = 1;
	
	public static final int REPUTATION_GOAL_ACQUIRE = 51;
	public static final int REPUTATION_GOAL_TRADER = 61;
	public static final int REPUTATION_GOAL_OWNER = 101;
	
	public static final double SETTLE_BUY_FACTOR = 0.90; 
	public static final double SETTLE_SELL_FACTOR = 1.10; 
	
	public static final int LEHEN_CREATE_POWER_1 = 500;
	public static final int LEHEN_CREATE_POWER_2 = 3000;
	public static final int LEHEN_CREATE_POWER_3 = 6000;
	public static final int LEHEN_CREATE_POWER_4 = 9000;

	public static final int TRADE_ORDER = 5;
	
	public static final int BUILDPLAN_GROUP_INTERN = 1;
	public static final int BUILDPLAN_GROUP_CONSTRUCT = 10;
	public static final int BUILDPLAN_GROUP_HOME = 100;
	public static final int BUILDPLAN_GROUP_PRODUCTION = 200;
	public static final int BUILDPLAN_GROUP_EQUIPMENT = 300;
	public static final int BUILDPLAN_GROUP_TRADE = 400;
	public static final int BUILDPLAN_GROUP_MILITARY = 500;
	public static final int BUILDPLAN_GROUP_ENTERTAIN = 600;
	public static final int BUILDPLAN_GROUP_NOBEL = 900;

	public static final String LINE = "=============================== ";

	public static byte getBlockId(Block block)
	{
		return getMaterialId(block.getType());
	}
	
	public static byte getMaterialId(Material mat)
	{
		switch (mat)
		{
		case STONE : return 1;
		case GRASS : return 2;
		case DIRT  : return 3;
		case COBBLESTONE : return 4;
		case WOOD  : return 5;
		case SAPLING : return 6;
		case BEDROCK : return 7;
		case WATER: return 8;
		case OBSIDIAN : return 49;
		case ICE : return 79;
		case MYCEL : return 110;
		case LAVA  : return 10;
		case GRAVEL: return 13;
		case LOG : return 17;
		case LOG_2: return 17;
		case SAND : return 12;
		case SANDSTONE : return 24;
		case CLAY: return 82;
		case MOSSY_COBBLESTONE : return 48;
		case IRON_BARDING: return 101;
		case FENCE: return 85;
		case WOOD_STAIRS: return 53;
		case COBBLESTONE_STAIRS: return 67;
		case BRICK: return 98;
		case WHEAT: return 59;
		case SANDSTONE_STAIRS: return (byte) 128;
		case CHEST : return (byte) 54;
		case WORKBENCH : return 58;
		case WOOD_DOOR: return 64;
		case SIGN: return 63;
		case WALL_SIGN : return 68;
		case BED : return 26;
		case BOOKSHELF : return 47;
		case TORCH : return 50;
		case WOOL : return 35;
		case SEEDS : return 59;
		case SOIL : return 60;
		case NETHER_WARTS : return 115;
		case SOUL_SAND : return 88;
		case NETHERRACK : return 87;
		case NETHER_BRICK: return 112;
		case NETHER_BRICK_STAIRS : return  114;
		case WOOD_STEP : return 126;
		case STEP : return 44;
		case MELON_BLOCK : return 103;
		case QUARTZ_ORE : return (byte) 153 ;
		case QUARTZ_BLOCK : return (byte) 155 ;
		case RED_MUSHROOM : return 40;
		case BROWN_MUSHROOM : return 39;
		
		default:
			return 0;
		}
	}

	
	public static char planValueToChar(byte value)
	{
		switch (value)
		{
		case 1 : return 'S';
		case 2 : return'G';
		case 3 : return'D';
		case 4 : return'C';
		case 5 : return 'W';
		case 6 : return 'i';
		case 7 : return'B';
		case 8 : return'w';
		case 12 : return's';
		case 13 : return'G';
		case 14 : return'g';
		case 15 : return'i';
		case 16 : return'c';
		case 17 : return'L';
		case 18 : return'l';
		case 24 : return'T';
		case 31 : return'g';
		case 35 : return 'O';
		case 56 : return'd';
		case 59 : return 'i';
		case 60 : return 's';
		case 110 : return'M';
		case 85 : return'#';
		case (byte) 254: return'.';
		case (byte) 255: return'X';
		case 54 : return '*';
		case 58 : return'*';
		case 64 : return '-';
		case 63 : return '-';
		case 68 : return '-';
		case 26 : return '=';
		case 47 : return '*';
		case 50 : return '*';
		default :
			return' ';
		}
		
	}

	public static Material getPlanMaterial(byte value)
	{
		switch (value)
		{
		case 1 : return Material.STONE;
		case 2 : return Material.GRASS;
		case 3 : return Material.DIRT;
		case 4 : return Material.COBBLESTONE;
		case 5 : return Material.WOOD;
		case 6 : return Material.SAPLING;
		case 7: return Material.BEDROCK;
		case 8 : return Material.WATER;
		case 12 : return Material.SAND;
		case 13 : return Material.GRAVEL;
		case 14 : return Material.GOLD_ORE;
		case 15 : return Material.IRON_ORE;
		case 16 : return Material.COAL_ORE;
		case 17 : return Material.LOG;
		case 18 : return Material.LEAVES;
		case 24 : return Material.SANDSTONE;
		case 26 : return Material.BED;
		case 31 : return Material.GRASS;
		case 35 : return Material.WOOL;
		case 39 : return Material.BROWN_MUSHROOM;
		case 40 : return Material.RED_MUSHROOM;
		case 44 : return Material.STEP ;
		case 56 : return Material.DIAMOND_ORE;
		case 59 : return Material.WHEAT;
		case 85 : return Material.FENCE;
		case 54 : return Material.CHEST;
		case 58 : return Material.WORKBENCH;
		case 60 : return Material.SOIL;
		case 64 : return Material.WOOD_DOOR;
		case 63 : return Material.SIGN;
		case 68 : return Material.WALL_SIGN;
		case 47 : return Material.BOOKSHELF;
		case 50 : return Material.TORCH;
		case 88 : return Material.SOUL_SAND;
		case 87 : return Material.NETHERRACK ;
		case 110 : return Material.MYCEL;
		case 112 :return Material.NETHER_BRICK;
		case 114 : return Material.NETHER_BRICK_STAIRS ;
		case 115 : return Material.NETHER_WARTS ;
		case 126 : return Material.WOOD_STEP ;
		case 103 : return Material.MELON_BLOCK;
		case (byte) 153 : return Material.QUARTZ_ORE;
		case (byte) 155 : return Material.QUARTZ_BLOCK;
		case (byte) 254: return Material.AIR;
		case (byte) 255: return Material.AIR;
		
		default :
			return Material.AIR;
		}
		
	}
	
	public static String showPlanValue (byte[] mapRow )
	{
		String charRow = "";
		for (int i = 0; i < mapRow.length; i++) 
		{
			charRow = charRow + planValueToChar(mapRow[i]);
		}
		return charRow;
	}

	
	@Override
	public Boolean initConfigData()
	{
		return null;
	}
	@Override
	public String getVersion()
	{
		return null;
	}
	@Override
	public String getPluginName()
	{
		return null;
	}
	@Override
	public ItemList getToolItems()
	{
		return null;
	}
	@Override
	public ItemList getWeaponItems()
	{
		return null;
	}
	@Override
	public ItemList getArmorItems()
	{
		return null;
	}
	@Override
	public BuildPlanType regionToBuildingType(String regionTypeName)
	{
		return null;
	}

	public static String setStrleft(String in, int len)
	{
		char[] out = new char[len];
		for (int i = 0; i < out.length; i++)
		{
			out[i] = ' ';
		}
		if (len >= in.length())
		{
			char[] zw  = in.toCharArray();
			for (int i = 0; i < zw.length; i++)
			{
				out[i] = zw[i]; 
			}
		} else
		{
			char[] zw  = in.toCharArray();
			for (int i = 0; i < out.length; i++)
			{
				out[i] = zw[i]; 
			}
		}
		return String.valueOf(out);
	}

	public static double format2(double value)
	{
		int value100 = (int)(value * 100);
		return ((double)value100/100.0);
	}

	public static String setStrformat2(double value, int len)
	{
		value = format2(value);
		String in = String.valueOf(value);
		return setCharright(in, len,' ');
	}

	public static String setStrright(double value, int len)
	{
		String in = String.valueOf(value);
		return setCharright(in, len,' ');
	}


	public static String set0right(int value, int len)
	{
		String in = String.valueOf(value);
		return setCharright(in, len, '0');
	}
	
	public static String setStrright(int value, int len)
	{
		String in = String.valueOf(value);
		return setCharright(in, len, ' ');
	}

	public static String setStrright(String value, int len)
	{
		String in = String.valueOf(value);
		return setCharright(in, len, ' ');
	}

	private static String setCharright(String in, int len, char c)
	{
		char[] out = new char[len];
		for (int i = 0; i < out.length; i++)
		{
			out[i] = c;
		}
		if (len >= in.length())
		{
			char[] zw  = in.toCharArray();
			int zwl = zw.length;
			for (int i = 0; i < zw.length; i++)
			{
				out[len-i-1] = zw[zwl-i-1]; 
			}
		} else
		{
			char[] zw  = in.toCharArray();
			int zwl = zw.length;
			if (zw.length <= out.length)
			{
				for (int i = 0; i < out.length; i++)
				{
					out[len-i] = zw[zwl-i]; 
				}
			} else
			{
				out[0] = '?';
				out[1] = '?';
			}
		}
		return String.valueOf(out);
	}

	/**
	 * charge number of items in storage of townhall without any storage buildings
	 * 
	 * @param settleType
	 * @return number of items 
	 */
	public static int defaultItemMax(SettleType settleType)
	{
		switch (settleType)
		{
		case VILLAGE: return 4 * ConfigBasis.CHEST_STORE; 
		case HAMLET : return 10 * ConfigBasis.CHEST_STORE;
		case TOWN   : return 12 * ConfigBasis.CHEST_STORE;
		case CITY   : return 20 * ConfigBasis.CHEST_STORE;
		case METROPOLIS  : return 30 * ConfigBasis.CHEST_STORE;
		case FORTRESS : return 10 * ConfigBasis.CHEST_STORE;
		case CAMP : return 10 * ConfigBasis.CHEST_STORE;
		case LEHEN_1 : return 10 * ConfigBasis.CHEST_STORE;
		case LEHEN_2 : return 12 * ConfigBasis.CHEST_STORE;
		case LEHEN_3 : return 14 * ConfigBasis.CHEST_STORE;
		case LEHEN_4 : return 16 * ConfigBasis.CHEST_STORE;
		default :
			return ConfigBasis.CHEST_STORE;
		}
	}

	public static int getDefaultSettler(BuildPlanType buildingType)
	{
		switch(buildingType)
		{
		case NONE : return 0;
		
		case CABIN: return 2;
		case COTTAGE : return 5;
		case HOME : return SETTLER_COUNT;
		case HOUSE : return (2 * SETTLER_COUNT);
		case LARGEHOUSE : return 5;
		case MANSION : return (3 * SETTLER_COUNT);
		case FARMHOUSE : return (2 * SETTLER_COUNT);
		case FARM : return(4 * SETTLER_COUNT);
		case HALL : return 5;
		case TOWNHALL : return 5;
		case PARISHHOUSE : return 3;
		case COLONY : return 0;
		case WALLN : return 0;
		case WALLE : return 0;
		case ROAD : return 0;
		case STEEPLE : return 0;
		case SHIP_0: return 2;
		case TAVERNE : return 0;
		case PILLAR : return 0;
		case GUARDHOUSE : return 5;
		case ARCHERY : return 5;
		case WATCHTOWER : return 5;
		case DEFENSETOWER: return 15;
		case BARRACK : return 20;
		case TOWER : return 5;
		case CASERN : return 20;
		case GARRISON : return 100;
		case HEADQUARTER : return 5;
		case KEEP : return 10;
		case CASTLE : return 20;
		case STRONGHOLD : return 30;
		case PALACE : return 40;

		default :
			return 0;
		}
		
	}
	
	public static int getDefaultWorker(BuildPlanType buildingType)
	{
		switch(buildingType)
		{
		case NONE : return 0;
		case ARCHERY : return 1;
		case ARMOURER : return 4;
		case ARMOURY : return 3;
		case AXESHOP : return 2;
		case BAKERY : return 1;
		case BIBLIOTHEK : return 1;
		case BARRACK : return 2;
		case BRICKWORK : return 2;
		case BLACKSMITH : return 1;
		case BOWMAKER : return 1;
		case BUTCHER : return 2;
		case CHARBURNER : return 1;
		case CHICKENHOUSE : return 1;
		case COWSHED : return 1;
		case CARPENTER : return 2;
		case CABINETMAKER : return 5;
		case CHAINMAKER : return 4;
		case CHURCH : return 2;
		case DIAMONDMINE : return 10;
		case DEFENSETOWER : return 1;
		case FARMHOUSE : return 2;
		case FARM : return 5;
		case FIELD : return 5;
		case FISHERHOOD : return 1;
		case FLETCHER : return 1;
		case GATE : return 2;
		case GARRISON : return 5;
		case GUARDHOUSE : return 1;
		case GOLDMINE : return 5;
		case GOLDSMELTER : return 5;
		case HALL : return 0;
		case HOME : return 0;
		case HOESHOP : return 1;
		case HUNTER : return 2;
		case HORSEBARN : return 1;
		case KNIFESHOP : return 1;
		case LIBRARY : return 5;
		case PICKAXESHOP : return 1;
		case PIGPEN : return 1;
		case QUARRY : return 1;
		case SHEPHERD : return 1;
		case SMALLIBRARY : return 2;
		case SMITH : return 2;
		case SMELTER : return 3;
		case SPADESHOP : return 1;
		case SPIDERSHED : return 1;
		case STONEMINE : return 3;
		case STONEYARD : return 1;
		case IRONMINE : return 5;
		case COALMINE : return 5;
		case EMERALDMINE : return 5;
		case TAMER : return 2;
		case TAVERNE : return 3;
		case TANNERY : return 4;
		case TRADER : return 5;
		case WHEAT : return 1;
		case WATCHTOWER : return 0;
		case WAREHOUSE : return 3;
		case WOODCUTTER : return 1;
		case WORKSHOP : return 5;
		default :
			return 1;
		}
		 
	}


	/**
	 * charge number of unit in settlement without any special military buildings
	 * @param settleType
	 * @return number of unit 
	 */
	public static int defaultUnitMax(SettleType settleType)
	{
		switch (settleType)
		{
		case VILLAGE : return 1 * ConfigBasis.CHURCH_Settler;
		case HAMLET : return 1 * ConfigBasis.HALL_Settler;
		case TOWN   : return 1 * ConfigBasis.HALL_Settler*2;
		case CITY   : return 2 * ConfigBasis.HALL_Settler*3;
		case METROPOLIS  : return 4 * ConfigBasis.HALL_Settler*4;
		case FORTRESS : return 5;
		case CAMP : return ConfigBasis.HALL_Settler*5;
		case LEHEN_1 : return 1 * ConfigBasis.HALL_Settler;
		case LEHEN_2   : return 1 * ConfigBasis.HALL_Settler*2;
		case LEHEN_3   : return 2 * ConfigBasis.HALL_Settler*3;
		case LEHEN_4  : return 4 * ConfigBasis.HALL_Settler*4;
		default :
			return ConfigBasis.HALL_Settler;
		}
	}

	public static int defaultPowerMax(SettleType settleType)
	{
		switch (settleType)
		{
		case HAMLET : return ConfigBasis.HALL_Power;
		case TOWN   : return ConfigBasis.TOWN_Power;
		case CITY   : return ConfigBasis.CITY_Power;
		case METROPOLIS  : return ConfigBasis.METROPOL_Power;
		case FORTRESS : return ConfigBasis.CASTLE_Power;
		case CAMP 	: return ConfigBasis.HALL_Power; 
		case LEHEN_1 	: return ConfigBasis.LEHEN_1_Power; 
		case LEHEN_2 	: return ConfigBasis.LEHEN_2_Power; 
		case LEHEN_3 	: return ConfigBasis.LEHEN_3_Power; 
		case LEHEN_4 	: return ConfigBasis.LEHEN_4_Power; 
		default :
			return 100;
		}
	}

	/**
	 * calculte ItemMax for the whole settlement
	 * @return ItemMax
	 */
	public static int calcItemMax(BuildingList buildingList, SettleType settleType)
	{
		int value = ConfigBasis.defaultItemMax(settleType);
		if (buildingList != null)
		{
			for (Building b : buildingList.values())
			{
				switch (b.getBuildingType()) 
				{
					case WAREHOUSE :
						value = value + getWarehouseItemMax(b.getBuildingType());
						break;
					case TRADER :
						value = value + getTraderItemMax(b.getBuildingType());
						break;
					case HALL :
						value = value + ConfigBasis.defaultItemMax(settleType);
						break;
					case TOWNHALL :
						value = value + ConfigBasis.defaultItemMax(settleType);
						break;
					case CHURCH :
						value = value + ConfigBasis.defaultItemMax(settleType);
						break;
					default :
						break;
				}
			}
		}
//		if (value == 0)
//		{
//			value = ConfigBasis.defaultItemMax(settleType);
//		}
		return value;
	}

	/**
	 * calculate extend for ItemMax for warehouse building
	 * @param building
	 * @param value  old ItemMax
	 * @return new ItemMax
	 */
	public static int getWarehouseItemMax(BuildPlanType buildingType)
	{
		switch(buildingType)
		{
		case HALL : return 10 * ConfigBasis.CHEST_STORE;
		case TOWNHALL   : return 12 * ConfigBasis.CHEST_STORE;
		case WAREHOUSE : return   ConfigBasis.WAREHOUSE_CHEST_FACTOR * ConfigBasis.CHEST_STORE;
		case TRADER    : return   ConfigBasis.TRADER_CHEST_FACTOR * ConfigBasis.CHEST_STORE;
		case WORKSHOP : return   0; //WerkstattChestFactor * Chest_Store;
		case FARM : return   0; //BauernhofChestFactor * Chest_Store;
		default :
			return 0 ;
			
		}
		 //value + (building.getWorkerNeeded()*WarehouseItemMaxFactor);
	}

	
	/**
	 * calculate extend for ItemMax for trader building
	 * @param building
	 * @param value  old ItemMax
	 * @return new ItemMax
	 */
	public static int getTraderItemMax(BuildPlanType buildingType)
	{
		switch(buildingType)
		{
			case TRADER    : return ConfigBasis.TRADER_CHEST_FACTOR * ConfigBasis.CHEST_STORE;
			default :
				return 0 ;
		}
	}

	public static int getOrderMax(Building building)
	{
		switch(building.getBuildingType())
		{
			case TRADER    : return TRADE_ORDER;
			default :
				return 0 ;
		}
	}
	
	/**
	 * 
	 * @return default weapon items
	 */
	public static ItemList initWeapon()
	{
		ItemList subList = new ItemList();

		subList.addItem("BOW",0);
		subList.addItem("DIAMOND_SWORD",0);
		subList.addItem("GOLD_SWORD",0);
		subList.addItem("IRON_SWORD",0);
		subList.addItem("STONE_SWORD",0);
		subList.addItem("WOOD_SWORD",0);
		subList.addItem("ARROW",0);
		
		return subList;
	}

	/**
	 * 
	 * @return default armor items
	 */
	public static ItemList initArmor()
	{
		ItemList subList = new ItemList();
		
		subList.addItem("LEATHER_BOOTS",0);
		subList.addItem("LEATHER_CHESTPLATE",0);
		subList.addItem("LEATHER_HELMET",0);
		subList.addItem("LEATHER_LEGGINGS",0);

		subList.addItem("DIAMOND_BOOTS",0);
		subList.addItem("DIAMOND_CHESTPLATE",0);
		subList.addItem("DIAMOND_HELMET",0);
		subList.addItem("DIAMOND_LEGGINGS",0);
		
		subList.addItem("GOLD_BOOTS",0);
		subList.addItem("GOLD_CHESTPLATE",0);
		subList.addItem("GOLD_HELMET",0);
		subList.addItem("GOLD_LEGGINGS",0);
		
		subList.addItem("IRON_BOOTS",0);
		subList.addItem("IRON_CHESTPLATE",0);
		subList.addItem("IRON_HELMET",0);
		subList.addItem("IRON_LEGGINGS",0);

		subList.addItem("CHAINMAIL_BOOTS",0);
		subList.addItem("CHAINMAIL_CHESTPLATE",0);
		subList.addItem("CHAINMAIL_HELMET",0);
		subList.addItem("CHAINMAIL_LEGGINGS",0);
		
		return subList;
	}
	
	/**
	 * 
	 * @return default tool items
	 */
	public static ItemList  initTool()
	{
		ItemList subList = new ItemList();

		subList.addItem("FISHING_ROD",0);
		subList.addItem("FLINT_AND_STEEL",0);
		subList.addItem("SHEARS",0);
		
		subList.addItem("DIAMOND_AXE",0);
		subList.addItem("DIAMOND_HOE",0);
		subList.addItem("DIAMOND_PICKAXE",0);
		subList.addItem("DIAMOND_SPADE",0);

		subList.addItem("GOLD_AXE",0);
		subList.addItem("GOLD_HOE",0);
		subList.addItem("GOLD_PICKAXE",0);
		subList.addItem("GOLD_SPADE",0);

		subList.addItem("IRON_AXE",0);
		subList.addItem("IRON_HOE",0);
		subList.addItem("IRON_PICKAXE",0);
		subList.addItem("IRON_SPADE",0);

		subList.addItem("STONE_AXE",0);
		subList.addItem("STONE_HOE",0);
		subList.addItem("STONE_PICKAXE",0);
		subList.addItem("STONE_SPADE",0);

		subList.addItem("WOOD_AXE",0);
		subList.addItem("WOOD_HOE",0);
		subList.addItem("WOOD_PICKAXE",0);
		subList.addItem("WOOD_SPADE",0);
		subList.addItem(Material.SADDLE.name(),0);
		subList.addItem(Material.FISHING_ROD.name(),0);
		subList.addItem(Material.COMPASS.name(),0);
		subList.addItem(Material.WATCH.name(),0);
		subList.addItem(Material.LEASH.name(),0);
		subList.addItem(Material.NAME_TAG.name(),0);

		return subList;
	}

	
	
	public static ItemList initBuildMaterial()
	{
		ItemList subList = new ItemList();

		subList.addItem(Material.COBBLESTONE.name(),0);
		subList.addItem(Material.LOG.name(),0);
		subList.addItem(Material.STONE.name(),0);
		subList.addItem(Material.BRICK.name(),0);
		subList.addItem(Material.NETHER_BRICK.name(),0);
//		subList.addItem(Material.WOOD_STEP.name(),0);
		subList.addItem(Material.ANVIL.name(),0);
		subList.addItem(Material.STEP.name(),0);
		subList.addItem(Material.DIRT.name(),0);
		subList.addItem(Material.GRASS.name(),0);
		subList.addItem(Material.WATER.name(),0);
		subList.addItem(Material.NETHERRACK.name(),0);
		subList.addItem(Material.WHEAT.name(),0);
		subList.addItem(Material.WOOD.name(),0);
		subList.addItem(Material.SOIL.name(),0);
		subList.addItem(Material.GLASS.name(),0);
//		subList.addItem(Material..name(),0);
		
		return subList;
	}

	public static ItemList initMaterial()
	{
		ItemList subList = new ItemList();

		subList.addItem(Material.COAL.name(),0);
		subList.addItem(Material.STICK.name(),0);
		subList.addItem(Material.WOOL.name(),0);
		subList.addItem(Material.SEEDS.name(),0);
		subList.addItem(Material.FENCE.name(),0);
		subList.addItem(Material.FENCE_GATE.name(),0);
		subList.addItem(Material.BED.name(),0);
		subList.addItem(Material.TORCH.name(),0);
		subList.addItem(Material.BOOKSHELF.name(),0);
		subList.addItem(Material.WOOD_DOOR.name(),0);
		subList.addItem(Material.CHEST.name(),0);
		subList.addItem(Material.WORKBENCH.name(),0);
		subList.addItem(Material.FURNACE.name(),0);
		subList.addItem(Material.WALL_SIGN.name(),0);
		subList.addItem(Material.SIGN.name(),0);
		subList.addItem(Material.SIGN_POST.name(),0);
		subList.addItem(Material.LEATHER.name(),0);
		subList.addItem(Material.IRON_INGOT.name(),0);

//		subList.addItem(Material..name(),0);
		
		return subList;
	}

	public static ItemList initOre()
	{
		ItemList subList = new ItemList();

		subList.addItem(Material.COAL_ORE.name(),0);
		subList.addItem(Material.IRON_ORE.name(),0);
		subList.addItem(Material.GOLD_ORE.name(),0);
		subList.addItem(Material.DIAMOND_ORE.name(),0);
		subList.addItem(Material.REDSTONE_ORE.name(),0);
//		subList.addItem(Material.EMERALD_ORE.name(),0);
		subList.addItem(Material.LAPIS_ORE.name(),0);
//		subList.addItem(Material.QUARTZ_ORE.name(),0);
//		subList.addItem(Material..name(),0);
			
		return subList;
	}
	
	public static ItemList initValuables()
	{
		ItemList subList = new ItemList();

		subList.addItem(Material.GOLD_NUGGET.name(),0);
		subList.addItem("EMERALD",0);
		subList.addItem(Material.DIAMOND.name(),0);
		subList.addItem(Material.GOLD_INGOT.name(),0);
//		subList.addItem(Material..name(),0);
			
		return subList;
	}
	
	public static ItemList initRawMaterial()
	{
		ItemList subList = new ItemList();

		subList.addItem(Material.DIRT.name(),0);
		subList.addItem(Material.STONE.name(),0);
		subList.addItem(Material.GRASS.name(),0);
		subList.addItem(Material.NETHERRACK.name(),0);
		subList.addItem(Material.LOG.name(),0);
		subList.addItem(Material.LOG_2.name(),0);
		subList.addItem(Material.GRAVEL.name(),0);
		subList.addItem(Material.WATER.name(),0);
		subList.addItem(Material.WOOL.name(),0);
		subList.addItem(Material.CLAY.name(),0);
		subList.addItem(Material.SAND.name(),0);
		subList.addItem(Material.LAVA.name(),0);
		subList.addItem(Material.LEAVES.name(),0);
//		subList.addItem(Material.LEAVES_2.name(),0);
		subList.addItem(Material.SANDSTONE.name(),0);
		subList.addItem(Material.SNOW.name(),0);
		subList.addItem(Material.ICE.name(),0);
		subList.addItem(Material.CACTUS.name(),0);
		subList.addItem(Material.SUGAR_CANE.name(),0);
		subList.addItem(Material.PUMPKIN.name(),0);
		subList.addItem(Material.SEEDS.name(),0);
		subList.addItem(Material.MELON.name(),0);
		subList.addItem(Material.VINE.name(),0);
		subList.addItem(Material.MYCEL.name(),0);
		subList.addItem(Material.HUGE_MUSHROOM_1.name(),0);
		subList.addItem(Material.HUGE_MUSHROOM_2.name(),0);
		subList.addItem(Material.MOSSY_COBBLESTONE.name(),0);
		subList.addItem(Material.SAPLING.name(),0);
		subList.addItem(Material.FLINT.name(),0);
//		subList.addItem(Material..name(),0);
			
		return subList;
	}

	public static ItemList initFoodMaterial()
	{
		ItemList subList = new ItemList();

		subList.addItem(Material.WHEAT.name(),0);
		subList.addItem(Material.BREAD.name(),0);
		subList.addItem(Material.RED_MUSHROOM.name(),0);
		subList.addItem(Material.BROWN_MUSHROOM.name(),0);
		subList.addItem(Material.MUSHROOM_SOUP.name(),0);
		subList.addItem(Material.RED_MUSHROOM.name(),0);
		subList.addItem(Material.BROWN_MUSHROOM.name(),0);
		subList.addItem(Material.COOKED_BEEF.name(),0);
		subList.addItem(Material.COOKED_CHICKEN.name(),0);
		subList.addItem(Material.GRILLED_PORK.name(),0);
		subList.addItem(Material.COOKIE.name(),0);
		subList.addItem(Material.COOKED_FISH.name(),0);
		subList.addItem(Material.COOKED_MUTTON.name(),0);
		subList.addItem(Material.COOKED_RABBIT.name(),0);
		subList.addItem(Material.RABBIT.name(),0);
		subList.addItem(Material.RABBIT_STEW.name(),0);
		subList.addItem(Material.RAW_BEEF.name(),0);
		subList.addItem(Material.RAW_CHICKEN.name(),0);
		subList.addItem(Material.RAW_FISH.name(),0);
		subList.addItem(Material.CARROT.name(),0);
		subList.addItem(Material.CAKE.name(),0);
		subList.addItem(Material.CAKE_BLOCK.name(),0);
		subList.addItem(Material.POTATO.name(),0);
		subList.addItem(Material.POTATO_ITEM.name(),0);
		subList.addItem(Material.MELON.name(),0);
		subList.addItem(Material.MELON_BLOCK.name(),0);
		
		
//		subList.addItem(Material..name(),0);

		return subList;
	}

	public static ItemList initIngnoreList()
	{
		ItemList subList = new ItemList();

		subList.addItem(Material.DIRT.name(),0);
		subList.addItem(Material.SOIL.name(),0);
		subList.addItem(Material.GRASS.name(),0);
		subList.addItem(Material.WATER.name(),0);
		subList.addItem(Material.SAND.name(),0);
//		subList.addItem(Material..name(),0);
			
		return subList;
	}
	
	public static ItemGroup getItemGroup(String itemRef)
	{
		if (initFoodMaterial().getItem(itemRef) != null) { return ItemGroup.FOOD; } 
		if (initArmor().getItem(itemRef) != null) { return ItemGroup.ARMOR; } 
		if (initBuildMaterial().getItem(itemRef) != null) { return ItemGroup.BUILDMATERIAL; } 
		if (initMaterial().getItem(itemRef) != null) { return ItemGroup.MATERIAL; } 
		if (initOre().getItem(itemRef) != null) { return ItemGroup.ORE; } 
		if (initRawMaterial().getItem(itemRef) != null) { return ItemGroup.RAW; } 
		if (initValuables().getItem(itemRef) != null) { return ItemGroup.VALUABLE; } 
		if (initWeapon().getItem(itemRef) != null) { return ItemGroup.WEAPON; } 
			
		return ItemGroup.NONE;
	}
	
	
	public static int getCreateMinPower(SettleType sType)
	{
		switch(sType)
		{
		case LEHEN_1 :	return LEHEN_CREATE_POWER_1;
		case LEHEN_2 :	return LEHEN_CREATE_POWER_2;
		case LEHEN_3 :	return LEHEN_CREATE_POWER_3;
		case LEHEN_4 :	return LEHEN_CREATE_POWER_4;
		default:
			return 0;
		}
	}

	
	@Override
	public BuildPlanType superRegionToBuildingType(String superRegionTypeName)
	{
		return null;
	}

	@Override
	public String getRegionType(BuildPlanType bType)
	{
		return null;
	}

	@Override
	public ItemList getBuildMaterialItems()
	{
		return null;
	}

	@Override
	public ItemList getMaterialItems()
	{
		return null;
	}

	@Override
	public ItemList getOreItems()
	{
		return null;
	}

	@Override
	public ItemList getValuables()
	{
		return null;
	}

	@Override
	public ItemList getRawItems()
	{
		return null;
	}

	@Override
	public ItemList getFoodItems()
	{
		return null;
	}

	@Override
	public boolean isUpdateCheck()
	{
		return false;
	}

	@Override
	public boolean isAutoUpdate()
	{
		return false;
	}

	public static boolean isMaterial(String value)
	{
		for (Material mat : Material.values())
		{
			if (mat.name().equalsIgnoreCase(value))
			{
				return true;
			}
			
		}
		return false;
	}
	
    public static String checkItemRefOut(ItemStack itemStack)
    {
    	switch (itemStack.getType())
    	{
    	case AIR: return itemStack.getType().name();
    	case SIGN : return Material.SIGN_POST.name();
    	case WATER : return Material.WATER_BUCKET.name();
    	case LAVA: return Material.LAVA_BUCKET.name();
    	case SOIL: return Material.DIRT.name();
    	
    	default:
    		return itemStack.getType().name();	
    	}
    }

    public static String checkItemRefBuild(String itemRef)
    {
    	if (isMaterial(itemRef) == false)
    	{
    		return Material.AIR.name();
    	}
    	switch (Material.valueOf(itemRef))
    	{
    	case SIGN_POST : return Material.SIGN.name();
    	case WALL_SIGN : return Material.SIGN.name();
    	case WATER_BUCKET : return Material.WATER.name();
    	case STATIONARY_WATER : return Material.WATER.name();
    	case LAVA_BUCKET: return Material.LAVA.name();
    	case STATIONARY_LAVA : return Material.LAVA.name();
    	case SOIL: return Material.DIRT.name();
    	case GRASS : return Material.DIRT.name();
    	case DOUBLE_PLANT: return Material.SEEDS.name();
    	case COAL_ORE: return Material.COAL.name();
    	case EMERALD_ORE: return Material.EMERALD.name();
    	case REDSTONE_ORE : return Material.REDSTONE.name();
    	default:
    		return itemRef;	
    	}
    }


	@Override
	public  ArrayList<EntityType> getMobsToRepel() 
	{
		return null;
	}

	public static int getMinStorage(int matFactor, int settlerCount, String itemRef, int sellLimit)
	{
//		RealmModel rModel,
//		int matFactor = rModel.getServer().getBioneFactor( biome, Material.getMaterial(itemRef));
		
			if (initTool().containsKey(itemRef))
			{
				return 16 - (10 * matFactor / 100) + sellLimit;
			}
			if (initWeapon().containsKey(itemRef))
			{
				return 16 - (30 * matFactor / 100) + sellLimit;
			}
			if (initArmor().containsKey(itemRef))
			{
				return 32- (30 * matFactor / 100) + sellLimit; 
			}
			if (initFoodMaterial().containsKey(itemRef))
			{
				return settlerCount * 16 ;
			}
			if (initValuables().containsKey(itemRef))
			{
				return 64 - (64 * matFactor / 100) + sellLimit;
			}
			if (initBuildMaterial().containsKey(itemRef))
			{
				return 196 - (64 * matFactor / 100) + sellLimit;
			}
			if (initOre().containsKey(itemRef))
			{
				return 64 - (64 * matFactor / 100) + sellLimit;
			}
			if (initMaterial().containsKey(itemRef))
			{
				return 16 - (14 * matFactor / 100) + sellLimit;
			}
			if (initRawMaterial().containsKey(itemRef))
			{
				return 64 - (64 * matFactor / 100) + sellLimit;
			}
			
			return 8 + sellLimit;
//			if (matFactor >= 0)
//			{
//		} else
//		{
//			return 9999;
//		}
	}
	
	
}
