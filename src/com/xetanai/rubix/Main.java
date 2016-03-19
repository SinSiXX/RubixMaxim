package com.xetanai.rubix;

import com.xetanai.rubix.Commands.*;

public class Main {

	public static void main(String[] args) {
		Bot rubix = new Bot("isaakrogers1@gmail.com","Xeta1230");
		
		rubix.registerCommand(new Help());
		rubix.registerAlias("help", "?");
		
		rubix.registerCommand(new GetAliases());
		
		rubix.registerCommand(new About());
		rubix.registerAlias("about","info");
		
		rubix.registerCommand(new Roll());
		
		rubix.registerCommand(new Whatgame());
		
		rubix.registerCommand(new Say());
		
		rubix.registerCommand(new Osu());
		
		rubix.registerCommand(new Redact());
		
		rubix.registerCommand(new Eval());
		
		System.out.println("Registered commands:");
		System.out.println(rubix.getCommandList());
		
		rubix.loadSettings().update();
	}
}
