package com.xetanai.rubix;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class SQLUtils {
	public static Connection sqlcon = null;
	
	private static void rawDelete(String table, String where)
	{
		PreparedStatement pst;
		
		try {
			pst = sqlcon.prepareStatement("DELETE FROM "+ table +" WHERE "+ where);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void rawInsert(String table, String[] keys, Object[] values)
	{
		PreparedStatement pst;
		String query = "INSERT INTO "+ table +" (";
		
		for(String x : keys)
		{
			query += x+",";
		}
		
		query = query.substring(0,query.length()-1);
		
		query += ") values (";
		
		for(Object x : values)
		{
			query += x+",";
		}
		
		query = query.substring(0,query.length()-1);
		
		query += ")";
		
		try {
			pst = sqlcon.prepareStatement(query);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void rawUpdate(String table, String where, String key, String val)
	{
		try {
			PreparedStatement pst = sqlcon.prepareStatement("UPDATE "+ table +" SET "+ key +"='"+ val +"' WHERE "+ where);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getColumnNames(String table)
	{
		PreparedStatement pst;
		ResultSet rs;
		List<String> names = new ArrayList<String>();
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM "+ table +" LIMIT 1");
			rs = pst.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();
			
			for(int i = 1; i <= md.getColumnCount(); i++)
			{
				names.add(md.getColumnName(i));
			}
			
			pst.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return names;
	}
	
	public static String getSettingVal(Server guild, String col)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM serversettings WHERE DiscordID = '"+ guild.getId() +"'");
			rs = pst.executeQuery();
			
			if(!rs.next())
				return null;
			
			String result = rs.getString(col);
			pst.close();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void createServerEntry(Server srv)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM serversettings WHERE DiscordID = '"+ srv.getId() +"'");
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				rawDelete("serversettings","DiscordID = "+ srv.getId());
			}
			rawInsert("serversettings",
					new String[]{"DiscordID"},
					new Object[]{srv.getId()});
			
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Server loadServer(String id)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		Server p = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM serversettings WHERE DiscordID = '"+ id +"'");
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				p = new Server(rs.getString("DiscordID"))
					.setMod(rs.getBoolean("DoModerate"))
					.setLewd(rs.getBoolean("AllowLewd"))
					.setWelcome(rs.getString("Greeting"))
					.setFarewell(rs.getString("Farewell"))
					.setWarnCap(rs.getInt("MaxWarns"))
					.setGreet(rs.getBoolean("DoGreet"))
					.setPrefix(rs.getString("Prefix"))
					.setDoCNF(rs.getBoolean("CommandNotFoundMsg"))
					.setAllowColor(rs.getBoolean("AllowColors"));
			}
			else
			{
				createServerEntry(new Server(id));
			}
			
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
	public static void changeServer(String id, String key, String val) throws SQLException
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		pst = sqlcon.prepareStatement("SELECT * FROM serversettings WHERE DiscordID = '"+ id +"'");
		rs = pst.executeQuery();
		
		if(!rs.next())
			createUserEntry(new Person(id));
		rawUpdate("serversettings","DiscordID = "+ id,key,val);
		
		pst.close();
	}
	
	public static void changeUser(String id, String key, boolean val)
	{
		if(val)
			changeUser(id, key, "1");
		else
			changeUser(id, key, "0");
	}
	
	public static void changeUser(String id, String key, int val)
	{
		changeUser(id, key, String.valueOf(val));
	}
	
	public static void changeUser(String id, String key, String val)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM users WHERE DiscordID = '"+ id +"'");
			rs = pst.executeQuery();
			
			if(!rs.next())
				createUserEntry(new Person(id));
			rawUpdate("users","DiscordID = "+ id,key,val);
			
			pst.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createUserEntry(Person usr)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM users WHERE DiscordID = '"+ usr.getId() +"'");
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				rawDelete("users","DiscordID = "+ usr.getId());
			}
			
			rawInsert("users",
					new String[]{"DiscordID","Afk"},
					new Object[]{usr.getId(),usr.isAfk()});
			
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Person loadUser(String id)
	{
		Person p = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM users WHERE DiscordID = '"+ id +"'");
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				p = new Person(rs.getString(rs.findColumn("DiscordID")));
				
				p.setAfk(rs.getBoolean("Afk"));
			}
			else
			{
				createUserEntry(new Person(id));
				pst.close();
				return new Person(id);
			}
			pst.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
	public static List<String> getOperators(String guild)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> ops = new ArrayList<String>();
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM operators WHERE GuildID = '"+ guild +"'");
			rs = pst.executeQuery();
			
			while(rs.next())
			{
				ops.add(rs.getString("UserID"));
			}
			
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ops;
	}
	
	public static void addOperator(String userid, String guildid)
	{
		rawInsert("operators",
				new String[]{"GuildID","UserID"},
				new Object[]{guildid,userid});
	}
	
	public static void removeOperator(String userid, String guildid)
	{
		rawDelete("operators","GuildID = "+ guildid +" and UserID = "+ userid);
	}
	
	public static void addBannedWord(String guildid, String word)
	{
		rawInsert("bannedwords",
				new String[]{"GuildID","Word"},
				new Object[]{guildid,"'"+word+"'"});
	}
	
	public static void removeBannedWord(String guildid, String word)
	{
		rawDelete("bannedwords","GuildID = "+ guildid +" and Word = '"+ word +"'");
	}
	
	public static List<String> getBannedWords(String guild)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> words = new ArrayList<String>();
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM bannedwords WHERE GuildID = '"+ guild +"'");
			rs = pst.executeQuery();
			
			while(rs.next())
			{
				words.add(rs.getString("Word"));
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return words;
	}
	
	public static void vote(String voterID, String targetID, boolean isPositive)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM votes WHERE VoterID = '"+ voterID +"' and TargetID = '"+ targetID+"'");
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				rawDelete("votes","VoterID = "+ voterID +" and TargetID = "+ targetID);
			}
			
			rawInsert("votes",
					new String[]{"VoterID","TargetID","isPos"},
					new Object[]{voterID,targetID,isPositive});
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int[] getFame(String targetID)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		int[] fame = new int[2];
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM votes WHERE TargetID = '"+ targetID+"'");
			rs = pst.executeQuery();
			
			while(rs.next())
			{
				if(rs.getBoolean("isPos"))
					fame[0]++;
				else
					fame[1]++;
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fame;
	}
}
