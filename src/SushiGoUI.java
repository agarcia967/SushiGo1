package com.agarcia967.SushiGo1;

import java.io.*;
import java.util.*;

interface SushiGoUI {

    /**
     * This method will run each time any player plays a new card.
     * Called on each player after all players select a card to play/take.
     *
     * @param player	The name of the player whose cards these are.
     * @param cards		A list of Strings relating to the list of cards that the
     * 					player (playerName) has already played/taken.
     */
    public void displayCardsPlayed(String playerName, LinkedList<String> cards);

	/**
	 * Displays the title of the game.
	 * Called once at the beginning of the game.
	 *
	 * @param title	The title of the game.
	 */
	public void displayGameTitleText(String title);

    /**
     * This method should display that the deck is doing something.
     * Called when the deck does something (shuffling, dealing, etc.).
     *
     * @param cards	A list of Strings relating to the list of cards that the
     * user should select from.
     */
    public void displayDeckActionText(String deckAction);

	/**
	 * This method should display the scores of each player.
	 * Called after the current round.
	 *
	 * @param players	The name of each player. Player scores can
	 *					be accessed via the Player.getPoints().
	 */
	public void displayScoreAll(LinkedList<Player> playerNames);

	/**
	 * This method should display the scores of a single player.
	 * Called when each player's score is calculated (after each round).
	 *
	 * @param player	The name of the player. The player's score
	 *					can be accessed via the Player.getPoints().
	 */
	public void displayScorePlayer(Player playerName);

    /**
     * This method should allow the user to select yes or no, true or false, etc.
     *
     * @param prompt			The text that the user should see prompting them to choose yes or no.
     * @param yesDisplayText	The text that displays referring to a true result (i.e. the button text).
     * @param noDisplayText		The text that displays referring to a false result (i.e. the button text).
     * @param invalidText		Displayed if the choice is invalid.
     * @return					The choice that the user has selected true=yes, false=no
     */
    public boolean userInputBoolean(String prompt, String yesDisplayText, String noDisplayText, String invalidText);

    /**
     * This method should allow the user to choose an integer.
     * CAUTION: Allowing the user to enter an invalid int into the game may break it.
     *
     * @param prompt	The text that the user should see prompting them to select a card.
     * @param minimum	The minimum value that the user may choose.
     * @param maximum	The maximum value that the user may choose.
     * @return			The integer that the user has selected. It must agree with the min
     *					and max, otherwise the game may break.
     */
    public int userInputInteger(String prompt, int min, int max, String invalidText);

    /**
     * This method should allow the user to select a card.
     * CAUTION: Allowing an int that is out-of-bounds for the LinkedList WILL break the game,
     *			throwing an IndexOutOfBoundsException.
     *
     * @param prompt		The text that the user should see prompting them to select a card.
     * @param cards			The list of the cards the user has to choose from.
     * @param invalidText	The text that should display if the user selects an invalid card.
     * @return				The index from the list above of the card the user has selected
     */
    public int userInputSelectCardFromHand(String prompt, LinkedList<String> cards, String invalidText);

    /**
     * This method should allow the user to pass plain text to the game.
     *
     * @param prompt	The text that the user should see prompting them to enter plain text.
     * @return			The plain text string that the user has entered that will be used by the game.
     */
    public String userInputString(String prompt);

    /**
     * This method should allow the user to select a language file.
     *
     * @param prompt			The text that the user should see prompting them to select a language file.
     * @param defaultDeckFile	The string of the path of the default language file.
     * @return					The plain text string is the path if the language file.
     */
    public String langFile(String prompt, String defaultLangFile);

    /**
     * This method should allow the user to select a deck configuration file.
     *
     * @param prompt			The text that the user should see prompting them to select a deck file.
     * @param defaultDeckFile	The string of the path of the default deck file.
     * @return					The plain text string is the path if the language file.
     */
    public String deckFile(String prompt, String defaultDeckFile);
}
