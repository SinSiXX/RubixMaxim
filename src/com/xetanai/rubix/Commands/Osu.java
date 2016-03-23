package com.xetanai.rubix.Commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.xetanai.rubix.Bot;
import com.xetanai.rubix.OsuUser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Osu extends Command {
	private static String keyword = "osu";
	private static String usage = "osu <username>";
	private static String helpShort = "Get information about an Osu! player.";
	private static String helpLong = "Shows detailed information about a player. Currently only in Osu!Standard, but will be expanded later.";
	
	public Osu()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	@Override
	public void onCalled(Bot bot, MessageReceivedEvent msg) throws Exception
	{
		String[] params = msg.getMessage().getContent().split(" ");
		List<String> modes = Arrays.asList("Standard","Taiko","Catch The Beat","Mania");
		int m = -1;
		
		if(params.length == 3)
		{
			for(String mode : modes)
			{
				if(params[2].toLowerCase().replaceAll("osu!","").equals(mode.toLowerCase()))
				{
					m=modes.indexOf(mode);
					break;
				}
			}
			if(m == -1)
			{
				sendMessage(bot, msg, "That mode wasn't valid. Try one of the following:\n`Standard, Taiko, Catch The Beat, Mania`");
				return;
			}
		}
		if(m == -1)
			m=0;
		
		URL fetch = new URL("https://osu.ppy.sh/api/get_user?k=e2dda97db22bd0c465851f474e25a43cac32c260&u="+ params[1] +"&m="+m);
		
		URLConnection con = fetch.openConnection();
		InputStream is = con.getInputStream();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		Gson gson = new GsonBuilder().setLenient().create();
		String json = "";
		String line;
		while((line = in.readLine()) != null)
			json += line;
		
		JsonParser parser = new JsonParser();
		JsonArray jArray = parser.parse(json).getAsJsonArray();
	    OsuUser usr = gson.fromJson( jArray.get(0) , OsuUser.class);
		
	    String rank = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.getRank()));
	    String localRank = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.getCountryRank()));
	    String pp = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(usr.getpp()));
	    
	    String hit3 = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.get300s()));
	    String hit1 = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.get100s()));
	    String hit5 = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(usr.get50s()));
	    
	    String score = NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(usr.getTotalScore()));
	    String scoreRanked = NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(usr.getRankedScore()));
	    
		String post = "";
		
		post += "__**"+ usr.getUsername() +" (ID #"+ usr.getId() +") Osu stats for "+ modes.get(m) +"**__:```apache\n";
		post += "Rankings   : #"+ rank +" (#"+ localRank +" in "+ usr.getCountry() +") "+ pp +"PP.\n";
		post += "Level      : "+ usr.getLevel() +" ("+ usr.getPlayCount() +" plays).\n";
		post += "Accuracy   : "+ usr.getAccuracy() +".\n";
		post += "Hits       : "+ hit3 +"x300, "+ hit1 +"x100, "+ hit5 +"x50.\n";
		post += "Score      : "+ score +" ("+ scoreRanked +" ranked).\n";
		post += "Ranks      : SS-"+ usr.getRankX() +"  S-"+ usr.getRankS() +"  A-"+ usr.getRankA()+"```\n";
		post += "http://a.ppy.sh/"+ usr.getId() +"\n";
		post += "http://osu.ppy.sh/u/"+ usr.getId();
		
		sendMessage(bot, msg, post);
	}
}
