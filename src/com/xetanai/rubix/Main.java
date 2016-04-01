package com.xetanai.rubix;

import com.xetanai.rubix.Commands.*;

import net.dv8tion.jda.entities.Guild;

public class Main {

	public static void main(String[] args) {
		Bot rubix = new Bot("EMAIL","PASSWORD");
		
		rubix.registerCommand(new Help());
		rubix.registerAlias("help", "?");
		
		rubix.registerCommand(new GetAliases());
		
		rubix.registerCommand(new About());
		rubix.registerAlias("about","info");
		
		rubix.registerCommand(new Roll());
		
		rubix.registerCommand(new Whatgame());
		
		rubix.registerCommand(new Say());
		rubix.registerAlias("say", "echo");
		
		rubix.registerCommand(new Osu());
		
		rubix.registerCommand(new Redact());
		rubix.registerAlias("redact", "undo");
		
		rubix.registerCommand(new Eval());
		
		rubix.registerCommand(new Op());
		rubix.registerAlias("op", "deop");
		rubix.registerAlias("op", "toggleop");
		
		rubix.registerCommand(new Afk());
		
		rubix.registerCommand(new Id());
		
		//rubix.registerCommand(new Games());
		
		rubix.registerCommand(new PayRespects());
		
		rubix.registerCommand(new Xetbooru());
		
		rubix.registerCommand(new Leave());
		
		rubix.registerCommand(new Config());
		
		if(rubix.getVersion().contains("Development"))
		{
			rubix.registerCommand(new WhatsNew());
			rubix.registerAlias("whatsnew", "changes");
		}
		
		// Ensure all servers exist in DB
		for(Guild x : rubix.getJDA().getGuilds())
		{
			Server srv = rubix.loadServer(x.getId());
			
			if(srv==null)
			{
				rubix.createServerEntry(new Server(x.getId()));
			}
		}
		
		System.out.println("Registered commands:");
		System.out.println(rubix.getCommandList());
		
		//Timer t = new Timer();
		//UpdateGames ugames = new UpdateGames(rubix);
		
		//t.scheduleAtFixedRate(ugames, 0L, 5000L); 
	}
}
