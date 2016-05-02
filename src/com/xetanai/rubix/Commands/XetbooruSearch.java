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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class XetbooruSearch extends Command {
	public XetbooruSearch()
	{
		super("xetbooru");
		setUsage("xetbooru [tags]");
		setHelp("Fetches 3 images from Xetbooru.",false);
		setHelp("Fetches 3 random images from Xetbooru.\n"
				+ "If tags are given, then it will limit the images to only those matching the tags provided.",true);
		this.setNsfw(true);
		needsPermissionTo(Permission.MESSAGE_ATTACH_FILES);
		needsPermissionTo(Permission.MESSAGE_EMBED_LINKS);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws Exception
	{
		String readiedTags = "";
		for(int i=1; i < params.length; i++)
		{
			readiedTags += params[i];
			if(i!=params.length-1)
				readiedTags += "+";
		}
		
		String urlRaw = "http://shimmie.xetbooru.us/index.php?q=/api/danbooru/find_posts";
		if(!readiedTags.equals(""))
			urlRaw += "&tags="+ readiedTags;
		
		URL url = new URL(urlRaw);
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String contents = "";
		String line;
		while((line = br.readLine()) != null)
			contents += line;
		List<String> matches = null;
		
		matches = getMd5List(contents);
		
		if(matches.size()==0)
		{
			sendMessage(msg, "No results found with your tags.");
			return;
		}
		
		Collections.shuffle(matches,new Random());
		
		// Time to upload
		
		sendMessage(msg,"http://shimmie.xetbooru.us/index.php?q=/post/list/"+ readiedTags.replace("+","%20") +"/1");
		
		for(int i=0;i<3;i++)
		{
			if(i>=matches.size())
				return; // Less than 3 images matched.

			String md5 = matches.get(i);
			
			getImage(md5);
			
			msg.getChannel().sendFile(new File("data/tmpimg.jpg"), null);
		}
	}
	
	public void getImage(String md5) throws Exception
	{
		URL url = new URL("http://shimmie.xetbooru.us/images/"+md5.substring(0,2)+"/"+md5);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		File outputFile = new File("data/tmpimg.jpg");
		
		BufferedImage image = ImageIO.read(is);
	    OutputStream os = new FileOutputStream(outputFile);
	    
	    ImageIO.write(image, "jpg", os);
	    os.close();
	}
	
	public List<String> getMd5List(String xml)
	{
		List<String> hashList = new ArrayList<String>();
		
		while(xml.contains("md5="))
		{
			int startindex = xml.indexOf("md5=")+5;
			hashList.add(xml.substring(startindex,startindex+32));
			xml = xml.substring(startindex)+32;
		}
		
		return hashList;
	}
}
