package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.security.auth.login.LoginException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;

public class Bot {
	private JDA jda;
	private List<Command> commandList = new ArrayList<Command>();
	private List<Alias> aliasList = new ArrayList<Alias>();
	private List<Person> visibleUsers = new ArrayList<Person>();
	private Settings settings = new Settings();
	private String version = "Development";
	private Message lastMessage;
	
	public Bot(String login, String password) {
		JDABuilder jdab = new JDABuilder().setEmail(login).setPassword(password);
		jdab.addListener(new MessageListener(this));
		
		try { /* Try to start Rubix/Maxim, and print any errors */
			jda = jdab.buildBlocking();
		}
		catch (IllegalArgumentException e)
        {
            System.out.println("[Main] (ERROR) The config was not populated. Please enter an email and password.");
        }
        catch (LoginException e)
        {
            System.out.println("[Main] (ERROR) The provided email / password combination was incorrect. Please provide valid details.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
		
		for (User user : jda.getUsers())
		{
			visibleUsers.add(new Person(user));
		}
	}
	
	public JDA getJDA()
	{
		return jda;
	}
	
	public String getVersion()
	{
		return version;
	}
	
	public Bot setLastMessage(Message newVal)
	{
		lastMessage = newVal;
		return this;
	}
	
	public Message getLastMessage()
	{
		return lastMessage;
	}
	
	public List<Person> getUsers()
	{
		return visibleUsers;
	}
	
	public List<Command> getCommandList()
	{
		return commandList;
	}
	
	public void registerCommand(Command cmd)
	{
		commandList.add(cmd);
	}
	
	public List<Alias> getAliasList()
	{
		return aliasList;
	}
	
	public void registerAlias(String cmd, String alias)
	{
		Command old = null;
		for (Command x : commandList)
		{
			if (x.getKeyword().equals(cmd))
				old=x;
		}
		
		aliasList.add(new Alias(old,alias));
	}
	
	public Settings getSettings()
	{
		return settings;
	}
	
	public void saveSettings()
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		BufferedWriter out;
		try
		{
			FileWriter filestream = new FileWriter("data/settings.json",false);
			out = new BufferedWriter(filestream);
			
			out.write(gson.toJson(settings));
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Bot loadSettings()
	{
		Gson gson = new Gson();
		
		String json;
		try {
			byte[] encoded = Files.readAllBytes(Paths.get("data/settings.json"));
			json = new String(encoded);
			settings = gson.fromJson(json, Settings.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Bot setName(String newname)
	{
		settings.setName(newname);
		jda.getAccountManager().setUsername(newname).update();
		return this;
	}
	
	public Bot update()
	{
		jda.getAccountManager().setUsername(settings.getName()).update();
		return this;
	}
}
