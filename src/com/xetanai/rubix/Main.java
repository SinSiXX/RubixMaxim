package com.xetanai.rubix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.xetanai.rubix.Commands.*;

import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;

public class Main {

	public static void main(String[] args) {
		File file = new File("Data/token.txt");
		BufferedReader reader = null;
		String tkn = "";
		
		try {
		    reader = new BufferedReader(new FileReader(file));
		    tkn = reader.readLine();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		JDABuilder jdab = new JDABuilder(tkn);
		jdab.addListener(new MessageListener());
		
		
		try { /* Try to start Rubix/Maxim, and print any errors */
			Bot.jda = jdab.buildBlocking();
			Class.forName("com.mysql.jdbc.Driver");
			
			SQLUtils.sqlcon = DriverManager.getConnection("jdbc:mysql://localhost:3306/rubixv2", "rubix", "Xeta1230");
		}
		catch (IllegalArgumentException e)
        {
            System.out.println("[Main] (ERROR) The config was not populated. Please enter an email and password.");
        }
        catch (LoginException e)
        {
            System.out.println("[Main] (ERROR) The provided email / password combination was incorrect. Please provide valid details.");
        }
		catch (SQLException e) {
			e.printStackTrace();
		}
        catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Bot.registerCommand(new Help());
		Bot.registerAlias("help", "?");
		Bot.registerCommand(new GetAliases());
		Bot.registerCommand(new About());
		Bot.registerAlias("about","info");
		Bot.registerCommand(new Roll());
		Bot.registerCommand(new Whatgame());
		Bot.registerCommand(new Say());
		Bot.registerAlias("say", "echo");
		Bot.registerCommand(new Osu());
		Bot.registerCommand(new Redact());
		Bot.registerAlias("redact", "undo");
		Bot.registerCommand(new Eval());
		Bot.registerCommand(new Op());
		Bot.registerAlias("op", "deop");
		Bot.registerAlias("op", "toggleop");
		Bot.registerCommand(new Afk());
		Bot.registerCommand(new Id());
		//rubix.registerCommand(new Games());
		Bot.registerCommand(new Xetbooru());
		Bot.registerCommand(new Leave());
		Bot.registerCommand(new Config());
		Bot.registerCommand(new Banword());
		Bot.registerCommand(new Listops());
		Bot.registerCommand(new Whois());
		Bot.registerCommand(new Vote());
		Bot.registerCommand(new ColorMe());
		
		// Ensure all servers exist in DB
		for(Guild x : Bot.jda.getGuilds())
		{
			Server srv = SQLUtils.loadServer(x.getId());
			
			if(srv==null)
			{
				SQLUtils.createServerEntry(new Server(x.getId()));
			}
		}
		
		System.out.println("Registered commands:");
		System.out.println(Bot.commandList);
		
		/* String channel = "146787312464822273";
		
		MessageHistory test = new MessageHistory(Bot.jda, Bot.jda.getTextChannelById(channel));
		List<Message> test2 = test.retrieveAll();
		
		String earliest = test2.get(test2.size()-1).getContent();
		System.out.println(test2.size());
		System.out.println(earliest); // Debug code to print the earliest message that can be fetched. */
		
		//Timer t = new Timer();
		//UpdateGames ugames = new UpdateGames(rubix);
		
		//t.scheduleAtFixedRate(ugames, 0L, 5000L); 
	}
}
