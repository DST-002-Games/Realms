package net.krglok.realms.command;

import java.util.ArrayList;

import net.krglok.realms.Realms;
import net.krglok.realms.TickTask;
import net.krglok.realms.Common.aRealmsCommand;
import net.krglok.realms.Common.RealmsCommandType;
import net.krglok.realms.Common.RealmsSubCommandType;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.model.ModelStatus;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdRealmsActivate extends aRealmsCommand
{

	public CmdRealmsActivate( )
	{
		super(RealmsCommandType.REALMS, RealmsSubCommandType.ACTIVATE);
		description = new String[] {
				ChatColor.YELLOW+"/realms ACTIVATE ",
		    	"Set the RealmsModel to Enable and make initialization.  ",
		    	"Only when enabled, the Settlement produce  ",
		    	"Only when enabled commands are accepted  ",
		    	"TickTask start running  ",
		    	"  "
			};
			requiredArgs = 0;
	}

	@Override
	public void setPara(int index, String value)
	{
		
	}

	@Override
	public void setPara(int index, int value)
	{
		
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
		return null;
	}

	@Override
	public void execute(Realms plugin, CommandSender sender)
	{
		ArrayList<String> msg = new ArrayList<String>();
		plugin.getRealmModel().OnEnable();
		if (plugin.getRealmModel().getModelStatus() == ModelStatus.MODEL_ENABLED)
		{
			msg.add("[Realm Model] Enabled");
			msg.add(plugin.getRealmModel().getModelName()+" Vers.: "+plugin.getRealmModel().getModelVersion());
			plugin.getTickTask();
			msg.add("Timer Task "+TickTask.getProdCounter());
			for (Settlement settle : plugin.getRealmModel().getSettlements().values())
			{
				msg.add(settle.getId()+":"+settle.getName()+"  in "+settle.getPosition().getWorld());
			}
		} else
		{
			msg.add("[Realm Model] NOT Enabled");
			msg.add("Something unknown is wrong :(");
			msg.add("Are the server overloaded ?  ");
			plugin.getLog().info("[Realm Model] NOT Enabled. Something unknown is wrong :( ");
		}
		plugin.getMessageData().printPage(sender, msg, 1);
	}

	@Override
	public boolean canExecute(Realms plugin, CommandSender sender)
	{
		if (sender.isOp() == false)
		{
			errorMsg.add("Only for Ops and Admins !  ");
			return false;
		}
		if (plugin.getRealmModel().getModelStatus() == ModelStatus.MODEL_DISABLED)
		{
			return true;
		}
		errorMsg.add("The Model is always Enabled !  ");
		return false;
	}

}
