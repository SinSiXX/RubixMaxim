package com.xetanai.rubix.Commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.xetanai.rubix.Bot;
import com.xetanai.rubix.OsuResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Osu extends Command {
	private static String keyword = "osu";
	private static String helpShort = "osu <username or id> - Get information about an Osu! player.";
	private static String helpLong = "osu <username or id>\nShows detailed information about a player. Currently only in Osu!Standard, but will be expanded later.";
	
	public Osu()
	{
		super(helpShort,helpLong,keyword);
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
		
		URLConnection con = fetch.openConnection();;
		InputStream is = con.getInputStream();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		Gson gson = new GsonBuilder().setLenient().create();
		String json = "";
		String line;
		while((line = in.readLine()) != null)
			json += line;
		
		JsonParser parser = new JsonParser();
		JsonArray jArray = parser.parse(json).getAsJsonArray();
	    OsuResponse usr = gson.fromJson( jArray.get(0) , OsuResponse.class);
		
		String post = "";
		
		post += usr.getUsername() +" (ID #"+ usr.getId() +") Osu stats for "+ modes.get(m) +":\n```";
		post += "Global rank: #"+ usr.getRank() +" (#"+ usr.getCountryRank() +" in "+ usr.getCountry() +") "+ usr.getpp() +"PP .\n";
		post += "Level: "+ usr.getLevel() +" ("+ usr.getPlayCount() +" plays).\n";
		post += "Accuracy: "+ usr.getAccuracy() +".\n";
		post += "Hits: "+ usr.get300s() +"x300, "+ usr.get100s() +"x100, "+ usr.get50s() +"x50.\n";
		post += "Score: "+ usr.getTotalScore() +" ("+ usr.getRankedScore() +" ranked).\n";
		post += "Ranks: SS-"+ usr.getRankX() +"  S-"+ usr.getRankS() +"  A-"+ usr.getRankA()+"```\n";
		post += "http://osu.ppy.sh/u/"+ usr.getId();
		
		sendMessage(bot, msg, post);
	}
}