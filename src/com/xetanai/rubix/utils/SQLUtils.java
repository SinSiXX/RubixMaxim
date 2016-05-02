package com.xetanai.rubix.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Chan;
import com.xetanai.rubix.enitites.Person;
import com.xetanai.rubix.enitites.Server;

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
			Bot.adminAlert(Bot.createErrorMessage(e));
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
			if(x.getClass().equals(String.class))
				query += "'"+ x +"',";
			else
				query += x+",";
		}
		
		query = query.substring(0,query.length()-1);
		
		query += ")";
		
		try {
			pst = sqlcon.prepareStatement(query);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
		}
	}
	
	private static void rawUpdate(String table, String where, String key, String val) throws SQLException
	{
		PreparedStatement pst = sqlcon.prepareStatement("UPDATE "+ table +" SET "+ key +"='"+ val +"' WHERE "+ where);
		pst.execute();
		pst.close();
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
			Bot.adminAlert(Bot.createErrorMessage(e));
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
			return null; // Do not notify.
		}
	}
	
	public static String getSettingVal(Chan chan, String col)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM channelsettings WHERE ChannelID = '"+ chan.getId() +"'");
			rs = pst.executeQuery();
			
			if(!rs.next())
				return null;
			
			String result = rs.getString(col);
			pst.close();
			
			return result;
		} catch (Exception e) {
			return null; // Do not notify.
		}
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
			Bot.adminAlert(Bot.createErrorMessage(e));
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
					.setAllowColor(rs.getBoolean("AllowColors"))
					.setDefaultChannel(rs.getString("DefaultChannel"));
			}
			else
			{
				createServerEntry(new Server(id));
				pst.close();
				return new Server(id);
			}
			
			pst.close();
		} catch (SQLException e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
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
			createServerEntry(new Server(id));
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
			Bot.adminAlert(Bot.createErrorMessage(e));
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
			Bot.adminAlert(Bot.createErrorMessage(e));
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
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				p = new Person(rs.getString(rs.findColumn("DiscordID")));
				
				p.setAfk(rs.getBoolean("Afk"));
				String rawLH = rs.getString("Lastheard");
				if(!rawLH.equals("Never"))
					p.setLastHeard(dateFormat.parse(rawLH));
			}
			else
			{
				createUserEntry(new Person(id));
				pst.close();
				return new Person(id);
			}
			pst.close();
			
		} catch (Exception e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
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
				new Object[]{guildid,word});
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
			Bot.adminAlert(Bot.createErrorMessage(e));
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
			Bot.adminAlert(Bot.createErrorMessage(e));
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
			Bot.adminAlert(Bot.createErrorMessage(e));
		}
		return fame;
	}
	
	public static String getFaq(String id, String keyword)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM faqentries WHERE DiscordID = '"+ id +"' and Keyword = '"+ keyword +"'");
			rs = pst.executeQuery();
			
			String faqentry = null;
			if(rs.next())
			{
				faqentry = rs.getString("Response");
			}
			
			pst.close();
			return faqentry;
		} catch (SQLException e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
		}
		return null;
	}
	
	public static List<String> getAllFaqs(String id)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> faqlist = new ArrayList<String>();
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM faqentries WHERE DiscordID = '"+ id +"'");
			rs = pst.executeQuery();
			
			while(rs.next())
				faqlist.add(rs.getString("Keyword"));
			
			pst.close();
		} catch(SQLException e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
		}
		
		return faqlist;
	}
	
	public static void removeFaq(String id, String keyword)
	{
		rawDelete("faqentries","DiscordID = '"+ id +"' and Keyword = '"+ keyword +"'");
	}
	
	public static void addFaq(String id, String keyword, String response)
	{
		rawInsert("faqentries",
				new String[]{"DiscordID","Keyword","Response"},
				new Object[]{id,keyword,response});
	}
	
	public static Chan loadChannel(String id)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		Chan c = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM channelsettings WHERE ChannelID = '"+ id +"'");
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				c = new Chan(id)
						.setIgnored(rs.getBoolean("Ignored"))
						.setLewd(rs.getBoolean("Lewd"));
			}
			else
			{
				createChannelEntry(new Chan(id));
				pst.close();
				return new Chan(id);
			}
			
			pst.close();
		} catch (SQLException e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
		}
		
		return c;
	}
	
	public static void createChannelEntry(Chan chn)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = sqlcon.prepareStatement("SELECT * FROM channelsettings WHERE ChannelID = '"+ chn.getId() +"'");
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				rawDelete("channelsettings","ChannelID = "+ chn.getId());
			}
			
			rawInsert("channelsettings",
					new String[]{"ChannelId"},
					new Object[]{chn.getId()});
			
			pst.close();
		} catch (SQLException e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
		}
	}
	
	public static void changeChannel(String id, String key, String val) throws SQLException
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		pst = sqlcon.prepareStatement("SELECT * FROM channelsettings WHERE ChannelID = '"+ id +"'");
		rs = pst.executeQuery();
		
		if(!rs.next())
		{
			createChannelEntry(new Chan(id));
			changeChannel(id,key,val);
		}
		rawUpdate("channelsettings","ChannelID = "+ id,key,val);
		
		pst.close();
	}
}
