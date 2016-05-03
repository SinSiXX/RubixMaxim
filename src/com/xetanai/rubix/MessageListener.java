package com.xetanai.rubix;

import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.PermissionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.xetanai.rubix.Commands.Command;
import com.xetanai.rubix.enitites.Chan;
import com.xetanai.rubix.enitites.Person;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.MiscUtils;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberLeaveEvent;

public class MessageListener extends ListenerAdapter{
	
	public MessageListener() {
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		System.out.println("[MessageListener] "+ event.getUser().getUsername() +" joined "+ event.getGuild().getName() +".");
		Server serv = SQLUtils.loadServer(event.getGuild().getId());
		int[] fame = SQLUtils.getFame(event.getUser().getId());
		
		if(serv.doGreet())
		{
			String welcomeMsg = serv.getWelcome();
			
			welcomeMsg = welcomeMsg.replace("{USER}", event.getUser().getUsername());
			
			event.getGuild().getPublicChannel().sendMessage(welcomeMsg);
		}
		
		double fameD = (double) fame[0]/(fame[0]+fame[1]);
		if(fame[1]!=0 && fameD<0.7)
			event.getGuild().getOwner().getPrivateChannel().sendMessage("A user with a poor reputation just joined your guild.\n"+
					event.getUser().getUsername() +" has a fame of "+ (int) Math.floor(fameD*100) +"%\n"+
					fame[0] +" have given them positive votes. "+ fame[1] +" have given them negative.");
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
	public void onGuildUnavailable(GuildUnavailableEvent event)
	{
		System.out.println("[MessageListener] A guild was just made unavailable: "+ event.getGuild().getName());
	}
	
	@Override
	public void onGuildAvailable(GuildAvailableEvent event)
	{
		System.out.println("[MessageListener] A guild was just made available: "+ event.getGuild().getName());
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event)
	{
		String post = "Hello, everyone, I'm Rubix, your new bot!\n";
		post += Bot.jda.getUserById(event.getGuild().getOwnerId()).getAsMention() +", thanks for having me! You've automatically been given operator permissions. Be sure to give it to your admins as well.\n";
		post += "Type !help to see what I can do! If you ever decide you don't want me anymore, just use !leave.\n";
		post += "__***It is highly recommended you change my prefix if used in conjunction with other bots!***__\nDo this with `!config Prefix <yournewprefixhere>`.";
		event.getGuild().getPublicChannel().sendMessage(post); // Thank the owner for having him. Rubix has manners.
	
		Bot.adminAlert("I was added to a new guild:```\n"+ event.getGuild().getName() +"\n"+ event.getGuild().getUsers().size() +" users.```");
		SQLUtils.loadServer(event.getGuild().getId());
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event)
	{
		Bot.adminAlert("I was removed from a guild:```\n"+ event.getGuild().getName() +"```");
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		MiscUtils.updateLastHeard(event.getAuthor().getId());
		
		if(event.getAuthor().isBot())
			return; // Rubix shall ignore bots.
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
							Bot.adminAlert(Bot.createErrorMessage(e, event));
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
			return;
		}
		
		Person sender = SQLUtils.loadUser(event.getAuthor().getId());
		Server guild = SQLUtils.loadServer(event.getGuild().getId());
		Chan channel = SQLUtils.loadChannel(event.getTextChannel().getId());
		
		/* Channel ignores */
		if(channel.isIgnored())
			if(!Bot.userIsOp(sender.getId(), guild.getId()))
				return;
		
		/* Mute */
		for(Role role : event.getGuild().getRolesForUser(event.getAuthor()))
		{
			if(role.getName().equals("Muted"))
			{
				event.getMessage().deleteMessage();
				return;
			}
		}
		
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
				if(sender.canUse(target, guild, channel))
				{
					try {
						target.onCalled(event, params, guild);
						return;
					} catch (Exception e) {
						event.getTextChannel().sendMessage("Something went wrong. The developers have been notified automatically.");
						Bot.adminAlert(Bot.createErrorMessage(e, event));
						return;
					}
				}
				else
				{
					event.getTextChannel().sendMessage("You're not allowed to use that command. Try another channel and check your permissions.");
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
			if(call.equalsIgnoreCase(prefix + cmd.getKeyword()))
				return cmd;
		
		for(Alias cmd : Bot.aliasList)
			if(call.equalsIgnoreCase(prefix + cmd.getKeyword()))
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
		if(!PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), Permission.MESSAGE_MANAGE, message.getGuild()))
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
		List<String> greetings = Arrays.asList("Hi!","Hello!","Hey!","Heya!","o/");
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
		if(message.contains("deez"))
		{
			event.getChannel().sendMessage("Ha! Goteem!");
			return;
		}
		
		for(String greet : greetings)
		{
			String matchable = greet.toLowerCase().replace("!","");
			if(message.contains(matchable))
			{
				Random rng = new Random();
				event.getChannel().sendMessage(greetings.get(rng.nextInt(greetings.size())));
				return;
			}
		}
	}
}
