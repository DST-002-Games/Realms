package net.krglok.realms.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.krglok.realms.builder.BuildPlanType;
import net.krglok.realms.data.DataInterface;
import net.krglok.realms.data.DataStorage;
import net.krglok.realms.data.ServerInterface;
import net.krglok.realms.manager.BuildManager;
import net.krglok.realms.manager.MapManager;
import net.krglok.realms.manager.ReputationList;
import net.krglok.realms.manager.SettleManager;
import net.krglok.realms.manager.TradeManager;
import net.krglok.realms.npc.NPCType;
import net.krglok.realms.npc.NpcData;
import net.krglok.realms.npc.NpcList;
import net.krglok.realms.unit.AbstractUnit;
import net.krglok.realms.unit.BattleFieldPosition;
import net.krglok.realms.unit.BattlePlacement;
import net.krglok.realms.unit.UnitArcher;
import net.krglok.realms.unit.UnitFactory;
import net.krglok.realms.unit.UnitHeavyInfantry;
import net.krglok.realms.unit.UnitKnight;
import net.krglok.realms.unit.UnitLightInfantry;
import net.krglok.realms.unit.UnitList;
import net.krglok.realms.unit.UnitMilitia;
import net.krglok.realms.unit.UnitType;

import org.bukkit.Material;
import org.bukkit.block.Biome;
//<<<<<<< HEAD
//=======
//>>>>>>> origin/PHASE2

/**
 * <pre>
 * represent the whole settlement the central object
 * settlement based on the superregion and region of HeroStronghold
 * incorporate the the rules for production, fertility and money
 * simulation of residents and workers
 * make trading and train units for military
 * the production will be started from an external task, every ingame day 
 * there are some Managers incorporated to give the settlement the possibility to ruled by a NPC
 * 
 * hint: the settle dont have a kingdomId , only the owner has a kingdomId 
 * </pre>
 * @author Windu
 *
 */
public class Settlement //implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7534071936212709937L;
	private static final double MIN_FOODCONSUM_COUNTER = -5.0;
	private static final double TAVERNE_UNHAPPY_FACTOR = 2.0;
	private static final double BASE_TAX_FACTOR = ConfigBasis.SALES_TAX;
	private static double TAVERNE_FREQUENT = 10.0;

	private static final String NEW_SETTLEMENT = "New Settlement";

	
	private static int COUNTER;
	
	private int id;
	private SettleType settleType = SettleType.NONE;
	private LocationData position;
	private String name;
	private int ownerId;
	private String ownerName = "";
	private Owner owner;
	private int kingdomId;
	private int tributId;
	private Boolean isCapital;
	private Barrack barrack ;
	private Warehouse warehouse ;
	private BuildingList buildingList;
	private Townhall townhall;
	private Bank bank;
	private Resident resident;
	private Trader trader;
//  private Headquarter headquarter;
	private ItemList requiredProduction;
	
	private Boolean isEnabled;
	private Boolean isActive;
	
//	private double hungerCounter = 0.0;
//	private double foodConsumCounter;
	private Double buildingTax ;
	
	private BoardItemList productionOverview;
	private BoardItemList taxOverview;

	private double EntertainFactor = 0.0;
	private double FoodFactor = 0.0;
	private double SettlerFactor = 0.0;

	private String world;
	private Biome biome;
	private long age;
	private BuildManager buildManager;
	private MapManager mapManager;
	private TradeManager tradeManager;
	private SettleManager settleManager;
	
	private ArrayList<Item> treasureList;
	
	private SignPosList signList;
	private ReputationList reputations;

	private double sales;
	private double taxSum;
//	private LogList logList;
	
	/**
	 * instance empty settlement with
	 * - sequential ID
	 */
	public Settlement() //LogList logList)
	{
		COUNTER++;
		id			= COUNTER;
		age         = 0;
		settleType 	= SettleType.NONE;
		position 	= new LocationData("", 0.0, 0.0, 0.0);
		name		= NEW_SETTLEMENT;
		ownerId 		= 0;
		setKingdomId(0);
		tributId = 0;
		isCapital	= false;
		barrack		= new Barrack(ConfigBasis.defaultUnitMax(settleType));
		barrack.setPowerMax(ConfigBasis.defaultPowerMax(settleType));
		warehouse	= new Warehouse(ConfigBasis.defaultItemMax(settleType));
		buildingList= new BuildingList();
		townhall	= new Townhall();
		bank		= new Bank(); //this.logList);
		resident	= new Resident();
		isEnabled   = true;
		isActive    = true;
//		foodConsumCounter = 0.0;
		sales = 0.0;
		taxSum = 0.0;
		requiredProduction = new ItemList();
		setBuildingTax(BASE_TAX_FACTOR);
		productionOverview = new BoardItemList();
		taxOverview = new BoardItemList();
		world = "";
		setBiome(Biome.SKY);
		trader = new Trader();
		buildManager = new BuildManager();
		mapManager  = new MapManager(settleType,70,true);
		tradeManager = new TradeManager ();
		settleManager = new SettleManager ();
		treasureList =  new ArrayList<Item>();
		reputations = new ReputationList();
		
		setSignList(new SignPosList());
	}

//<<<<<<< HEAD
	/**
	 * used by read from file
	 * @param priceList
	 */
	public Settlement(ItemPriceList priceList) //, LogList logList)
//=======
//	public Settlement(ItemPriceList priceList)
//>>>>>>> origin/PHASE2
	{
		COUNTER++;
		id			= COUNTER;
		age         = 0;
		settleType 	= SettleType.NONE;
		position 	= new LocationData("", 0.0, 0.0, 0.0);
		name		= NEW_SETTLEMENT;
		ownerId 		= 0;
		setKingdomId(0);
		tributId = 0;
		isCapital	= false;
//		this.logList = logList;
		barrack		= new Barrack(ConfigBasis.defaultUnitMax(settleType));
		barrack.setPowerMax(ConfigBasis.defaultPowerMax(settleType));
		warehouse	= new Warehouse(ConfigBasis.defaultItemMax(settleType));
		buildingList= new BuildingList();
		townhall	= new Townhall();
		bank		= new Bank(); //this.logList);
		resident	= new Resident();
		isEnabled   = true;
		isActive    = true;
//		foodConsumCounter = 0.0;
		sales = 0.0;
		taxSum = 0.0;
		requiredProduction = new ItemList();
		setBuildingTax(BASE_TAX_FACTOR);
		productionOverview = new BoardItemList();
		taxOverview = new BoardItemList();
		world = "";
		setBiome(Biome.SKY);
		trader = new Trader();
		buildManager = new BuildManager();
		mapManager  = new MapManager(settleType,70,true);
		tradeManager = new TradeManager (priceList);
		settleManager = new SettleManager ();
		treasureList =  new ArrayList<Item>();
		reputations = new ReputationList();
		setSignList(new SignPosList());
	}
	
	/**
	 * instances settlement with
	 * - with sequential ID
	 * - owner
	 * 
	 * @param Owner
	 */
	public Settlement(int ownerId, LocationData position) //, LogList logList)
	{
		COUNTER++;
		id			= COUNTER;
		age         = 0;
		settleType 	= SettleType.NONE;
		name		= NEW_SETTLEMENT;
		this.position 	= position;
		this.ownerId = ownerId;
		setKingdomId(0);
		tributId = 0;
		isCapital	= false;
		barrack		= new Barrack(ConfigBasis.defaultUnitMax(settleType));
		barrack.setPowerMax(ConfigBasis.defaultPowerMax(settleType));
		warehouse	= new Warehouse(ConfigBasis.defaultItemMax(settleType));
		buildingList= new BuildingList();
		townhall	= new Townhall();
//		this.logList = logList;
		bank		= new Bank(); //this.logList);
		resident	= new Resident();
		isEnabled   = true;
		isActive    = true;
//		foodConsumCounter = 0.0;
		sales = 0.0;
		taxSum = 0.0;
		requiredProduction = new ItemList();
		setBuildingTax(BASE_TAX_FACTOR);
		productionOverview = new BoardItemList();
		taxOverview = new BoardItemList();
		world = "";
		trader = new Trader();
		setBiome(Biome.SKY);
		buildManager = new BuildManager();
		mapManager  = new MapManager(settleType,70,true);
		tradeManager = new TradeManager ();
		settleManager = new SettleManager ();
		treasureList =  new ArrayList<Item>();
		reputations = new ReputationList();
		setSignList(new SignPosList());
}

	/**
	 * instances settlement with
	 * - with sequential ID
	 * - owner
	 * - name of stellemnet
	 * @param Owner
	 * @param settleType
	 * @param name
	 */
	public Settlement(int ownerId, LocationData position, SettleType settleType, String name, Biome biome) //, LogList logList)
	{
		COUNTER++;
		age         = 0;
		id			= COUNTER;
		this.settleType = settleType;
		this.name		= name;
		this.position 	= position;
		this.ownerId = ownerId;
		setKingdomId(0);
		tributId = 0;
		isCapital	= false;
		barrack		= new Barrack(ConfigBasis.defaultUnitMax(settleType));
		barrack.setPowerMax(ConfigBasis.defaultPowerMax(settleType));
		warehouse	= new Warehouse(ConfigBasis.defaultItemMax(settleType));
		buildingList= new BuildingList();
		townhall	= new Townhall();
//		this.logList = logList;
		bank		= new Bank(); //this.logList);
		resident	= new Resident();
		isEnabled   = true;
		isActive    = true;
//		foodConsumCounter = 0.0;
		sales = 0.0;
		taxSum = 0.0;
		requiredProduction = new ItemList();
		setBuildingTax(BASE_TAX_FACTOR);
		productionOverview = new BoardItemList();
		taxOverview = new BoardItemList();
		world = "";
		this.biome = biome;
		trader = new Trader();
		buildManager = new BuildManager();
		mapManager  = new MapManager(settleType,70,true);
		tradeManager = new TradeManager ();
		settleManager = new SettleManager ();
		treasureList =  new ArrayList<Item>();
		reputations = new ReputationList();
		setSignList(new SignPosList());
}
	
	
	/**
	 * 
	 * instances settlement with
	 * - without sequential ID
	 * @param id
	 * @param settleType
	 * @param name
	 * @param position
	 * @param owner
	 * @param isCapital
	 * @param barrack
	 * @param warehouse
	 * @param buildingList
	 * @param townhall
	 * @param bank
	 * @param resident
	 */
	public Settlement(int id, SettleType settleType, String name, 
			LocationData position, int ownerId,
			Boolean isCapital, Barrack barrack, Warehouse warehouse,
			BuildingList buildingList, Townhall townhall, Bank bank,
			Resident resident, String world, Biome biome, long age,
			ItemPriceList priceList,
			int kingdomId,
			int lehenId
			)
	{
		this.id = id;
		this.age        = age;
		this.settleType = settleType;
		this.name = name;
		this.ownerId = ownerId;
		this.setKingdomId(kingdomId);
		this.tributId = lehenId;
		this.isCapital = isCapital;
		this.position = position;
		this.barrack = barrack;
		this.barrack.setPowerMax(ConfigBasis.defaultPowerMax(settleType));
		this.warehouse = warehouse;
		this.buildingList = buildingList;
		this.townhall = townhall;
		this.bank = bank;
		this.resident = resident;
		isEnabled   = true;
		isActive    = true;
//		foodConsumCounter = 0.0;
		sales = 0.0;
		taxSum = 0.0;
		requiredProduction = new ItemList();
		setBuildingTax(BASE_TAX_FACTOR);
		productionOverview = new BoardItemList();
		taxOverview = new BoardItemList();
		this.world = world;
		this.setBiome(biome);
		trader = new Trader();
		buildManager = new BuildManager();
		mapManager  = new MapManager(settleType,70,true);
		tradeManager = new TradeManager (priceList);
		settleManager = new SettleManager ();
		treasureList =  new ArrayList<Item>();
		reputations = new ReputationList();
		setSignList(new SignPosList());
}

	/**
	 * Klassenmethode zum auslesen des Instanzen counter
	 * @return
	 */
	public static int getCounter()
	{
		return COUNTER;
	}

	/**
	 * Klassenmethode zum setzen des Instanzen counter
	 * sequential instances counter
	 * @param iD
	 */
	public static void initCounter(int iD)
	{
		COUNTER = iD;
	}
	
//	public void setLogList(LogList logList)
//	{
//		this.logList = logList;
//	}
	
	public Boolean isEnabled()
	{
		return isEnabled;
	}
	
	public Boolean isActive()
	{
		return isActive;
	}
	
	public void setIsActive(boolean value)
	{
		this.isActive = value;
	}
	

	
	public void initSettlement(ItemPriceList priceList)
	{
		tradeManager.setPriceList(priceList);
		warehouse.setItemMax(ConfigBasis.calcItemMax( buildingList,  warehouse,  settleType));
		setSettlerMax();
		setWorkerNeeded();
}
	
	/**
	 * actual number of the settlement
	 * @return
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Set the actual number of the settlement
	 * only be useful to initialize a stored settlement
	 * @param id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	public SettleType getSettleType()
	{
		return settleType;
	}

	public void setSettleType(SettleType settleType)
	{
		this.settleType = settleType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(int ownerId)
	{
		this.ownerId = ownerId;
	}

	public Boolean getIsCapital()
	{
		return isCapital;
	}

	public void setIsCapital(Boolean isCapital)
	{
		this.isCapital = isCapital;
	}

	public Barrack getBarrack()
	{
		return barrack;
	}

//	public void setBarrack(Barrack barrack)
//	{
//		this.barrack = barrack;
//	}

	/**
	 * give power of settlement = sum of barrack and units
	 * @return
	 */
	public int getPower()
	{
		int power = barrack.getPower();
		for (NpcData unit : barrack.getUnitList())
		{
			power = power + unit.getPower();
		}
		
		return power;
	}
	
	/**
	 * setup a standard defender BattlePlacement
	 * @return
	 */
	public BattlePlacement getDefenders()
	{
//		UnitFactory unitFactory = new UnitFactory();
		BattlePlacement units = new BattlePlacement();

		UnitList unitList = new UnitList();
		for (NpcData unit : barrack.getUnitList())
		{
			unitList.add(unit);
		}
//		unitList.add(unitFactory.erzeugeUnit(UnitType.MILITIA));
//		unitList.add(unitFactory.erzeugeUnit(UnitType.MILITIA));
//		unitList.add(unitFactory.erzeugeUnit(UnitType.MILITIA));
//		unitList.add(unitFactory.erzeugeUnit(UnitType.MILITIA));
//		unitList.add(unitFactory.erzeugeUnit(UnitType.MILITIA));
//		unitList.add(unitFactory.erzeugeUnit(UnitType.MILITIA));
//		unitList.add(unitFactory.erzeugeUnit(UnitType.MILITIA));
		units.setPlaceUnit(BattleFieldPosition.CENTER, unitList);

		return units;
	}

	public Warehouse getWarehouse()
	{
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse)
	{
		this.warehouse = warehouse;
	}

	public BuildingList getBuildingList()
	{
		return buildingList;
	}

	/**
	 * 
	 * @param buildingList
	 */
	public void setBuildingList(BuildingList buildingList)
	{
		this.buildingList = buildingList;
		this.checkBuildingType();

	}

	/**
	 * expand buildingList with values of newBuildingList
	 * instances buildingList if not present
	 * @param newBuildingList
	 */
	public void addBuildingList(BuildingList newBuildingList)
	{
		for (Building b : newBuildingList.values())
		{
			buildingList.addBuilding(b);
		}
		this.checkBuildingType();
	}
	
	
	
	public void initTreasureList()
	{
		treasureList.clear();
		treasureList.add(new Item(Material.WOOD.name(),1));
		treasureList.add(new Item(Material.STICK.name(),1));
		treasureList.add(new Item(Material.IRON_ORE.name(),1));
		treasureList.add(new Item(Material.IRON_INGOT.name(),1));
		treasureList.add(new Item(Material.LOG.name(),1));
		treasureList.add(new Item(Material.SEEDS.name(),1));
		treasureList.add(new Item(Material.DIRT.name(),1));
//		treasureList.add(new Item(Material.CARROT.name(),1));
		treasureList.add(new Item(Material.BREAD.name(),1));
		treasureList.add(new Item(Material.WATER.name(),1));
		treasureList.add(new Item(Material.WOOL.name(),1));
//		treasureList.add(new Item(Material.EMERALD.name(),1));
		treasureList.add(new Item(Material.SAND.name(),1));
		treasureList.add(new Item(Material.GOLD_NUGGET.name(),1));
		treasureList.add(new Item(Material.WOOD.name(),1));
		treasureList.add(new Item(Material.STICK.name(),1));
		treasureList.add(new Item(Material.IRON_ORE.name(),1));
		treasureList.add(new Item(Material.IRON_INGOT.name(),1));
		treasureList.add(new Item(Material.LOG.name(),1));
		treasureList.add(new Item(Material.SEEDS.name(),1));
		treasureList.add(new Item(Material.DIRT.name(),1));
//		treasureList.add(new Item(Material.CARROT.name(),1));
		treasureList.add(new Item(Material.BREAD.name(),1));
		treasureList.add(new Item(Material.WATER.name(),1));
		treasureList.add(new Item(Material.WOOL.name(),1));
//		treasureList.add(new Item(Material.EMERALD.name(),1));
		treasureList.add(new Item(Material.SAND.name(),1));
		treasureList.add(new Item(Material.GOLD_NUGGET.name(),1));
		treasureList.add(new Item(Material.WOOD.name(),1));
		treasureList.add(new Item(Material.STICK.name(),1));
		treasureList.add(new Item(Material.IRON_ORE.name(),1));
		treasureList.add(new Item(Material.IRON_INGOT.name(),1));
		treasureList.add(new Item(Material.LOG.name(),1));
		treasureList.add(new Item(Material.SEEDS.name(),1));
		treasureList.add(new Item(Material.DIRT.name(),1));
//		treasureList.add(new Item(Material.CARROT.name(),1));
		treasureList.add(new Item(Material.BREAD.name(),1));
		treasureList.add(new Item(Material.WATER.name(),1));
		treasureList.add(new Item(Material.WOOL.name(),1));
//		treasureList.add(new Item(Material.EMERALD.name(),1));
		treasureList.add(new Item(Material.SAND.name(),1));
		treasureList.add(new Item(Material.GOLD_NUGGET.name(),1));
		treasureList.add(new Item(Material.WOOD.name(),1));
		treasureList.add(new Item(Material.STICK.name(),1));
		treasureList.add(new Item(Material.IRON_ORE.name(),1));
		treasureList.add(new Item(Material.IRON_INGOT.name(),1));
		treasureList.add(new Item(Material.LOG.name(),1));
		treasureList.add(new Item(Material.SEEDS.name(),1));
		treasureList.add(new Item(Material.DIRT.name(),1));
//		treasureList.add(new Item(Material.CARROT.name(),1));
		treasureList.add(new Item(Material.BREAD.name(),1));
		treasureList.add(new Item(Material.WATER.name(),1));
		treasureList.add(new Item(Material.WOOL.name(),1));
//		treasureList.add(new Item(Material.EMERALD.name(),1));
		treasureList.add(new Item(Material.SAND.name(),1));
		treasureList.add(new Item(Material.GOLD_NUGGET.name(),1));
	}
	
	
	/**
	 * recalculate settlement parameter based on buildings
	 * - Warehouse.maxItem from actual buildingList with  ConfigBasis.getWarehouseItemMax
	 * - Barrack.maxUnit
	 * - Trader.maxOrder
	 */
	public void checkBuildingType()
	{
		this.barrack.setUnitMax(buildingList.getMaxUnit());
		this.warehouse.setItemMax(buildingList.getMaxStorage());
		this.trader.setOrderMax(buildingList.getMaxOrder());
//		for(Building building : this.buildingList.values())
//		{
//			switch(building.getBuildingType())
//			{
//			case HALL: 
//				this.townhall.setIsEnabled(true);
//				this.warehouse.setItemMax(ConfigBasis.calcItemMax(this.buildingList, this.warehouse, this.getSettleType()));
//				break;
//			case WAREHOUSE :
//				this.warehouse.setItemMax(ConfigBasis.calcItemMax(this.buildingList, this.warehouse, this.getSettleType()));
//				break;
//			case TRADER :
//				this.trader.setActive(true);
//				this.trader.setEnabled(true);
//				this.warehouse.setItemMax(ConfigBasis.calcItemMax(this.buildingList, this.warehouse, this.getSettleType()));
//				this.trader.setOrderMax(this.trader.getOrderMax()+5);
//				break;
//			case GUARDHOUSE :
//				this.barrack.setUnitMax(this.barrack.getUnitMax() + building.getUnitSpace());
//				break;
//			case CASERN :
//				this.barrack.setUnitMax(this.barrack.getUnitMax() + building.getUnitSpace());
//				break;
//			case GARRISON :
//				this.barrack.setUnitMax(this.barrack.getUnitMax() + building.getUnitSpace());
//				break;
//			case WATCHTOWER :
//			case DEFENSETOWER :
//			case BARRACK :
//			case TOWER :
//			case HEADQUARTER :
//			case KEEP:
//			case CASTLE:
//			case STRONGHOLD :
//			case PALACE:
//				this.barrack.setUnitMax(this.barrack.getUnitMax() + building.getUnitSpace());
//				break;
//			default :
//				break;
//			}
//		}
	}
	

	public Townhall getTownhall()
	{
		return townhall;
	}

	public void setTownhall(Townhall townhall)
	{
		this.townhall = townhall;
	}

	public Bank getBank()
	{
		return bank;
	}

	public void setBank(Bank bank)
	{
		this.bank = bank;
	}

	public Resident getResident()
	{
		return resident;
	}

	public void setResident(Resident resident)
	{
		this.resident = resident;
	}

	public LocationData getPosition()
	{
		return position;
	}

	public void setPosition(LocationData position)
	{
		this.position = position;
	}
	
	public Double getBuildingTax()
	{
		return buildingTax;
	}

	public void setBuildingTax(Double buildingTax)
	{
		this.buildingTax = buildingTax;
	}

//	public double getFoodConsumCounter()
//	{
//		return foodConsumCounter;
//	}

	public double getEntertainFactor()
	{
		return EntertainFactor;
	}

	public double getFoodFactor()
	{
		return FoodFactor;
	}

	public double getSettlerFactor()
	{
		return SettlerFactor;
	}

	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
	}

	public Trader getTrader()
	{
		return trader;
	}

	public void setTrader(Trader trader)
	{
		this.trader = trader;
	}

	public BoardItemList getProductionOverview()
	{
		return productionOverview;
	}

	public BoardItemList getTaxOverview()
	{
		return taxOverview;
	}
	
//	public LogList getLogList()
//	{
//		return logList;
//	}

	/**
	 * Create a new settlement by SettleType 
	 * and regionTypes List <String, String>  for building list
	 * 
	 * @param settleType
	 * @param settleName
	 * @param owner
	 * @param regionTypes
	 * @return
	 */
	public static Settlement createSettlement(LocationData position, SettleType 
											settleType, String settleName, int ownerId, 
											HashMap<String,String> regionTypes, 
											HashMap<String,String> regionBuildings,
											Biome biome) //,LogList logList)
	{
		if (settleType != SettleType.NONE)
		{
			Settlement settlement = new Settlement(ownerId,position, settleType, settleName,biome); //,logList);
//			BuildingList buildingList = new BuildingList();
			int regionId = 0;
			String BuildingTypeName = "";
			String regionType = "";
			boolean isRegion = false;
			BuildingList bList = new BuildingList();
			for (String region : regionTypes.keySet())
			{
				regionId = Integer.valueOf(region);
				
				regionType = regionTypes.get(region);
				
				BuildingTypeName   = regionBuildings.get(region);
				isRegion = true;
				bList.addBuilding(Building.createRegionBuilding(BuildingTypeName, regionId, regionType, isRegion));
			}
			settlement.setBuildingList(bList);
			settlement.checkBuildingType();
			settlement.setStoreCapacity();

//			settlement.setBuildingList(buildingList);
			return settlement;
		}
		return null;
	}

	
	/**
	 * check amount in warehouse for take items
	 * if not set requiredItemList
	 * @param prodFactor
	 * @param items
	 * @return
	 */
	public boolean checkStock(double prodFactor, ItemList items)
	{
		int iValue = 0;
		// Check amount in warehouse
		boolean isStock = true;
		for (String itemRef : items.keySet())
		{
			iValue = (int) (items.getValue(itemRef)*prodFactor);
			if (this.warehouse.getItemList().getValue(itemRef) < iValue)
			{
//				System.out.println("miss: "+itemRef+":"+iValue);
				isStock = false;
				if (requiredProduction.containsKey(itemRef))
				{
					requiredProduction.depositItem(itemRef, iValue);
				} else
				{
					requiredProduction.depositItem(itemRef, iValue);
//					requiredProduction.addItem(itemRef, iValue);
				}
			}
		}
		return isStock;
	}
	
	/**
	 * consum items from warehouse
	 * @param prodFactor
	 * @param items
	 */
	public void consumStock(double prodFactor, ItemList items)
	{
		int iValue = 0;
		for (Item item : items.values())
		{
			iValue = (int)((double) item.value() *prodFactor);
			this.getWarehouse().withdrawItemValue(item.ItemRef(), iValue);
//			System.out.println("Withdraw-"+item.ItemRef()+":"+iValue+":"+prodFactor);
		}
	}
	
	@SuppressWarnings("unused")
	private void checkDecay()
	{
		int wheat = warehouse.getItemList().getValue("WHEAT");
		// berechnet mindestBestand
		wheat = wheat - (resident.getSettlerMax()*5);
		if (wheat > 0)
		{
			int decay = wheat / 100;
			warehouse.withdrawItemValue("WHEAT", decay);
		}
	}
	
	public int getUsedBuildingCapacity()
	{
		return warehouse.getUsedCapacity();
	}
	
	private int getFoundCapacity()
	{
		int usedBuildCap = getUsedBuildingCapacity();
		if ( usedBuildCap > warehouse.getItemCount())
		{
			return warehouse.getItemMax() - usedBuildCap - 512;
		} else
		{
			return warehouse.getItemMax() - warehouse.getItemCount() - 512;
		}
	}

	
	private void addTreasure2List(ServerInterface server, Biome biome, Material mat)
	{
		int matFactor  = 0;
		matFactor = server.getBioneFactor( biome, mat);
		if (matFactor > 0)
		{
			int anz = matFactor / 25;
			for(int i=0; i < anz; i++)
			{
				treasureList.add(new Item(mat.name(), 1));
			}
		}
		if (matFactor < 0)
		{
			int anz = matFactor / -25;
			for(int i=0; i < anz; i++)
			{
				int index = -1;
				for (int j=0; j < treasureList.size(); j++)
				{
					if (treasureList.get(j).ItemRef()== mat.name())
					{
						index = j;
					}
				}
				if (index > -1)
				{
					Item item = treasureList.get(index);
					treasureList.remove(item);
				}
				
			}
		}
		
	}
	
	public void expandTreasureList(Biome biome, ServerInterface server)
	{
		addTreasure2List(server, biome, Material.WHEAT);
		addTreasure2List(server, biome, Material.SEEDS);
		addTreasure2List(server, biome, Material.COBBLESTONE);
		addTreasure2List(server, biome, Material.LOG);
		addTreasure2List(server, biome, Material.WOOL);
		addTreasure2List(server, biome, Material.GOLD_NUGGET);
		addTreasure2List(server, biome, Material.LEATHER);
		addTreasure2List(server, biome, Material.RAW_BEEF );
		addTreasure2List(server, biome, Material.PORK );
		addTreasure2List(server, biome, Material.RAW_CHICKEN );
		addTreasure2List(server, biome, Material.FEATHER );
		addTreasure2List(server, biome, Material.RAW_FISH );
//		addTreasure2List(server, biome, Material.EMERALD );
		addTreasure2List(server, biome, Material.RED_MUSHROOM ); 
		addTreasure2List(server, biome, Material.BROWN_MUSHROOM ); 
		addTreasure2List(server, biome, Material.IRON_ORE );
		addTreasure2List(server, biome, Material.COAL_ORE );
		addTreasure2List(server, biome, Material.DIAMOND_ORE );
//		addTreasure2List(server, biome, Material.EMERALD_ORE );
		addTreasure2List(server, biome, Material.REDSTONE_ORE );
		addTreasure2List(server, biome, Material.LAPIS_ORE );
		addTreasure2List(server, biome, Material.GOLD_ORE );
	}

	
	private String getFoundItem()
	{
		int Dice = treasureList.size()-1;
		int wuerfel = (int) (Math.random()*Dice+1);
		return treasureList.get(wuerfel).ItemRef();
//		switch (wuerfel)
//		{
//		case 1 : return Material.WOOD.name();
//		case 2 : return Material.STICK.name();
//		case 3 : return Material.IRON_ORE.name();
//		case 4 : return Material.IRON_INGOT.name();
//		case 5 : return Material.LOG.name();
//		case 6 : return Material.SEEDS.name();
//		case 7 : return Material.WOOD.name();
//		case 8 : return Material.DIRT.name();
//		case 9 : return Material.SEEDS.name();
//		case 10 : return Material.CARROT.name();
//		case 11 : return Material.BREAD.name();
//		case 12 : return Material.WATER.name();
//		case 13 : return Material.WOOL.name();
//		case 14 : return Material.EMERALD.name();
//		case 15 : return Material.DIRT.name();
//		case 16 : return Material.SAND.name();
//		case 20 : return Material.GOLD_NUGGET.name();
//		default :
//			return Material.AIR.name();
//		}
	}
	
	private void getTreasue(ServerInterface server, NpcData npc)
	{
		int Dice = 100;
		int wuerfel = 0;
		String foundItem = "";

		wuerfel = (int) (Math.random()*Dice+1);
		if (wuerfel < 8)
		{
			foundItem = getFoundItem();
			if (foundItem != Material.AIR.name())
			{
//				System.out.println("Treasure: "+foundItem);
				if (getFoundCapacity() > 1)
				{
					npc.depositMoney(server.getItemPrice(foundItem));
					warehouse.depositItemValue(foundItem, 1);
					productionOverview.addCycleValue(foundItem, 1);
				}
			}
		}
	}
	
	/**
	 * check for treasure and give child money when hungry
	 *  
	 * @param server
	 */
	private void checkFoundItems(ServerInterface server)
	{
		if (getFoundCapacity() < resident.getSettlerCount()-townhall.getWorkerCount())
		{
			return;
		}
		int notWorker = resident.getSettlerCount()-townhall.getWorkerCount();
		NpcList homeNpc = resident.getNpcList(); //.getSettleWorker();
		NpcList treasureNpc = new NpcList();
//		Iterator<NpcData> npcIterator = homeNpc.values().iterator();
		for (NpcData npc : homeNpc.values())
		{
			if (npc.getWorkBuilding() == 0)
			{
				treasureNpc.putNpc(npc);
			}
			// give hungry chil money from settlement
			if (npc.isChild())
			{
				if (npc.getMoney() <= 1.0)
				{
					if (npc.hungerCounter < ConfigBasis.HUNGER_BEGGAR)
					{
						if (bank.getKonto() > resident.getSettlerCount())
						{
//							System.out.println("ChildBeggar"+npc.getId());
							bank.withdrawKonto(1.0, "ChildBeggar", this.id);
							npc.depositMoney(2.0);
						}
					}
				}
			}
		}

		for (NpcData npc : treasureNpc.values())
		{
			getTreasue(server, npc);
			if (npc.getNpcType() == NPCType.BEGGAR)
			{
				getTreasue(server, npc);
			}

		}
		
	}
	
	/**
	 * get Taxe from buildings and deposit in bank
	 * @param server
	 */
	public void getTaxe(ServerInterface server)
	{
		double taxSum = 0;
		for (Building building : buildingList.values())
		{
			if (building.isEnabled())
			{
				taxSum = taxSum + building.getTaxe(server, this.resident.getSettlerCount());
			}
		}
		taxSum = taxSum + townhall.getWorkerCount() * ConfigBasis.SETTLER_TAXE;
//		taxSum = resident.getSettlerCount() * SETTLER_TAXE;
		System.out.println("Tax Sum : "+String.valueOf(taxSum));
		bank.addKonto(taxSum,"TAX", getId());
	}
	
	/**
	 * calculate settlerMax from buildings and set 
	 */
	public void setSettlerMax()
	{
		int settlerMax = 5;
		for (Building building : buildingList.values())
		{
			if (building.isEnabled())
			{
				settlerMax = settlerMax + building.getSettler();
			}
		}
		resident.setSettlerMax(settlerMax);
	}
	
	/**
	 * calculate happines for entertaiment
	 * @return happiness
	 */
	private double calcEntertainment()
	{
		int tavernNeeded = (resident.getSettlerCount() / ConfigBasis.ENTERTAIN_SETTLERS);
		int tavernCount = 0;
		double factor = 0.0;
		for (Building building : buildingList.values())
		{
			if (building.isEnabled())
			{
				if (building.getBuildingType() == BuildPlanType.TAVERNE)
				{
					tavernCount++;
				}
			}
		}
		if (tavernCount > 0)
		{
			if (tavernNeeded >= tavernCount)
			{
			  factor = ((double) tavernCount  / (double)tavernNeeded );
			} else
			{
				factor = 0.5;
			}
		}
		
		return factor;
	}
	
	/**
	 * calculate the whole happines for the different influences 
	 */
	public void doHappiness(DataInterface data)
	{
		double sumDif = 0.0;
//		double resiDif = 0.0;
		EntertainFactor = calcEntertainment();
//		SettlerFactor = resident.calcResidentHappiness(SettlerFactor); //resident.getHappiness());
//		FoodFactor = 
		consumeFood(); //SettlerFactor);
		sumDif = EntertainFactor + SettlerFactor + FoodFactor;
//		logList.addHappiness("CYCLE", getId(), sumDif, EntertainFactor, SettlerFactor, FoodFactor, "CraftManager", getAge());
//		resident.setHappiness(sumDif);
		resident.doSettlerCalculation(buildingList,data);
		this.getResident().setNpcList(data.getNpcs().getSubList(this.id));
//		logList.addSettler("CYCLE", getId(), resident.getSettlerCount(), resident.getBirthrate(), resident.getDeathrate(), "CraftManager", getAge());
		UnitFactory unitFactory = new UnitFactory();
		for (NpcData unit : barrack.getUnitList())
		{
			ItemList ingredients = unit.getUnit().getConsumItems();
			double prodFactor  = 1.0;
			if (checkStock(prodFactor, ingredients))
			{
				consumStock(prodFactor, ingredients);
				if (unit.getHappiness() < 1.0)
				{
					unit.getUnit().addHappiness(0.1);
				}
			} else
			{
				if (unit.getHappiness() > -1.0)
				{
					unit.getUnit().addHappiness(-0.1);
				}
			}
		}
		int value = (int) (resident.getSettlerCount() * resident.getHappiness()); 
		barrack.addPower(value);
	}
	
	
	private double checkConsume(String foodItem , int amount, int required, double happyFactor, NpcData npc, NpcData parent)
	{
		double factor = 0.0;
		double cost = tradeManager.getPriceList().getBasePrice(foodItem);
		// check for money of food
		if (parent.getMoney() < cost)
		{
//			System.out.println("No food money !"+npc.getId());
			amount = 0;
		}
		if (required > amount)
		{	
			// keine Versorgung
			if (resident.getSettlerCount() > 5)
			{
//				System.out.println("keine Versorgung :"+npc.getId());
//				factor = npc.hungerCounter + ((double)required / (double)resident.getSettlerMax()) * -1.0;
				factor = -0.1;
				if (npc.foodConsumCounter > MIN_FOODCONSUM_COUNTER)
				{
					npc.foodConsumCounter = npc.foodConsumCounter + factor;
				}
				// setzte required food
				requiredProduction.depositItem(foodItem, required);
				npc.hungerCounter = npc.hungerCounter + factor ; // hungerCounter + factor;
			}
		} else
		{
			npc.hungerCounter = 0.0;
			parent.withdrawMoney(cost);
			bank.depositKonto(cost, "Food", this.id);
			warehouse.withdrawItemValue(foodItem, required);
//			System.out.println(foodItem+":"+required);
			productionOverview.addCycleValue(foodItem, (required* -1));
//			if (npc.foodConsumCounter > MIN_FOODCONSUM_COUNTER)
//			{
			npc.foodConsumCounter = npc.foodConsumCounter + (double)required; //((double)resident.getSettlerCount() / 20.0);
			if (npc.foodConsumCounter > 0)
			{
				npc.foodConsumCounter = 0.0; //npc.foodConsumCounter + (double)required; //((double)resident.getSettlerCount() / 20.0);
			}
//			}
			if (npc.foodConsumCounter < 0.0)
			{
				
				// ziemlich tief !! -5.0
				if (npc.getHappiness() < MIN_FOODCONSUM_COUNTER)
				{
					factor = 0.1; //changed
				} else
				{
					factor = happyFactor;
				}
//				System.out.println("Min Food Consum"+factor);
			} else
			{
				if (npc.getHappiness() < 0.6)
				{
					if (resident.getSettlerMax() > resident.getSettlerCount())
					{
						factor = happyFactor;
					} else
					{
						factor = happyFactor/2;
					}
//					System.out.println("Low Happiness "+factor);
				} else
				{
//					System.out.println("Normal Happiness"+factor);
					factor = happyFactor;
				}
				npc.foodConsumCounter = 0;
			}
		}
		return factor;
	}
	
	
	private void  checkNpcFeed(NpcData npc, int required, NpcData parent)
	{
		double factor = 0.0; 
		String foodItem = "";
		int amount = 0;
		if (npc.getNpcType() != NPCType.CHILD)
		{
			// Fish consume before wheat consum
			// if not enough bread then the rest will try to consum wheat
			foodItem = Material.COOKED_FISH.name();
			amount = warehouse.getItemList().getValue(foodItem);
			if (amount > 0)
			{
				// check for money for food
				if (parent.getMoney() > tradeManager.getPriceList().getBasePrice(foodItem))
				{
					if (amount > required)
					{
						factor = factor + checkConsume(foodItem, amount, required, 0.3,npc,parent);
						
					} else
					{
						required = required - amount;
						factor = factor + checkConsume(foodItem, amount, amount, 0.3, npc,parent);
					}
					npc.setHappiness(npc.getHappiness() + factor);
					return ;
					}
			}
			// Mushroom Soup consume before wheat or mushroom consum
			// if not enough bread then the rest will try to consum wheat
			foodItem = "MUSHROOM_SOUP";
			amount = warehouse.getItemList().getValue(foodItem);
			if (amount > 0)
			{
				// check for money for food
				if (parent.getMoney() > tradeManager.getPriceList().getBasePrice(foodItem))
				{
					if (amount > required)
					{
						factor = factor + checkConsume(foodItem, amount, required,0.3, npc,parent);
		
					} else
					{
						required = required - amount;
						factor = factor + checkConsume(foodItem, amount, amount,0.3, npc,parent);
					}
					npc.setHappiness(npc.getHappiness() + factor);
					return ;
				}
			}
			// Mushroom consume before wheat consum
			// if not enough bread then the rest will try to consum wheat
			foodItem = "RED_MUSHROOM";
			amount = warehouse.getItemList().getValue(foodItem);
			if (amount > 0)
			{
				// check for money for food
				if (parent.getMoney() > tradeManager.getPriceList().getBasePrice(foodItem))
				{
					if (amount > required)
					{
						factor = factor + checkConsume(foodItem, amount, required, 0.0, npc,parent);
						
					} else
					{
						required = required - amount;
						factor = factor + checkConsume(foodItem, amount, amount, 0.0, npc,parent);
					}
					npc.setHappiness(npc.getHappiness() + factor);
					return ;
				}
			}
			// Mushroom consume before wheat consum
			// if not enough bread then the rest will try to consum wheat
			foodItem = "BROWN_MUSHROOM";
			amount = warehouse.getItemList().getValue(foodItem);
			if (amount > 0)
			{
				// check for money for food
				if (parent.getMoney() > tradeManager.getPriceList().getBasePrice(foodItem))
				{
					if (amount > required)
					{
						factor = factor + checkConsume(foodItem, amount, required, 0.0, npc,parent);
						
					} else
					{
						required = required - amount;
						factor = factor + checkConsume(foodItem, amount, amount, 0.0, npc,parent);
					}
					npc.setHappiness(npc.getHappiness() + factor);
					return ;
				}
			}
			// Bread consume before wheat consum
			// if not enough bread then the rest will try to consum wheat
			foodItem = "BREAD";
			amount = warehouse.getItemList().getValue(foodItem);
			if (amount > 0)
			{
				// check for money for food
				if (parent.getMoney() > tradeManager.getPriceList().getBasePrice(foodItem))
				{
					if (amount > required)
					{
						factor = factor + checkConsume(foodItem, amount, required, 0.5, npc,parent);
						
					} else
					{
						required = required - amount;
						factor = factor + checkConsume(foodItem, amount, amount, 0.5, npc,parent);
					}
		//			System.out.println("BREAD "+factor+":"+(npc.getHappiness() + factor));
					npc.setHappiness(npc.getHappiness() + factor);
					return ;
				}
			}
		}
		//  Wheat is the last consum item
		//  without wheat the residents are very unhappy
		foodItem = "WHEAT";
		amount = warehouse.getItemList().getValue(foodItem);
		if (amount > required)
		{
			factor = factor + checkConsume(foodItem, amount, required,0.0, npc,parent);
			
		} else
		{
			factor = factor + checkConsume(foodItem, amount, required, 0.0, npc,parent);
		}
		npc.setHappiness(npc.getHappiness() + factor);
//		return factor;
	}
	
	/**
	 * calculate happines for the food supply of the settlers
	 * - no influence if fodd supply is guarantee 
	 * - haevy influence if food supply too low.
	 * the settlers are all supplied or none  
	 * @param oldFactor
	 * @return happiness factor of food supply 
	 */
	private void consumeFood() //double oldFactor)
	{
//		int required = resident.getSettlerCount();
		
//		int notWorker = resident.getSettlerCount()-townhall.getWorkerCount();
		NpcList homeNpc = resident.getNpcList(); //.getSettleWorker();
//		Iterator<NpcData> npcIterator = homeNpc.values().iterator();
		for (NpcData npc : homeNpc.values())
		{
			if (npc.isAlive())
			{
				if (npc.isChild())
				{
					NpcData parent = homeNpc.get(npc.getFather());
					if (parent != null)
					{
						if (parent.isAlive() == false)
						{
							parent = null;
						}
					}
					if (parent == null)
					{
						parent = homeNpc.get(npc.getMother());
						if (parent != null)
						{
							if (parent.isAlive() == false)
							{
								parent = null;
							}
						}
					}
					if (parent != null)
					{	
						if (parent.getMoney() < 1.0)
						{
//							System.out.println("Child Money");
							checkNpcFeed(npc, 1, npc);
						} else
						{
//							System.out.println("Child Parent");
							checkNpcFeed(npc, 1, parent);
						}
					} else
					{
//						System.out.println("Child ");
						checkNpcFeed(npc, 1, npc);
					}
						
				} else
				{
					if (npc.getNpcType() == NPCType.MANAGER)
					{
						double salery = 3.0;
						bank.withdrawKonto(salery, "MANAGER", this.id);
						npc.depositMoney(salery);
					}
					checkNpcFeed(npc, 1,npc);
					
				}
			}
		}
		
//		return factor;
	}
	
	/**
	 * calculte needed worker for buildings
	 */
	public void setWorkerNeeded()
	{
		int workerSum = 0;
		for (Building building : buildingList.values())
		{
			if (building.isEnabled())
			{
				workerSum = workerSum + building.getWorkerNeeded();
			}
		}
		townhall.setWorkerNeeded(workerSum);
	}

	/**
	 * calculate the actual stack size for the warehouse items
	 */
	private void setStoreCapacity()
	{
		warehouse.setStoreCapacity();			
	}
	
	/**
	 * check the storage capacity and the maxStorage for each item based on :
	 * - item store < maxSlots / 4
	 * - item store <=  item.capacity * 2;
	 * 
	 * @param server
	 * @param building
	 * @return
	 */
	private boolean checkStoreCapacity(ServerInterface server, Building building)
	{
		String itemRef = "";
		boolean isResult = true;
		boolean isMulti = false;
		ItemArray products = building.buildingProd(server, building.getHsRegionType());
		for (Item item : products)
		{
			itemRef = item.ItemRef();
			// check MaxStorage
			if ((warehouse.getItemList().getValue(itemRef)/64) < (warehouse.getItemMax() / 64 / 5))
			{
				// check if multi items produce 
				if (products.size() > 1)
				{
					isMulti = true;
				}
			} else
			{
//				System.out.println(getId()+" :MaxItems "+itemRef+":"+(warehouse.getItemList().getValue(itemRef)/64) +"/"+ (warehouse.getItemMax() / 64 / 4));
				isResult = false;
			}
		}
		// check if one of multi items can be produce
		if (isMulti)
		{
			isResult = isMulti;
		}
		return isResult;
	}
	
	
	private void resetWorkerBuild(NpcList homeNpc)
	{
		Iterator<NpcData> npcIterator = homeNpc.values().iterator();
		while (npcIterator.hasNext())
		{
			npcIterator.next().setWorkBuilding(0); 
		}
	}
	
	/**
	 * set workers to buildings. no priority
	 * @param workerSum
	 * @return workers without workingplace
	 */
	public int setWorkerToBuilding(int workerSum)
	{
//		Iterator<Building> buildingIterator = buildingList.values().iterator(); 
		NpcList homeNpc = resident.getNpcList().getSettleWorker();
		Iterator<NpcData> npcIterator = homeNpc.values().iterator();
		resetWorkerBuild(homeNpc);
//		System.out.println(" hasNext: "+npcIterator.hasNext());
		for (Building building : buildingList.getSubList(BuildPlanType.WHEAT).values())
		{
			int installed = 0;
			while ((installed < building.getWorkerNeeded()) && npcIterator.hasNext())
			{
				NpcData npc = npcIterator.next();
				if (npc.getNpcType() != NPCType.MANAGER)
				{
					npc.setWorkBuilding(building.getId());
	//				System.out.println(building.getBuildingType()+" : "+npc.getId());
					installed++;
				}
			}
		}

		for (Building building : buildingList.getGroupSubList(ConfigBasis.BUILDPLAN_GROUP_PRODUCTION).values())
		{
			int installed = 0;
			if (building.getBuildingType() != BuildPlanType.WHEAT)
			{
				while ((installed < building.getWorkerNeeded()) && npcIterator.hasNext())
				{
					NpcData npc = npcIterator.next();
					if (npc.getNpcType() != NPCType.MANAGER)
					{
						npc.setWorkBuilding(building.getId());
		//				System.out.println(building.getBuildingType()+" : "+npc.getId());
						installed++;
					}
				}
			}
		}
		
		for (Building building : buildingList.getSubList(BuildPlanType.FARMHOUSE).values())
		{
			int installed = 0;
			while ((installed < building.getWorkerNeeded()) && npcIterator.hasNext())
			{
				NpcData npc = npcIterator.next();
				if (npc.getNpcType() != NPCType.MANAGER)
				{
					npc.setWorkBuilding(building.getId());
	//				System.out.println(building.getBuildingType()+" : "+npc.getId());
					installed++;
				}
			}
		}
		
		for (Building building : buildingList.getSubList(BuildPlanType.FARM).values())
		{
			int installed = 0;
			while ((installed < building.getWorkerNeeded()) && npcIterator.hasNext())
			{
				NpcData npc = npcIterator.next();
				if (npc.getNpcType() != NPCType.MANAGER)
				{
					npc.setWorkBuilding(building.getId());
	//				System.out.println(building.getBuildingType()+" : "+npc.getId());
					installed++;
				}
			}
		}


		for (Building building : buildingList.getGroupSubList(ConfigBasis.BUILDPLAN_GROUP_EQUIPMENT).values())
		{
			int installed = 0;
			while ((installed < building.getWorkerNeeded()) && npcIterator.hasNext())
			{
				NpcData npc = npcIterator.next();
				if (npc.getNpcType() != NPCType.MANAGER)
				{
					npc.setWorkBuilding(building.getId());
	//				System.out.println(building.getBuildingType()+" : "+npc.getId());
					installed++;
				}
			}
		}

		for (Building building : buildingList.getGroupSubList(ConfigBasis.BUILDPLAN_GROUP_ENTERTAIN).values())
		{
			int installed = 0;
			while ((installed < building.getWorkerNeeded()) && npcIterator.hasNext())
			{
				NpcData npc = npcIterator.next();
				if (npc.getNpcType() != NPCType.MANAGER)
				{
					npc.setWorkBuilding(building.getId());
	//				System.out.println(building.getBuildingType()+" : "+npc.getId());
					installed++;
				}
			}
		}

		for (Building building : buildingList.getGroupSubList(ConfigBasis.BUILDPLAN_GROUP_TRADE).values())
		{
			int installed = 0;
			while ((installed < building.getWorkerNeeded()) && npcIterator.hasNext())
			{
				NpcData npc = npcIterator.next();
				if (npc.getNpcType() != NPCType.MANAGER)
				{
					npc.setWorkBuilding(building.getId());
	//				System.out.println(building.getBuildingType()+" : "+npc.getId());
					installed++;
				}
			}
		}
		// reset all other npc
//		System.out.println(" ClearNext: "+npcIterator.hasNext());
		while (npcIterator.hasNext())
		{
			npcIterator.next().setWorkBuilding(0);
		}
		
		int workerCount = 0;
		for (NpcData  npc : resident.getNpcList().values())
		{
			if (npc.getWorkBuilding() > 0)
			{
				workerCount++;
			}
		}
		townhall.setWorkerCount(workerCount);
		return workerSum-workerCount;
	}

	/**
	 * check if production is necessary for a building
	 * 
	 * @param server
	 */
	public void checkBuildingsEnabled(ServerInterface server)
	{
		for (Building building : buildingList.values())
		{
			if (building.getHsRegionType() != null)
			{	
				if (building.isActive())
				{
					// Pruefe ob StorageCapacitaet des Types ausgelastet ist
					switch (BuildPlanType.getBuildGroup(building.getBuildingType()))
					{
						case 200 : // normal production
							if (checkStoreCapacity(server, building))
							{
								building.setIsEnabled(true);
							} else
							{
								building.setIsEnabled(false);
								if (building.getBuildingType() == BuildPlanType.CABINETMAKER)
								{
									building.initIdle(5);
								}
							}
							// pruefe ob Stronghold region enabled sind
							server.checkRegionEnabled(building.getHsRegion());
							break;
						case 500: //unit production
							if (building.getMaxTrain() > 0)
							{
								building.setIsEnabled(true);
							} else
							{
								building.setIsEnabled(false);
							}

							break;
						default:  // other buildings are not effected
						break;	
					}
				}
			} else
			{
				System.out.println("BuildRegionType null :"+building.getId()+":"+building.getBuildingType());
				building.setIsEnabled(false);
			}
		}
	}

	/**
	 * 
	 * @return list of required items from last production cycle
	 */
	public ItemList getRequiredProduction()
	{
		return requiredProduction;
	}

	public void setWorkerSale(Building building, double account)
	{
		NpcList homeNpc = resident.getNpcList().getBuildingWorker(building.getId());
		if (homeNpc.size() > 0)
		{
			double value = account / homeNpc.size();
			for (NpcData npc : homeNpc.values())
			{
				if (value > 0.0)
				{
					npc.depositMoney(value);
				}
			}
		} else
		{
//			System.out.println("No NPC");
		}
	}
	
	/**
	 * get production get from producer buildings in the settlement
	 * each Building will separate calculate 
	 * @param server
	 */
	public void doProduce(ServerInterface server, DataInterface data)
	{
		// increment age of the Setlement in production cycles
		age++;
		double prodFactor = 1;
		int iValue = 0;
		double sale = 0.0;
		double cost = 0.0;
		double account = 0.0; 
		ItemArray products;
		ItemList ingredients;
		requiredProduction.clear();
		productionOverview.resetLastAll();
		setStoreCapacity();
		initTreasureList();
		expandTreasureList(getBiome(), server);
//		checkDecay();
		checkFoundItems(server);
		for (Building building : buildingList.values())
		{
			// setze defaultBiome auf Settlement Biome 
			if (building.getBiome() == null)
			{
				building.setBiome(biome);
			}
			building.setSales(0.0);
			building.addIdlleTime();
			if ((building.isEnabled())
				&& building.isIdleReady()	
				)
			{
				if ((BuildPlanType.getBuildGroup(building.getBuildingType())== 200)
					|| (BuildPlanType.getBuildGroup(building.getBuildingType())== 300))
				{
					sale = 0.0;
					cost = 0.0;
					account = 0.0;
					iValue = 0;
					products = building.produce(server);
//					for (Item item : products)
					if (products.size() > 0)
					{
						Item item = products.get(0);
						
						switch(building.getBuildingType())
						{
						case WORKSHOP:
							ingredients = server.getRecipe(item.ItemRef());
							ingredients.remove(item.ItemRef());
							prodFactor = server.getRecipeFactor(item.ItemRef(),this.biome, item.value());
//							System.out.println("WS " +item.ItemRef()+":"+item.value()+"*"+prodFactor);
							break;
						case BAKERY:
							if (building.isSlot())
							{
	//							System.out.println("SLOT "+item.ItemRef());
								ingredients = server.getRecipe(item.ItemRef());
								ingredients.remove(item.ItemRef());
							} else
							{
								ingredients = server.getRegionUpkeep(building.getHsRegionType());
							}
							prodFactor = server.getRecipeFactor(item.ItemRef(), this.biome, item.value());
							break;
						case TAVERNE:
							if (resident.getHappiness() > Resident.getBaseHappines())
							{
								sale = resident.getSettlerCount() * TAVERNE_FREQUENT / 100.0 * resident.getHappiness();
							} else
							{
								if (resident.getHappiness() > 0.0)
								{
									sale = resident.getSettlerCount() * TAVERNE_FREQUENT / 100.0 * resident.getHappiness()*TAVERNE_UNHAPPY_FACTOR;
								}
							}
							if (resident.getDeathrate() > 0)
							{
								sale = resident.getSettlerCount() * TAVERNE_FREQUENT / 100.0 * TAVERNE_UNHAPPY_FACTOR;
							}
							building.setSales(sale);	
							ingredients = new ItemList();
							break;
						default :
//							System.out.println("doProd:"+building.getHsRegionType()+":"+BuildPlanType.getBuildGroup(building.getBuildingType()));
							ingredients = new ItemList();
							ingredients = server.getRecipeProd(item.ItemRef(),building.getHsRegionType());
							prodFactor = 1;
//								System.out.println(this.getId()+" :doProd:"+building.getHsRegionType()+":"+ingredients.size());
							prodFactor = server.getRecipeFactor(item.ItemRef(), this.biome, item.value());
//							if (building.getBuildingType() == BuildPlanType.TANNERY)
//							{
//								System.out.println(this.getId()+" :item: "+item.ItemRef()+" igred:"+ingredients.size()+": factor:"+prodFactor);
//							}
							break;
						}
//						System.out.println("check");
						if (checkStock(prodFactor, ingredients))
						{
	//						iValue = item.value();
							// berechne die MaterialKosten der Produktion
							cost = server.getRecipePrice(item.ItemRef(), ingredients);
							// berechne Verkaufpreis der Produktion
							for (Item product : products)
							{
								iValue = (int)((double) product.value() *prodFactor);
								sale = sale + (building.calcSales(server,product)*iValue);
//								System.out.println("Prod value" +product.ItemRef()+":"+iValue+" | "+prodFactor+" |"+(building.calcSales(server,product)*iValue));
								
//								System.out.println("Prod deposit: "+product.ItemRef()+":"+iValue);
								warehouse.depositItemValue(product.ItemRef(),iValue);
								productionOverview.addCycleValue(product.ItemRef(), iValue);
							}
							if ((sale - cost) > 0.0)
							{
								// berechne Ertrag fuer Building .. der Ertrag wird versteuert !!
								account = (sale-cost); // * (double) iValue / 2;
//								logList.addProductionSale(building.getBuildingType().name(), getId(), building.getId(), account, "CraftManager",getAge());
							} else
							{
								account =  1.0 * (double) iValue;
//								logList.addProductionSale(building.getBuildingType().name(), getId(), building.getId(), account, "CraftManager",getAge());
							}
//							System.out.println("Prod account: "+sale+"-"+cost+"="+account);
							double salary = account / 3.0 * 2.0;
							setWorkerSale( building,  salary);
							account = account - salary;
							building.addSales(account); //-cost);
							if (this.ownerId != building.getOwnerId())
							{
								Owner bOwner = data.getOwners().getOwner(building.getId());
								if (bOwner !=null)
								{
									bOwner.depositCost(cost);
									bOwner.depositSales(account);
								}
								bank.depositKonto(cost, "ProdCost ", getId());
								
							} else
							{
								bank.depositKonto(account, "ProdSale ", getId());
							}
							consumStock(prodFactor, ingredients);
						} else
						{
//							System.out.println("No stock for produce " +building.getHsRegionType()+"|"+item.ItemRef()+":"+item.value()+"*"+prodFactor);
						}
					}
//					building.addSales(sale);
				}
				
				// unit production
				if (BuildPlanType.getBuildGroup(building.getBuildingType())== 5)
				{
					if (building.isEnabled())
					{
						switch(building.getBuildingType())
						{
						case GUARDHOUSE:
							if (building.getTrainCounter() == 0)
							{
								NpcData recrute = resident.findRecrute();
								if (recrute != null)
								{
									ingredients = building.militaryProduction();
									prodFactor  = 1.0;
									if (checkStock(prodFactor, ingredients))
									{
										// ausr�stung abbuchen
										consumStock(prodFactor, ingredients);
										// Siedler aus vorrat nehmen
										recrute.setUnitType(UnitType.ROOKIE);
										recrute.setWorkBuilding(building.getId());
//										resident.depositSettler(-1);
										// Counter starten
										building.addTrainCounter(1);
									} else
									{
										System.out.println("No Traning Start due to Stock");
									}
								} else
								{
									System.out.println("No Traning Start due to Resident");
								}
		//						System.out.println("GUARD " +item.ItemRef()+":"+item.value()+"*"+prodFactor);
							} else
							{
								ingredients = building.militaryConsum();
								prodFactor  = 1.0;
								if (checkStock(prodFactor, ingredients))
								{
									consumStock(prodFactor, ingredients);
									building.addTrainCounter(1);
								} else
								{
									System.out.println("No Traning Consum");
								}
							}
							break;
						default:
							break;
						}
					}
				}
			} else
			{
//				System.out.println(this.getId()+" :doEnable:"+building.getHsRegionType()+":"+building.isEnabled());
			}
		}
		productionOverview.addCycle();
	}

	/**
	 * berechnet tax von Bev�lkerung 
	 * vom Umsatz der Geb�ude
	 * 
	 */
	public void doCalcTax()
	{
//		double taxSum = 0.0;
		double value = 0.0; 
		for (Building building : buildingList.values())
		{
			value = (building.getSales() * BASE_TAX_FACTOR/ 100.0);
			if (value > 0.0)
			{
				taxOverview.addCycleValue(building.getId()+"."+building.getHsRegionType() , value);
			}
			sales = sales + value;
			// reset building.sale
			building.setSales(0.0);
		}
//		taxSum = townhall.getWorkerCount()  * ConfigBasis.SETTLER_TAXE;
		taxSum = taxSum + (resident.getSettlerCount() * ConfigBasis.SETTLER_TAXE);
		bank.depositKonto(sales, "TAX_COLLECTOR", getId());
//		System.out.println("doCalcTax "+this.getId()+" : "+ConfigBasis.setStrformat2(sales,7)+"/"+ConfigBasis.setStrformat2(taxSum,7));
		//  Kingdom tax calculated in RealmModel
	}
	
	/**
	 * give trained units to barrack
	 * @param unitFactory
	 */
	public void doUnitTrain(UnitFactory unitFactory)
	{
		for (Building building : buildingList.values())
		{
			// unit production
			if (BuildPlanType.getBuildGroup(building.getBuildingType())== 500)
			{
				if (building.isEnabled())
				{
					switch(building.getBuildingType())
					{
					case GUARDHOUSE:
						if (building.isTrainReady())
						{
//						System.out.println("GUARD " +item.ItemRef()+":"+item.value()+"*"+prodFactor);
							NpcData recrute = barrack.getUnitList().getBuildingRecrute(building.getId());
							recrute.setWorkBuilding(0);
							recrute.setUnitType(UnitType.MILITIA);
							UnitMilitia.initData(recrute.getUnit());
							building.addMaxTrain(-1);
							building.setIsEnabled(false);
							building.setTrainCounter(0);
						} else
						{
						}
						break;
					case ARCHERY:
						if (building.isTrainReady())
						{
//						System.out.println("GUARD " +item.ItemRef()+":"+item.value()+"*"+prodFactor);
							NpcData recrute = barrack.getUnitList().getBuildingRecrute(building.getId());
							recrute.setWorkBuilding(0);
							recrute.setUnitType(UnitType.ARCHER);
							UnitArcher.initData(recrute.getUnit());
							building.addMaxTrain(-1);
							building.setIsEnabled(false);
							building.setTrainCounter(0);
						} else
						{
						}
						break;
					case BARRACK:
						if (building.isTrainReady())
						{
//						System.out.println("GUARD " +item.ItemRef()+":"+item.value()+"*"+prodFactor);
							NpcData recrute = barrack.getUnitList().getBuildingRecrute(building.getId());
							recrute.setWorkBuilding(0);
							recrute.setUnitType(UnitType.LIGHT_INFANTRY);
							UnitLightInfantry.initData(recrute.getUnit());
							building.addMaxTrain(-1);
							building.setIsEnabled(false);
							building.setTrainCounter(0);
						} else
						{
						}
						break;
					case CASERN:
						if (building.isTrainReady())
						{
//						System.out.println("GUARD " +item.ItemRef()+":"+item.value()+"*"+prodFactor);
							NpcData recrute = barrack.getUnitList().getBuildingRecrute(building.getId());
							recrute.setWorkBuilding(0);
							recrute.setUnitType(UnitType.HEAVY_INFANTRY);
							UnitHeavyInfantry.initData(recrute.getUnit());
							building.addMaxTrain(-1);
							building.setIsEnabled(false);
							building.setTrainCounter(0);
						} else
						{
						}
						break;
					case TOWER:
						if (building.isTrainReady())
						{
//						System.out.println("GUARD " +item.ItemRef()+":"+item.value()+"*"+prodFactor);
							NpcData recrute = barrack.getUnitList().getBuildingRecrute(building.getId());
							recrute.setWorkBuilding(0);
							recrute.setUnitType(UnitType.KNIGHT);
							UnitKnight.initData(recrute.getUnit());
							building.addMaxTrain(-1);
							building.setIsEnabled(false);
							building.setTrainCounter(0);
						} else
						{
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}

	/**
	 * @return the buildManager
	 */
	public BuildManager buildManager()
	{
		return buildManager;
	}

	public TradeManager tradeManager()
	{
		return tradeManager;
	}

	public SettleManager settleManager()
	{
		return settleManager;
	}


	/**
	 * @return the biome
	 */
	public Biome getBiome()
	{
		return biome;
	}

	/**
	 * @param biome the biome to set
	 */
	public void setBiome(Biome biome)
	{
		this.biome = biome;
	}

	/**
	 * @return the mapManager
	 */
	public MapManager getMapManager()
	{
		return mapManager;
	}

	/**
	 * @return the treasureList
	 */
	public ArrayList<Item> getTreasureList()
	{
		return treasureList;
	}

	/**
	 * @param treasureList the treasureList to set
	 */
	public void setTreasureList(ArrayList<Item> treasureList)
	{
		this.treasureList = treasureList;
	}

	public long getAge()
	{
		return age;
	}

	public void setAge(long value)
	{
		this.age = value;
	}

	public SignPosList getSignList() 
	{
		return signList;
	}

	public void setSignList(SignPosList signList) 
	{
		this.signList = signList;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Owner nOwner)
	{
		if (nOwner != null)
		{
			this.owner = nOwner;
			this.ownerId = nOwner.getId();
			this.ownerName = nOwner.getPlayerName();
		}
	}
		

	public Owner getOwner()
	{
		return owner;
	}

	public ReputationList getReputations()
	{
		return reputations;
	}
	
	public void setReputationList(ReputationList repList)
	{
		this.reputations = repList;
	}

	/**
	 * @return the kingdomId
	 */
	public int getKingdomId()
	{
		return kingdomId;
	}

	/**
	 * @param kingdomId the kingdomId to set
	 */
	public void setKingdomId(int kingdomId)
	{
		this.kingdomId = kingdomId;
	}

	/**
	 * @return the lehenId
	 */
	public int getTributId()
	{
		return tributId;
	}

	/**
	 * @param lehenId the lehenId to set
	 */
	public void setTributId(int lehenId)
	{
		this.tributId = lehenId;
	}

	/**
	 * @return the sales
	 */
	public double getSales()
	{
		return sales;
	}

	/**
	 * @param sales the sales to set
	 */
	public void setSales(double sales)
	{
		this.sales = sales;
	}

	/**
	 * @return the taxSum
	 */
	public double getTaxSum()
	{
		return taxSum;
	}

	/**
	 * @param taxSum the taxSum to set
	 */
	public void setTaxSum(double taxSum)
	{
		this.taxSum = taxSum;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName()
	{
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName)
	{
		this.ownerName = ownerName;
	}
	
}
