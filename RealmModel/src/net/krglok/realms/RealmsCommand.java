package net.krglok.realms;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import net.krglok.realms.core.BuildingType;
import net.krglok.realms.core.SettleType;


public abstract class RealmsCommand implements iRealmsCommand
{
	Realms plugin;
	CommandSender sender;

	private RealmsCommandType command;
	private RealmsSubCommandType subCommand;
	protected String[] description;
	protected int requiredArgs;
	
	
	public RealmsCommand(RealmsCommandType command, RealmsSubCommandType subCommand)
	{
		this.sender = sender;
		this.plugin = plugin;
		this.command = command; 
		this.subCommand = subCommand;
		this.description = null;
		this.requiredArgs = 0;
	}


	@Override
	public abstract String[] getParaTypes();
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public abstract void execute(Realms plugin, CommandSender sender);
//	{
//		// TODO Auto-generated method stub
		ArrayList<String> msg = new ArrayList<String>();
//		
//	}

	@Override
	public abstract boolean canExecute(Realms plugin, CommandSender sender);
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public String[] getDescription()
	{
		return description;
	}

	@Override
	public void setDescription(String[] newDescription)
	{
		this.description = newDescription;
	}

	
	@Override
	public RealmsCommandType command()
	{
		return this.command;
	}
	
	@Override
	public RealmsSubCommandType subCommand()
	{
		return this.subCommand;
	}


	@Override
	public ArrayList<String> getDescriptionString()
	{
		ArrayList<String> msg = new ArrayList<String>();
		for (int i = 0; i < description.length; i++)
		{
			msg.add(description[i].toString());
		}
		return msg;
	}
	
	@Override
	public int getRequiredArgs()
	{
		return requiredArgs;
	}

}