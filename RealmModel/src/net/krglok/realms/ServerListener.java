package net.krglok.realms;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import multitallented.redcastlemedia.bukkit.herostronghold.Util;
import multitallented.redcastlemedia.bukkit.herostronghold.region.Region;
import multitallented.redcastlemedia.bukkit.herostronghold.region.RegionCondition;
import multitallented.redcastlemedia.bukkit.herostronghold.region.SuperRegion;
import net.citizensnpcs.npc.entity.nonliving.TNTPrimedController.TNTPrimedNPC;
import net.krglok.realms.builder.BuildPlanMap;
import net.krglok.realms.builder.BuildPlanType;
import net.krglok.realms.command.CmdRealmsBookList;
import net.krglok.realms.command.CmdSettleAddBuilding;
import net.krglok.realms.command.CmdSettleBuildingList;
import net.krglok.realms.command.CmdSettleInfo;
import net.krglok.realms.command.CmdSettleMarket;
import net.krglok.realms.command.CmdSettleNoSell;
import net.krglok.realms.command.CmdSettleProduction;
import net.krglok.realms.command.CmdSettleRequired;
import net.krglok.realms.command.CmdSettleTrader;
import net.krglok.realms.command.CmdSettleWarehouse;
import net.krglok.realms.command.RealmsPermission;
import net.krglok.realms.core.Building;
import net.krglok.realms.core.CommonLevel;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Item;
import net.krglok.realms.core.ItemList;
import net.krglok.realms.core.ItemPrice;
import net.krglok.realms.core.LocationData;
import net.krglok.realms.core.NobleLevel;
import net.krglok.realms.core.Owner;
import net.krglok.realms.core.SettleType;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.core.SignPos;
import net.krglok.realms.manager.ReputationData;
import net.krglok.realms.manager.ReputationStatus;
import net.krglok.realms.manager.ReputationType;
import net.krglok.realms.model.McmdBuilder;
import net.krglok.realms.model.ModelStatus;
import net.krglok.realms.science.CaseBook;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Golem;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

/**
 * <pre>
 * This is the implementation of the EventHandlers 
 * the Event will catched , selected 
 * and transfered to the special command Handler
 * 
 * @author oduda
 *
 *</pre>
 */
public class ServerListener implements Listener
{
	private Realms plugin;
	private int lastPage;
	private int marketPage;
	private int bookPage;
	private int bookId;
	private long lastHunt = 0;
	private long lastTame = 0; 
	private ArrayList<String> donatePlayer = new ArrayList<String>();
	
	public ServerListener(Realms plugin)
	{
		this.plugin = plugin;
		this.lastPage = 0;
		this.marketPage = 0;
		this.bookPage =0;
		this.bookId = 0;
	}

	/**
	 * suppress mob spawn in a range of a settlement. 
	 * @param event
	 */
	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST )
	public void onCreatureSpawn( CreatureSpawnEvent event )
	{
		// Check for other plugins that have cancelled the event,
		// egg spawns, spawner spawns, and neutral mobs.
		if( event.isCancelled() 
			||	( event.getSpawnReason() == SpawnReason.EGG ) 
			||	( event.getSpawnReason() == SpawnReason.SPAWNER ) 
			||	( event.getSpawnReason() == SpawnReason.SPAWNER_EGG ) 
			||	( event.getEntity() instanceof Squid ) 
			||	( event.getEntity() instanceof NPC ) 
			||	( event.getEntity() instanceof Golem ) 
//			||	( !plugin.getMobRepellentConfiguration().shouldRepelNeutralMobs() &&( event.getEntity() instanceof Animals ) ) 
			)
		{
			return;
		}
		if (plugin.getConfigData() == null)
		{
			plugin.getLog().log(Level.WARNING,"[REALMS] event onCreatureSpawn, getConfig == null ");
			return;
		}
		
		ArrayList<EntityType> mobsToRepel = plugin.getConfigData().getMobsToRepel();
		// Now check to make sure the mob is in the list
		if( !mobsToRepel.isEmpty() )
		{
			if( !mobsToRepel.contains( event.getEntityType() ) )
				return;
		}
		
		if( findStrongholdAtLocation(plugin, event.getLocation() ) )
		{
//			System.out.print("[REALMS] Spawn suppress "+event.getEntityType().name());
			event.setCancelled( true );
		}
	}

	/**
	 * Handle explosion on settlements.
	 * Suppress destruction and reduce power instead 
	 * Work with herostronghold functions and conditions for regions
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        /*if (event.isCancelled() || !(event.getEntity() instanceof Creeper || event.getEntity() instanceof EnderDragon
                || event.getEntity() instanceof TNTPrimed || event.getEntity() instanceof Fireball)) {
            return;
        }*/
//        System.out.println("Get Explosion!");
    	
        ArrayList<RegionCondition> conditions = new ArrayList<RegionCondition>();
        conditions.add(new RegionCondition("denyexplosion", true, 4));
        conditions.add(new RegionCondition("denyexplosionnoreagent", false, 4));
        conditions.add(new RegionCondition("powershield", true, 0));
        if (event.getEntity() == null) 
        {
            
        } else if (event.getEntity().getClass().equals(Creeper.class)) 
        {
            conditions.add(new RegionCondition("denycreeperexplosion", true, 4));
            conditions.add(new RegionCondition("denycreeperexplosionnoreagent", false, 4));
        } else if (event.getEntity().getClass().equals(TNTPrimed.class)) 
        {
            conditions.add(new RegionCondition("denytntexplosion", true, 4));
            conditions.add(new RegionCondition("denytntexplosionnoreagent", false, 4));
        } else if (event.getEntity().getClass().equals(Fireball.class)) 
        {
            conditions.add(new RegionCondition("denyghastexplosion", true, 4));
            conditions.add(new RegionCondition("denyghastexplosionnoreagent", false, 4));
        }
        if (plugin.stronghold.getRegionManager().shouldTakeAction(event.getLocation(), null, conditions)) 
        {
            for (SuperRegion sr : plugin.stronghold.getRegionManager().getContainingSuperRegions(event.getLocation())) 
            {
                if (sr.getPower() > 0 ) 
                {
//                	plugin.stronghold.getRegionManager().reduceRegion(sr);
                  System.out.println("DenyShield active!");
                }
                event.setCancelled(true);
            }
            return;
        }
        
    }
	
	/**
	 * send update check message to ops
	 * @param event
	 */
    @EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) 
    {
    	if (event.getPlayer()== null) return;    	
		if (event.getPlayer().isOp()) 
		{
			if (plugin.getConfigData() == null)
			{
				plugin.getLog().log(Level.WARNING,"[REALMS] event onPlayerJoin, getConfig == null ");
				return;
			}
			
			String msg = "[Realms] Updatecheck : "+plugin.getConfigData().getPluginName()+" Vers.: "+plugin.getConfigData().getVersion();
			plugin.getLog().log(Level.WARNING,msg);
		} 
		if(plugin.getData().getOwners().containUuid(event.getPlayer().getUniqueId().toString()) == false)
		{
			if(plugin.getData().getOwners().getOwnerName(event.getPlayer().getName()) == null)
			{
				Owner owner = Owner.initDefaultOwner();
				owner.setPlayerName(event.getPlayer().getName());
				owner.setUuid(event.getPlayer().getUniqueId().toString());
				owner.setCommonLevel(CommonLevel.COLONIST);
				owner.setNobleLevel(NobleLevel.COMMONER);
				plugin.getData().getOwners().addOwner(owner);
				plugin.getData().writeOwner(owner);
				event.getPlayer().sendMessage("Owner is inilized for you !");
				event.getPlayer().sendMessage("use /Realms Owner for link to your existing settlements");
				plugin.getLog().log(Level.INFO,"Owner init for "+event.getPlayer().getName());
			}
		}

		return; // no OP => OUT
	}
	
    /**
     * do special action on chest in settlement
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event)
    {
//    	return;
    	if (event.getPlayer() instanceof Player)
    	{
//    		System.out.println(event.getInventory().getTitle());
//    		System.out.println(event.getPlayer().getOpenInventory().getTitle());
    		checkSettleChest(event);
    	}
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryOpen(InventoryOpenEvent event)
    {
    	return;
    }
    
    /**
     * handle action on WALL_SIGN, SIGN_POST
     * BLAZEROD and Books
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
    	if (event.getPlayer().isOp() == false)
    	{
			if (event.getPlayer().hasPermission(RealmsPermission.ADMIN.getValue().toLowerCase()) == false)
			{
				if (event.getPlayer().hasPermission(RealmsPermission.USER.getValue().toLowerCase()) == false)
				{
					event.getPlayer().sendMessage(ChatColor.RED+"You not have permission realms.user !");
					event.getPlayer().sendMessage(ChatColor.YELLOW+"Contact the OP or ADMIN for setup permission.");
					return ;
				}
			}
    	}
    	Block b = event.getClickedBlock();
    	if (b != null)
    	{
    		if (b.getType() == Material.CHEST)
    		{
//    			System.out.println("[REALMS] Click chest "+event.getPlayer().getName());
    	    	Player player = (Player) event.getPlayer();
    			Location pos = player.getLocation();
    			String sRegion = findSuperRegionAtLocation(plugin, player); 
    			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
    			if (settle != null)
    			{
    				String region = findRegionAtLocation(plugin, player);
    				if ((region.equalsIgnoreCase(BuildPlanType.HALL.name()))
       				|| (region.equalsIgnoreCase(BuildPlanType.TOWNHALL.name())))
    				{
    					if (b.getRelative(BlockFace.UP).getType() == Material.WALL_SIGN)
    					{
    						Sign sign = (Sign) b.getRelative(BlockFace.UP).getState();
    						String l0 = sign.getLine(0);
    						if (l0.equals("[DONATE]"))
    						{
    							donatePlayer.add(player.getUniqueId().toString());
    						}
    					}
    				}
    			}
    			
    		}
	    	if (b.getType() == Material.IRON_BLOCK)
	    	{
	    		Chest chest = null;
	    		Block redstone = null;
	    		if (b.getRelative(BlockFace.UP).getType() == Material.CHEST)
	    		{
	    			chest = (Chest) b.getRelative(BlockFace.UP).getState();
	    		}
	    		if (b.getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK)
	    		{
	    			redstone = b.getRelative(BlockFace.DOWN);
	    		}
	    		if (event.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL)
	    		{
	    			if((chest != null) && (redstone != null))
	    			{
	    				event.getPlayer().sendMessage("You triggerd a Catapult");
	    				float loud = (float) 20.0;
	    				float pitch = (float) 90.0;
	    				event.getPlayer().getWorld().playSound(b.getLocation(), Sound.FIREWORK_BLAST, loud, pitch);
	    				shotArrow(b);
	    			}
	    		}
	    	}
        	ArrayList<String> msg = new ArrayList<String>();
	    	if (b.getType() == Material.WALL_SIGN)
	    	{
	    		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
	    		{
	    	    	if (event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD)
	    	    	{
//	    	    		cmdRegisterSign(event, b);
	    	    	} else
	    	    	{
	    	    		cmdWallSign(event, b);
	    	    	}
	    		}
	    		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
	    		{
	    	    	if (event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD)
	    	    	{
//	    	    		cmdSignUpdate(event, b);
	    	    	} else
	    	    	{
	    	    		cmdLeftWallSign(event,b);
	    	    	}
	    		}
	    		return;
	    	}
	    	if (b.getType() == Material.SIGN_POST)
	    	{
	    		Sign sign = (Sign) b.getState();
	    		String l0 = sign.getLine(0);
	    		String l1 = sign.getLine(1);
	    		if (l0.contains("[BUILD]"))
	    		{
	    			cmdBuildat(event, b);
	    		}
	    		// other signpost Commands
	    		cmdSignPost(event, b);
	    		return;
	    	}

	    	if (b.getType() == Material.BOOKSHELF)
	    	{
	    		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
	    		{
	    			cmdBookList(event,b);
	    		}
	    		return;
	    	}
	    	if (event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD)
	    	{
//    			event.getPlayer().sendMessage("You hold a Blazerod :");
	    		cmdBlazeRod(event);
	    	}
	    	if (event.getPlayer().getItemInHand().getType() == Material.BOOK)
	    	{
//    			System.out.println("BOOK");
//	    		cmdBuildBook(event);
	    	}
	    	if (event.getPlayer().getItemInHand().getType() == Material.BOOK_AND_QUILL)
	    	{
	    		cmdBuildBook(event);
//	    		System.out.println("BookEdit");
	    	}

    	}
    }
    
    /**
     * do action with BLAZEROD in settlements
     * @param event
     */
    private void cmdBlazeRod(PlayerInteractEvent event)
    {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
	    	Block target = event.getClickedBlock();
	    	Building building = null;
	    	Settlement settle = null;
	    	int regionId = 0;
	    	Location pos = target.getLocation();
			ArrayList<Region> targets =  plugin.stronghold.getRegionManager().getContainingRegions(pos);
	    	ArrayList<String> msg = new ArrayList<String>();
			msg.add("== HeroStronghold : Region Info");
			if (targets != null)
			{
				if (targets.size() > 0)
				{
		    		for (Region region : targets)
		    		{
		    			if (plugin.getData().getBuildings().containRegion(region.getID()))
		    			{
		    				building = plugin.getData().getBuildings().getBuildingByRegion(region.getID());
		    				msg.add(region.getID()+":"+region.getType()+" Building :"+building.getId()+":"+building.getOwnerId());
		    			} else
		    			{
		    				regionId = region.getID();
			    			if (region.getOwners().size() > 0)
			    			{
			    				msg.add(region.getID()+":"+region.getType()+":"+region.getOwners().get(0));
			    			} else
			    			{
			    				msg.add(region.getID()+":"+region.getType()+":");
			    			}
		    			}
		    		}
				} else
				{
		    		msg.add("No Region found ! ");
				}
	
			} else
			{
	    		msg.add("No Region found ! ");
			}
			for (SuperRegion sRegion : plugin.stronghold.getRegionManager().getContainingSuperRegions(pos))
			{
				if (plugin.getData().getSettlements().containsName(sRegion.getName()) == true)
				{
					settle = plugin.getData().getSettlements().findName(sRegion.getName());
					msg.add(sRegion.getName()+":"+sRegion.getType()+": ["+settle.getId()+"] :"+settle.getOwnerId());
				} else
				{
					if (sRegion.getOwners().size() > 0)
					{
						msg.add(sRegion.getName()+":"+sRegion.getType()+":"+sRegion.getOwners().get(0));
					} else
					{
						msg.add(sRegion.getName()+":"+sRegion.getType()+":");
					}
				}
			}
			if (event.getPlayer().isSneaking() == true)
			{
				if (settle != null)
				{
					if (building == null)
					{
						if (event.getPlayer().isOp())
						{
							CmdSettleAddBuilding  cmd = new CmdSettleAddBuilding();
							cmd.setPara(0, settle.getId());
							cmd.setPara(1, regionId);
							cmd.execute(plugin, event.getPlayer());
						}
					}
				}
			}
			
			plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
		}    	
    }

    /**
     * unused at this time
     * @param event
     */
    private void cmdBuildPlanBook(PlayerInteractEvent event)
    {

    }
    
    /**
     * unused at this time
     * @param event
     */
    private void cmdRequiredBook(PlayerInteractEvent event)
    {
    	

    }

    
	private boolean findStrongholdAtLocation(Realms plugin,Location position)
	{
	    for (SuperRegion sRegion : plugin.stronghold.getRegionManager().getContainingSuperRegions(position))
	    {
	    	SettleType settleType = plugin.getConfigData().superRegionToSettleType(sRegion.getType());
	    	if (settleType != SettleType.NONE)
	    	{
	    		return true;
	    	}
	    }
	    for (Region region : plugin.stronghold.getRegionManager().getContainingRegions(position))
	    {
	    	BuildPlanType bType = plugin.getConfigData().regionToBuildingType(region.getType());
	    	if (bType != BuildPlanType.NONE)
	    	{
	    		return true;
	    	}
	    }
		return false;
	}

    /**
     * give first superegion at player position
     * @param plugin
     * @param player
     * @return superregion name
     */
	private String findSuperRegionAtLocation(Realms plugin, Player player)
	{
		Location position = player.getLocation();
		SuperRegion sRegion =  findSuperRegionAtPosition( plugin,  position);
		if (sRegion != null)
		{
	    	SettleType settleType = plugin.getConfigData().superRegionToSettleType(sRegion.getType());
	    	if (settleType != SettleType.NONE)
	    	{
	    		return sRegion.getName();
	    	}
	    }
		return "";
	}

	/**
	 * give first superregion at position
	 * @param plugin
	 * @param position
	 * @return superregion object  or null
	 */
	private SuperRegion findSuperRegionAtPosition(Realms plugin, Location position)
	{
	    for (SuperRegion sRegion : plugin.stronghold.getRegionManager().getContainingSuperRegions(position))
	    {
	    	if (sRegion != null)
	    	{
	    		return sRegion;
	    	}
	    }
		return null;
	}

	/**
	 * give first building at player position
	 * @param plugin
	 * @param player
	 * @return buildungtype as string
	 */
	private String findRegionAtLocation(Realms plugin, Player player)
	{
		Location position = player.getLocation();
		Region region = findRegionAtPosition( plugin, position);
	    if ( region != null)
	    {
	    	BuildPlanType bType = plugin.getConfigData().regionToBuildingType(region.getType());
	    	if (bType != BuildPlanType.NONE)
	    	{
	    		return bType.name();
	    	}
	    }
		return "";
	}

	/**
	 * give region id at player position
	 * @param plugin
	 * @param player
	 * @return region id
	 */
	private Integer findRegionIdAtLocation(Realms plugin, Player player)
	{
		Location position = player.getLocation();
		Region region = findRegionAtPosition( plugin, position);
	    if ( region != null)
	    {
	    	BuildPlanType bType = plugin.getConfigData().regionToBuildingType(region.getType());
	    	if (bType != BuildPlanType.NONE)
	    	{
	    		return region.getID();
	    	}
	    }
		return -1;
	}
	
	/**
	 * give first region at position
	 * @param plugin
	 * @param position
	 * @return  region object
	 */
	private Region findRegionAtPosition(Realms plugin,Location position)
	{
	    for (Region region : plugin.stronghold.getRegionManager().getContainingRegions(position))
	    {
	    	if (region != null)
	    	{
	    		return region;
	    	}
	    }
		return null;
	}

	
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPlayerEditBookEvent(PlayerEditBookEvent event)
    {
    	if (event.getPlayer().getItemInHand().getType() == Material.BOOK_AND_QUILL)
    	{
    		//cmdBuildBook(event);
    		System.out.println("BookEdit");
    	}
    	
    }

    private void cmdBuildBook(PlayerInteractEvent event)
    {
    	Player player = event.getPlayer();
    	ItemStack handItem = event.getPlayer().getItemInHand();
    	ArrayList<String> msg = new ArrayList<String>();
    	if (plugin.getRealmModel().getModelStatus() == ModelStatus.MODEL_ENABLED)
    	{
    		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
    		{
		    	Block target = ((PlayerInteractEvent) event).getClickedBlock();
		    	Location pos = target.getLocation();
				if (handItem != null)
				{
					ItemMeta meta = handItem.getItemMeta();
					if (meta != null)
					{
		    			System.out.println("CheckMeta");
						if (meta.getDisplayName() == "[WHEAT]")
						{
							if (buildAt(pos, "WHEAT", player, msg))
							{
						    	msg.add(":"+meta.getDisplayName()+":");
								
							} else
							{
						    	msg.add("Not Build  WHEAT !");
						    	msg.add(":"+meta.getDisplayName()+":");
							}
						} else
						{
					    	msg.add("Not a BuildPlan name WHEAT!");
					    	msg.add(":"+meta.getDisplayName()+":");
						}
					} else
					{
				    	msg.add("No Meta in Hand !");
				    	msg.add(" ");
					}
						
				}
    		}
    	} else
    	{
			msg.add("[Realm Model] NOT enabled or too busy");
			msg.add("Try later again");
    	}
		plugin.getMessageData().printPage(player, msg, 1);
    }
    
    private boolean buildAt(Location pos, String name, Player player, ArrayList<String> msg)
    {
		LocationData iLoc = new LocationData(pos.getWorld().getName(), pos.getX(), pos.getY(), pos.getZ()+1);
		String sRegion = findSuperRegionAtLocation(plugin, player); 
		Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
		if (settle != null)
		{
			if (BuildPlanType.getBuildPlanType(name) != BuildPlanType.NONE)
			{
				BuildPlanMap buildPLan = plugin.getRealmModel().getData().readTMXBuildPlan(BuildPlanType.getBuildPlanType(name), 4, -1);
				

				if (checkBuild(pos, name, player, msg))
				{
					BuildPlanType bType = BuildPlanType.getBuildPlanType(name);
					McmdBuilder modelCommand = new McmdBuilder(plugin.getRealmModel(), settle.getId(), bType, iLoc,player);
					plugin.getRealmModel().OnCommand(modelCommand);
			    	msg.add(ChatColor.GREEN+"BUILD "+bType.name()+" in "+settle.getName()+" at "+(int)pos.getX()+":"+(int)pos.getY()+":"+(int)pos.getZ());
			    	msg.add(" ");
			    	return true;
				} else
				{
			    	msg.add(" ");
			    	msg.add(ChatColor.RED+"Above material is not available");
			    	msg.add("Give Items to warehouse ! ");
			    	msg.add(" ");
					return false;
				}
			} else
			{
		    	msg.add(ChatColor.RED+"No buildPlan set in Line 1");
		    	msg.add(" ");
		    	return false;
			}
		} else
		{
	    	msg.add("ChatColor.RED+Not in Range of a Settlement ");
	    	msg.add(" ");
	    	return false;
		}
    }
    
    private boolean checkBuild(Location pos, String name, Player player, ArrayList<String> msg)
    {
		LocationData iLoc = new LocationData(pos.getWorld().getName(), pos.getX(), pos.getY()+1, pos.getZ()+1);
		String sRegion = findSuperRegionAtLocation(plugin, player); 
		Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
		ItemList needMat = new ItemList();
		if (settle != null)
		{
			if (BuildPlanType.getBuildPlanType(name) != BuildPlanType.NONE)
			{
				BuildPlanType bType = BuildPlanType.getBuildPlanType(name);
				needMat = settle.settleManager().checkBuildingMaterials(plugin.getRealmModel(), settle, bType);
				if (needMat.isEmpty())
				{
					return true;
				} else
				{
					for (Item item : needMat.values())
					{
						msg.add(item.ItemRef()+":"+item.value());
					}
			    	return false;
				}
			} else
			{
		    	msg.add("No BuildPlan set in Line 1");
		    	msg.add(" ");
		    	return false;
			}
		} else
		{
	    	msg.add("Not in Range of a Settlement ");
	    	msg.add(" ");
	    	return false;
		}
    }

    private boolean checkAt(Location pos, String name, Player player, ArrayList<String> msg)
    {
		LocationData iLoc = new LocationData(pos.getWorld().getName(), pos.getX(), pos.getY()+1, pos.getZ()+1);
		String sRegion = findSuperRegionAtLocation(plugin, player); 
		Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
		if (settle != null)
		{
			if (BuildPlanType.getBuildPlanType(name) != BuildPlanType.NONE)
			{
				if (checkBuild(pos, name, player, msg))
				{
					BuildPlanType bType = BuildPlanType.getBuildPlanType(name);
			    	msg.add("Ready to BUILD "+bType.name()+" in "+settle.getName()+" at "+(int)pos.getX()+":"+(int)pos.getY()+":"+(int)pos.getZ());
			    	msg.add(" ");
			    	return true;
				} else
				{
			    	msg.add("Some Material not available");
			    	msg.add("Give Items to warehouse ! ");
					return false;
				}
			} else
			{
		    	msg.add("No BuildPlan set in Line 1");
		    	msg.add(" ");
		    	return false;
			}
		} else
		{
	    	msg.add("Not in Range of a Settlement ");
	    	msg.add(" ");
	    	return false;
		}
    }

    private void  checkSettleChest(InventoryCloseEvent event)
    {
    	Player player = (Player) event.getPlayer();
		Location pos = event.getPlayer().getLocation();
		Inventory inventory = event.getInventory();
		String sRegion = findSuperRegionAtLocation(plugin, player); 
		Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
		ItemList needMat = new ItemList();
		if (settle != null)
		{
			String region = findRegionAtLocation(plugin, player);
			
			if ((region.equalsIgnoreCase(BuildPlanType.HALL.name()))
					|| (region.equalsIgnoreCase(BuildPlanType.TOWNHALL.name())))
			{
				if (event.getView().getType() == InventoryType.CHEST)
				{
					donatePlayer.remove(player.getUniqueId().toString());
//					System.out.println("You are in a HALL closed a Chest");
					if (inventory.getSize() > 0)
					{
						for (ItemStack itemStack :inventory.getContents())
						{
							if (itemStack != null)
							{
								String name = itemStack.getType().name();
								settle.getWarehouse().depositItemValue(name, itemStack.getAmount());
								if (ConfigBasis.initFoodMaterial().containsKey(name))
								{
									settle.getReputations().addValue(ReputationType.FOOD, player.getName(), name, ConfigBasis.VALUABLE_POINT);
									System.out.println(" REPUTATION VALUABLE : "+name+": 1");
									player.sendMessage(ChatColor.GREEN+"You get Reputation for your donation");
									
								} else 
								if (ConfigBasis.initValuables().containsKey(name))
								{
									settle.getReputations().addValue(ReputationType.VALUABLE, player.getName(), name, ConfigBasis.VALUABLE_POINT);
									System.out.println(" REPUTATION VALUABLE : "+name+": 1");
									player.sendMessage(ChatColor.GREEN+"You get Reputation for your donation");
									
								} else 
								if (settle.getRequiredProduction().containsKey(name))
								{
									settle.getReputations().addValue(ReputationType.REQUIRED, player.getName(), name, ConfigBasis.REQUIRED_POINT);
									System.out.println("REPUTATION REQUIRED: "+name+": 1");
									player.sendMessage(ChatColor.GREEN+"You get Reputation for your donation");
									
								} else
								{
									if (settle.getReputations().containsKey(ReputationData.getRefName(player.getName(), ReputationType.DONATION)))
									{
										if(settle.getReputations().get(ReputationData.getRefName(player.getName(), ReputationType.DONATION)).getItemValues().containsKey(name))
										{
											player.sendMessage(ChatColor.DARK_PURPLE+"You always donate this item");
										} else
										{
											settle.getReputations().addValue(ReputationType.DONATION, player.getName(), name, ConfigBasis.DONATION_POINT);
											System.out.println(" REPUTATION DONATION : "+name+": 1");
											player.sendMessage(ChatColor.GREEN+"You get Reputation for your donation");
										}
										
									} else
									{
										settle.getReputations().addValue(ReputationType.DONATION, player.getName(), name, ConfigBasis.DONATION_POINT);
										System.out.println(" REPUTATION DONATION : "+name+": 1");
										player.sendMessage(ChatColor.GREEN+"You get Reputation for your donation");
									}
								}
//								System.out.println("HALL : "+itemStack.getType().name()+":"+itemStack.getAmount()+"OpenChests :"+donatePlayer.size());
							}
						}
						inventory.clear();
					}
				}
			}
		}
    }

    private void cmdWallSign(PlayerInteractEvent event, Block b)
    {
		Sign sign = (Sign) b.getState();
		String l0 = sign.getLine(0);
		String l1 = sign.getLine(1);
		String l2 = sign.getLine(2);
		String l3 = sign.getLine(3);
		
		if (l0.contains("[TRAP]"))
		{
			cmdSignPost(event,b);
		}		
		if (l0.contains("[HUNT]"))
		{
			cmdSignPost(event,b);
		}		
		if (l0.contains("[SPIDERSHED]"))
		{
			cmdSignPost(event,b);
		}		

		if (l0.contains("[ACQUIRE]"))
		{
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			// ohne settlement k�nnen auch geb�ude erworben werden
			// aber wenn in einem settlement , dann muss reputation vorhanden sein
			if (settle != null)
			{
				// der owner braucht keinen Nachweis
//				System.out.println("ACQUIRE Settlement "+settle.getId()+":"+settle.getName());
				if (settle.getOwnerId().equalsIgnoreCase(event.getPlayer().getName()) == false)
				{
					System.out.println("ACQUIRE Reputation "+settle.getId()+":"+settle.getReputations().getReputation(event.getPlayer().getName()));
					// ein fremder muss genug reputation haben
					if (settle.getReputations().getReputation(event.getPlayer().getName()) < ReputationStatus.CITIZEN.getValue())
					{
						event.getPlayer().sendMessage(ChatColor.DARK_PURPLE+"You need more Reputation in this settlement !");
						event.getPlayer().sendMessage(ChatColor.DARK_PURPLE+"You need more than "+ReputationStatus.CITIZEN.getValue());
						return;
					}
				}
			}
			Region region = findRegionAtPosition(plugin, b.getLocation());
			if (region != null)
			{
				double cost = plugin.getServerData().getRegionTypeCost(region.getType());
				cost = cost * 2;
				if (plugin.economy.has(event.getPlayer().getName(), cost))
				{
					if (event.getPlayer().getInventory().getItemInHand().getType() == Material.AIR)
					{
						EconomyResponse eResponse = plugin.economy.withdrawPlayer(event.getPlayer().getName(), cost);
						Building building = plugin.getRealmModel().getBuildings().getBuildingByRegion(region.getID());
						building.setOwnerId(event.getPlayer().getName());
						plugin.getData().writeBuilding(building);
						event.getPlayer().sendMessage(ChatColor.GREEN+"You are now owner of this building");
						event.getPlayer().sendMessage(ChatColor.YELLOW+"Remember to remove the AQUIRE sign !");
						// ohne settlement k�nnen auch geb�ude erworben werden
						// aber dann gibt es auch keine reputation!
						if (settle != null)
						{
							settle.getReputations().addValue(ReputationType.MEMBER, event.getPlayer().getName(), region.getType(), ConfigBasis.VALUABLE_POINT);
							System.out.println(" REPUTATION MEMBER : "+region.getType()+": 1");
							event.getPlayer().sendMessage(ChatColor.GREEN+"You gain reputation as citizen of the settlement ");
						}
					} else
					{
						event.getPlayer().sendMessage(ChatColor.DARK_PURPLE+"Your hand must be empty !");
					}
					
				} else
				{
					event.getPlayer().sendMessage(ChatColor.DARK_PURPLE+"You have need "+ConfigBasis.format2(cost)+plugin.economy.currencyNamePlural());
				}
			}
				
		}
		
		if (l0.contains("[WAREHOUSE]"))
		{
//			cmdBuildPlanBook(event);
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleWarehouse cmdWare = new CmdSettleWarehouse();
				cmdWare.setPara(0, settle.getId());
				cmdWare.setPara(1, this.lastPage);
				if (cmdWare.canExecute(plugin, event.getPlayer()))
				{
//					cmdWare.execute(plugin, event.getPlayer());
					cmdWare.execute(plugin, event.getPlayer());
					lastPage = cmdWare.getPage()+1;
				}
			}
			return;
		}
		if (l0.contains("[INFO]"))
		{
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleInfo cmdInfo = new CmdSettleInfo();
				cmdInfo.setPara(0, settle.getId());
				cmdInfo.setPara(1, 1);
				if (cmdInfo.canExecute(plugin, event.getPlayer()))
				{
					cmdInfo.execute(plugin, event.getPlayer());
				}
			}
			return;
		}
		if (l0.contains("[TRADER]"))
		{
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleTrader cmd = new CmdSettleTrader();
				cmd.setPara(0, settle.getId());
				cmd.setPara(1, 1);
				if (cmd.canExecute(plugin, event.getPlayer()))
				{
					cmd.execute(plugin, event.getPlayer());
				}
			}
			return;
		}
		if (l0.contains("[BUILDINGS]"))
		{
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleBuildingList cmd = new CmdSettleBuildingList();
				cmd.setPara(0, settle.getId());
				cmd.setPara(1, marketPage);
				if (cmd.canExecute(plugin, event.getPlayer()))
				{
					cmd.execute(plugin, event.getPlayer());
					marketPage = cmd.getPage()+1;
				}
		}
			return;
		}
		if (l0.contains("[PRODUCTION]"))
		{
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleProduction cmd = new CmdSettleProduction();
				cmd.setPara(0, settle.getId());
				cmd.setPara(1, marketPage);
				if (cmd.canExecute(plugin, event.getPlayer()))
				{
					cmd.execute(plugin, event.getPlayer());
					marketPage = cmd.getPage()+1;
				}
			}
			return;
		}

		if (l0.contains("[MARKET]"))
		{
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleMarket cmd = new CmdSettleMarket();
				cmd.setPara(0, settle.getId());
				cmd.setPara(1, marketPage);
				if (cmd.canExecute(plugin, event.getPlayer()))
				{
					cmd.execute(plugin, event.getPlayer());
					marketPage = cmd.getPage()+1;
				}
			}
			return;
		}

		if (l0.contains("[NOSELL]"))
		{
//			event.getPlayer().sendMessage("You will get a Book with required Items for the Settlement :"+l1);
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleNoSell  cmd = new CmdSettleNoSell();
				cmd.setPara(0, settle.getId());
				cmd.setPara(1, this.lastPage);
				cmd.execute(plugin, event.getPlayer());
				lastPage = cmd.getPage()+1;
				
			}
//			cmdRequiredBook(event);
		}

		
		if (l0.contains("[SELL]"))
		{
//			event.getPlayer().sendMessage("You will get a Book with required Items for the Settlement :"+l1);
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				String itemRef = l1.toUpperCase();
				System.out.println("Sell :"+itemRef);
				if (settle.settleManager().getDontSell().containsKey(itemRef))
				{
					l3 = "NO SELL";
					sign.update();
					event.getPlayer().sendMessage("No sell of "+itemRef);
				} else
				{
					int amount = 1;
					try
					{
						amount = Integer.valueOf(l2);
						if (amount < 1 )
						{
							amount = 1;
						}
						
					} catch (Exception e)
					{
						System.out.println("Sell amount exeption! ");
						amount = 1;
					}
					double price = plugin.getData().getPriceList().getBasePrice(itemRef);
					price = price * amount;
					l3 = ConfigBasis.setStrformat2(price,7);
					sign.update();
					int stock = settle.getWarehouse().getItemList().getValue(itemRef);
					if (stock > (amount * 2))
					{
						if (Realms.economy != null)
						{
							if (Realms.economy.has(event.getPlayer().getName(),price))
							{
								ItemStack item = new ItemStack(Material.getMaterial(itemRef), amount);
								event.getPlayer().getInventory().addItem(item);
								Realms.economy.withdrawPlayer(event.getPlayer().getName(), price);
								settle.getWarehouse().withdrawItemValue(itemRef, amount);
								System.out.println("Settle SELL :"+itemRef+":"+amount+":"+price);
								event.getPlayer().sendMessage("You bought "+itemRef+":"+amount+ConfigBasis.setStrformat2(price,9));
								event.getPlayer().updateInventory();
							}
						} else
						{
							event.getPlayer().sendMessage("NO economy !");
						}
					} else
					{
						System.out.println("No Stock");
						event.getPlayer().sendMessage("No Stock of "+itemRef);
					}
				}
				
			}
//			cmdRequiredBook(event);
		}

		if (l0.contains("[BUY]"))
		{
//			event.getPlayer().sendMessage("You will get a Book with required Items for the Settlement :"+l1);
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				String itemRef = l1.toUpperCase();
				System.out.println("Buy :"+itemRef);
				if ((settle.getWarehouse().getItemList().getValue(itemRef)/64) > (settle.getWarehouse().getItemMax() / 64 / 5))
				{
					l3 = "NO BUY";
					sign.update();
					event.getPlayer().sendMessage("No buy of "+itemRef);
					
				} else
				{
					int amount = 1;
					try
					{
						amount = Integer.valueOf(l2);
						if (amount < 1 )
						{
							amount = 1;
						}
						if (amount > 64 )
						{
							amount = 64;
						}
						
					} catch (Exception e)
					{
						System.out.println("Buy amount exeption! ");
						amount = 1;
					}
					double price = plugin.getData().getPriceList().getBasePrice(itemRef);
					price = price * amount;
					l3 = ConfigBasis.setStrformat2(price,7);
					sign.update();
//					int stock = settle.getWarehouse().getItemList().getValue(itemRef);
					if (Realms.economy != null)
					{
						if (settle.getBank().getKonto() > price)
						{
							ItemStack itemStack = new ItemStack(Material.getMaterial(itemRef), amount);
							if (event.getPlayer().getInventory().contains(Material.getMaterial(itemRef), amount) == true)
							{
								event.getPlayer().getInventory().removeItem(itemStack);
								Realms.economy.depositPlayer(event.getPlayer().getName(), price);
								settle.getWarehouse().depositItemValue(itemRef, amount);
								settle.getBank().withdrawKonto(price, event.getPlayer().getName(), settle.getId());
								event.getPlayer().sendMessage("You sold "+itemRef+":"+amount+ConfigBasis.setStrformat2(price,9));
								System.out.println("Settle BUY "+itemRef+":"+amount);
								event.getPlayer().updateInventory();
							} else
							{
								event.getPlayer().sendMessage("You have not enough items:"+itemRef+":"+amount);
							}
						} else
						{
							event.getPlayer().sendMessage("The settlement has not enough money");
						}
					} else
					{
						event.getPlayer().sendMessage("NO economy !");
					}
				}
				
			}
//			cmdRequiredBook(event);
		}
		
		if (l0.contains("[REQUIRE]"))
		{
//			event.getPlayer().sendMessage("You will get a Book with required Items for the Settlement :"+l1);
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				CmdSettleRequired  cmd = new CmdSettleRequired();
				if (cmd.canExecute(plugin, event.getPlayer()))
				{
					cmd.setPara(0, settle.getId());
					cmd.setPara(1, this.lastPage);
					cmd.execute(plugin, event.getPlayer());
					lastPage = cmd.getPage()+1;
				}
			}
//			cmdRequiredBook(event);
		}

		if (l0.contains("[WORKSHOP]"))
		{
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			Integer regionId = findRegionIdAtLocation(plugin, event.getPlayer());
			Building building = settle.getBuildingList().getBuildingByRegion(regionId);
	    	ArrayList<String> msg = new ArrayList<String>();

	    	if (event.getPlayer().isOp() == false)
	    	{
		    	if (building.getOwnerId().equalsIgnoreCase(event.getPlayer().getName()) == false)
				{
					msg.add("You are not the owner");
					plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
					
				}
	    	}
			msg.add("Settlement ["+settle.getId()+"] : "+ChatColor.YELLOW+settle.getName());
			int index = 0;
			for (Item item :  building.getSlots())
			{
				if (item != null)
				{
					msg.add(ChatColor.YELLOW+"Slot"+index+": "+ChatColor.GREEN+item.ItemRef()+":"+item.value());
				}
				index++;
			}
			msg.add(" ");
			plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
		}

		if (l0.contains("[TRAIN]"))
		{
	    	ArrayList<String> msg = new ArrayList<String>();
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			Integer regionId = findRegionIdAtLocation(plugin, event.getPlayer());
			for (Building building : settle.getBuildingList().values())
			{
				if (regionId == building.getHsRegion())
				{
					if (BuildPlanType.getBuildGroup(building.getBuildingType()) == 5 )
					{
						sign.setLine(1, String.valueOf(building.getTrainType().name()));
						sign.update();
						building.addMaxTrain(1);
						msg.add("Settlement ["+settle.getId()+"] : "
								+ChatColor.YELLOW+settle.getName()
								+ChatColor.GREEN+" Age: "+settle.getAge()
								+":"+settle.getProductionOverview().getCycleCount());
						msg.add("Building: "+building.getBuildingType().name());
						msg.add("Train   : "+ChatColor.YELLOW+building.getTrainType().name());
						msg.add("Need    : "+ChatColor.YELLOW+ConfigBasis.setStrright(building.getTrainTime(),4)+" Cycles");
					} else
					{
						msg.add("Building: "+building.getBuildingType().name());
						msg.add("Train   : "+ChatColor.RED+"not possible !");
					}
					plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
				}
			}
		}
		if (l0.contains("[BOOK]"))
		{
	    	System.out.println("Book Sign "+bookId);
	    	Region region = findRegionAtPosition(plugin, event.getPlayer().getLocation());
	    	if (event.getPlayer().isOp() == false)
	    	{
				if ((region.getType().equalsIgnoreCase(BuildPlanType.LIBRARY.name()) == false)
					&&  (region.getType().equalsIgnoreCase("BIBLIOTHEK") == false)
					&&  (region.getType().equalsIgnoreCase("HALL") == false)
					)
				{
					event.getPlayer().sendMessage(ChatColor.GREEN+"The Booklist is available in BIBLIOTHEK or LIBRARY");
					return;
				}
	    	}
			if (event.getPlayer().getItemInHand().getType() == Material.BOOK)
			{
		    	System.out.println("Book in Hand "+bookId);
		    	if (bookId > 0)
		    	{
					CaseBook cBook = plugin.getRealmModel().getCaseBooks().get(bookId);
	    			if (cBook.isEnabled())
	    			{
	    				event.getPlayer().sendMessage("You get a new Book");
	    	    		PlayerInventory inventory = event.getPlayer().getInventory();
	    	    		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
	    	    		book = CaseBook.writeBook(book, cBook);
	    	    		event.getPlayer().sendMessage("Create Book "+cBook.getId()+" : "+cBook.getAuthor()+" | "+cBook.getTitel());
//	    				inventory.addItem(book);
	    				event.getPlayer().setItemInHand(book);
	    			} else
	    			{
	    				event.getPlayer().sendMessage(ChatColor.RED+"The book is not Enabled");
	    			}
					
		    	} else
		    	{
					event.getPlayer().sendMessage(ChatColor.RED+"Book Id not valid !");
		    	}
			} else
			{
		    	System.out.println("Book List "+bookId);
		    	// toogle id from 1 to size 
				if ((bookId+1) <= plugin.getRealmModel().getCaseBooks().size()  )
				{
					bookId++;
				} else
				{
					bookId = 1;
				}
				CaseBook cBook = plugin.getRealmModel().getCaseBooks().get(bookId);
	    		sign.setLine(1, String.valueOf(bookId));
	    		sign.setLine(2, cBook.getTitel());
	    		sign.setLine(3, "Enable: "+cBook.isEnabled());
				sign.update(true);
			}
			
		}
		if (l0.contains("[DONATE]"))
		{
			event.getPlayer().sendMessage(ChatColor.GREEN+"Put items in chest below this wallsign !");
			event.getPlayer().sendMessage(ChatColor.GREEN+"You will earn some reputation");
			event.getPlayer().sendMessage(ChatColor.GREEN+"You stay in HALL or TOWNHALL");
		}
    	
    }

    private void cmdLeftWallSign(PlayerInteractEvent event, Block b)
    {
		Sign sign = (Sign) b.getState();
		String l0 = sign.getLine(0);
		String l1 = sign.getLine(1);
		String l2 = sign.getLine(2);
		String l3 = sign.getLine(3);

		if (l0.contains("[SELL]"))
		{
//			event.getPlayer().sendMessage("You will get a Book with required Items for the Settlement :"+l1);
			String sRegion = findSuperRegionAtLocation(plugin, event.getPlayer()); 
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion);
			if (settle != null)
			{
				String itemRef = l1.toUpperCase();
				System.out.println("Check Sell :"+itemRef);
				if (settle.settleManager().getDontSell().containsKey(itemRef))
				{
					l3 = "NO SELL";
				} else
				{
					int amount = 1;
					try
					{
						amount = Integer.valueOf(l2);
						if (amount < 1 )
						{
							amount = 1;
						}
						
					} catch (Exception e)
					{
						amount = 1;
					}
					double price = plugin.getData().getPriceList().getBasePrice(itemRef);
					price = price * amount;
					l3 = ConfigBasis.setStrformat2(price,7);
				}
				sign.update();
			}
//			cmdRequiredBook(event);
		}

    }
    
    
    private void cmdSignPost(PlayerInteractEvent event, Block b)
    {
		Sign sign = (Sign) b.getState();
		String l0 = sign.getLine(0);
		String l1 = sign.getLine(1);
    	
		if (l0.contains("[HUNT]"))
		{
			long actTime = plugin.getServer().getWorlds().get(0).getTime();
			if (Math.abs(actTime - lastHunt)< 1000)
			{
		    	ArrayList<String> msg = new ArrayList<String>();
				msg.add("[REALMS] Timeout hunt 1 h");
				msg.add(" ");
    			plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
				System.out.println("[REALMS] Timeout hunt");
				return;
			}
			lastHunt = plugin.getServer().getWorlds().get(0).getTime();
			Location pos = b.getLocation();
			// find required region
			Region region = findRegionAtPosition( plugin, pos);
			if (region == null)
			{
				System.out.println("NO Region found");
				return;
			}
			if (region.getType().equalsIgnoreCase("HUNTER") == false)
			{
				System.out.println("WRONG Region found");
				return;
			}
			for(Entity entity : pos.getWorld().getEntities()) 
			{
				EntityType eType = entity.getType(); 
                if( eType== EntityType.SPIDER) 
                {
                    double distance = entity.getLocation().distance(pos);
                    if(distance <= 100) 
                    {
                    	entity.remove();
                    	System.out.println("Spider hunt ");
                    	ItemStack itemStack = new ItemStack(Material.SPIDER_EYE,1,(short) 0);
//                    	Egg spiderEgg = 
                    	event.getPlayer().getInventory().addItem(itemStack);
						event.getPlayer().updateInventory();
}						return;
                }
                if( eType== EntityType.SKELETON) 
                {
                    double distance = entity.getLocation().distance(pos);

                    if(distance <= 100) 
                    {
                    	entity.remove();
                    	System.out.println("Skeleton hunt ");
                    	ItemStack itemStack = new ItemStack(Material.BONE,2,(short) 0);
//                    	Egg spiderEgg = 
                    	event.getPlayer().getInventory().addItem(itemStack);
						event.getPlayer().updateInventory();
						return;
                    }
                }
                if( eType== EntityType.ZOMBIE) 
                {
                    double distance = entity.getLocation().distance(pos);

                    if(distance <= 100) 
                    {
                    	entity.remove();
                    	System.out.println("ZOMBIE hunt ");
                    	ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH,2,(short) 0);
//                    	Egg spiderEgg = 
                    	event.getPlayer().getInventory().addItem(itemStack);
						event.getPlayer().updateInventory();
						return;
                    }
                }
                if( eType== EntityType.CREEPER) 
                {
                    double distance = entity.getLocation().distance(pos);

                    if(distance <= 100) 
                    {
                    	entity.remove();
                    	System.out.println("Creeper hunt ");
                    	ItemStack itemStack = new ItemStack(Material.SULPHUR,2,(short) 0);
//                    	Egg spiderEgg = 
                    	event.getPlayer().getInventory().addItem(itemStack);
						event.getPlayer().updateInventory();
						return;
                    }
                }
            }
			return;
		}
		
		if (l0.contains("[TRAP]"))
		{
			long actTime = plugin.getServer().getWorlds().get(0).getTime();
			if (Math.abs(actTime - lastTame)< 1000)
			{
		    	ArrayList<String> msg = new ArrayList<String>();
				msg.add("[REALMS] Timeout hunt 1 h");
				msg.add(" ");
    			plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
				System.out.println("[REALMS] Timeout hunt");
				return;
			}
			lastTame = plugin.getServer().getWorlds().get(0).getTime();
			Location pos = b.getLocation();
			// find required region
			Region region = findRegionAtPosition( plugin, pos);
			if (region == null)
			{
				System.out.println("NO Region found");
				return;
			}
			if (region.getType().equalsIgnoreCase("TAMER") == false)
			{
				System.out.println("Wrong Region found");
				return;
			}
        	Location tele = new Location(region.getLocation().getWorld(),region.getLocation().getX(),region.getLocation().getY(),region.getLocation().getZ());
        	tele.setY(tele.getY()-2);

			for(Entity entity : pos.getWorld().getEntities()) 
			{
				if (tele.distance(entity.getLocation()) > 7.5)
				{
					EntityType eType = entity.getType(); 
	                if( eType== EntityType.PIG) 
	                {
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Pig trap ");
	//                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 90);
	//                    	event.getPlayer().getInventory().addItem(itemStack);
	//						event.getPlayer().updateInventory();
							return;
	                    }
	                }
	                if( eType== EntityType.SHEEP) 
	                {
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Sheep trap ");
	//                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 91);
	////                    	Egg spiderEgg = 
	//                    	event.getPlayer().getInventory().addItem(itemStack);
	//						event.getPlayer().updateInventory();
							return;
	                    }
	                }
	                if( eType== EntityType.COW) 
	                {
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Cow trap ");
	//                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 92);
	////                    	Egg spiderEgg = 
	//                    	event.getPlayer().getInventory().addItem(itemStack);
	//						event.getPlayer().updateInventory();
							return;
	                    }
	                }
	                if( eType== EntityType.HORSE) 
	                {
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Horse trap ");
	//                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 100);
	////                    	Egg spiderEgg = 
	//                    	event.getPlayer().getInventory().addItem(itemStack);
	//						event.getPlayer().updateInventory();
							return;
	                    }
	                }
	                if( eType== EntityType.OCELOT) 
	                {
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Ocelot trap ");
	//                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 98);
	////                    	Egg spiderEgg = 
	//                    	event.getPlayer().getInventory().addItem(itemStack);
	//						event.getPlayer().updateInventory();
							return;
	                    }
	                }
	                if( eType== EntityType.WOLF) 
	                {
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Wolf trap ");
	//                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 95);
	////                    	Egg spiderEgg = 
	//                    	event.getPlayer().getInventory().addItem(itemStack);
	//						event.getPlayer().updateInventory();
							return;
	                    }
	                }
	                if( eType== EntityType.VILLAGER) 
	                {
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Villager trap ");
	//                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 120);
	////                    	Egg spiderEgg = 
	//                    	event.getPlayer().getInventory().addItem(itemStack);
	//						event.getPlayer().updateInventory();
							return;
	                    }
	                }
				}
			}
			return;	
		}
		if (l0.contains("[SPIDERSHED]"))
		{
//			if (plugin.getServ)
			Location pos = b.getLocation();
			// find required region
			Region region = findRegionAtPosition( plugin, pos);
			if (region.getType().equalsIgnoreCase("SPIDERSHED") == false)
			{
				return;
			}
        	Location tele = new Location(region.getLocation().getWorld(),region.getLocation().getX(),region.getLocation().getY(),region.getLocation().getZ());
        	tele.setY(tele.getY()-1);

			for(Entity entity : pos.getWorld().getEntities()) 
			{
				if (tele.distance(entity.getLocation()) > 7.5)
				{
					EntityType eType = entity.getType(); 
	                if( eType== EntityType.SPIDER) 
	                {
	                	
	                    double distance = entity.getLocation().distance(pos);
	
	                    if(distance <= 100) 
	                    {
	                    	entity.teleport(tele);
	                    	System.out.println("Spider trap ");
//	                    	ItemStack itemStack = new ItemStack(Material.MONSTER_EGG,1,(short) 52);
//	//                    	Egg spiderEgg = 
//	                    	event.getPlayer().getInventory().addItem(itemStack);
//							event.getPlayer().updateInventory();
							return;
	                    }
	                }
				}
			}
		}
    }
    
    
    private Location getSignBase(Block b)
    {
    	Location position = b.getLocation();
    	if (b.getRelative(BlockFace.NORTH).getType() != Material.AIR)
    	{
    		return b.getRelative(BlockFace.NORTH).getLocation();
    	}
    	if (b.getRelative(BlockFace.EAST).getType() != Material.AIR)
    	{
    		return b.getRelative(BlockFace.EAST).getLocation();
    	}
    	if (b.getRelative(BlockFace.SOUTH).getType() != Material.AIR)
    	{
    		return b.getRelative(BlockFace.SOUTH).getLocation();
    	}
    	if (b.getRelative(BlockFace.WEST).getType() != Material.AIR)
    	{
    		return b.getRelative(BlockFace.WEST).getLocation();
    	}
    	
    	return position;
    }

    /**
     * check a sign if registered.
     * when not registered write to sign list of the settlement
     * @param event
     * @param b 
     */
    private void cmdRegisterSign(PlayerInteractEvent event, Block b)
    {
    	Location pos = getSignBase(b);
    	Region region = findRegionAtPosition(plugin, pos );
    	if (region != null)
    	{
    		SuperRegion sRegion = findSuperRegionAtPosition(plugin, b.getLocation());
    		if (sRegion != null)
    		{
    			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion.getName());
    			if (settle != null)
    			{
    				event.getPlayer().sendMessage("Register Sign to Settlement: "+settle.getName());
    				if (settle.getBuildingList().containRegion(region.getID()) == true)
    				{
	    				int BuildingId = settle.getBuildingList().getBuildingByRegion(region.getID()).getId();
	    				if (settle.getSignList().containsKey(BuildingId) == false)
	    				{
	    					Sign sBlock =	((Sign) b.getState());
	    					String[] text = sBlock.getLines();
	    					settle.getSignList().put
	    					(
	    						BuildingId, 
	    						new SignPos
	    						(
	    							BuildingId,
	    							new LocationData(
		    							b.getLocation().getWorld().getName(),
		    							b.getLocation().getX(),
		    							b.getLocation().getY(),
		    							b.getLocation().getZ()
	    							),
	    							text
	    						)
	    					);
	    				}
    				}
    			}
    		}
    	}
    	
    }
    
    private void cmdSignUpdate(PlayerInteractEvent event, Block b)
    {
		SuperRegion sRegion = findSuperRegionAtPosition(plugin, b.getLocation());
		if (sRegion != null)
		{
			Settlement settle = plugin.getRealmModel().getSettlements().findName(sRegion.getName());
			if (settle != null)
			{
				event.getPlayer().sendMessage("Update Sign on Settlement: "+settle.getName());
				plugin.doSignUpdate(settle);
			}
		}
    }
    
    private void cmdBookList(PlayerInteractEvent event, Block b)
    {
    	Region region = findRegionAtPosition(plugin, b.getLocation());
    	if (event.getPlayer().isOp() == false)
    	{
    		if ((region.getType().equalsIgnoreCase(BuildPlanType.LIBRARY.name()) == false)
    			&&  (region.getType().equalsIgnoreCase("BIBLIOTHEK") == false))
    		{
    			return;
    		}
    	}
		CmdRealmsBookList cmd = new CmdRealmsBookList();
		cmd.setPara(0, marketPage);
		cmd.execute(plugin, event.getPlayer());
		bookPage = cmd.getPage()+1;
    	
    }

	@EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = true )
    private void onNPC(EntityInteractEvent event)
    {
//    	System.out.println("[REALMS] NPC EntityInteractEvent");
    	event.setCancelled(true);
    }

	private void shotArrow(Block b)
	{
        //Calculate trajectory of the arrow
        Location loc = b.getRelative(BlockFace.UP, 2).getLocation();
//        Location playerLoc = player.getLocation();
        Vector vel = new Vector(loc.getX()-30, loc.getY(), loc.getZ());
//        
//        //Spawn and set velocity of the arrow
        double speed = 0.5;
//        Entity tnt = b.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
//        tnt.setVelocity(vel.multiply(speed));
//        tnt.setFireTicks(150);
        Arrow arrow = b.getWorld().spawnArrow(loc, vel, (float) (0.2 * speed), 12);
//        arrow.setPassenger(arg0);
		ArrayList<String> lore = new ArrayList<String>();
        arrow.setFireTicks(250);
        arrow.setVelocity(vel.multiply(speed));
        
	}

	private void cmdBuildat(PlayerInteractEvent event, Block b)
	{
    	ArrayList<String> msg = new ArrayList<String>();
		Sign sign = (Sign) b.getState();
		String l0 = sign.getLine(0);
		String l1 = sign.getLine(1);
//			System.out.println("SignPost");
		if (l1 != "")
		{
			Location pos = b.getLocation();
	    	if (event.getPlayer().getItemInHand().getType() == Material.BOOK)
	    	{
    			System.out.println("Check");
	    		checkAt(pos, l1, event.getPlayer(), msg);
	    		plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
	    		return;
	    	} else
	    	{
	    		// start Build with empty hand on RightClick
    	    	if (
    	    		(event.getPlayer().getItemInHand().getType() == Material.AIR)
    	    		&& (event.getAction() == Action.RIGHT_CLICK_BLOCK)
    	    		)
    	    	{
	    			System.out.println("BuildAt");
	    			if (buildAt( pos, l1, event.getPlayer(), msg))
	    			{
				    	msg.add("Build startet  ");
				    	msg.add(" ");
		    			sign.setLine(0, "");
		    			sign.update();
	    			} else
	    			{
				    	msg.add("Build NOT started : "+l1);
				    	msg.add(" ");
	    			}
	    			plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
	    			return;
    	    	} else
    	    	{
			    	msg.add("Use empty hand for startup build: "+l1);
			    	msg.add(" ");
    	    	}
    			plugin.getMessageData().printPage(event.getPlayer(), msg, 1);
    			return;
	    	}
		}
	}
	
}
