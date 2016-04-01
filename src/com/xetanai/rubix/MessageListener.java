package com.xetanai.rubix;

import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.utils.InviteUtil;
import net.dv8tion.jda.utils.InviteUtil.Invite;

public class MessageListener extends ListenerAdapter{
	private Bot bot;
	
	public MessageListener(Bot robot) {
		bot = robot;
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		System.out.println("[MessageListener] A new user has joined the guild "+ event.getGuild().getName() +": "+ event.getUser().getUsername());
		Server serv = bot.loadServer(event.getGuild().getId());
		
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
		System.out.println("[MessageListener] "+ event.getUser().getUsername() +" left "+ event.getGuild().getName() +".");
		Server serv = bot.loadServer(event.getGuild().getId());
		
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
		post += bot.getJDA().getUserById(event.getGuild().getOwnerId()).getAsMention() +", thanks for having me! You've automatically been given operator permissions. Be sure to give it to your admins as well.\n";
		post += "Type !help to see what I can do! If you ever decide you don't want me anymore, just use !leave.";
		event.getGuild().getPublicChannel().sendMessage(post); // Thank the owner for having him. Rubix has manners.
	
		bot.createServerEntry(new Server(event.getGuild().getId()));
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getAuthor().getId().equals(bot.getJDA().getSelfInfo().getId()))
			return; // Rubix shall ignore himself.
		if(event.isPrivate())
		{
			Invite inv = InviteUtil.resolve(event.getMessage().getContent());
			if(inv==null)
			{
				event.getTextChannel().sendMessage("I can't function in PM, sorry! Try adding me to your guild by sending me an invite.");
			}
			else
			{
				InviteUtil.join(inv, bot.getJDA(),joinedGuild ->
				 {
				     System.out.println("[InvitationPM] Accepted invite to: "+ joinedGuild.getName() +".");
				 });
			}
			return; // Rubix shall ignore PMs, unless it's an invite
		}
		
		Person sender = bot.loadUser(event.getAuthor().getId());
		Server guild = bot.loadServer(event.getGuild().getId());
		
		/* Anti-vulgarity */
		if(isVulgar(event, guild))
		{
			event.getMessage().deleteMessage();
			event.getAuthor().getPrivateChannel().sendMessage("Uh oh. It looks like you were using inappropriate language in a channel/server where it's not allowed, so your message has been removed.\nYour message was:\n"+ event.getMessage().getRawContent());
			
			return; // Ignore any commands that they might've been sending.
		}
		
		if(isCommand(event, guild)) /* Commands */
		{
			String[] params = event.getMessage().getContent().split(" ");
			Command target = getCommand(params[0], guild);
			
			if(target != null)
			{
				if(sender.can(bot, target, guild))
				{
					try {
						target.onCalled(bot, event, params, guild);
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
				event.getTextChannel().sendMessage("That command doesn't exist. Use "+ guild.getPrefix() +"help for a command list.");
				return;
			}
		}
		else
		{
			if(sender.isAfk())
			{
				event.getTextChannel().sendMessage("Welcome back, "+ event.getAuthor().getAsMention() +". I've automatically taken you off AFK.");
				bot.saveUser(sender.setAfk(false));
				return;
			}
			for(User mention : event.getMessage().getMentionedUsers())
			{
				Person mentioned = bot.loadUser(mention.getId());
				
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
		for(Command cmd : bot.getCommandList())
			if(call.equals(guild.getPrefix() + cmd.getKeyword()))
				return cmd;
		
		for(Alias cmd : bot.getAliasList())
			if(call.equals(guild.getPrefix() + cmd.getKeyword()))
				return cmd.getCommand();
		return null;
	}
	
	public boolean isCommand(MessageReceivedEvent message, Server guild)
	{
		String prefix = "!";
		if(guild.getPrefix() != null)
		{
			prefix = guild.getPrefix();
		}
		
		if(message.getMessage().getContent().startsWith(prefix))
		{
			System.out.println("[MessageListener] Command received from "+ message.getAuthor().getUsername() +".");
			return true;
		}
		return false;
	}
	
	public boolean isVulgar(MessageReceivedEvent message, Server guild)
	{
		
		boolean canRemove = false;
		List<Role> botroles = message.getGuild().getRolesForUser(bot.getJDA().getSelfInfo());
		for(Role x : botroles)
			if(x.hasPermission(Permission.MESSAGE_MANAGE))
				canRemove = true;
		if(!canRemove)
			return false;
		
		if(message.getAuthor().equals(bot.getJDA().getSelfInfo()) || message.isPrivate())
			return false;
	
		String msg = message.getMessage().getRawContent().toLowerCase();
		
		if(!guild.isVulgar())
		{
			Server globalList = bot.loadServer("-1");
			
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
}
