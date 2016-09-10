/**
 * @(#)SushiGoMainText.java
 *
 *
 * @author
 * @version 1.00 2016/7/10
 */

package com.zalgebar.SushiGo1;

import com.zalgebar.INIManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.LinkedList;

public class SushiGoGame {

	static boolean DEBUG = true;

	static HashMap<String,String> cardNames;
	private HashMap<String,Integer> cardCounts;
	private HashMap<String,String> interfaceText;

	private SushiGoUI ui;
	private LinkedList<Card> deck;
	private int numberOfPlayers;
	private int numCardsPerPlayer;
	private LinkedList<Player> players;
	private LinkedList<LinkedList<Card>> hands;

    /**
     * Creates a new instance of <code>SushiGoGame</code>.
     */
    public SushiGoGame(SushiGoUI newUI){
		this.ui = newUI;
    	initializeCardCountsLists();
    	initializeCardNamesLists();
    	initializeInterfaceText();
    	readDeck();
    	readLang();
    }

    private void initializeCardNamesLists(){
    	cardNames = new HashMap<String,String>();
    	///// Default values /////
    	cardNames.put("name.Tempura","Tempura");
    	cardNames.put("name.Sashimi","Sashimi");
    	cardNames.put("name.Dumplings","Dumplings");
    	cardNames.put("name.Maki.1","Maki x1");
    	cardNames.put("name.Maki.2","Maki x2");
    	cardNames.put("name.Maki.3","Maki x3");
    	cardNames.put("name.Nigiri.Egg","Egg Nigiri");
    	cardNames.put("name.Nigiri.Salmon","Salmon Nigiri");
    	cardNames.put("name.Nigiri.Squid","Squid Nigiri");
    	cardNames.put("name.Pudding","Pudding");
    	cardNames.put("name.Wasabi","Wasabi");
    	cardNames.put("name.Chopsticks","Chopsticks");
    }

    private void initializeCardCountsLists(){
    	cardCounts = new HashMap<String,Integer>();
    	///// Default values /////
    	cardCounts.put("count.Tempura",14);
    	cardCounts.put("count.Sashimi",14);
    	cardCounts.put("count.Dumplings",14);
    	cardCounts.put("count.Maki.1",6);
    	cardCounts.put("count.Maki.2",8);
    	cardCounts.put("count.Maki.3",12);
    	cardCounts.put("count.Nigiri.Egg",5);
    	cardCounts.put("count.Nigiri.Salmon",10);
    	cardCounts.put("count.Nigiri.Squid",5);
    	cardCounts.put("count.Pudding",10);
    	cardCounts.put("count.Wasabi",6);
    	cardCounts.put("count.Chopsticks",4);
    	cardCounts.put("count.min_players",2);
    	cardCounts.put("count.max_players",5);
    	cardCounts.put("count.cards_for_max",7);
    	cardCounts.put("count.cards_for_min",10);
    }

    private void initializeInterfaceText(){
    	interfaceText = new HashMap<String,String>();
    	///// Default Values /////
    	interfaceText.put("display.dealing","Dealing...");
		interfaceText.put("display.main_title","SushiGo!");
		interfaceText.put("display.players_turn","It is $p's turn.");
		interfaceText.put("display.rotate_hands","Rotating hands...");
		interfaceText.put("display.shuffling","Shuffling...");
		interfaceText.put("prompt.boolean.invalid","Please enter $y or $n.");
		interfaceText.put("prompt.boolean.no","No");
		interfaceText.put("prompt.boolean.yes","Yes");
		interfaceText.put("prompt.card_sel.invalid","Please select a valid card.");
		interfaceText.put("prompt.integer.invalid","Please enter a number greater than or equal to $min and less than or equal to $max.");
		interfaceText.put("prompt.number_players","How many human players are playing?");
		interfaceText.put("prompt.player_name","What is this player's name?");
		interfaceText.put("prompt.pick_deck_file","Please select a deck configuration file.");
		interfaceText.put("prompt.pick_lang_file","Please select a language file.");
		interfaceText.put("prompt.use_chopsticks","Would you like to use your $c?");
		interfaceText.put("prompt.use_wasabi_on_nigiri","Would you like to use a $w on this $n?");
    }

    private void initializePlayersAndHands(){
    	players = new LinkedList<Player>();
    	for(int i = 0; i<this.numberOfPlayers; i++){
    		players.addLast(new Player(ui.userInputString(interfaceText.get("prompt.player_name"))));
    	}
    	hands = new LinkedList<LinkedList<Card>>();

    	int maxPlayer = cardCounts.get("count.max_players");
    	int minPlayer = cardCounts.get("count.min_players");
    	int maxCards = cardCounts.get("count.cards_for_max");
    	int minCards = cardCounts.get("count.cards_for_min");
    	this.numCardsPerPlayer = minCards;
    	int temp = minPlayer;
    	while(temp<players.size()){
    		numCardsPerPlayer--;
    		temp++;
    	}
    	if(DEBUG) System.out.println("Num Players: "+players.size());
    	if(DEBUG) System.out.println("Num Cards:   "+numCardsPerPlayer);
    }

    public void play(){
    	numberOfPlayers = ui.userInputInteger(
    		interfaceText.get("prompt.number_players"),
    		cardCounts.get("count.min_players"),
    		cardCounts.get("count.max_players"),
    		interfaceText.get("prompt.integer.invalid")
    			.replace("$min",Integer.toString(cardCounts.get("count.min_players")))
    				.replace("$max",Integer.toString(cardCounts.get("count.max_players")))
    		);

    	newDeck();
    	initializePlayersAndHands();
    	shuffleDeck();
    	shuffleDeck();
    	shuffleDeck();

    	///// Round 1 /////
    	dealCards();
    	round();
    	scorePlayers();
    	ui.displayScoreAll(players);

    	///// Round 2 /////
    	dealCards();
    	round();
    	scorePlayers();
    	ui.displayScoreAll(players);

    	///// Round 3 /////
    	dealCards();
    	round();
    	scorePlayersDessert();
    	ui.displayScoreAll(players);
    }

    private void round(){
    	//while there are cards in someone's hand
    	  //all players take 1 card from hand
    	  //all players reveal card
    	    //if there is only one card left, take and reveal
    	  //all players pass hands
    	  rotateHands(true);
    }

	/**
     * Reads in the configuration files (Deck.ini) for a deck of cards.
     */
    private void readDeck(){
		String temp = ""; //used for reading data from the INIManager

		INIManager mgr = new INIManager();
		mgr.readFile(ui.deckFile(interfaceText.get("prompt.pick_deck_file"),"Deck.ini"));

		///// Relate COUNT of each type of card into the game /////
    	if(DEBUG) System.out.print("Reading file for card counts in deck... ");
		for(String key : cardCounts.keySet()){
			try{
				//convert text found in the INI to integer and pass into the HashMap
				temp = mgr.getValue("DECK",key);
				if(temp!=null){ //check the key/value is in the file
					cardCounts.put(key,Integer.parseInt(temp));
				} else {
					if(DEBUG) System.out.println("Key '" + key +
						"' not found in file. Default loaded.");
					//default values are already loaded
				}
			} catch(NumberFormatException e){
				//the String read from file is not a number
				if(DEBUG) System.out.println("Value for card '" + key +
					"' cannot be parsed into a number.");
			}
		}
		if(DEBUG) System.out.println("Done.");
    }

	/**
     * Reads in the configuration files (Deck.ini) for a deck of cards.
     */
    private void readLang(){
		String temp = ""; //used for reading data from the INIManager

		INIManager mgr = new INIManager();
		mgr.readFile(ui.langFile(interfaceText.get("prompt.pick_lang_file"),"en_us.ini"));

		///// Relate NAME of each type of card into the game /////
		if(DEBUG) System.out.print("Reading language file... ");
		for(String key : cardNames.keySet()){
			temp = mgr.getValue("CARD NAMES",key);
			if(temp!=null){ //check the key/value is in the file
				cardNames.put(key,temp);
			} else {
				if(DEBUG) System.out.println("Key '" + key +
					"' not found in file. Default loaded.");
				//default values are already loaded
			}
		}
		for(String key : interfaceText.keySet()){
			temp = mgr.getValue("INTERFACE",key);
			if(temp!=null){ //check the key/value is in the file
				interfaceText.put(key,temp);
			} else {
				if(DEBUG) System.out.println("Key '" + key +
					"' not found in file. Default loaded.");
				//default values are already loaded
			}
		}
		if(DEBUG) System.out.println("Done.\n");
    }

	/**
     * Creates a new deck. Depending upon the configuration
     * file, this method distributes the number of cards of
     * each type into the deck.
     */
    private void newDeck(){
    	deck = new LinkedList<Card>();

    	for(int i = 0; i<cardCounts.get("count.Tempura"); i++){
    		deck.addLast(new Tempura());
    	}
    	for(int i = 0; i<cardCounts.get("count.Sashimi"); i++){
    		deck.addLast(new Sashimi());
    	}
    	for(int i = 0; i<cardCounts.get("count.Dumplings"); i++){
    		deck.addLast(new Dumplings());
    	}
    	for(int i = 0; i<cardCounts.get("count.Maki.3"); i++){
    		deck.addLast(new Maki(MakiType.TRIO));
    	}
    	for(int i = 0; i<cardCounts.get("count.Maki.2"); i++){
    		deck.addLast(new Maki(MakiType.DUO));
    	}
    	for(int i = 0; i<cardCounts.get("count.Maki.1"); i++){
    		deck.addLast(new Maki(MakiType.ONE));
    	}
    	for(int i = 0; i<cardCounts.get("count.Nigiri.Egg"); i++){
    		deck.addLast(new Nigiri(NigiriType.EGG));
    	}
    	for(int i = 0; i<cardCounts.get("count.Nigiri.Salmon"); i++){
    		deck.addLast(new Nigiri(NigiriType.SALMON));
    	}
    	for(int i = 0; i<cardCounts.get("count.Nigiri.Squid"); i++){
    		deck.addLast(new Nigiri(NigiriType.SQUID));
    	}
    	for(int i = 0; i<cardCounts.get("count.Pudding"); i++){
    		deck.addLast(new Pudding());
    	}
    	for(int i = 0; i<cardCounts.get("count.Wasabi"); i++){
    		deck.addLast(new Wasabi());
    	}
    	for(int i = 0; i<cardCounts.get("count.Chopsticks"); i++){
    		deck.addLast(new Chopsticks());
    	}
    }

	/**
     * Randomly redistributes the location of each card in the deck.
     */
    private void shuffleDeck(){
    	ui.displayDeckActionText(interfaceText.get("display.shuffling"));
    	//if(DEBUG) System.out.println(deck.toString());
    	for(Card c : deck){
    		Card temp = c;
    		int index = randInt(0,deck.size());
    		c = deck.get(index);
    		deck.set(index,temp);
    	}
    	//if(DEBUG) System.out.println(deck.toString());
    }

    /**
     * Selects a random integer based on min and max.
     *
     * @param min	Minimum value for the random integer.
     * @param max	Maximum value for the random integer.
     */
    private int randInt(int min, int max){
    	return min+(int)(Math.random()*((max-min)-1));
    }

	/**
     * Distributes cards to every player's hand.
     * Number distributed depends upon the number of players.
     * <table>
	 * <tr><th>Players</th> <th>Cards</th></tr>
	 * <tr><td>2</td>		<td>10</td></tr>
	 * <tr><td>3</td>		<td>9</td></tr>
	 * <tr><td>4</td>		<td>8</td></tr>
	 * <tr><td>5</td>		<td>7</td></tr>
	 * </table>
     */
    private void dealCards(){
    	ui.displayDeckActionText(interfaceText.get("display.dealing"));
    	for(int i = 0; i<numCardsPerPlayer; i++){
	    	for(LinkedList<Card> hand : hands){
	    		hand.addLast(deck.pop());
		    	if(DEBUG) System.out.println(hand.toString());
	    	}
    	}
    }

    /**
     * Rotates the hands to new players.
     *
     * @param direction	True passes to left, False passes to right.
     */
    private void rotateHands(boolean direction){
    	ui.displayDeckActionText(interfaceText.get("display.rotate_hands"));
    	//TODO: Add your code here
    }

	/**
     * Gets the current score for all players for a round and applies it.
     */
    private void scorePlayers(){
    	//allplayers.getNumMaki();
    	//compare all maki
    	  //the player(s) with the most get 6 points divided evenly
    	  //the player(s) with the 2nd most get 3 points divided evenly
    	//pass division of points to winning players
    	//TODO: Add your code here
    }

	/**
     * Gets the score for all players for a dessert round and applies it.
     */
    private void scorePlayersDessert(){
    	scorePlayers();
    	//player.getNumPudding();
    	//compare num pudding
    	  //if two player game, no negative points
    	  //if all players have same amount, no points to anyone
    	//pass division of points to winning players
    	//TODO: Add your code here
    }
}
