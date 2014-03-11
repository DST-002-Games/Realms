package net.krglok.realms.unit;

import net.krglok.realms.core.ItemList;

/**
 * 
 * @author Windu
 *
 */
public interface iUnitData
{

	public abstract int getSpeed();

	public abstract void setSpeed(int speed);

	public abstract int getOffense();

	public abstract void setOffense(int offense);

	public abstract int getDefense();

	public abstract void setDefense(int defense);

	public abstract int getOffenseRange();

	public abstract void setOffenseRange(int offenseRange);

	public abstract int getMaxStorage();

	public abstract void setMaxStorage(int maxStorage);

	public abstract int getArmor();

	public abstract void setArmor(int armor);

	public abstract double getRequiredCost();

	public abstract void setRequiredCost(double requiredCost);

	public abstract double getConsumCost();

	public abstract void setConsumCost(double consumCost);

	public abstract long getConsumTime();

	public abstract void setConsumTime(long consumTime);

	public abstract ItemList getRequiredItems();

	public abstract UnitTypeList getRequiredUnits();

	public abstract ItemList getConsumItems();

}