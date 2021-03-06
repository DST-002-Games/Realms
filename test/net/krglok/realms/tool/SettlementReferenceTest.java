package net.krglok.realms.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import net.krglok.realms.Common.Item;
import net.krglok.realms.Common.ItemList;
import net.krglok.realms.Common.ItemPrice;
import net.krglok.realms.Common.ItemPriceList;
import net.krglok.realms.builder.BuildPlanMap;
import net.krglok.realms.builder.BuildPlanType;
import net.krglok.realms.builder.BuildPosition;
import net.krglok.realms.builder.SettleSchema;
import net.krglok.realms.core.Building;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.core.SettlementList;
import net.krglok.realms.data.ConfigInterface;
import net.krglok.realms.data.SettlementData;
import net.krglok.realms.manager.BuildManager;
import net.krglok.realms.unittest.ConfigTest;
import net.krglok.realms.unittest.DataTest;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

/**
 * @author Windu
 * 
 * description :
 * The is a reporting modul for the settlements.It can used offline in the deveplopment 
 * 
 */
public class SettlementReferenceTest
{
	
	private ItemPriceList readPriceData() 
	{
        String base = "BASEPRICE";
        ItemPriceList items = new ItemPriceList();
		try
		{
			//\\Program Files\\BuckitTest\\plugins\\Realms
            File DataFile = new File("\\GIT\\OwnPlugins\\Realms\\plugins\\Realms", "baseprice.yml");
//            if (!DataFile.exists()) 
//            {
//            	DataFile.createNewFile();
//            }
            FileConfiguration config = new YamlConfiguration();
            config.load(DataFile);
            
            if (config.isConfigurationSection(base))
            {
            	
    			Map<String,Object> buildings = config.getConfigurationSection(base).getValues(false);
            	for (String ref : buildings.keySet())
            	{
            		Double price = config.getDouble(base+"."+ref,0.0);
            		ItemPrice item = new ItemPrice(ref, price);
            		items.add(item);
            	}
            }
		} catch (Exception e)
		{
		}
		return items;
	}
	
	private String getRequired(Settlement settle, int index)
	{
		int i = 0;
		for (Item item: settle.getRequiredProduction().values())
		{
			if (i == index)
			{
				return item.ItemRef();
			}
			i++;
		}
		
		return "";
	}

	private String getHighValuePrice(Settlement settle, ItemPriceList priceList)
	{
		String itemRef = "";
		double balance = 0;
		double price = 0.0;
		for (Item item : settle.getWarehouse().getItemList().values())
		{
			
			price = priceList.getBasePrice(item.ItemRef());
			if ((balance < (item.value() * price)) && (item.ItemRef().equals(Material.GOLD_NUGGET.name()) == false ))
			{
				balance = item.value() * price;
				itemRef = item.ItemRef();
			}
		}
		
		return itemRef;
		
	}
	
	private String getHighValue(Settlement settle)
	{
		String itemRef = "";
		int balance = 0;
		for (Item item : settle.getWarehouse().getItemList().values())
		{
			
			if ((balance < item.value()) && (item.ItemRef().equals(Material.GOLD_NUGGET.name()) == false ))
			{
				balance = item.value();
				itemRef = item.ItemRef();
			}
		}
		
		return itemRef;
		
	}
	
	private String getHighPrice(Settlement settle, ItemPriceList priceList)
	{
		String itemRef = "";
		double balance = 0.0;
		double price = 0.0;
		for (Item item : settle.getWarehouse().getItemList().values())
		{
			
			price = priceList.getBasePrice(item.ItemRef());
			if ((balance < price) && (item.ItemRef().equals(Material.GOLD_NUGGET.name()) == false ))
			{
				balance = price;
				itemRef = item.ItemRef();
			}
		}
		
		return itemRef;
		
	}


	private double getWarehouseBalance(Settlement settle, ItemPriceList priceList)
	{
		double balance = 0.0;
		double price = 0.0;
		for (Item item : settle.getWarehouse().getItemList().values())
		{
			
			price = priceList.getBasePrice(item.ItemRef());
			balance = balance + (item.value()*price);
		}
		
		return balance;
	}
	
	
	private ItemList getTools(Settlement settle, ConfigInterface config)
	{
		ItemList items = new ItemList();
		ItemList searchList = config.getToolItems();
		items = settle.getWarehouse().findItemsInWarehouse(searchList);
		return items;
	}

	private void sellValuePrice(Settlement settle, ItemPriceList priceList)
	{
		final double MAX_VALUE = 100.0;
		final int   MAX_AMOUNT = 100;
		String itemRef = getHighValuePrice(settle, priceList);
		int sellAmount = 0;
		int amount = settle.getWarehouse().getItemList().getValue(itemRef);
		double sellPrice = priceList.getBasePrice(itemRef);
		sellAmount = amount;
		if (amount > MAX_AMOUNT)
		{
			sellAmount = MAX_AMOUNT;
		} 
		if((Math.round(sellAmount) * sellPrice) > MAX_VALUE)
		{
			sellAmount = (int) (MAX_VALUE / sellPrice);
		}
		System.out.println("  ");
		System.out.println(settle.getId()+":"+settle.getName()+" SellOrder");
		System.out.print(" |"+" item       ");
		System.out.print(" | "+" Amount");
		System.out.print(" | "+"  Price");
		System.out.print(" | "+"  Value");
		System.out.println(" ");
		System.out.print(" |"+ConfigBasis.setStrleft(itemRef,12));
		System.out.print(" | "+ConfigBasis.setStrright(String.valueOf(sellAmount),7));
		System.out.print(" | "+ConfigBasis.setStrright(String.valueOf(sellPrice),7));
		System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)(sellPrice*sellAmount)),7));
		System.out.println(" ");
		
	}

	
	private ArrayList<BuildPlanType> findUnavailableBuilding(Settlement settle, SettleSchema needed)
	{
		ArrayList<BuildPlanType> notFound = new ArrayList<BuildPlanType>();
		for (BuildPosition bPos : needed.getbPositions())
		{
			boolean isFound = false;
			for (String bType : settle.getBuildingList().getBuildTypeList().keySet())
			{
				if (bType == bPos.getbType().name())
				{
					isFound = true;
				}
			}
			if (isFound == false)
			{
				notFound.add(bPos.getbType());
			}
		}
		
		return notFound;
	}
	
	private BuildPlanType checkNeededBuilding(Settlement settle)
	{
		BuildPlanType newType = BuildPlanType.NONE;
		
		ArrayList<BuildPlanType> notFound = findUnavailableBuilding(settle, SettleSchema.initDefaultHamlet());
		if (notFound.isEmpty())
		{
			notFound = findUnavailableBuilding(settle, SettleSchema.initBiomeHamlet());
		}
		if (notFound.size() > 0)
		{
			newType = notFound.get(0);
		} else
		{
			newType = BuildPlanType.NONE;
		}
		return newType;
	}
	
	private ItemList checkRequiredInWarehouse(Settlement settle, ItemList required)
	{
		ItemList notFound = new ItemList();
		ItemList foundList = settle.getWarehouse().findItemsInWarehouse(required);
		for (Item item : required.values())
		{
			for (Item found : foundList.values())
			{
				boolean isFound = false;
				if (item.ItemRef().equals(found.ItemRef()))
				{
					if (item.value() <= found.value())
					{
						isFound = true;
					}
				}
				if (isFound == false)
				{
					notFound.addItem(item);
				}
			}
		}
		return notFound;
	}
	
	private RegionData getRegion(int id, ArrayList<RegionData> regions)
	{
		for (RegionData region : regions)
		{
			if (region.getId() == id)
			{
				return region;
			}
		}
		
		return null;
	}
	
	private void showBuildingList(SettlementList settleList, DataTest data)
	{
		String path = "\\GIT\\OwnPlugins\\Realms\\plugins\\HeroStronghold";
		ArrayList<RegionData> regions = StrongholdTools.getRegionData(path);
		System.out.println("  ");
		System.out.println("BuildingList Settlement ");
		for (Settlement settle : settleList.values())
		{
			System.out.println("-------------------------------------------------");
			System.out.print(settle.getId());
			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
			System.out.print("|"+ConfigBasis.setStrright(settle.getPosition().getX(),7));
			System.out.print("|"+ConfigBasis.setStrright(settle.getPosition().getY(),7));
			System.out.print("|"+ConfigBasis.setStrright(settle.getPosition().getZ(),7));
			System.out.println(" ");


			System.out.print("  |"+"Building/Region ");
			System.out.println(" ");
			for (Building building : settle.getBuildingList().values())
			{
				if (building.getBuildingType() == BuildPlanType.WHEAT)
				{
					System.out.print("  |"+ConfigBasis.setStrright(building.getId(),2));
					System.out.print("|"+ConfigBasis.setStrright(" >",2));
					System.out.print(" : "+ConfigBasis.setStrleft(building.getBuildingType().name(), 12));
					System.out.print("|"+ConfigBasis.setStrright(building.getPosition().getX(),7));
					System.out.print("|"+ConfigBasis.setStrright(building.getPosition().getY(),7));
					System.out.print("|"+ConfigBasis.setStrright(building.getPosition().getZ(),7));
					System.out.println(" ");
					RegionData region = getRegion(building.getHsRegion(), regions);
					if (region != null)
					{
						System.out.print("  |"+ConfigBasis.setStrright(" ",2));
						System.out.print("|"+ConfigBasis.setStrright(building.getHsRegion(),2));
						System.out.print(" : "+ConfigBasis.setStrleft(region.getType(), 12));
						System.out.print("|"+ConfigBasis.setStrright(region.getPosX(),7));
						System.out.print("|"+ConfigBasis.setStrright(region.getPosY(),7));
						System.out.print("|"+ConfigBasis.setStrright(region.getPosZ(),7));
						System.out.println(" ");
					} else
					{
						System.out.println("Region not found  "+ building.getHsRegion());
					}
				}
			}
			for (Building building : settle.getBuildingList().values())
			{
				if (building.getBuildingType() != BuildPlanType.WHEAT)
				{
					System.out.print("  |"+ConfigBasis.setStrright(building.getId(),2));
					System.out.print("|"+ConfigBasis.setStrright(" >",2));
					System.out.print(" : "+ConfigBasis.setStrleft(building.getBuildingType().name(), 12));
					System.out.print("|"+ConfigBasis.setStrright(building.getPosition().getX(),7));
					System.out.print("|"+ConfigBasis.setStrright(building.getPosition().getY(),7));
					System.out.print("|"+ConfigBasis.setStrright(building.getPosition().getZ(),7));
					System.out.println(" ");
					RegionData region = getRegion(building.getHsRegion(), regions);
					if (region != null)
					{
						System.out.print("  |"+ConfigBasis.setStrright(" ",2));
						System.out.print("|"+ConfigBasis.setStrright(building.getHsRegion(),2));
						System.out.print(" : "+ConfigBasis.setStrleft(region.getType(), 12));
						System.out.print("|"+ConfigBasis.setStrright(region.getPosX(),7));
						System.out.print("|"+ConfigBasis.setStrright(region.getPosY(),7));
						System.out.print("|"+ConfigBasis.setStrright(region.getPosZ(),7));
						System.out.println(" ");
					} else
					{
						System.out.println("Region not found  "+ building.getHsRegion());
					}
				}
			}
		}

	}
	
	@Test
	public void testReadSettledata()
	{
		final double MIN_MONEY_FACTOR = 50.0; 
		ConfigTest config = new ConfigTest();
		String path = "\\GIT\\OwnPlugins\\Realms\\plugins";
		LogList logList = new LogList(path);
		DataTest data     = new DataTest();
		ItemPriceList priceList = readPriceData();
//      File DataFile = new File(path, "Realms");
//		SettlementData sData = new SettlementData(path);
		SettlementList settleList = data.getTestSettlements(); //new SettlementList(0);

		System.out.println("==Read Settlement from File ==");
//		ArrayList<String> sList =  sData.readSettleList();
//		for (String sName : sList)
//		{
////			settleList.addSettlement(sData.readSettledata(Integer.valueOf(sName),data.getPriceList())); //,logList));
//		}
		System.out.println("Settle Overview ");
		System.out.print("id"+"|Name        ");
		System.out.print(" |"+"Setler");
		System.out.print("|"+" Bank");
		System.out.print(" |"+" Stock");
		System.out.print(" | "+"HiPrice   ");
		System.out.print(" | "+"HiValue   ");
		System.out.print(" |"+"Gold");
		System.out.print(" |"+"Emer.");
		System.out.println(" ");
		for (Settlement settle : settleList.values())
		{
			System.out.print(settle.getId());
			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
			System.out.print(" | "+ConfigBasis.setStrleft(String.valueOf(settle.getResident().getSettlerCount()),2));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)settle.getBank().getKonto()),5));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)getWarehouseBalance(settle, priceList)),5));
			System.out.print(" | "+ConfigBasis.setStrleft(getHighPrice(settle, priceList),10));
			System.out.print(" | "+ConfigBasis.setStrleft(getHighValue(settle),10));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf(settle.getWarehouse().getItemList().getValue(Material.GOLD_NUGGET.name())),3));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf(settle.getWarehouse().getItemList().getValue(Material.EMERALD.name())),2));
			System.out.print(" | "+ ConfigBasis.setStrleft(getRequired(settle, 0),10));
			System.out.println(" ");
		}

		System.out.println("  ");
		System.out.println("Required Money value ");
		System.out.print("id"+"|Name         ");
		System.out.print(" |"+"Beds");
		System.out.print("|"+" Bank ");
		System.out.print(" |"+" Money");
		System.out.print(" | "+" Factor");
		System.out.println(" ");
		
		for (Settlement settle : settleList.values())
		{
			System.out.print(settle.getId());
			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
			System.out.print(" | "+ConfigBasis.setStrleft(String.valueOf(settle.getResident().getSettlerMax()),2));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)settle.getBank().getKonto()),5));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)(settle.getResident().getSettlerMax()*MIN_MONEY_FACTOR)),5));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf(MIN_MONEY_FACTOR),5));
			System.out.println(" ");
		}
		
		System.out.println("  ");
		System.out.println("Warehouse Max item amount or price");
		System.out.print("id"+"|Name         ");
		System.out.print(" |"+" item        ");
		System.out.print(" | "+"Amount ");
		System.out.print(" | "+"  Value");
		System.out.println(" ");
		for (Settlement settle : settleList.values())
		{
			System.out.print(settle.getId());
			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
			System.out.print(" | "+ConfigBasis.setStrleft(getHighValue(settle),12));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)(settle.getWarehouse().getItemList().getValue(getHighValue(settle)))),7));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)(settle.getWarehouse().getItemList().getValue(getHighValue(settle))*priceList.getBasePrice(getHighValue(settle)))),7));
			System.out.println(" ");
		}
		
		System.out.println("  ");
		System.out.println("Warehouse Max item valueprice ");
		System.out.print("id"+"|Name         ");
		System.out.print(" |"+" item        ");
		System.out.print(" | "+"Amount ");
		System.out.print(" | "+"  Value");
		System.out.println(" ");
		for (Settlement settle : settleList.values())
		{
			System.out.print(settle.getId());
			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
			System.out.print(" | "+ConfigBasis.setStrleft(getHighValuePrice(settle, priceList),12));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)(settle.getWarehouse().getItemList().getValue(getHighValuePrice(settle, priceList)))),7));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)(settle.getWarehouse().getItemList().getValue(getHighValuePrice(settle, priceList))*priceList.getBasePrice(getHighValuePrice(settle, priceList)))),7));
			System.out.println(" ");
		}
		for (Settlement settle : settleList.values())
		{
			sellValuePrice(settle, priceList);
		}

		System.out.println("  ");
		System.out.println("Warehouse Tools ");
		for (Settlement settle : settleList.values())
		{
			System.out.print(settle.getId());
			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
			System.out.println(" ");
			System.out.print("  |"+"-item        ");
			System.out.print(" | "+"Amount ");
			System.out.print(" | "+"  Value");
			System.out.println(" ");
			ItemList tools = getTools(settle, config);
			for (Item item : tools.values())
			{
			System.out.print("  | "+ConfigBasis.setStrleft(item.ItemRef(),12));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf(item.value()),7));
			System.out.print(" | "+ConfigBasis.setStrright(String.valueOf((int)(item.value()*priceList.getBasePrice(item.ItemRef()))),7));
			System.out.println(" ");
			}
		}
		
		for (Settlement settle : settleList.values())
		{
			sellValuePrice(settle, priceList);
		}

//		System.out.println("  ");
//		System.out.println("Needed Buildings / Resources");
//		for (Settlement settle : settleList.getSettlements().values())
//		{
//			System.out.print(settle.getId());
//			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
//			System.out.println(" ");
//			BuildPlanType neededBuilding = checkNeededBuilding(settle);
//			System.out.print("   |"+"Building needed ");
//			System.out.print("   |"+neededBuilding);
//			
//			System.out.println(" ");
//			BuildPlanMap buildPlan = data.readTMXBuildPlan(neededBuilding, 4, -1);
//			
//			ItemList required = BuildManager.makeMaterialList(buildPlan);
//			System.out.print("   |"+"Items needed ");
//			System.out.println(" ");
//			for (Item item : required.values())
//			{
//				System.out.print("    |"+item.ItemRef());
//				System.out.print(" : "+item.value());
//				System.out.println(" ");
//				
//			}
//		}

		System.out.println("  ");
		System.out.println("Buy Request ");
		for (Settlement settle : settleList.values())
		{
			System.out.print(settle.getId());
			System.out.print(" | "+ConfigBasis.setStrleft(settle.getName(),12));
			System.out.println(" ");

			BuildPlanType neededBuilding = checkNeededBuilding(settle);
			BuildPlanMap buildPlan = data.readTMXBuildPlan(neededBuilding, 4, -1);
			ItemList buyRequest = checkRequiredInWarehouse(settle, BuildManager.makeMaterialList(buildPlan));
			System.out.print("  |"+"Buy order ");
			System.out.println(" ");
			for (Item item : buyRequest.values())
			{
				System.out.print("    |"+item.ItemRef());
				System.out.print(" : "+item.value());
				System.out.println(" ");
			}
		}
	
		showBuildingList( settleList,  data);
	}


}
