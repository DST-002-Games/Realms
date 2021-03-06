package net.krglok.realms.command;

import java.util.ArrayList;

import net.krglok.realms.Realms;
import net.krglok.realms.Common.aRealmsCommand;
import net.krglok.realms.Common.RealmsCommandType;
import net.krglok.realms.Common.RealmsSubCommandType;
import net.krglok.realms.core.Building;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Owner;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.data.BookStringList;
import net.krglok.realms.kingdom.Kingdom;
import net.krglok.realms.kingdom.Lehen;
import net.krglok.realms.npc.GenderType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdFeudalInfo extends aRealmsCommand
{

	private int page;
	private int lehenId;
	
	public CmdFeudalInfo()
	{
		super(RealmsCommandType.FEUDAL, RealmsSubCommandType.INFO);
		description = new String[] {
				ChatColor.YELLOW+"/feudal INFO [lehenId] [page]  ",
				"Show a overview Information of the Lehen",
		    	"Resident, Units, Bankaccount",
		    	"Happines, Fertility and Foodstorage",
		    	"and the vasale references",
		    	" "
			};
			requiredArgs = 1;
			page = 0;
			lehenId = 0;
	}

	@Override
	public void setPara(int index, String value)
	{

	}

	@Override
	public void setPara(int index, int value)
	{
		switch (index)
		{
		case 0:
			lehenId = value;
			break;
		case 1:
			page = value;
			break;
		default:
			break;
		}
	}

	@Override
	public void setPara(int index, boolean value)
	{

	}

	@Override
	public void setPara(int index, double value)
	{

	}

	@Override
	public String[] getParaTypes()
	{
		return new String[] { int.class.getName(), int.class.getName() };
	}

	@Override
	public void execute(Realms plugin, CommandSender sender)
	{
		BookStringList msg = new BookStringList();
		Lehen lehen = plugin.getData().getLehen().getLehen(lehenId);
		Lehen parent = plugin.getData().getLehen().getLehen(lehen.getParentId());
		Kingdom kingdom = plugin.getData().getKingdoms().getKingdom(lehen.getKingdomId());
		// create list
		msg.add("Lehen "+lehen.getId());
		msg.add(" "+lehen.getName());
		msg.add("Kingdom "+kingdom.getId());
		msg.add(" "+kingdom.getName());
		msg.add("Title "+lehen.getNobleLevel());
		
		if (parent != null)
		{
			msg.add("Landlord "+parent.getOwner().getNobleLevel());
			msg.add(" "+parent.getOwner().getPlayerName());
			msg.add(" from "+parent.getName());
		}
		msg.add("Beds : "+lehen.getSettlerMax());
		msg.add("Settlers : "+lehen.getResident().getSettlerCount());
		msg.add("Happiness: "+(int) (lehen.getResident().getHappiness()));
		int breed = lehen.getResident().getNpcList().getSchwanger().size();
		int breedBase = lehen.getResident().getNpcList().getGender(GenderType.WOMAN).size();
		int child = lehen.getResident().getNpcList().getChild().size();
		msg.add("Fertility: "+breed+"/"+breedBase+" child: "+child);
		msg.add("Barrack : "+(lehen.getBarrack().getUnitList().size())+"/"+lehen.getBarrack().getUnitMax());
		msg.add("Bank  : "+((int) lehen.getBank().getKonto()));
//		msg.add("Power     : "+lehen.getPower());

		msg.add("Buildings : ["+lehen.getBuildingList().size()+"]");
//		for (Building building : lehen.getBuildingList().values())
//		{
//    		msg.add(ChatColor.YELLOW+"+"+building.getId()
//    				+" | "+ChatColor.YELLOW+building.getBuildingType()
//    				+" | "+ChatColor.GOLD+building.getSettler()
//    				+" Train: "+building.getTrainType()
//    				);
//		}
		
		msg.add("Your tributs are");
	    for (Settlement settle : plugin.getData().getSettlements().values())
	    {
	    	
	    	if (settle.getTributId() == lehenId)
	    	{
	    		msg.add("+"+settle.getId()
	    				+" | "+settle.getName()
//	    				+" | "+settle.getSettleType().name()
//	    				+" Owner: "+settle.getOwnerId()
//	    				+" in "+settle.getPosition().getWorld()
	    				);
	    	}
	    }

		msg.add("Your liege are :");
		for (Lehen vasal : plugin.getData().getLehen().values())
		{
			if (vasal.getParentId() == lehen.getId())
			{
	    		msg.add(" "+vasal.getId()
	    				+" | "+vasal.getName()
	    				+" | "+vasal.getNobleLevel()
//	    				+" | "+vasal.getOwnerId()
//	    				+" | "+vasal.getKingdomId()
	    				);
			}
		}
		
		
		msg.add(" ");
    	if (sender instanceof Player)
		{
    		Player player = ((Player) sender);
        	PlayerInventory inventory = player.getInventory();
        	ItemStack holdItem = player.getItemInHand();
        	if (holdItem.getData().getItemType() != Material.WRITTEN_BOOK)
        	{
        		holdItem  = new ItemStack(Material.WRITTEN_BOOK, 1);
   				inventory.addItem(holdItem);
        	}
			writeBook(holdItem, msg, lehen.getName(),"Lehen Info");
			player.updateInventory();
		}
		
		plugin.getMessageData().printPage(sender, msg, page);

	}

	@Override
	public boolean canExecute(Realms plugin, CommandSender sender)
	{
		if (plugin.getData().getLehen().getLehen(lehenId) == null)
		{
			errorMsg.add(ChatColor.RED+"The lehenId is wrong !");
			return false;
		}
		Player player = (Player) sender;
		Owner owner = plugin.getData().getOwners().getOwner(player.getUniqueId().toString());
		if (isOpOrAdmin(sender) == false)
		{
			if (plugin.getData().getLehen().getLehen(lehenId).getOwnerId() != owner.getId())
			{
				errorMsg.add(ChatColor.RED+"You are not the owner !");
				return false;
			}
		}
			
		return true;
	}

}
