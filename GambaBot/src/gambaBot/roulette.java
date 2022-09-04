package gambaBot;

import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class roulette {
	private  String[] roulette_table = {"0", "red_1", "black_2", "red_3", "black_4", "red_5", "black_6", "red_7", "black_8", "red_9", "black_10",
			"red_11", "black_12", "red_13", "black_14", "red_15", "black_16", "red_17", "black_18", "red_19",
			"black_20", "red_21", "black_22", "red_23", "black_24", "red_25", "black_26", "red_27", "black_28", "red_29", "black_30", 
			"red_31", "black_32", "red_33", "black_34", "red_35", "black_36"};
	
	protected  String outcome = roulette_table[gen()];
	
	private  int gen() {
		Random rng = new Random();
		int x = rng.nextInt(roulette_table.length);
		return x;
	}
	
	public void getValue(GuildMessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Roulette");
		eb.setDescription(outcome);
		event.getChannel().sendMessage(eb.build()).queue();
	}
	
	
}
