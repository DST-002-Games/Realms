package net.krglok.realms.command;

import java.util.ArrayList;

import net.krglok.realms.Realms;
import net.krglok.realms.Common.Item;
import net.krglok.realms.Common.ItemPriceList;
import net.krglok.realms.Common.aRealmsCommand;
import net.krglok.realms.Common.RealmsCommandType;
import net.krglok.realms.Common.RealmsSubCommandType;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.data.BookStringList;
import net.krglok.realms.model.ModelStatus;
import net.krglok.realms.npc.GenderType;
import net.krglok.realms.npc.NPCType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdSettleInfo extends aRealmsCommand
{
	int settleID;
	int buildingId;
	int page ;

	public CmdSettleInfo( )
	{
		super(RealmsCommandType.SETTLE, RealmsSubCommandType.INFO);
		description = new String[] {
				ChatColor.YELLOW+"/settle INFO [SettleID] [BuildingId] ",
				"Show a overview Information of the settlement",
		    	"Settlers, Workers, Bankaccount",
		    	"Happines, Fertility and Foodstorage",
		    	"  "
		};
		requiredArgs = 1;
		this.settleID = 0;
		this.buildingId = 0;
		this.page = 1;  //default value
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
			settleID = value;
			break;
		case 1 :
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
		return new String[] { int.class.getName(), int.class.getName()  };
	}
	
	private ArrayList<String> makeSettleAnalysis(Settlement settle, int moth, ItemPriceList priceList)
	{
		ArrayList<String> msg = new ArrayList<>();
		// Resident Analyse
		msg.add(" ");
		msg.add("Bevölkerungsanalyse  ");
		if (settle.getResident().getSettlerCount() > settle.getResident().getSettlerMax())
		{
			msg.add("! Sie haben Überbevölkerung in der Siedlung. ");
			msg.add("  Dies macht die Siedler unglücklich auf lange Sicht!");
		}
		if (settle.getResident().getHappiness() < 0)
		{
			msg.add("! Ihre Siedler sind unglücklich. ");
		}
		if (settle.getResident().getNpcList().getBeggarNpc().size() > 0)
		{
			msg.add("! In der Siedler leiden "+settle.getResident().getNpcList().getBeggarNpc().size()+" Bettler  Hunger. ");
		}
		if (settle.getSettlerFactor() < 0.0)
		{
			msg.add("! Ihre Siedler haben keinen Wohnraum. ");
		}
//		if (settle.getEntertainFactor() < 0.9)
//		{
//			msg.add("! Ihre Siedler haben wenig Unterhaltung. ");
//		}
		if ((settle.getResident().getSettlerCount() < 8))
		{
			msg.add("! Ihre Siedler sind verhungert. Sie haben als Verwalter versagt!");
			msg.add("! Es würde mich nicht wundern, wenn eine Revolte ausbricht!!");
		}

		msg.add("  ");
		msg.add("Wirtschaftsanalyse  ");
		msg.add("! Ihre Siedler haben "+(int)(settle.getBank().getKonto())+" Thaler erarbeitet.");

		double price = 0.0;
		double balance = 0.0;
		for (Item item : settle.getWarehouse().getItemList().values())
		{
			price = Math.round(priceList.getBasePrice(item.ItemRef()));
			balance = balance + (item.value()*price);
		}
		msg.add("! Das Warenlager hat einen Wert von:  "+balance);
		
		if (settle.getTownhall().getWorkerCount() < settle.getTownhall().getWorkerNeeded())
		{
			msg.add("! Es fehlen Arbeiter. Deshalb produzieren einige Gebäude nichts!");
		}
		if (settle.getResident().getSettlerCount() < settle.getTownhall().getWorkerNeeded())
		{
			msg.add("! Es fehlen Siedler. Deshalb produzieren einige Gebäude nichts!");
		}
		if (settle.getResident().getSettlerCount() > settle.getTownhall().getWorkerNeeded())
		{
			msg.add("! Sie haben "+(settle.getResident().getSettlerCount() -settle.getTownhall().getWorkerNeeded())+" Siedler ohne Arbeit. ");
			msg.add("  Sie könnten neue Arbeitsgebäude bauen !");
		}

		if (settle.getRequiredProduction().size() > 0)
		{
			msg.add("!  Es fehlen "+settle.getRequiredProduction().size()+" verschiedene Rohstoffe zur Produktion.");
		}
		
		if ((settle.getWarehouse().getItemMax()-settle.getWarehouse().getItemCount()) < 512)
		{
			msg.add("!  Die Lagerkapazität ist knapp !  ");
			msg.add("   Freie Kapazitäte nur "+(settle.getWarehouse().getItemMax()-settle.getWarehouse().getItemCount()));
		}
		return msg;
	}


	@Override
	public void execute(Realms plugin, CommandSender sender)
	{
		BookStringList msg = new BookStringList();
		Settlement settle = plugin.getRealmModel().getSettlements().getSettlement(settleID);
		if (plugin.getRealmModel().getModelStatus() == ModelStatus.MODEL_ENABLED) 
		{
			ItemPriceList priceList = plugin.getData().getPriceList();
			int month = 1;
			if (settle != null)
			{
				msg.add("Settlement ["+settle.getId()+"] ");
				msg.add(": "+settle.getName());
				msg.add(" Age: "+settle.getAge()+" :"+settle.getProductionOverview().getCycleCount());
				msg.add(" Owner "+settle.getOwnerId());
				msg.add(" : Tribut to "+settle.getTributId());
				msg.add(" : Kingdom: "+settle.getKingdomId());
				msg.add("Biome: "+settle.getBiome());
				msg.add(" Enable:"+settle.isEnabled());
				msg.add(" Activ: "+settle.isActive());
				msg.add("Beds : "+settle.getResident().getSettlerMax());
				msg.add("Settlers: "+settle.getResident().getSettlerCount());
				msg.add("Workers : "+settle.getTownhall().getWorkerCount());
				msg.add("Happi: "+(int) (settle.getResident().getHappiness()));
				int breed = settle.getResident().getNpcList().getSchwanger().size();
				int breedBase = settle.getResident().getNpcList().getGender(GenderType.WOMAN).size();
				int child = settle.getResident().getNpcList().getChild().size();
				msg.add("Fertility: "+breed+"/"+breedBase);
				msg.add(": child: "+child);
//				msg.add("Deathrate: "+ChatColor.RED+settle.getResident().getDeathrate());
				msg.add("Barrack: "+(settle.getBarrack().getUnitList().size())+"/"+settle.getBarrack().getUnitMax());
				msg.add("Bank   : "+((int) settle.getBank().getKonto()));
				msg.add("Storage: "+settle.getWarehouse().getItemCount()+"/"+settle.getWarehouse().getItemMax());
				msg.add("Power  : "+settle.getPower());
				msg.add("Food : WHEAT "+settle.getWarehouse().getItemList().getValue("WHEAT"));
				msg.add("Food : BREAD "+settle.getWarehouse().getItemList().getValue("BREAD"));
				msg.add("=================");
				msg.add("Required Items : "+settle.getRequiredProduction().size());
				for (String itemRef : settle.getRequiredProduction().keySet())
				{
					Item item = settle.getRequiredProduction().getItem(itemRef);
					msg.add(ConfigBasis.setStrleft(item.ItemRef()+"__________",12)+":"+ConfigBasis.setStrright(item.value(),5));
				}
				msg.addAll(makeSettleAnalysis( settle, month, priceList));
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
					writeBook(holdItem, msg, settle.getName(),"INFO");
					((Player) sender).updateInventory();
				}
			}
		} else
		{
			msg.add("[Realm Model] NOT enabled or too busy");
			msg.add("Try later again");
		}
		plugin.getMessageData().printPage(sender, msg, page);
		page = 1;
	}

	@Override
	public boolean canExecute(Realms plugin, CommandSender sender)
	{
		if (plugin.getRealmModel().getModelStatus() == ModelStatus.MODEL_ENABLED)
		{
			if (sender.isOp())
			{
				return true;
			}
			if (plugin.getRealmModel().getSettlements().containsID(settleID))
			{
				return true;
			}
			errorMsg.add("Settlement not found !!!");
			errorMsg.add("The ID is wrong or not a number ?");
			return false;
		}
		errorMsg.add("[Realm Model] NOT enabled or too busy");
		errorMsg.add("Try later again");
		return false;
	}

}
