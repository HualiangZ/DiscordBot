package gambaBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.ArrayList;
import java.awt.Color;
import java.io.File;
import java.util.Random;

public class BlackJack {
	private File current = new File("./cards");
	private File[] filesInDir = current.listFiles();
	private ArrayList<Integer> cards = new ArrayList<Integer>();// cards that have been played
	private ArrayList<Integer> playerCards = new ArrayList<Integer>();// cards in player's hand
	private ArrayList<Integer> botCards = new ArrayList<Integer>();// cards in bot's hand
	private int botNum;
	private int playerNum;
	private boolean palyerBusted = false;
	private boolean botBusted = false;
	private boolean playerStand = false;
	private boolean botStand = false;
	
	
	// give hand to player
	public void setPlayerHands(GuildMessageReceivedEvent event) {
		int num = genCard();
		int num2 = genCard();

		cards.add(num);
		cards.add(num2);
		playerCards.add(num);
		playerCards.add(num2);
		File card1 = new File("./cards/" + filesInDir[num].getName());
		File card2 = new File("./cards/" + filesInDir[num2].getName());
		playerCount();

		event.getChannel().sendMessage("Your Hand " + getPlayerNum()).addFile(card1).addFile(card2).queue();
		

	}

	// give hand to bot
	public void setBotHands(GuildMessageReceivedEvent event) {
		int num = genCard();
		int num2 = genCard(); // hidden from player

		cards.add(num);
		cards.add(num2);
		botCards.add(num);
		botCards.add(num2);
		File card1 = new File("./cards/" + filesInDir[num].getName());

		event.getChannel().sendMessage("Bots Hand ").addFile(card1).queue();
		

	}

	public void PlayerStand() {
		setPlayerStand(true);
	}
	
	public void BotStand() {
		setBotStand(true);
	}
	
	public void playerHit(GuildMessageReceivedEvent event) {
		int num = genCard();
		cards.add(num);
		playerCards.add(num);
		
		File card1 = new File("./cards/" + filesInDir[num].getName());
		this.playerCount(); 
		
		if(this.getPlayerNum()>21) {
			this.setPalyerBusted(true);
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Black Jack: Your Hand");
		eb.setDescription("Total: " + getPlayerNum());
		eb.setThumbnail("attachment://" + filesInDir[num].getName());
		eb.setColor(Color.green);
		event.getChannel().sendMessage(eb.build()).addFile(card1, filesInDir[num].getName()).queue();
	}
	
	public void botHit(GuildMessageReceivedEvent event) {
		int num = genCard();
		cards.add(num);
		botCards.add(num);

		File card1 = new File("./cards/" + filesInDir[num].getName());
		botCount();
		
		int x = this.getBotNum();
		
		if(x>21) {
			this.setBotBusted(true);
		}	
		
	}
	
	// count the player's hand
	public void playerCount() {
		int total = 0;
		int aceNum = 0;
		setPlayerNum(total);
		setPalyerBusted(false);
		
		for (int i : playerCards) {
			String str = filesInDir[i].getName();
			StringBuilder sb = new StringBuilder();
			boolean found = false;
			
			//finds the number in the string
			for (char c : str.toCharArray()) {
				if (Character.isDigit(c)) {
					sb.append(c);
					found = true;
				} else if (str.contains("ace")) {
					total += 11;
					aceNum += 1;
					break;
				} else if (str.contains("jack") || str.contains("queen") || str.contains("king")) {
					total += 10;
					break;
				} else if (found) {
					// If we already found a digit before and this char is not a digit, stop looping
					break;
				}
			}
			String x = sb.toString();
			int num;
			try {
				num = Integer.parseInt(x);
			} catch (NumberFormatException e) {
				num = 0;
			}
			total += num;
		}

		if(total > 21) {
			while (aceNum != 0) {
				if (aceNum == 0 && total > 21) {
					setPalyerBusted(true);
					break;
				}
				total -= 10;
			}
		}
		
		setPlayerNum(total);
	}

	// count the bot's hand
	public void botCount() {
		int total = 0;
		int aceNum = 0;
		setBotNum(total);
		
		for (int i : botCards) {
			String str = filesInDir[i].getName();
			StringBuilder sb = new StringBuilder();
			boolean found = false;

			for (char c : str.toCharArray()) {
				if (Character.isDigit(c)) {
					sb.append(c);
					found = true;
				} else if (str.contains("ace")) {
					total += 11;
					aceNum += 1;
					break;
				} else if (str.contains("jack") || str.contains("queen") || str.contains("king")) {
					total += 10;
					break;
				} else if (found) {
					// If we already found a digit before and this char is not a digit, stop looping
					break;
				}
			}
			String x = sb.toString();
			int num;
			try {
				num = Integer.parseInt(x);
			} catch (NumberFormatException e) {
				num = 0;
			}
			total += num;
		}

		if(total > 21) {
			while (aceNum != 0) {
				if (aceNum == 0 && total > 21) {
					setBotBusted(true);
					break;
				}
				total -= 10;
			}
		}
		
		setBotNum(total);
	}

	// get a random card from deck
	public int genCard() {
		Random gen = new Random();
		int x = gen.nextInt(52);
		x += 1;
		for (int i = 0; i < cards.size(); i++) {
			if (x == cards.get(i)) {
				i = 0;
				x = gen.nextInt(52);
				x += 1;
			}
		}
		return x;
	}

	public void clear() {
		playerCards.clear();
		botCards.clear();
	}
	
	public void showHidden(GuildMessageReceivedEvent event) {
		File card1 = new File("./cards/" + filesInDir[botCards.get(1)].getName());
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Black Jack: Hidden Card");
		eb.setThumbnail("attachment://" + filesInDir[botCards.get(1)].getName());
		eb.setColor(Color.red);
		event.getChannel().sendMessage(eb.build()).addFile(card1, filesInDir[botCards.get(1)].getName()).queue();
	}
	
	
	
	
	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public int getBotNum() {
		return botNum;
	}

	public void setBotNum(int botNum) {
		this.botNum = botNum;
	}

	public boolean isPalyerBusted() {
		return palyerBusted;
	}

	public void setPalyerBusted(boolean palyerBusted) {
		this.palyerBusted = palyerBusted;
	}

	public boolean isBotBusted() {
		return botBusted;
	}

	public void setBotBusted(boolean botBusted) {
		this.botBusted = botBusted;
	}

	public boolean isPlayerStand() {
		return playerStand;
	}

	public void setPlayerStand(boolean stand) {
		this.playerStand = stand;
	}

	public boolean isBotStand() {
		return botStand;
	}

	public void setBotStand(boolean botStand) {
		this.botStand = botStand;
	}
}
