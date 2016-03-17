package com.xetanai.rubix;

import net.dv8tion.jda.hooks.ListenerAdapter;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class MessageListener extends ListenerAdapter {
	private String prefix;
	private Bot bot;
	
	public MessageListener(Bot robot, String pre) {
		prefix = pre;
		bot = robot;
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
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
        		cmd.onCalled(bot,event);
        		return;
        	}
        }
        for (Alias alias : bot.getAliasList())
        {
        	if (event.getMessage().getContent().startsWith(prefix+alias.getKeyword()))
        	{
        		System.out.println("[MessageListener] Command is an alias for "+ alias.getCommand().getKeyword() +". Executing that.");
        		alias.getCommand().onCalled(bot, event);
        		return;
        	}
        }
        
        System.out.println("[MessageListener] Not a registered command. Ignoring.");
    }
}
