package com.xetanai.rubix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import com.xetanai.rubix.Commands.*;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;

public class Main {
	private static String tkn = "";
	private static String carbontkn = "";
	private static String dbpass = "";
	
	public static void main(String[] args) {
		File file = new File("Data/token.txt");
		BufferedReader reader = null;
		
		
		try {
		    reader = new BufferedReader(new FileReader(file));
		    tkn = reader.readLine();
		    carbontkn = reader.readLine();
		    dbpass = reader.readLine();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		JDABuilder jdab = new JDABuilder().setBotToken(tkn);
		
		Bot.registerCommand(new Help());
		Bot.registerAlias("help", "?");
		Bot.registerCommand(new GetAliases());
		Bot.registerCommand(new About());
		Bot.registerAlias("about","info");
		Bot.registerCommand(new Roll());
		Bot.registerCommand(new Whatgame());
		Bot.registerCommand(new Say());
		Bot.registerAlias("say", "echo");
		Bot.registerCommand(new Osu());
		Bot.registerCommand(new Redact());
		Bot.registerAlias("redact", "undo");
		Bot.registerCommand(new Eval());
		Bot.registerCommand(new Op());
		Bot.registerAlias("op", "deop");
		Bot.registerAlias("op", "toggleop");
		Bot.registerCommand(new Afk());
		Bot.registerCommand(new Id());
		//rubix.registerCommand(new Games());
		Bot.registerCommand(new Xetbooru());
		Bot.registerCommand(new XetbooruSearch());
		Bot.registerCommand(new Leave());
		Bot.registerCommand(new Config());
		Bot.registerCommand(new ChanConfig());
		Bot.registerCommand(new Banword());
		Bot.registerCommand(new Listops());
		Bot.registerCommand(new Whois());
		Bot.registerCommand(new Vote());
		Bot.registerCommand(new ColorMe());
		Bot.registerCommand(new Mute());
		Bot.registerCommand(new Translate());
		Bot.registerCommand(new Faq());
		Bot.registerCommand(new IgnoreChannel());
		
		jdab.addListener(new MessageListener());
		
		try { /* Try to start Rubix/Maxim, and print any errors */
			Class.forName("com.mysql.jdbc.Driver");
			
			SQLUtils.sqlcon = DriverManager.getConnection("jdbc:mysql://localhost:3306/rubixv2", "rubix", dbpass);
			Bot.jda = jdab.buildBlocking();
		}
		catch (IllegalArgumentException e)
        {
            System.out.println("[Main] (ERROR) The config was not populated. Please enter an email and password.");
        }
        catch (LoginException e)
        {
            System.out.println("[Main] (ERROR) The provided email / password combination was incorrect. Please provide valid details.");
        }
		catch (Exception e) {
			Bot.adminAlert(Bot.createErrorMessage(e));
		}
		
		// Ensure all servers exist in DB
		for(Guild x : Bot.jda.getGuilds())
		{
			Server srv = SQLUtils.loadServer(x.getId());
			
			if(srv==null)
			{
				SQLUtils.createServerEntry(new Server(x.getId()));
			}
		}
		
		System.out.println("Registered commands:");
		System.out.println(Bot.commandList);
		
		Timer carbonTimer = new Timer();
		TimerTask carbonPost = new TimerTask() {
			@Override
			public void run()
			{
				System.out.println("[CarboniteX] Making a POST request...");
				try {
					HttpClient httpclient = HttpClients.createDefault();
					HttpPost httppost = new HttpPost("https://www.carbonitex.net/discord/data/botdata.php");
					
					List<NameValuePair> params = new ArrayList<NameValuePair>(2);
					params.add(new BasicNameValuePair("key",carbontkn));
					params.add(new BasicNameValuePair("servercount",String.valueOf(Bot.jda.getGuilds().size())));
					
					httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					HttpResponse resp = httpclient.execute(httppost);
					HttpEntity ent = resp.getEntity();
					
					if(ent != null)
					{
						InputStream is = ent.getContent();
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						System.out.println(br.readLine());
						
						is.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		if(!Bot.isDev())
		{
			carbonTimer.schedule(carbonPost,1000*60*60);
			carbonPost.run(); // Run on startup
		}
		
		//Timer t = new Timer();
		//UpdateGames ugames = new UpdateGames(rubix);
		
		//t.scheduleAtFixedRate(ugames, 0L, 5000L); 
	}
}
