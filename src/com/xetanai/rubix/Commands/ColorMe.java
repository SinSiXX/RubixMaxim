package com.xetanai.rubix.Commands;

import java.awt.Color;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.RoleManager;
import net.dv8tion.jda.utils.PermissionUtil;

public class ColorMe extends Command {
	public ColorMe()
	{
		super("colorme");
		setUsage("colorme <rgb>");
		setHelp("Change your name color.",false);
		setHelp("Changes your color to the RGB value provided.\n"
				+ "It must be comma separated, and without spaces.",true);
	
		needsPermissionTo(Permission.MANAGE_ROLES);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		if(!guild.allowsColor())
		{
			sendMessage(msg, "Color changing is disabled. Use `"+ guild.getPrefix() +"config AllowColors 1` to enable.");
			return;
		}
		if(!PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), Permission.MANAGE_ROLES, msg.getGuild()))
		{
			sendMessage(msg, "I don't have permission to change your color. I must be allowed to manage roles.");
			return;
		}
		
		
		if(params.length==1)
		{
			sendMessage(msg, "Give me an RGB to change it to.\nhttp://www.rapidtables.com/web/color/RGB_Color.htm");
			return;
		}
		
		int[] rgb = new int[3];
		
		try {
			rgb[0] = Integer.parseInt(params[1].split(",")[0]);
			rgb[1] = Integer.parseInt(params[1].split(",")[1]);
			rgb[2] = Integer.parseInt(params[1].split(",")[2]);
		} catch (Exception e)
		{
			sendMessage(msg, "That's not an RGB value.");
			return;
		}
		
		if(rgb.length!=3)
		{
			sendMessage(msg, "That's not an RGB value.");
		}
		
		Color clr = null;
		try {
			clr = new Color(rgb[0],rgb[1],rgb[2]);
		} catch(Exception e) {
			sendMessage(msg, "That's not an RGB value.");
			return;
		}
		
		List<Role> roles = msg.getGuild().getRolesForUser(msg.getAuthor());
		for(Role x : roles)
		{
			if(x.getName().equals(msg.getAuthor().getId()))
			{
				x.getManager().delete();
				break;
			}
		}
		
		System.out.println("Creating a new role for user "+ msg.getAuthor().getUsername());
		RoleManager newrole = msg.getGuild().createRole().setName(msg.getAuthor().getId());
		newrole.update();
		msg.getGuild().getManager().addRoleToUser(msg.getAuthor(), newrole.getRole()).update();

		
		newrole.setColor(clr).update();
		msg.getTextChannel().sendMessage("Your color was updated.");
	}

}
