package com.xetanai.rubix;

import javax.security.auth.login.LoginException;

public class Main {

	public static void main(String[] args) {
		Bot rubix = new Bot("isaakrogers1@gmail.com","Xeta1230");
		rubix.getJDAB().addListener(new MessageListener(rubix, "!"));
		
		rubix.registerCommand(new Help());
		
		try { /* Try to start Rubix/Maxim, and print any errors */
			rubix.getJDAB().buildBlocking();
		}
		catch (IllegalArgumentException e)
        {
            System.out.println("[Main] (ERROR) The config was not populated. Please enter an email and password.");
        }
        catch (LoginException e)
        {
            System.out.println("[Main] (ERROR) The provided email / password combination was incorrect. Please provide valid details.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}
}
