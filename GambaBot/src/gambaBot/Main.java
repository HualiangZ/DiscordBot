package gambaBot;
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import gambaBot.Commands;

public class Main {
	public static void main(String[] args) throws LoginException{

	JDABuilder jda = JDABuilder.createDefault("token");
	jda.setStatus(OnlineStatus.ONLINE);
	jda.addEventListeners(new Commands());
	jda.build();
	}
}
