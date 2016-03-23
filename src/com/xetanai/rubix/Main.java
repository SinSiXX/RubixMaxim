package com.xetanai.rubix;

import java.util.Timer;

import com.xetanai.rubix.Commands.*;

public class Main {

	public static void main(String[] args) {
		// Bot rubix = new Bot("REDACTED","REDACTED");
		Bot rubix = new Bot("REDACTED","REDACTED");
		
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
		
		rubix.registerCommand(new Games());
		
		rubix.registerCommand(new PayRespects());
		
		// rubix.registerCommand(new Xetbooru());
		
		if(rubix.getVersion().contains("Development"))
		{
			rubix.registerCommand(new WhatsNew());
			rubix.registerAlias("whatsnew", "changes");
		}
		
		System.out.println("Registered commands:");
		System.out.println(rubix.getCommandList());
		
		rubix.loadSettings().update();
		
		Timer t = new Timer();
		UpdateGames ugames = new UpdateGames(rubix);
		
		t.scheduleAtFixedRate(ugames, 0L, 5000L); 
	}
}
