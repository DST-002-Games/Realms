package net.krglok.realms.tool;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.krglok.realms.Common.Item;
import net.krglok.realms.Common.ItemList;
import net.krglok.realms.Common.ItemPrice;
import net.krglok.realms.Common.ItemPriceList;
import net.krglok.realms.builder.RegionConfig;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.data.DataStorage;
import net.krglok.realms.manager.CraftManager;
import net.krglok.realms.model.RealmModel;
import net.krglok.realms.unittest.ConfigTest;
import net.krglok.realms.unittest.ServerTest;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.junit.Test;

public class ItemPriceListTest
{

	String dataFolder  = "\\GIT\\OwnPlugins\\Realms\\plugins\\Realms"; 

	DataStorage data = new DataStorage(dataFolder);

	ServerTest server = new ServerTest(data);

	private boolean isOutput = false;
	private String getBaseKey()
	{
		return "BASEPRICE";
	}

	public void writePriceData(ItemPriceList Items) 
	{
		try
		{
			//\\Program Files\\BuckitTest\\plugins\\Realms
            File DataFile = new File("\\GIT\\OwnPlugins\\Realms\\plugins\\Realms\\", "baseprice.yml");
            if (!DataFile.exists()) 
            {
            	DataFile.createNewFile();
            }
            System.out.println(DataFile.exists());
            
            FileConfiguration config = new YamlConfiguration();
            config.load(DataFile);
            
            String base = getBaseKey();
            ConfigurationSection settleSec = config.createSection(base);
            for (ItemPrice item : Items.values())
            {
            	config.set(MemorySection.createPath(settleSec, item.ItemRef()),ConfigBasis.format2(item.getBasePrice()));
            }
            config.save(DataFile);
			
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	
	
	public ItemPriceList readPriceData() 
	{
        String base = getBaseKey();
        ItemPriceList items = new ItemPriceList();
		try
		{
			//\\Program Files\\BuckitTest\\plugins\\Realms
            File DataFile = new File("\\GIT\\OwnPlugins\\Realms\\plugins\\Realms", "baseprice.yml");
            if (!DataFile.exists()) 
            {
            	items = testPriceList();
            	DataFile.createNewFile();
            	return items;
            }
            
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

	public ItemPriceList testPriceList()
	{
		ItemPriceList items = new ItemPriceList();
//		for (Material mat : Material.values())
//		{
//			if (mat.name().contains("IRON"))
//			{
//				items.add(mat.name(), 1.0);
//			}
//		}
		items.add("WHEAT", 0.30);
		items.add("LOG", 0.5);
		items.add("COBBLESTONE", 0.1);
		items.add("SAND", 0.2);
		items.add("DIRT", 0.2);
		items.add("GRASS", 0.5);
		items.add("STONE", 1.7);
		items.add("COAL", 3.0);
		items.add("WOOL", 0.5);
		items.add("IRON_ORE", 15.0);
		items.add("SEEDS", 0.2);
		items.add("GRAVEL", 0.5);
		items.add("FLINT", 1.0);
		items.add("FEATHER", 0.5);
		items.add("GOLD_NUGGET", 44.0);
		
		return items;
	}
	
	
    private ArrayList<ItemStack> processItemStackList(List<String> input, String filename) {
        ArrayList<ItemStack> returnList = new ArrayList<ItemStack>();
        for (String current : input) {
            String[] params = current.split("\\.");
            if (Material.getMaterial(params[0]) != null) {
                ItemStack is;
                if (params.length < 3) {
                    is = new ItemStack(Material.getMaterial(params[0]),Integer.parseInt(params[1]));
                } else {
                    is = new ItemStack(Material.getMaterial(params[0]),Integer.parseInt(params[1]), Short.parseShort(params[2]));
                }
                returnList.add(is);
            } else {
            	System.out.println("[Stronghold] could not find item " + params[0] + " in " + filename);
            }
        }
        return returnList;
    }
	
	
	@SuppressWarnings("unused")
	private RegionConfig getRegionConfig(String pathName, String sRegionFile)
	{
        try {
//        		sRegionFile = sRegionFile;
        		File currentRegionFile = new File(pathName,sRegionFile);
        		if (currentRegionFile == null)
        		{
        			System.out.println("ERROR in "+pathName+"\\"+sRegionFile);
        		}
        		
                FileConfiguration rConfig = new YamlConfiguration();
                rConfig.load(currentRegionFile);
                String regionName = currentRegionFile.getName().replace(".yml", "");
                RegionConfig regionConfig = new RegionConfig(regionName,
                        rConfig.getString("group", regionName),
                        (ArrayList<String>) rConfig.getStringList("friendly-classes"),
                        (ArrayList<String>) rConfig.getStringList("enemy-classes"),
                        (ArrayList<String>) rConfig.getStringList("effects"),
                        (int) Math.pow(rConfig.getInt("radius"), 2),
                        (int) Math.pow(rConfig.getInt("build-radius", rConfig.getInt("radius", 2)), 2),
                        processItemStackList(rConfig.getStringList("requirements"), currentRegionFile.getName()),
                        rConfig.getStringList("super-regions"),
                        processItemStackList(rConfig.getStringList("reagents"), currentRegionFile.getName()),
                        processItemStackList(rConfig.getStringList("upkeep"), currentRegionFile.getName()),
                        processItemStackList(rConfig.getStringList("output"), currentRegionFile.getName()),
                        rConfig.getDouble("upkeep-chance"),
                        rConfig.getDouble("money-requirement"),
                        rConfig.getDouble("upkeep-money-output"),
                        rConfig.getDouble("exp"),
                        rConfig.getString("description"),
                        rConfig.getInt("power-drain", 0),
                        rConfig.getInt("housing", 0),
                        rConfig.getStringList("biome"));
                return regionConfig;
            } catch (Exception e) {
                System.out.println("[Stronghold] failed to load " + sRegionFile);
                e.printStackTrace();
            }
        return null;
	}
	



	
	private boolean isInList(String name, String[] sList)
	{
		for (int i = 0; i < sList.length; i++)
		{
			if (name.contains(sList[i]))
			{
				return true;
			}
		}
		return false;
	}

	
	@SuppressWarnings("unused")
	private String[] setBasisList()
	{
		return new String[] 
        		{"HALL",
				"WAREHOUSE",
				"TAVERNE",
        		"HOME", 
        		"WHEAT", 
        		"WOODCUTTER",
        		"QUARRY",
        		"SHEPHERD",
        		"CARPENTER",
        		"CABINETMAKER"
        		};
	}

	@SuppressWarnings("unused")
	private String[] setErweitertList()
	{
		return new String[] 
        		{"AXESHOP",
        		"HOESHOP", 
        		"PICKAXESHOP", 
        		"KNIFESHOP",
        		"SPADESHOP",
        		"BRICKWORK",
        		"NETHER_BRICKWORK",
        		"FARMHOUSE",
        		"BAKERY",
        		"CHARBURNER",
        		"COWSHED",
        		"CHICKENHOUSE",
        		"FISHERHOOD"
        		};
	}

	@SuppressWarnings("unused")
	private String[] setEnhancedList()
	{
		return new String[] 
        		{"WORKSHOP",
				"STONEMINE",
				"SMELTER",
				"FARM",
				"PIGPEN"
        		};
	}

	private String[] setBuildingList()
	{
		return new String[] 
        		{"HALL",
				"WAREHOUSE",
				"TAVERNE",
        		"HOME", 
        		"WHEAT", 
        		"WOODCUTTER",
        		"QUARRY",
        		"SHEPHERD",
        		"CARPENTER",
        		"CABINETMAKER",
        		"AXESHOP",
        		"HOESHOP", 
        		"PICKAXESHOP", 
        		"KNIFESHOP",
        		"SPADESHOP",
        		"BRICKWORK",
        		"NETHER_BRICKWORK",
        		"FARMHOUSE",
        		"BAKERY",
        		"CHARBURNER",
        		"COWSHED",
        		"CHICKENHOUSE",
        		"FISHERHOOD",
        		"WORKSHOP",
				"STONEMINE",
				"SMELTER",
				"FARM",
				"PIGPEN"

        		};
	}
	
	private void setProductPrice(ItemPriceList itemPrices, ItemPrice productPrice)
	{
		if (productPrice.getBasePrice() > 0.0)
		{
			itemPrices.put(productPrice.ItemRef(), productPrice);
		}
	}

	
	private ItemPrice getProductPrice(String searchRef, ItemPriceList itemPrices, String[] sList )
	{
		String sRegionFile = "";
        HashMap<String,Integer> ingredient = new HashMap<String,Integer>();
        Item product = new Item();
        RegionConfig region;

		String path = "\\GIT\\OwnPlugins\\Realms\\plugins\\HeroStronghold";
        File regionFolder = new File(path, "RegionConfig");
        if (!regionFolder.exists()) 
        {
        	System.out.println("Folder not found !");
            return null;
        }
        for (File RegionFile : regionFolder.listFiles()) 
        {
        	
        	sRegionFile = RegionFile.getName();
            if ( isInList(sRegionFile,sList))
            {
	        	region= getRegionConfig(path+"\\RegionConfig", sRegionFile);

	            for (ItemStack item : region.getOutput())
	            {
		            if (searchRef.equalsIgnoreCase(item.getType().name()) )
		            {
//		            	System.out.println(searchRef+" == "+item.getType().name());
			            for (ItemStack found : region.getUpkeep())
			            {
			            	ingredient.put(found.getType().name(), found.getAmount());
//			            	System.out.println(searchRef+" == "+item.getType().name()+"/"+found.getType().name());
			            }
			            product.setItemRef(item.getType().name());
			            product.setValue(item.getAmount());
		            }
	            }
            }
        }
		if (ingredient.size() == 0 )
		{
//			System.out.println("RawMaterial: "+searchRef);
		} else
		{
			if (product.ItemRef() == searchRef)
			{
				double prodCost = 0.0;
				double basePrice = 0.0;
				ItemPrice productPrice = new ItemPrice(product.ItemRef(), 0.0);
				for (String itemRef : ingredient.keySet())
				{
					prodCost = prodCost + (ingredient.get(itemRef) * itemPrices.getBasePrice(itemRef) * 1.25);
//					System.out.println(itemRef+": "+ingredient.get(itemRef)+" : "+itemPrices.getBasePrice(itemRef)*1.25);
				}
				basePrice = (prodCost / 0.75 / (double) product.value());
				productPrice.setBasePrice(basePrice);
//				System.out.println("Baseprice : "+basePrice);
//				System.out.println("Product: "+product.ItemRef()+" :"+basePrice);
				setProductPrice(itemPrices, productPrice);
				return productPrice;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private void showIngredientListing(HashMap<String,Integer> ingredient) 
	{
//        String sRegionFile = "";
//        String[] outLines ;

        // REAGENTS LIST
        for (String sName : ingredient.keySet())
        {
        	int value = ingredient.get(sName);
            System.out.println(sName+":"+value);
        }
		        
		
	}
	
	@SuppressWarnings("unused")
	private ItemPriceList setPriceList (ItemPriceList itemPrices, HashMap<String,Integer> items)
	{
		for (String itemRef : items.keySet())
		{
			if (itemPrices.containsKey(itemRef) == false)
			{
				itemPrices.put(itemRef, new ItemPrice(itemRef, 0.0));
			}
		}
		
		return itemPrices;
	}
	
	
	private void getStrongholdConstructionMaterial(String[] sList, ItemPriceList itemPrices)
	{
		String path = "\\GIT\\OwnPlugins\\Realms\\plugins\\HeroStronghold";
        File regionFolder = new File(path, "RegionConfig");
        if (!regionFolder.exists()) {
        	System.out.println("Folder not found !");
            return;
        }
        HashMap<String,Integer> required = new HashMap<String,Integer>();
        HashMap<String,Integer> reagent = new HashMap<String,Integer>();
        HashMap<String,Integer> ingredient = new HashMap<String,Integer>();
        HashMap<String,Integer> product = new HashMap<String,Integer>();
        String sRegionFile = "";
        
        RegionConfig region;
        
        System.out.println("[Stronghold] Product cost" );
        for (File RegionFile : regionFolder.listFiles()) 
        {
        	sRegionFile = RegionFile.getName();
            if ( isInList(sRegionFile,sList))
            {
	        	region= getRegionConfig(path+"\\RegionConfig", sRegionFile);

	            for (ItemStack item : region.getRequirements())
	            {
	            	required.put(item.getType().name(), 0);
	            }
	            for (ItemStack item : region.getReagents())
	            {
	            	reagent.put(item.getType().name(), 0);
	            }
	            for (ItemStack item : region.getUpkeep())
	            {
	            	ingredient.put(item.getType().name(), 0);
	            }
	            for (ItemStack item : region.getOutput())
	            {
	            	product.put(item.getType().name(), 0);
	            }
            }
        }
//        showIngredientListing(required);
//        showIngredientListing(reagent);
//        showIngredientListing(ingredient);
//        showIngredientListing(product);
        ItemPrice productPrice = new ItemPrice();
        for (String itemRef : product.keySet())
        {
        	if (itemPrices.getBasePrice(itemRef) == 0.0)
        	{
        		productPrice = getProductPrice(itemRef, itemPrices, sList);
        		if (productPrice == null)
        		{
        			itemPrices.put(itemRef, new ItemPrice(itemRef, 0.0));
        		}
        	}
        }
        for (String itemRef : required.keySet())
        {
        	if (itemPrices.getBasePrice(itemRef) == 0.0)
        	{
        		productPrice = getProductPrice(itemRef, itemPrices, sList);
        		if (productPrice == null)
        		{
        			itemPrices.put(itemRef, new ItemPrice(itemRef, 0.0));
        		}
        	}
        }
        for (String itemRef : reagent.keySet())
        {
        	if (itemPrices.getBasePrice(itemRef) == 0.0)
        	{
        		productPrice = getProductPrice(itemRef, itemPrices, sList);
        		if (productPrice == null)
        		{
        			itemPrices.put(itemRef, new ItemPrice(itemRef, 0.0));
        		}
        	}
        }
        
	}
	
	private int getMinStorage(ServerTest server, Biome biome, String itemRef)
	{
		ConfigTest config = new ConfigTest();
		int matFactor = server.getBiomeFactor( biome, Material.getMaterial(itemRef));
		int sellLimit = 0;
		if (itemRef.equalsIgnoreCase(Material.COBBLESTONE.name()))
		{
//		  System.out.print(">");	
		}
		if (matFactor >= 0)
		{
			if (config.getToolItems().containsKey(itemRef))
			{
				return 32 - (64 * matFactor / 100) + sellLimit;
			}
			if (config.getWeaponItems().containsKey(itemRef))
			{
				return 64 - (64 * matFactor / 100) + sellLimit;
			}
			if (config.getArmorItems().containsKey(itemRef))
			{
				return 64- (64 * matFactor / 100) + sellLimit; 
			}
			if (config.getFoodItems().containsKey(itemRef))
			{
				return 21 * 16 ;
			}
			if (config.getValuables().containsKey(itemRef))
			{
				return 64 - (64 * matFactor / 100) + sellLimit;
			}
			if (config.getBuildMaterialItems().containsKey(itemRef))
			{
//				System.out.print("B:");
				return 196 - (64 * matFactor / 100) + sellLimit;
			}
			if (config.getOreItems().containsKey(itemRef))
			{
				return 64 - (64 * matFactor / 100) + sellLimit;
			}
			if (config.getMaterialItems().containsKey(itemRef))
			{
//				System.out.print("M:");
				return 32 - (64 * matFactor / 100) + sellLimit;
			}
			if (config.getRawItems().containsKey(itemRef))
			{
//				System.out.print("R: ");
				return 64 - (64 * matFactor / 100) + sellLimit;
			}
			
			return 8 + sellLimit;
		} else
		{
			return 999;
		}
	}

	
	@Test
	public void testItemPriceList()
	{
		String[] sList;
        sList = setBuildingList();
        ItemPriceList itemPrices = testPriceList();
		
		getStrongholdConstructionMaterial(sList, itemPrices);
		getStrongholdConstructionMaterial(sList, itemPrices);
		
		double price = 0.0;
		System.out.println("== new Pricelist =================");
		for (ItemPrice itemPrice : itemPrices.values())
		{
			price = itemPrice.getBasePrice() ;
			itemPrice.setBasePrice(price);
			System.out.println(ConfigBasis.setStrleft(itemPrice.ItemRef(),12)+" : "+ConfigBasis.setStrformat2(price, 5));
		}
		System.out.println("== error Pricelist =================");
		for (ItemPrice itemPrice : itemPrices.values())
		{
			if (itemPrice.getBasePrice() == 0.0)
			{
				ItemList inggredients = server.getRecipe(itemPrice.ItemRef());
				System.out.println(ConfigBasis.setStrleft(itemPrice.ItemRef(),12)+" : "+ConfigBasis.setStrformat2(0.0, 5));
				double value = itemPrices.getPriceOfList(inggredients);
				for (Item item : inggredients.values())
				{
					System.out.println(ConfigBasis.setStrleft("-"+item.ItemRef(),12)+" : "+ConfigBasis.setStrformat2(value, 5));
				}
			}		
		}
		writePriceData(itemPrices) ;
		Biome biome = Biome.FOREST;
		System.out.println("== MinStoreList =======: "+biome);
		for (ItemPrice itemPrice : itemPrices.values())
		{
			price = itemPrice.getBasePrice() ;
			itemPrice.setBasePrice(price);
			int minStore = getMinStorage(server, biome, itemPrice.ItemRef());
			System.out.println(ConfigBasis.setStrleft(itemPrice.ItemRef(),12)+" : "+ConfigBasis.setStrright(minStore, 5));
		}
		
		int col = 0;
		ArrayList<String> list = new ArrayList<String>();
		for (Material mat : Material.values())
		{
			list.add(mat.name());
		}
		Collections.sort(list);
		if (isOutput)
		{
			System.out.println("== Material List == ["+Material.values().length+"]");
			for (String name : list)
			{
				Material mat = Material.getMaterial(name);
				System.out.print(ConfigBasis.setStrright(String.valueOf(mat.getId()),4)+":"+ConfigBasis.setStrleft(mat.name(),22));
				col++;
				if (col > 2)
				{
					col = 0;
					System.out.println("");
				}
			}
		}
		
		fail("Not yet implemented");
	}

}
