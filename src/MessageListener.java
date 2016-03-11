import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;

public class MessageListener extends ListenerAdapter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
        {
            new JDABuilder()
                    .setEmail("isaakrogers1@gmail.com")
                    .setPassword("Xeta1230")
                    .addListener(new MessageListener())
                    .buildBlocking();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("The config was not populated. Please enter an email and password.");
        }
        catch (LoginException e)
        {
            System.out.println("The provided email / password combination was incorrect. Please provide valid details.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        
        if(message.getContent().startsWith("!"))
        {
        	System.out.print("[Rubix] Command received from "+ message.getAuthor().getUsername() +": "+ message.getContent());
        }
    }

}
