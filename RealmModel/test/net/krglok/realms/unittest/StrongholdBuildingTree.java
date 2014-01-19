package net.krglok.realms.unittest;


import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import multitallented.redcastlemedia.bukkit.herostronghold.region.RegionType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;


public class StrongholdBuildingTree
{

	@Test
	public void getStrongholdConstructionMaterial()
	{
		String path = "\\GIT\\OwnPlugins\\Realms\\plugins\\HeroStronghold";
        File regionFolder = new File(path, "RegionConfig");
        if (!regionFolder.exists()) 
        {
        	System.out.println("Folder not found !");
            return;
        }
        
        RegionType[] Region = new RegionType[100];
        System.out.println("[Stronghold] Building cost" );
        for (File RegionFile : regionFolder.listFiles()) 
        {
        	
//        	sRegionFile = RegionFile.getName();
//            if ( isInList(sRegionFile,sList))
//            {
//	        	region= getRegionConfig(path+"\\RegionConfig", sRegionFile);
//	            System.out.println(sRegionFile.replace(".yml", "")+"  Cost : "+region.getMoneyRequirement());
//
//	            for (ItemStack item : region.getRequirements())
//	            {
//	            	required.put(item.getType().name(), 0);
//	            }
//	            for (ItemStack item : region.getReagents())
//	            {
//	            	reagent.put(item.getType().name(), 0);
//	            }
//	            for (String item : region.getSuperRegions())
//	            {
//	            	superRef.put(item, 0);
//	            }
//	            for (ItemStack item : region.getUpkeep())
//	            {
//	            	ingredient.put(item.getType().name(), 0);
//	            }
//	            for (ItemStack item : region.getOutput())
//	            {
//	            	product.put(item.getType().name(), 0);
//	            }
//            }
        }
	}
}