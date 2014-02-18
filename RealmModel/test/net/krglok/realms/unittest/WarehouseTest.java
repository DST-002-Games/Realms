package net.krglok.realms.unittest;

import static org.junit.Assert.*;

import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Item;
import net.krglok.realms.core.ItemList;
import net.krglok.realms.core.Warehouse;
import net.krglok.realms.data.ConfigTest;
import org.junit.Test;

public class WarehouseTest
{

	@Test
	public void testWarehouse()
	{
		Warehouse warehouse = new Warehouse(1000);
		int expected = 1000;
		int actual = warehouse.getItemMax();
		assertEquals(expected, actual);
		
	}

	@Test
	public void testCheckItemMax()
	{
		ConfigTest data = new ConfigTest();
		ItemList iList = data.getToolItems();
		Warehouse warehouse = new Warehouse(1000);
		warehouse.setItemList(iList);

		String lastRef = "";
		int i = 0;
		int value = 0;
		for (String itemRef : iList.keySet())
		{
			i++;
			value = value + i;
			warehouse.depositItemValue(itemRef, i);
			lastRef = itemRef;
		}
		Boolean expected = true;
		Boolean actual = warehouse.depositItemValue(lastRef, i);
		assertEquals(expected, actual);
	}

	@Test
	public void testCheckItemMaxNot()
	{
		ConfigTest data = new ConfigTest();
		ItemList iList = data.getToolItems();
		Warehouse warehouse = new Warehouse(200);
		warehouse.setItemList(iList);

		String lastRef = "";
		int i = 0;
		int value = 0;
		for (String itemRef : iList.keySet())
		{
			i++;
			value = value + i;
			warehouse.depositItemValue(itemRef, i);
			lastRef = itemRef;
		}
		Boolean expected = false;
		Boolean actual = warehouse.depositItemValue(lastRef, i);
		assertEquals(expected, actual);
	}

	@Test
	public void testCheckCapacity()
	{
		ConfigTest data = new ConfigTest();
		ItemList iList = data.getToolItems();
		Warehouse warehouse = new Warehouse(17260);
		warehouse.setItemList(iList);

		String lastRef = "";
		int i = 0;
		int value = 0;
		for (String itemRef : iList.keySet())
		{
			i++;
			value = value + (int)(Math.random()*i);
			warehouse.depositItemValue(itemRef, value);
			lastRef = itemRef;
		}
		warehouse.setStoreCapacity();
		Boolean expected = false;
		Boolean actual = warehouse.depositItemValue(lastRef, i);
		if (expected != actual)
		{
			System.out.println("== CheckCapacity ================ ");
			for (Item item : iList.values())
			{
				System.out.println(ConfigBasis.setStrleft(item.ItemRef(),16)+":"+warehouse.getItemList().getValue(item.ItemRef())+":"+warehouse.getTypeCapacityList().get(item.ItemRef()));
			}
			
		}
		
		assertEquals(expected, actual);
	}
	

}
