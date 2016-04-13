package com.xetanai.rubix;

import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageAcknowledgedEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.utils.InviteUtil;
import net.dv8tion.jda.utils.InviteUtil.Invite;

public class MessageListener extends ListenerAdapter{
	
	public MessageListener() {
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		System.out.println("[MessageListener] "+ event.getUser().getUsername() +" joined "+ event.getGuild().getName() +".");
		Server serv = SQLUtils.loadServer(event.getGuild().getId());
		
		if(serv.doGreet())
		{
			String welcomeMsg = serv.getWelcome();
			
			welcomeMsg = welcomeMsg.replace("{USER}", event.getUser().getUsername());
			
			event.getGuild().getPublicChannel().sendMessage(welcomeMsg);
		}
	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event)
	{
		if(event.getUser().getId().equals(Bot.jda.getSelfInfo().getId()))
			return;
		System.out.println("[MessageListener] "+ event.getUser().getUsername() +" left "+ event.getGuild().getName() +".");
		Server serv = SQLUtils.loadServer(event.getGuild().getId());
		
		if(serv.doGreet())
		{
			String byeMsg = serv.getFarewell();
			
			byeMsg = byeMsg.replace("{USER}", event.getUser().getUsername());
			
			event.getGuild().getPublicChannel().sendMessage(byeMsg);
		}
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event)
	{
		String post = "Hello, everyone, I'm Rubix, your new bot!\n";
		post += Bot.jda.getUserById(event.getGuild().getOwnerId()).getAsMention() +", thanks for having me! You've automatically been given operator permissions. Be sure to give it to your admins as well.\n";
		post += "Type !help to see what I can do! If you ever decide you don't want me anymore, just use !leave.\n";
		post += "__***It is highly recommended you change my prefix if used in conjunction with other bots!***__\nDo this with `!config Prefix <yournewprefixhere>`.";
		event.getGuild().getPublicChannel().sendMessage(post); // Thank the owner for having him. Rubix has manners.
	
		SQLUtils.createServerEntry(new Server(event.getGuild().getId()));
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getAuthor().getId().equals(Bot.jda.getSelfInfo().getId()))
			return; // Rubix shall ignore himself.
		if(event.isPrivate())
		{
			if(isCommand(event,null))
			{
				String[] params = event.getMessage().getContent().split(" ");
				Command target = getCommand(params[0], null);
				
				if(target!=null)
				{
					if(target.allowPM())
					{
						try {
							target.onCalled(event, params, null);
							return;
						} catch(Exception e) {
							e.printStackTrace();
							return;
						}
					}
					else
					{
						event.getPrivateChannel().sendMessage("That command can't be used in PM.");
						return;
					}
				}
			}
		}
		
		Person sender = SQLUtils.loadUser(event.getAuthor().getId());
		Server guild = SQLUtils.loadServer(event.getGuild().getId());
		
		/* Anti-vulgarity */
		if(isVulgar(event, guild))
		{
			event.getMessage().deleteMessage();
			event.getAuthor().getPrivateChannel().sendMessage("Uh oh. It looks like you were using inappropriate language in a channel/server where it's not allowed, so your message has been removed.\nYour message was:\n"+ event.getMessage().getRawContent());
			
			return; // Ignore any commands that they might've been sending.
		}
		
		if(event.getMessage().getMentionedUsers().contains(Bot.jda.getSelfInfo()))
		{
			parseMention(event,guild);
		}
		
		if(isCommand(event, guild)) /* Commands */
		{
			String[] params = event.getMessage().getContent().split(" ");
			Command target = getCommand(params[0], guild);
			
			if(target != null)
			{
				if(sender.canUse(target, guild))
				{
					try {
						target.onCalled(event, params, guild);
						return;
					} catch (Exception e) {
						event.getTextChannel().sendMessage("An error occurred.\n```"+ e.toString() +"```");
						e.printStackTrace();
						return;
					}
				}
				else
				{
					event.getTextChannel().sendMessage("You're not allowed to use that command.");
					return;
				}
			}
			else
			{
				System.out.println("Doesn't exist. Ignoring.");
				if(guild.doCNFMessage())
				{
					event.getTextChannel().sendMessage("That command doesn't exist. Use `"+ guild.getPrefix() +"help` for a command list.");
				}
				return;
			}
		}
		else
		{
			if(sender.isAfk())
			{
				event.getTextChannel().sendMessage("Welcome back, "+ event.getAuthor().getAsMention() +". I've automatically taken you off AFK.");
				SQLUtils.changeUser(sender.getId(),"Afk","0");
				return;
			}
			for(User mention : event.getMessage().getMentionedUsers())
			{
				Person mentioned = SQLUtils.loadUser(mention.getId());
				
				if(mentioned.isAfk())
				{
					event.getTextChannel().sendMessage(mention.getUsername() +" is AFK, and probably won't see your message, so I'll PM them your message.");
					mention.getPrivateChannel().sendMessage(event.getAuthor().getUsername() +" mentioned you in a message while you were AFK. They said ```"+ event.getMessage().getContent() +"```");
				}
			}
		}
	}
	
	public Command getCommand(String call, Server guild)
	{
		String prefix = "!";
		if(guild!=null)
			prefix = guild.getPrefix();
		
		for(Command cmd : Bot.commandList)
			if(call.equals(prefix + cmd.getKeyword()))
				return cmd;
		
		for(Alias cmd : Bot.aliasList)
			if(call.equals(prefix + cmd.getKeyword()))
				return cmd.getCommand();
		return null;
	}
	
	public boolean isCommand(MessageReceivedEvent message, Server guild)
	{
		String prefix = "!";
		if(guild!=null)
			prefix = guild.getPrefix();
		
		if(message.getMessage().getContent().startsWith(prefix))
		{
			System.out.println("[MessageListener] Command received from "+ message.getAuthor().getUsername() +": "+ message.getMessage().getContent() +".");
			return true;
		}
		return false;
	}
	
	public boolean isVulgar(MessageReceivedEvent message, Server guild)
	{
		
		boolean canRemove = false;
		List<Role> botroles = message.getGuild().getRolesForUser(Bot.jda.getSelfInfo());
		for(Role x : botroles)
			if(x.hasPermission(Permission.MESSAGE_MANAGE))
				canRemove = true;
		if(!canRemove)
			return false;
		
		if(message.getAuthor().equals(Bot.jda.getSelfInfo()) || message.isPrivate())
			return false;
		
		if(Bot.userIsOp(message.getAuthor().getId(), message.getGuild().getId()))
			return false;
	
		String msg = message.getMessage().getRawContent().toLowerCase();
		
		if(guild.isModded())
		{
			Server globalList = SQLUtils.loadServer("-1");
			
			for(String word : globalList.getBannedWords())
				if(msg.equals(word) || msg.matches(".*\\b"+ word +"\\b.*"))
				{
					System.out.println("[Chat Filter] Profanity detected from "+ message.getAuthor().getUsername() +" in "+ message.getTextChannel().getName() +"@"+ message.getGuild().getName() +".");
					return true;
				}
		}
		
		for(String word : guild.getBannedWords()) // Custom ban word list
			if(msg.equals(word) || msg.matches(".*\\b"+ word +"\\b.*"))
			{
				System.out.println("[Chat Filter] Custom banned word found from "+ message.getAuthor().getUsername() +" in "+ message.getTextChannel().getName() +"@"+ message.getGuild().getName() +".");
				return true;
			}
		return false;
	}
	
	private void parseMention(MessageReceivedEvent event, Server guild)
	{
		String message = event.getMessage().getRawContent().toLowerCase().trim();
		System.out.println("[MessageListener] @mention from "+ event.getAuthor() +": "+ message);
		
		if(message.contains("prefix"))
		{
			event.getChannel().sendMessage(event.getAuthor().getUsername() +", my prefix for this server is `"+ guild.getPrefix() +"`. Use `"+ guild.getPrefix() +"help` for a command list.");
			return;
		}
		if(message.equals(Bot.jda.getSelfInfo().getAsMention()))
		{
			event.getChannel().sendMessage("Yes?");
			return;
		}
	}
}
