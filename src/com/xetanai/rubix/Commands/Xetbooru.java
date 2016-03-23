package com.xetanai.rubix.Commands;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xetanai.rubix.Bot;
import com.xetanai.rubix.XetbooruImage;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Xetbooru extends Command {
	private static String keyword = "xtbr";
	private static String usage = "xtbr <id>";
	private static String helpShort = "Repeat what you say.";
	private static String helpLong = "Repeats exactly what you supply as the parameter.";
	
	public Xetbooru()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setNsfw(true);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg) throws Exception
	{
		String[] params = msg.getMessage().getRawContent().split(" ");
		
		URL url = new URL("http://shimmie.xetbooru.us/index.php?q=/api/shimmie/get_image/"+ params[1]);
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		Gson gson = new GsonBuilder().setLenient().create();
		String json = "";
		String line;
		while((line = br.readLine()) != null)
			json += line;
		
		JsonParser parser = new JsonParser();
		JsonObject jObject = parser.parse(json).getAsJsonObject();
	    XetbooruImage img = gson.fromJson( jObject , XetbooruImage.class);
	    
	    /* START GETTING IMAGE */
	    
	    URL url2 = new URL("http://shimmie.xetbooru.us/images/"+img.getHash().substring(0,2)+"/"+img.getHash());
	    File outputFile = new File("data/tmpimg."+img.getExt());
	    URLConnection conn2 = url2.openConnection();
	    InputStream in2 = conn2.getInputStream();
	    
	    BufferedImage image = ImageIO.read(in2);
	    
	    OutputStream os = new FileOutputStream(outputFile);
	    
	    ImageIO.write(image, img.getExt(), os);
	    
	    sendFile(bot, msg, outputFile);
	}
}
