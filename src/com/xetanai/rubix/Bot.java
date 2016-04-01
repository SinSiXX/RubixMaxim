package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;

public class Bot {
	private JDA jda;
	private List<Command> commandList = new ArrayList<Command>();
	private List<Alias> aliasList = new ArrayList<Alias>();
	private String version = "2.3.1";
	private Message lastMessage;
	private Connection sqlcon;
	
	public Bot(String login, String password) {
		JDABuilder jdab = new JDABuilder().setEmail(login).setPassword(password);
		jdab.addListener(new MessageListener(this));
		
		
		try { /* Try to start Rubix/Maxim, and print any errors */
			jda = jdab.buildBlocking();
			Class.forName("com.mysql.jdbc.Driver");
			
			sqlcon = DriverManager.getConnection("jdbc:mysql://localhost:3306/rubix", "rubix", "Xeta1230");
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
	}
	
	public JDA getJDA()
	{
		return jda;
	}
	
	public Connection getSQL()
	{
		return sqlcon;
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
	
	public void saveUser(Person usr)
	{
		String query;
		PreparedStatement pst;
		ResultSet rs;
		PreparedStatement preparedStmt;
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM users WHERE DiscordID = "+usr.getId());
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				query = " delete from users where DiscordID="+usr.getId();
				preparedStmt = sqlcon.prepareStatement(query);
				preparedStmt.execute();
			}
			query = " insert into users (DiscordID, Usernames, Games, Afk) values (?, ?, ?, ?)";
			preparedStmt = sqlcon.prepareStatement(query);
			preparedStmt.setString (1, usr.getId());
			preparedStmt.setString (2, jda.getUserById(usr.getId()).getUsername());
			preparedStmt.setString (3, "");
			preparedStmt.setBoolean(4, usr.isAfk());
			
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Person loadUser(String id)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		Person p = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM users WHERE DiscordID = "+id);
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				p = new Person(rs.getString(rs.findColumn("DiscordID")));
				String afk = rs.getString(rs.findColumn("Afk"));
				
				p.setAfk((afk.equals("1")));
			}
			else
			{
				saveUser(new Person(id));
				return new Person(id);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pst != null)
					pst.close();
				if(rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return p;
	}
	
	public void createServerEntry(Server srv)
	{
		String query;
		PreparedStatement pst;
		ResultSet rs;
		PreparedStatement preparedStmt;
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM servers WHERE DiscordID = "+srv.getId());
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				query = " delete from servers where DiscordID="+srv.getId();
				preparedStmt = sqlcon.prepareStatement(query);
				preparedStmt.execute();
			}
			query = " insert into servers (DiscordID, Operators, Muted, BannedWords) values (?, ?, ?, ?)";
			preparedStmt = sqlcon.prepareStatement(query);
			preparedStmt.setString (1, srv.getId());
			
			String rawOpArray = "";
			for(String usrid : srv.getOperators())
			{
				rawOpArray += usrid +",";
			}
			
			preparedStmt.setString (2, rawOpArray);
			
			String mutedUsers = "";
			for(String usrid : srv.getMutedUsers())
			{
				mutedUsers += usrid +",";
			}
			preparedStmt.setString (3, mutedUsers);
			
			String bannedWords = "";
			for(String usrid : srv.getBannedWords())
			{
				bannedWords += usrid +",";
			}
			preparedStmt.setString (4, bannedWords);
			
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeServer(String id, String key, String val) throws SQLException
	{
		PreparedStatement pst = null;
		
		try {
			pst = sqlcon.prepareStatement(" UPDATE servers SET "+ key +"='"+ val +"' WHERE DiscordID="+ id +";");
			pst.execute();
		} finally {
			try {
				if(pst != null)
					pst.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Server loadServer(String id)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		Server p = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM servers WHERE DiscordID="+id);
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				p = new Server(rs.getString(rs.findColumn("DiscordID")))
						.setLewd(rs.getBoolean(rs.findColumn("Lewd")))
						.setVulgar(rs.getBoolean(rs.findColumn("Vulgar")))
						.setWarnCap(rs.getInt(rs.findColumn("WarnCap")))
						.setPrefix(rs.getString(rs.findColumn("Prefix")))
						.setWelcome(rs.getString(rs.findColumn("MsgWelcome")))
						.setFarewell(rs.getString(rs.findColumn("msgGoodbye")))
						.setGreet(rs.getBoolean(rs.findColumn("DoGreet")));
				
				String[] rawOpArray = rs.getString(rs.findColumn("Operators")).split(",");
				for(String usrid : rawOpArray)
				{
					if(!usrid.equals(""))
						p.addOperator(usrid);
				}
				
				String[] mutedIds = rs.getString(rs.findColumn("Muted")).split(",");
				for(String usrid : mutedIds)
				{
					if(!usrid.equals(""))
						p.addMutedUser(usrid);
				}
				
				String[] bannedWords = rs.getString(rs.findColumn("BannedWords")).split(",");
				for(String word : bannedWords)
				{
					if(!word.equals(""))
						p.addBannedWord(word);
				}
				
			}
			else
			{
				createServerEntry(new Server(id));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pst != null)
					pst.close();
				if(rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return p;
	}
	
	public boolean isOp(String userid, String serverid)
	{
		if(userid.equals(jda.getGuildById(serverid).getOwnerId()))
			return true;
		
		Server srv = loadServer(serverid);
		
		List<String> ops = srv.getOperators();
		
		return ops.contains(userid);
	}
}