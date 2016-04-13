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
import com.xetanai.rubix.Server;
import com.xetanai.rubix.XetbooruImage;
import com.xetanai.rubix.XetbooruUser;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Xetbooru extends Command {
	private static String keyword = "xtbr";
	private static String usage = "xtbr [id]";
	private static String helpShort = "Fetches an image from Xetbooru.";
	private static String helpLong = "Gets and sends an image from Xetbooru when given an image id.";
	
	public Xetbooru()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setNsfw(true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws Exception
	{
		
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
	    
	    /* USER */
	    
	    URL url2 = new URL("http://shimmie.xetbooru.us/index.php?q=/api/shimmie/get_user&id="+ img.getOwner());
		URLConnection conn2 = url2.openConnection();
		InputStream in2 = conn2.getInputStream();
		
		BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
		Gson gson2 = new GsonBuilder().setLenient().create();
		String json2 = br2.readLine();
		
		JsonParser parser2 = new JsonParser();
		JsonObject jObject2 = parser2.parse(json2).getAsJsonObject();
	    XetbooruUser usr = gson2.fromJson( jObject2 , XetbooruUser.class);
	    
	    /* START GETTING IMAGE */
	    
	    URL url3 = new URL("http://shimmie.xetbooru.us/images/"+img.getHash().substring(0,2)+"/"+img.getHash());
	    File outputFile = new File("data/tmpimg."+img.getExt());
	    URLConnection conn3 = url3.openConnection();
	    InputStream in3 = conn3.getInputStream();
	    
	    BufferedImage image = ImageIO.read(in3);
	    
	    OutputStream os = new FileOutputStream(outputFile);
	    
	    ImageIO.write(image, img.getExt(), os);
	    
	    String post = "**Size:** "+ img.getheight() +"x"+ img.getWidth() +"\n**Tags:** ";
	    for(int i=0; i<10; i++)
	    {
	    	if(i<img.getTags().length)
	    		post += img.getTags()[i] +", ";
	    }
	    post = post.substring(0,post.length()-2);
	    
	    post += "\n**Uploader:** "+ usr.getName() +" ("+ usr.getUploadCount() +" other images)";
	    
	    sendFile(msg, outputFile, post);
	}
}
