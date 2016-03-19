package com.xetanai.rubix;

import net.dv8tion.jda.hooks.ListenerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class MessageListener extends ListenerAdapter{
	private Bot bot;
	
	public MessageListener(Bot robot) {
		bot = robot;
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
		String prefix = bot.getSettings().getPrefix();
		
		try {
			if(isVulgar(event))
			{
				event.getMessage().deleteMessage();
				event.getAuthor().getPrivateChannel().sendMessage("Uh oh. It looks like you were using inappropriate language in a channel/server where it's not allowed, so your message has been removed.\nYour message was:\n"+ event.getMessage().getRawContent());
				return;
			}
		} catch (Exception e) {
			event.getChannel().sendMessage("An error occured.\n```"+ e +"```");
			e.printStackTrace();
		}
		
        if (event.getMessage().getContent().startsWith(prefix))
        {
        	System.out.println("[MessageListener] Command received from "+ event.getAuthor().getUsername() +": "+ event.getMessage().getContent());
        	if (bot.getSettings().deleteCommands() && !event.isPrivate())
        		event.getMessage().deleteMessage();
        }
        else
        	return;
        
        for (Command cmd : bot.getCommandList())
        {
        	if (event.getMessage().getContent().startsWith(prefix+cmd.getKeyword()))
        	{
        		System.out.println("[MessageListener] Found the command. Executing.");
        		try {
					cmd.onCalled(bot,event);
				} catch (Exception e) {
					event.getChannel().sendMessage("An error occured.\n```"+ e +"```");
					e.printStackTrace();
				}
        		return;
        	}
        }
        for (Alias alias : bot.getAliasList())
        {
        	if (event.getMessage().getContent().startsWith(prefix+alias.getKeyword()))
        	{
        		System.out.println("[MessageListener] Command is an alias for "+ alias.getCommand().getKeyword() +". Executing that.");
        		try {
					alias.getCommand().onCalled(bot, event);
				} catch (Exception e) {
					event.getChannel().sendMessage("An error occured.\n```"+ e +"```");
					e.printStackTrace();
				}
        		return;
        	}
        }
        
        System.out.println("[MessageListener] Not a registered command. Ignoring.");
    }
	
	public boolean isVulgar(MessageReceivedEvent message) throws Exception
	{
		if(message.getAuthor().equals(bot.getJDA().getSelfInfo()) || message.isPrivate())
			return false;
		
		List<Role> botroles = message.getGuild().getRolesForUser(bot.getJDA().getSelfInfo());		
		List<String> bannedwords = Files.readAllLines(Paths.get("data/bannedwords.txt"));
		List<String> whitelist = Files.readAllLines(Paths.get("data/nsfw.at"));
		
		boolean canRemove = false;
		for(Role x : botroles)
			if(x.hasPermission(Permission.MESSAGE_MANAGE))
				canRemove = true;
		if(!canRemove)
			return false;
		
		String msg = message.getMessage().getRawContent().toLowerCase();
		
		for(String word : bannedwords)
			if(msg.equals(word) || msg.matches(".*\\b"+ word +"\\b.*"))
			{
				System.out.println("[Chat Filter] Profanity detected from "+ message.getAuthor().getUsername() +" in "+ message.getTextChannel().getName() +"@"+ message.getGuild().getName() +".");
				for(String entry : whitelist)
					if(entry.split("@")[1].equals(message.getGuild().getName()) || entry.split("@")[1].equals("*"))
						if(entry.split("@")[0].equals(message.getTextChannel()) || entry.split("@")[0].equals("*"))
						{
							System.out.println("[Chat Filter] Ignoring profanity in "+ message.getTextChannel().getName() +"@"+ message.getGuild().getName() +" due to whitelist.");
							return false;
						}
				return true;
			}
		return false;
	}
}
