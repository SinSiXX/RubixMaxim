package com.xetanai.rubix;

import com.xetanai.rubix.Commands.*;

public class Main {

	public static void main(String[] args) {
		Bot rubix = new Bot("isaakrogers1@gmail.com","Xeta1230");
		
		rubix.registerCommand(new Help());
		rubix.registerAlias("help", "?");
		
		rubix.registerCommand(new GetAliases());
		
		rubix.registerCommand(new About());
		
		System.out.println(rubix.getCommandList());
		
		rubix.loadSettings();		
	}
}
