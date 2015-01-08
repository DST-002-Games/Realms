package net.krglok.realms.npc;

import net.krglok.realms.core.Item;
import net.krglok.realms.core.ItemList;
import net.krglok.realms.core.LocationData;
import net.krglok.realms.unit.UnitType;

public class NPCData
{
	private int Id;
	private NPCType npcType ;
	private UnitType unitType ;
	private String name;
	private GenderType gender;
	private int age;
	private EthnosType ethno;
	
	private int settleId;
	private int homeBuilding;
	private int workBuilding;
	
	private double money;
	private Item itemInHand;
	private BackpackList backPack;
	private boolean isMaried;
	private int npcHusband;
	private int pcHusband;
	private int mother;
	private int father;
	
	private LocationData location;
	
	public NPCData()
	{
		this.Id = 0;
		this.npcType = NPCType.BEGGAR;
		this.unitType = UnitType.SETTLER;
		this.settleId = 0;
	}
	
	public NPCData(int npcId, NPCType npcType, UnitType unitType, int settleId, int buildingId)
	{
		this.Id = npcId;
		this.npcType = npcType;
		this.unitType = unitType;
		this.settleId = settleId;
	}

	public int getId()
	{
		return Id;
	}

	public void setId(int npcId)
	{
		this.Id = npcId;
	}

	public NPCType getNpcType()
	{
		return npcType;
	}

	public void setNpcType(NPCType npcType)
	{
		this.npcType = npcType;
	}

	public UnitType getUnitType()
	{
		return unitType;
	}

	public void setUnitType(UnitType unitType)
	{
		this.unitType = unitType;
	}

	public int getSettleId()
	{
		return settleId;
	}

	public void setSettleId(int settleId)
	{
		this.settleId = settleId;
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the gender
	 */
	public GenderType getGender()
	{
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(GenderType gender)
	{
		this.gender = gender;
	}

	/**
	 * @return the age
	 */
	public int getAge()
	{
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age)
	{
		this.age = age;
	}

	/**
	 * @return the ethno
	 */
	public EthnosType getEthno()
	{
		return ethno;
	}

	/**
	 * @param ethno the ethno to set
	 */
	public void setEthno(EthnosType ethno)
	{
		this.ethno = ethno;
	}

	/**
	 * @return the homeBuilding
	 */
	public int getHomeBuilding()
	{
		return homeBuilding;
	}

	/**
	 * @param homeBuilding the homeBuilding to set
	 */
	public void setHomeBuilding(int homeBuilding)
	{
		this.homeBuilding = homeBuilding;
	}

	/**
	 * @return the workBuilding
	 */
	public int getWorkBuilding()
	{
		return workBuilding;
	}

	/**
	 * @param workBuilding the workBuilding to set
	 */
	public void setWorkBuilding(int workBuilding)
	{
		this.workBuilding = workBuilding;
	}

	/**
	 * @return the money
	 */
	public double getMoney()
	{
		return money;
	}

	/**
	 * @param money the money to set
	 */
	public void setMoney(double money)
	{
		this.money = money;
	}

	/**
	 * @return the itemInHand
	 */
	public Item getItemInHand()
	{
		return itemInHand;
	}

	/**
	 * @param itemInHand the itemInHand to set
	 */
	public void setItemInHand(Item itemInHand)
	{
		this.itemInHand = itemInHand;
	}

	/**
	 * @return the backPack
	 */
	public ItemList getBackPack()
	{
		return backPack;
	}

	/**
	 * @param backPack the backPack to set
	 */
	public void setBackPack(BackpackList backPack)
	{
		this.backPack = backPack;
	}

	/**
	 * @return the isMaried
	 */
	public boolean isMaried()
	{
		return isMaried;
	}

	/**
	 * @param isMaried the isMaried to set
	 */
	public void setMaried(boolean isMaried)
	{
		this.isMaried = isMaried;
	}

	/**
	 * @return the npcHusband
	 */
	public int getNpcHusband()
	{
		return npcHusband;
	}

	/**
	 * @param npcHusband the npcHusband to set
	 */
	public void setNpcHusband(int npcHusband)
	{
		this.npcHusband = npcHusband;
	}

	/**
	 * @return the pcHusband
	 */
	public int getPcHusband()
	{
		return pcHusband;
	}

	/**
	 * @param pcHusband the pcHusband to set
	 */
	public void setPcHusband(int pcHusband)
	{
		this.pcHusband = pcHusband;
	}

	/**
	 * @return the mother
	 */
	public int getMother()
	{
		return mother;
	}

	/**
	 * @param mother the mother to set
	 */
	public void setMother(int mother)
	{
		this.mother = mother;
	}

	/**
	 * @return the father
	 */
	public int getFather()
	{
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(int father)
	{
		this.father = father;
	}

}
