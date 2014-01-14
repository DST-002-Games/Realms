package net.krglok.realms.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import net.krglok.realms.core.Building;
import net.krglok.realms.core.BuildingType;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Item;
import net.krglok.realms.core.OwnerList;
import net.krglok.realms.core.SettleType;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.data.ConfigTest;
import net.krglok.realms.data.DataTest;
import net.krglok.realms.data.MessageTest;
import net.krglok.realms.data.TestServer;

import org.junit.Test;

public class RealmLoopTest
{

	private Boolean isOutput = true; // set this to false to suppress println
	int days = 0;
	int loopCount = 0;

	private Settlement createSettlement()
	{
		DataTest testData = new DataTest();
		OwnerList ownerList =  testData.getTestOwners();
		
		ConfigTest config = new ConfigTest();
		config.initRegionBuilding();
	
		HashMap<String,String> regionTypes = new HashMap<String,String>(); // testData.defaultRegionList();
		regionTypes.put("1","haupthaus");
		regionTypes.put("2","haus_einfach");
		regionTypes.put("3","haus_einfach");
		regionTypes.put("4","haus_einfach");
		regionTypes.put("5","haus_einfach");
		regionTypes.put("6","haus_einfach");
		regionTypes.put("7","haus_einfach");
		regionTypes.put("8","haus_einfach");
		regionTypes.put("9","haus_einfach");
		regionTypes.put("10","haus_einfach");
		regionTypes.put("11","haus_einfach");
		regionTypes.put("12","haus_einfach");
		regionTypes.put("13","haus_einfach");
		regionTypes.put("14","haus_einfach");
		regionTypes.put("15","haus_einfach");
		regionTypes.put("16","haus_einfach");
		regionTypes.put("17","haus_einfach");
		regionTypes.put("18","haus_einfach");
		regionTypes.put("19","haus_einfach");
		regionTypes.put("20","haus_einfach");
		regionTypes.put("60","taverne");
		regionTypes.put("65","kornfeld");
		regionTypes.put("66","kornfeld");
		regionTypes.put("67","kornfeld");
		regionTypes.put("68","kornfeld");
		regionTypes.put("69","markt");
		HashMap<String,String> regionBuildings = config. makeRegionBuildingTypes(regionTypes);

		SettleType settleType = SettleType.SETTLE_HAMLET;
		String settleName = "New Haven";
		
		Settlement settle = Settlement.createSettlement(settleType, settleName, ownerList.getOwner("NPC0").getPlayerName(),regionTypes, regionBuildings);

		settle.getWarehouse().depositItemValue("WHEAT",settle.getResident().getSettlerMax()*2 );
		settle.getWarehouse().depositItemValue("BREAD",settle.getResident().getSettlerMax()*2 );
		settle.getWarehouse().depositItemValue("WOOD_HOE",settle.getResident().getSettlerMax());
		settle.getWarehouse().depositItemValue("WOOD_AXE",settle.getResident().getSettlerMax());
		settle.getWarehouse().depositItemValue("WOOD_PICKAXE",settle.getResident().getSettlerMax());
		settle.getWarehouse().depositItemValue("LOG",settle.getResident().getSettlerMax());
		settle.getWarehouse().depositItemValue("WOOD",settle.getResident().getSettlerMax());
		settle.getWarehouse().depositItemValue("STICK",settle.getResident().getSettlerMax());
		settle.getWarehouse().depositItemValue("COBBLESTONE",settle.getResident().getSettlerMax());
		
		settle.getResident().setSettlerCount(25);
		settle.setSettlerMax();
		return settle;
	}
	
	private String showBalkenSettler(Settlement settle, boolean isDay)
	{
		int rs = settle.getResident().getSettlerCount();
		rs = (rs / 5) + 1; 
		int as = 0;
		as = rs;
		String sb = "";
		if (isDay)
		{
			for (int j = 0; j < as+1; j++)
			{
				sb = sb + "-";
			}
		} else
		{
			for (int j = 0; j < as+1; j++)
			{
				sb = sb + " ";
			}
		}
		return sb+"#" +"     "+ settle.getResident().getSettlerCount();
	}

	private void showBuildings(Settlement settle)
	{
		System.out.println("== Buildings "+settle.getBuildingList().getBuildingList().size());
		for (Building buildg : settle.getBuildingList().getBuildingList().values())
		{
			System.out.println("- "+buildg.getHsRegion()+" : "+buildg.getHsRegionType()+" :W "+buildg.getWorkerInstalled()+" :E "+buildg.isEnabled());
		}
		
	}

	private void showWarehouse(Settlement settle)
	{
		System.out.println("== Warehouse ["+settle.getWarehouse().getItemCount()+"/"+settle.getWarehouse().getItemMax()+"]");
		for (Item item : settle.getWarehouse().getItemList().values())
		{
			System.out.println("- "+item.ItemRef()+" : "+item.value());
		}
	}
	
	private void showReqList(Settlement settle)
	{
		String reqI = "Requsted Item ["+settle.getRequiredProduction().size()+"] "+"-";
		for (String itemRef : settle.getRequiredProduction().keySet())
		{
			reqI = reqI+itemRef+":"+settle.getRequiredProduction().getItem(itemRef).value()+"-";
		}
		System.out.println(reqI);
	}
	
	private void makeSettleAnalysis(Settlement settle, int moth)
	{
		ArrayList<String> msg = new ArrayList<>();
		// Resident Analyse
		msg.add(" ");
		msg.add("Sieldungstatus  ========================");
		msg.add("Einwohner     : "+settle.getResident().getSettlerCount());
		msg.add("Arbeiter      : "+settle.getTownhall().getWorkerCount());
		msg.add("freie Siedler : "+(settle.getResident().getSettlerCount()-settle.getTownhall().getWorkerCount()));
		msg.add("Betten        : "+settle.getResident().getSettlerMax());
		msg.add("Bankkonto     : "+(int) settle.getBank().getKonto());
		msg.add("Anzahl Gebäude: "+(int) settle.getBuildingList().size());
		msg.add("Items im Lager: "+(int) settle.getWarehouse().getItemCount());
		msg.add("fehlende Items: "+(int) settle.getRequiredProduction().size());

		msg.add("!  ");
		msg.add("Bevölkerungsanalyse  ");
		if (settle.getResident().getSettlerCount() > settle.getResident().getSettlerMax())
		{
			msg.add("!  Sie haben Überbevölkerung in der Siedlung. Dies macht die Siedler unglücklich auf lange Sicht!");
		}
		if (settle.getResident().getHappiness() < 0)
		{
			msg.add("!  Ihre Siedler sind unglücklich. Das wird sie veranlassen zu verschwinden!");
		}
		if (settle.getFoodFactor() < 0.0)
		{
			msg.add("!  Ihre Siedler leiden Hunger. Das ist wohl der Grund warum sie unglücklich sind!");
		}
		if (settle.getSettlerFactor() < 0.0)
		{
			msg.add("!  Ihre Siedler haben keinen Wohnraum. Das ist wohl der Grund warum sie unglücklich sind!");
		}
		if (settle.getEntertainFactor() < 0.9)
		{
			msg.add("!  Ihre Siedler haben wenig Unterhaltung. Etwas mehr Unterhltung macht sie glücklicher!");
		}
		if ((settle.getFoodFactor() < 0.0) && (settle.getResident().getSettlerCount() < 8))
		{
			msg.add("!  Ihre Siedler sind verhungert. Sie haben als Verwalter versagt!");
			msg.add("!  Es würde mich nicht wundern, wenn eine Revolte ausbricht!!");
		}

		msg.add("  ");
		msg.add("Wirtschaftsanalyse  ");
		msg.add("!  Ihre Siedler haben "+(int)(settle.getBank().getKonto())+" Thaler erarbeitet.  Herzlichen Glückwunsch.");
		
		if (settle.getTownhall().getWorkerCount() < settle.getTownhall().getWorkerNeeded())
		{
			msg.add("!  Es fehlen Arbeiter. Deshalb produzieren einige Gebäude nichts!");
		}
		if (settle.getResident().getSettlerCount() < settle.getTownhall().getWorkerNeeded())
		{
			msg.add("!  Es fehlen Siedler. Deshalb produzieren einige Gebäude nichts!");
		}
		if (settle.getResident().getSettlerCount() > settle.getTownhall().getWorkerNeeded())
		{
			msg.add("!  Sie haben "+(settle.getResident().getSettlerCount() -settle.getTownhall().getWorkerNeeded())+" Siedler ohne Arbeit. Sie könnten neue Arbeitsgebäude bauen !");
		}

		if (settle.getRequiredProduction().size() > 0)
		{
			msg.add("!  Es fehlen "+settle.getRequiredProduction().size()+" verschiedene Rohstoffe zur Produktion.");
		}
		
		if ((settle.getWarehouse().getItemMax()-settle.getWarehouse().getItemCount()) < 512)
		{
			msg.add("!  Die Lagerkapazität ist knapp !  Freie Kapazitäte nur "+(settle.getWarehouse().getItemMax()-settle.getWarehouse().getItemCount()));
		}
		
		msg.add("!  ");
		for (String s : msg)
		{
			System.out.println(s);
		}
	}
	
	private void doLoop(int loopMax, RealmModel rModel)
	{
		int dayCount = (int) ConfigBasis.GameDay;
		for (int i = 0; i < loopMax; i++)
		{
			loopCount++;
			rModel.OnTick();
			if ((loopCount % dayCount) == 0)
			{
				rModel.OnProduction();
				rModel.OnTrade();
			} 
		}
	}
	
	@Test
	public void testRealmModelLoop()
	{
		ConfigTest config = new ConfigTest();
		config.initConfigData();
		config.initRegionBuilding();
		config.initSuperSettleTypes();
		int realmCounter = config.getRealmCounter();
		int settlementCounter = config.getSettlementCounter();

		TestServer server = new TestServer();
		DataTest testData = new DataTest();
		MessageTest message = new MessageTest();
		
		RealmModel rModel = new RealmModel(
				realmCounter, 
				settlementCounter,
				server,
				config,
				testData,
				message);
		String command = RealmCommandType.MODEL.name();
		String subCommand = "version";
		RealmCommand realmCommand = new RealmCommand(command, subCommand);
		
		Boolean expected = true; 
		Boolean actual = false; 
		rModel.OnEnable();
		rModel.getSettlements().getSettlement(1).getWarehouse().depositItemValue("LOG", 32);

		int loopMax = (int) ConfigBasis.GameDay * 30;
		doLoop(loopMax, rModel);
		
		actual =  (rModel.getcommandQueue().size() == 0) & (rModel.getProductionQueue().size() == 0);

		if (isOutput)
		{
			days = (int) (loopCount / ConfigBasis.GameDay);
			int month = days / 30;
			System.out.println(" ");
			System.out.println("== Laufzeit "+days+" Tage ");
			for (Settlement settle : rModel.getSettlements().getSettlements().values())
			{
				makeSettleAnalysis(settle, month);
//				showBuildings(settle);
//				showWarehouse(settle);
				showReqList(settle);
			}
		}
		assertEquals(expected, actual);
	}

}
