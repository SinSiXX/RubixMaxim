package com.xetanai.rubix.Commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.xetanai.rubix.YandexTranslation;
import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Translate extends Command {
	public Translate()
	{
		super("translate");
		setUsage("translate <language> <phrase>");
		setHelp("Translates a phrase.",false);
		setHelp("Uses Yandex.translate to translate a given phrase.\n"
				+ "If only one language code is given, then it will try to autodetect the language given.\n"
				+ "Alternatively, you can separate 2 codes with a dash to give the source and target language. (IE en-es)\n"
				+ "A list of language codes can be found here:\n"
				+ "https://tech.yandex.com/translate/doc/dg/concepts/langs-docpage/?ncrnd=913",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws Exception
	{
		if(params.length==1 || params.length==2)
		{
			sendMessage(msg, "Please supply a language code and phrase.");
			return;
		}
		
		String langCode = params[1];
		String phrase = "";
		for(int i=2;i<params.length;i++)
		{
			phrase += params[i];
			if(i!=params.length-1)
				phrase += " ";
		}
		
		URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20160422T232029Z.ed55d43eafb82911.90974954f20eb5255b9fdb63e4364411c3104738&text="+ URLEncoder.encode(phrase,"UTF-8") +"&lang="+ langCode);
		URLConnection conn = url.openConnection();
		HttpURLConnection conn2 = (HttpURLConnection) url.openConnection();
		
		if(conn2.getResponseCode()==400)
		{
			sendMessage(msg, "That language isn't supported.\nSee here for a list of supported languages and their codes:\nhttps://tech.yandex.com/translate/doc/dg/concepts/langs-docpage/?ncrnd=913");
			return;
		}
		
		InputStream is = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
	    
	    String reply = "";
	    String ln;
		while((ln = br.readLine())!=null)
			reply += ln;
		
		Gson gson = new Gson();
		YandexTranslation trns = gson.fromJson(reply, YandexTranslation.class);
		
		// Output time~
		
		String post = "";
		
		if(trns.getMessage()!=null)
		{
			sendMessage(msg, "I couldn't translate that.\n"+ trns.getMessage());
			return;
		}
		
		post += "Translated from "+ trns.getLang().substring(0,2) +" to "+ trns.getLang().substring(3)+"\n"
				+"**"+ trns.getText().get(0) +"**";
		sendMessage(msg, post);
	}
}
