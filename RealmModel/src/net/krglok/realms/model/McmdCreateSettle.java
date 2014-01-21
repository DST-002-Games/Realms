package net.krglok.realms.model;

import multitallented.redcastlemedia.bukkit.herostronghold.region.Region;
import multitallented.redcastlemedia.bukkit.herostronghold.region.SuperRegion;
import net.krglok.realms.core.Building;
import net.krglok.realms.core.BuildingType;
import net.krglok.realms.core.Owner;
import net.krglok.realms.core.SettleType;
import net.krglok.realms.core.Settlement;

public class McmdCreateSettle implements iModelCommand
{
	private ModelCommandType commandType = ModelCommandType.CREATESETTLEMENT;
	private RealmModel rModel;
	private String playerName;
	private SettleType settleType;
	private String superRegionName;
	
	public McmdCreateSettle(RealmModel rModel, String superRegionName, String playerName, SettleType settleType)
	{
		this.rModel = rModel;
		this.superRegionName = superRegionName;
		this.playerName = playerName;
		this.settleType = settleType;
	}
	
	@Override
	public ModelCommandType command()
	{
		return commandType;
	}

	@Override
	public String[] getParaTypes()
	{
		return new String[] {RealmModel.class.getName(), String.class.getName(), String.class.getName(), SettleType.class.getName()};
	}

	@Override
	public void execute()
	{
		String playerName = "";
		boolean isNPC = false;
		Owner owner = null; 
		if ((playerName == "") )
		{
			playerName = "NPC1";
			isNPC = true;
		} else
		{
			for (Owner fOwner : rModel.getOwners().getOwners().values())
			{
				if (playerName.equalsIgnoreCase(fOwner.getPlayerName()))
				{
					owner = fOwner;
				}
			}
    		if (owner == null)
    		{
    			owner = new Owner(playerName, isNPC);
    		}
		}
		Settlement settlement = new Settlement(playerName, settleType, superRegionName);
		rModel.getSettlements().addSettlement(settlement);
//		System.out.println(superRegionName+" : "+settlement.getId());
		for (Region region : rModel.getServer().getRegionInSuperRegion(superRegionName))
		{
			int hsRegion = region.getID();
			String hsRegionType = region.getType();
    		BuildingType buildingType = rModel.getConfig().regionToBuildingType(hsRegionType);
			Building building = new Building(buildingType, hsRegion, hsRegionType, true);
			Settlement.addBuilding(building, settlement);
		}
		// make not dynamic initialization
		settlement.setSettlerMax();
		settlement.setWorkerNeeded();

		// minimum settler on create
		settlement.getResident().setSettlerCount(settlement.getResident().getSettlerMax()/2);
		settlement.getWarehouse().depositItemValue("WHEAT",settlement.getResident().getSettlerMax()*2 );
		settlement.getWarehouse().depositItemValue("BREAD",settlement.getResident().getSettlerMax()*2 );
		settlement.getWarehouse().depositItemValue("WOOD_HOE",settlement.getResident().getSettlerMax());
		settlement.getWarehouse().depositItemValue("WOOD_AXE",settlement.getResident().getSettlerMax());
		settlement.getWarehouse().depositItemValue("WOOD_PICKAXE",settlement.getResident().getSettlerMax());
		settlement.getWarehouse().depositItemValue("LOG",settlement.getResident().getSettlerMax());
		settlement.getWarehouse().depositItemValue("WOOD",settlement.getResident().getSettlerMax());
		settlement.getWarehouse().depositItemValue("STICK",settlement.getResident().getSettlerMax());
		settlement.getWarehouse().depositItemValue("COBBLESTONE",settlement.getResident().getSettlerMax());
		settlement.setWorkerToBuilding(settlement.getResident().getSettlerCount());
		settlement.getBank().depositKonto((double) (settlement.getResident().getSettlerCount()*10) , "CREATE");

		rModel.getData().writeSettlement(settlement);
	}

	@Override
	public boolean canExecute()
	{
			return true;
	}

}