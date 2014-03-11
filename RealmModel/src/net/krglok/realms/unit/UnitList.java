package net.krglok.realms.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Die UnitList enth�lt die individuellen Units.
 * Jede Unit wird einzeln verwaltet, weil ansonsten keine visualisierung mit NPC
 * und keine individuelle Verfolgung bei K�mpfen m�glich ist. 
 * Die Units werden �ber die Class identifiziert.
 * @author Windu
 *
 */
public class UnitList extends  ArrayList<IUnit> 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2750568392824062736L;

	public List<IUnit> getTypeUnits(Class<?> unitType)
	{
		ArrayList<IUnit> subList = new ArrayList<IUnit>();
		
		for (IUnit unit : this)
		{
//			if (unit instanceof unitType)
//			{
//				
//			}
		}
		
		return subList;
	}
	
}
