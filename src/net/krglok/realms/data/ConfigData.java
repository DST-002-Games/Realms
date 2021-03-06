package net.krglok.realms.data;

import java.util.ArrayList;
import java.util.HashMap;

import net.krglok.realms.Common.ItemList;
import net.krglok.realms.builder.BuildPlanType;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.SettleType;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

/**
 * read Data from YML file 
 * used for initialize the Plugin and the RealmModel with data
 * Hint: the plugin are not referenced here, so it is independant from the plugin system
 * this is useful for test and simuation
 * configFile; hold the plugin configFile reference;
 * configFile; is loaded by the plugin
 * 
 * @author Windu
 *
 */
public class ConfigData extends ConfigBasis implements ConfigInterface
{

	// RegionTypeBuildingType
	private HashMap<String,String> regionBuildingTypes;
	// SuperRegionTypeBuildingType
	private HashMap<String,String> superBuildingTypes;
	// SuperRegionTypeSettlementType
	private HashMap<String,String> superSettleTypes;

	private HashMap<String, String> buildPlanRegions;
	
	private ArrayList<String> npcNames;
	
	private ItemList toolItems;
	private ItemList weaponItems;
	private ItemList armorItems;
	private ItemList buildItems;
	private ItemList materialItems;
	private ItemList oreItems;
	private ItemList valuableItems;
	private ItemList rawItems;
	private ItemList foodItems;
	
	private int realmCounter ;
	private int settlementCounter ;
	private int buildingCounter;
	
	private boolean isUpdateCheck;
	
	private boolean isAutoUpdate;

	private boolean isLogList;
	
	private boolean isLoaded = false;
	
	private boolean isSpawnAnimal = false;
	
	private boolean isTrackPlayer = false;
	
	private boolean initBuildingPos = false;
	
	private ArrayList<EntityType> mobList;
	
	protected FileConfiguration configFile;
	
	public ConfigData(FileConfiguration configFile )
	{
		regionBuildingTypes = new HashMap<String, String>();
		superBuildingTypes = new HashMap<String, String>();
		superSettleTypes = new HashMap<String, String>();
		buildPlanRegions  = new HashMap<String, String>();
		mobList = new ArrayList<EntityType>();
		npcNames = new ArrayList<String>();
		setRealmCounter(0);
		setSettlementCounter(0);
		
		this.configFile = configFile;
		PLUGIN_VER = configFile.getString(CONFIG_PLUGIN_VER);
		realmCounter = configFile.getInt(CONFIG_REALM_COUNTER, 0);
		settlementCounter = configFile.getInt(CONFIG_SETTLEMENT_COUNTER, 0);
		setBuildingCounter(configFile.getInt("buildingCounter", 0));
		isUpdateCheck = configFile.getBoolean("updatecheck", false);
		isAutoUpdate  = configFile.getBoolean("autoupdate", false);
		isLogList  = configFile.getBoolean("loglist", false);
		isSpawnAnimal  = configFile.getBoolean("spawnAnimal", false);
		isSpawnAnimal  = configFile.getBoolean("trackPlayer", true);
		initBuildingPos = configFile.getBoolean("initBuildingPos",false);
//		configFile.options().copyDefaults(true);
		setLoaded(true);
	}

	/**
	 * @return the isLoaded
	 */
	public boolean isLoaded()
	{
		return isLoaded;
	}

	/**
	 * @param isLoaded the isLoaded to set
	 */
	public void setLoaded(boolean isLoaded)
	{
		this.isLoaded = isLoaded;
	}

	@Override
	public Boolean initConfigData()
	{
		
		initRegionBuilding();
		initSuperSettleTypes();
		initBuildPlanRegion();
		armorItems = ConfigBasis.initArmor();
		weaponItems = ConfigBasis.initWeapon();
		toolItems = ConfigBasis.initTool();
		buildItems  = ConfigBasis.initBuildMaterial();
		materialItems = ConfigBasis.initMaterial();
		oreItems = ConfigBasis.initOre();
		valuableItems = ConfigBasis.initValuables();
		rawItems = ConfigBasis.initRawMaterial();
		foodItems  = ConfigBasis.initFoodMaterial();
		
		return true;
	}

	@Override
	public String getVersion()
	{
		return PLUGIN_VER;
	}

	@Override
	public String getPluginName()
	{
		return PLUGIN_NAME;
	}

	@Override
	public ItemList getToolItems()
	{
		return toolItems;
	}

	@Override
	public ItemList getWeaponItems()
	{
		return weaponItems;
	}

	@Override
	public ItemList getArmorItems()
	{
		return armorItems;
	}

	@Override
	public ItemList getBuildMaterialItems()
	{
		return buildItems;
	}
	
	@Override
	public ItemList getMaterialItems()
	{
		return materialItems;
	}
	
	@Override
	public ItemList getOreItems()
	{
		return oreItems;
	}
	
	@Override
	public ItemList getValuables()
	{
		return valuableItems;
	}
	
	@Override
	public ItemList getRawItems()
	{
		return rawItems;
	}
	
	@Override
	public ItemList getFoodItems()
	{
		return foodItems;
	}
	
	public  void initMobsToRepel() 
	{
		this.mobList.add(EntityType.SPIDER);
//		return mobList;
	}

	
	public  ArrayList<EntityType> getMobsToRepel() 
	{
		return mobList;
	}

	/**
	 * erzeugt eine List von superRegiontypen mit SettlementTypen
	 */
	private void initSuperSettleTypes()
	{
		superSettleTypes.put("Mine", SettleType.HAMLET.name());
		superSettleTypes.put("Burg", SettleType.FORTRESS.name());
		superSettleTypes.put("Siedlung", SettleType.HAMLET.name());
		superSettleTypes.put("Dorf", SettleType.TOWN.name());
		superSettleTypes.put("Stadt", SettleType.CITY.name());
		superSettleTypes.put("Metropole", SettleType.METROPOLIS.name());
	}

	/**
	 * erzeugt eine Liste von RegionTypen zu BuildingTypen
	 */
	private void initRegionBuilding()
	{
//		regionBuildingTypes.put("haus_gross", BuildPlanType.HOUSE.name());
		regionBuildingTypes.put("bauern_hof",BuildPlanType.FARM.name());
		regionBuildingTypes.put("bauern_haus",BuildPlanType.FARMHOUSE.name());
		regionBuildingTypes.put("colony", BuildPlanType.COLONY.name());
		regionBuildingTypes.put("fischer", BuildPlanType.FISHERHOOD.name());
		regionBuildingTypes.put("haupthaus", BuildPlanType.HALL.name());
		regionBuildingTypes.put("haus_baecker",BuildPlanType.BAKERY.name());
		regionBuildingTypes.put("haus_einfach",BuildPlanType.HOME.name());
		regionBuildingTypes.put("haus_stadt", BuildPlanType.HOUSE.name());
		regionBuildingTypes.put("holzfaeller",BuildPlanType.WOODCUTTER.name());
		regionBuildingTypes.put("huehnerstall",BuildPlanType.CHICKENHOUSE.name());
		regionBuildingTypes.put("koehler", BuildPlanType.CHARBURNER.name());
		regionBuildingTypes.put("kornfeld", BuildPlanType.WHEAT.name());
		regionBuildingTypes.put("markt", BuildPlanType.WAREHOUSE.name());
		regionBuildingTypes.put("rathaus", BuildPlanType.HALL.name());
		regionBuildingTypes.put("rinderstall",BuildPlanType.COWSHED.name());
		regionBuildingTypes.put("schaefer", BuildPlanType.SHEPHERD.name());
		regionBuildingTypes.put("schmelze",BuildPlanType.SMELTER.name());
		regionBuildingTypes.put("schreiner", BuildPlanType.CARPENTER.name());
		regionBuildingTypes.put("schweinestall",BuildPlanType.PIGPEN.name());
		regionBuildingTypes.put("shop_waxe",BuildPlanType.AXESHOP.name());
		regionBuildingTypes.put("shop_whoe",BuildPlanType.HOESHOP.name());
		regionBuildingTypes.put("shop_wpaxe",BuildPlanType.PICKAXESHOP.name());
		regionBuildingTypes.put("shop_wspade",BuildPlanType.SPADESHOP.name());
		regionBuildingTypes.put("shop_wsword",BuildPlanType.KNIFESHOP.name());
		regionBuildingTypes.put("smith",BuildPlanType.BLACKSMITH.name());
		regionBuildingTypes.put("stadtwache",BuildPlanType.GUARDHOUSE.name());
		regionBuildingTypes.put("steinbruch", BuildPlanType.QUARRY.name());
		regionBuildingTypes.put("steinmine",BuildPlanType.STONEMINE.name());
		regionBuildingTypes.put("tanner",BuildPlanType.TANNERY.name());
		regionBuildingTypes.put("taverne", BuildPlanType.TAVERNE.name());
		regionBuildingTypes.put("tischler",BuildPlanType.CABINETMAKER.name());
		regionBuildingTypes.put("tower", BuildPlanType.TOWER.name());
		regionBuildingTypes.put("watchtower",BuildPlanType.WATCHTOWER.name());
		regionBuildingTypes.put("werkstatt",BuildPlanType.WORKSHOP.name());
		regionBuildingTypes.put("ziegelei",BuildPlanType.BRICKWORK.name());
		
	}

	/**
	 * 
	 * @return list of <superRegionTypeName,SettleTypName>
	 */
	public HashMap<String, String> getSuperSettleTypes()
	{
		return superSettleTypes;
	}

	/**
	 * Set list to internal strongholdAreas
	 * 
	 * @param strongholdAreas
	 *            <superRegionTypeName,SettleTypName>
	 */
	public void setSuperSettleTypes(HashMap<String, String> strongholdAreas)
	{
		this.superSettleTypes = strongholdAreas;
	}

	/**
	 * 
	 * @return <superRegionTypeName,buildingTypeName>
	 */
	public HashMap<String, String> getSuperBuildingTypes()
	{
		return superBuildingTypes;
	}

	/**
	 * 
	 * @param superBuildingTypes
	 */
	public void setSuperBuildingTypes(HashMap<String, String> superBuildingTypes)
	{
		this.superBuildingTypes = superBuildingTypes;
	}

	/**
	 * 
	 * @return List of <regionTypeName,buildingTypName>
	 */
	public HashMap<String, String> getRegionBuildingTypes()
	{
		return regionBuildingTypes;
	}
	
	public HashMap<String, String> getBuildPlanRegions()
	{
		return buildPlanRegions;
	}

	@Override
	public String getRegionType(BuildPlanType bType)
	{
		return bType.name();
		
//		for (String key : buildPlanRegions.keySet())
//		{
//			if (buildPlanRegions.get(key).equalsIgnoreCase(bType.name()))
//			{
//				return key;
//			}
//		}
//		
//		return "";
	}
	
	/**
	 * set the list to strongholdTypes
	 * 
	 * @param regionBuildings
	 *            <regionTypeName,buildingTypName>
	 */
	public void setRegionBuildingTypes(HashMap<String, String> regionBuildings)
	{
		regionBuildingTypes = regionBuildings;
	}

	/**
	 * Wandelt einen superRegionTyp in einen BuildingTyp
	 * 
	 * @param superRegionTypeName
	 * @return Buildingtyp or BUILDING_NONE
	 */
	public BuildPlanType superRegionToBuildingType(String superRegionTypeName)
	{
		String name = superBuildingTypes.get(superRegionTypeName);
		return BuildPlanType.getBuildPlanType(name);
	}

	/**
	 * Wandelt einen RegionTyp in einen BuildingTyp
	 * 
	 * @param regionTypeName
	 * @return Buildingtyp or BUILDING_NONE
	 */
	@Override
	public BuildPlanType regionToBuildingType(String regionTypeName)
	{
		
		if (BuildPlanType.getBuildPlanType(regionTypeName) != BuildPlanType.NONE)
		{
			return BuildPlanType.getBuildPlanType(regionTypeName);
		}
		String name = regionBuildingTypes.get(regionTypeName);

		return BuildPlanType.getBuildPlanType(name);
	}

	/**
	 * Wandelt einen superRegionType in einen SettlementTyp based on
	 * superSettleTypes
	 * 
	 * @param ref
	 * @return
	 */
	public SettleType superRegionToSettleType(String superRegionTypeName)
	{
		if (SettleType.getSettleType(superRegionTypeName) != SettleType.NONE)
		{
			return SettleType.getSettleType(superRegionTypeName);
		}
		String name = superSettleTypes.get(superRegionTypeName);
		return SettleType.getSettleType(name);
	}
	
	/**
	 * make List of buildingTypes for the List of regionTypes
	 * 
	 * @param collection <regionTypeName>
	 * @return List of <i,BuildingType>
	 */
	public HashMap<String, String> makeRegionBuildingTypes(HashMap<String, String> regions)
	{
		HashMap<String, String> regionBuildings = new HashMap<String, String>();
		BuildPlanType bType;
		String regionType;
		for (String regionName :regions.keySet())
		{
			regionType = regions.get(regionName);
			bType = regionToBuildingType(regionType);
			regionBuildings.put(regionName, bType.name());
		}
		for (BuildPlanType buType : BuildPlanType.values())
		{
			regionBuildings.put(buType.name(), buType.name());
		}
		return regionBuildings;
	}

	/**
	 * make List of settleTypes for the List of superRegionTypes
	 * 
	 * @param collection
	 * @return List <i, SettleTypes>
	 */
	public HashMap<String, String> makeSuperRegionSettleTypes(HashMap<String, String> superRegions)
	{
		HashMap<String, String> regionBuildings = new HashMap<String, String>();
		SettleType bType;
		String regionType;
		for (String regionName :superRegions.keySet())
		{
			regionType = superRegions.get(regionName);
			bType = superRegionToSettleType(regionType);
			regionBuildings.put(regionName, bType.name());
		}
		return regionBuildings;
	}


	/**
	 * @return the settlementCounter
	 */
	public int getSettlementCounter()
	{
		return settlementCounter;
	}

	/**
	 * @param settlementCounter the settlementCounter to set
	 */
	public void setSettlementCounter(int settlementCounter)
	{
		this.settlementCounter = settlementCounter;
	}

	/**
	 * @return the realmCounter
	 */
	public int getRealmCounter()
	{
		return realmCounter;
	}

	/**
	 * @param realmCounter the realmCounter to set
	 */
	public void setRealmCounter(int realmCounter)
	{
		this.realmCounter = realmCounter;
	}

	public void initBuildPlanRegion()
	{
//		 = new HashMap<BuildPlanType,String>();
		buildPlanRegions.put("",BuildPlanType.NONE.name());
		
	}

	
	/**
	 * @return the buildingCounter
	 */
	public int getBuildingCounter()
	{
		return buildingCounter;
	}

	/**
	 * @param buildingCounter the buildingCounter to set
	 */
	public void setBuildingCounter(int buildingCounter)
	{
		this.buildingCounter = buildingCounter;
	}
		
	public boolean isUpdateCheck()
	{
		return isUpdateCheck;
	}
	
	public boolean isAutoUpdate()
	{
		return isAutoUpdate;
	}

	/**
	 * @return the isLogList
	 */
	public boolean isLogList()
	{
		return isLogList;
	}

	/**
	 * @param isLogList the isLogList to set
	 */
	public void setLogList(boolean isLogList)
	{
		this.isLogList = isLogList;
	}

	public boolean isSpawnAnimal()
	{
		return this.isSpawnAnimal;
	}

	/**
	 * @return the npcNames
	 */
	public ArrayList<String> getNpcNames()
	{
		return npcNames;
	}

	/**
	 * @param npcNames the npcNames to set
	 */
	public void setNpcNames(ArrayList<String> npcNames)
	{
		this.npcNames = npcNames;
	}

	/**
	 * @return the initBuildingPos
	 */
	public boolean isInitBuildingPos()
	{
		return initBuildingPos;
	}

	/**
	 * @param initBuildingPos the initBuildingPos to set
	 */
	public void setInitBuildingPos(boolean initBuildingPos)
	{
		this.initBuildingPos = initBuildingPos;
	}


}
