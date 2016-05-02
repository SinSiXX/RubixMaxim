package com.xetanai.rubix.Commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.xetanai.rubix.OsuUser;
import com.xetanai.rubix.enitites.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Osu extends Command {
	public Osu()
	{
		super("osu");
		setUsage("osu <username> [mode]");
		setHelp("Get information on an Osu! player.",false);
		setHelp("Shows detailed information about a player in Osu!\n"
				+ "If no gamemode is provided, then it will get their standard stats.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws Exception
	{
		if(params.length==1)
		{
			sendMessage(msg, "Please supply an Osu! username.");
			return;
		}
		
		List<String> modes = Arrays.asList("Standard","Taiko","Catch The Beat","Mania");
		int m = -1;
		params[1] = params[1].replace("~", " ");
		
		if(params.length == 3)
		{
			for(String mode : modes)
			{
				if(params[2].toLowerCase().equals(mode.toLowerCase()))
				{
					m=modes.indexOf(mode);
					break;
				}
			}
			if(m == -1)
			{
				sendMessage(msg, "That mode wasn't valid. Try one of the following:\n`Standard, Taiko, Catch The Beat, Mania`");
				return;
			}
		}
		if(m == -1)
			m=0;
		
		URL fetch;
		URLConnection con;
		InputStream is = null;
		OsuUser usr = null;
		File cachedUser = new File("data/cache/osu/"+ params[1] +"-"+ m +".json");
		
		if(cachedUser.isFile())
		{
			System.out.println("Using cached copy.");
			FileReader fr = new FileReader(cachedUser);
			BufferedReader br = new BufferedReader(fr);
			Gson gson = new Gson();

			String contents = "";
			String ln;
			while((ln = br.readLine())!=null)
				contents += ln;
			
			br.close();
			usr = gson.fromJson(contents, OsuUser.class);
			
			Date nowD = new Date();
			int now = (int) (nowD.getTime()/1000);
			int then = (int) (usr.getDateRetrieved().getTime()/1000);
			
			if(((now-then)/60) > 10) // Cached osu users are valid for 10 minutes.
			{
				usr = null;
				System.out.println("Cached copy expired, requesting up to date response.");
			}
			else
			{
				System.out.println("Cached copy valid for "+ (10-((now-then)/60)) +" more minutes.");
			}
		}
		
		if(usr==null)
		{
			try {
				fetch = new URL("https://osu.ppy.sh/api/get_user?k=e2dda97db22bd0c465851f474e25a43cac32c260&u="+ params[1] +"&m="+m);
			} catch(MalformedURLException e) {
				sendMessage(msg,"That username looks strange. Try using your ID instead.");
				return;
			}
			
			con = fetch.openConnection();
			is = con.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String contents = "";
			String ln;
			while((ln = br.readLine())!=null)
				contents += ln;
			
			usr = parseJson(contents);
			
			if(usr==null)
			{
				sendMessage(msg,"That player doesn't exist. If your username contains a space, use a `~` in its place, or use your ID.");
				return;
			}
			
			usr.setDateRetrieved(new Date());
			
			cachedUser.createNewFile();
			FileWriter fw = new FileWriter(cachedUser);
			BufferedWriter bw = new BufferedWriter(fw);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			bw.write(gson.toJson(usr));
			
			bw.close();
		}
		
		String rank;
		
		try {
			rank = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.getRank()));
		} catch(NumberFormatException e) {
			sendMessage(msg,"Looks like that user's never played "+ modes.get(m) +".");
			return;
		}
	    String localRank = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.getCountryRank()));
	    String pp = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(usr.getpp()));
	    
	    String hit3 = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.get300s()));
	    String hit1 = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.get100s()));
	    String hit5 = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.get50s()));
	    
	    String score = NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(usr.getTotalScore()));
	    String scoreRanked = NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(usr.getRankedScore()));
	    
		String post = "";
		
		post += "__**"+ usr.getUsername() +" (ID #"+ usr.getId() +") Osu stats for "+ modes.get(m) +"**__:";
		if(usr.getRank().equals("0"))
			post += "```\nThis user hasn't earned a rank in this mode.```\n";
		else
		{
			int now = (int) new Date().getTime()/1000;
			int then = (int) usr.getDateRetrieved().getTime()/1000;
			
			post += "\nStats updated "+ ((now/60)-(then/60)) +" minute(s) ago.";
			post += "```apache\nRankings   : #"+ rank +" (#"+ localRank +" in "+ usr.getCountry() +") "+ pp +"PP.\n";
			post += "Level      : "+ usr.getLevel() +" ("+ usr.getPlayCount() +" plays).\n";
			post += "Accuracy   : "+ usr.getAccuracy() +".\n";
			post += "Hits       : "+ hit3 +"x300, "+ hit1 +"x100, "+ hit5 +"x50.\n";
			post += "Score      : "+ score +" ("+ scoreRanked +" ranked).\n";
			post += "Ranks      : SS-"+ usr.getRankX() +"  S-"+ usr.getRankS() +"  A-"+ usr.getRankA()+"```\n";
		}
		post += "http://a.ppy.sh/"+ usr.getId() +"\n";
		post += "http://osu.ppy.sh/u/"+ usr.getId();
		
		sendMessage(msg, post);
	}
	
	private OsuUser parseJson(String json)
	{
		Gson gson = new GsonBuilder().setLenient().create();
		
		JsonParser parser = new JsonParser();
		JsonArray jArray = parser.parse(json).getAsJsonArray();
		
		OsuUser ret = null;
		
		try {
			ret = gson.fromJson(jArray.get(0), OsuUser.class);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
		return ret;
	}
}
