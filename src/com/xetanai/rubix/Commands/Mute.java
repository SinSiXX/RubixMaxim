package com.xetanai.rubix.Commands;

import java.awt.Color;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.RoleManager;
import net.dv8tion.jda.utils.PermissionUtil;

public class Mute extends Command
{
    public Mute()
    {
        super("mute");
        setUsage("mute <Users>");
        setHelp("Mutes or unmutes users.",false);
        setHelp("Toggles mute on all mentioned users.\n"
        		+ "If no users are mentioned, Rubix will search for one user by username.",true);
        this.setElevation(true);
        needsPermissionTo(Permission.MESSAGE_MANAGE);
        needsPermissionTo(Permission.MANAGE_ROLES);
    }
 
    @Override
    public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
    {
    	if(!PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), Permission.MANAGE_ROLES, msg.getGuild()))
        {
            sendMessage(msg, "I'm not allowed to manage roles, and so I cannot mute users.\nI need Manage Messages and Manage Roles permissions for muting to work.");
            return;
        }
    	if(!PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), Permission.MESSAGE_MANAGE, msg.getGuild()))
        {
            sendMessage(msg, "I'm not allowed to manage messages, and so I cannot mute users.\nI need Manage Messages and Manage Roles permissions for muting to work.");
            return;
        }
    	
    	if(params.length==1)
    	{
    		sendMessage(msg, "Please supply at least one user to mute/unmute. **This toggles mute status**.");
    		return;
    	}
 
        Role mutedRole = null;
        for(Role role : msg.getGuild().getRoles())
        {
            if(role.getName().equals("Muted"))
            {
                mutedRole = role;
                break;
            }
        }
       
        if(mutedRole==null)
        {
            RoleManager rmg = msg.getGuild().createRole()
                .setColor(new Color(0,0,0))
                .setName("Muted");
            rmg.update();
            mutedRole = rmg.getRole();
        }
 
        List<User> usrs = searchUsers(msg,params);
 
        if(usrs.size()==0)
        {
            sendMessage(msg, "I couldn't find anyone by that name. Try mentioning them instead.");
            return;
        }
       
        String post = "";
        for(User target : usrs)
        {
        	if(target.equals(msg.getAuthor()))
        	{
        		post += "***You can't mute yourself***.\n";
        	}
        	else
        	{
	        	boolean isMuted = false;
	            for(Role role : msg.getGuild().getRolesForUser(target))
	            {
	                if(role.getName().equals("Muted")) // Already muted.
	                   isMuted = true;
	            }
	            
	            if(isMuted) // Unmute them.
	            {
	            	msg.getGuild().getManager().removeRoleFromUser(target, mutedRole).update();
	            	post += target.getAsMention() +" is no longer muted.\n";
	            }
	            else // Mute them.
	            {
	            	msg.getGuild().getManager().addRoleToUser(target, mutedRole).update();
	            	post += target.getAsMention() +" is now muted.\n";
	            }
        	}
        }
        
        sendMessage(msg, post);
    }
}