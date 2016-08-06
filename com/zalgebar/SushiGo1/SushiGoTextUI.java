package com.zalgebar.SushiGo1;

import com.zalgebar.SushiGo1.SushiGoUI;
import java.util.LinkedList;
import java.util.Scanner;

public class SushiGoTextUI implements SushiGoUI {

	/**
	 * Method displayCardsPlayed
	 *
	 *
	 * @param playerName
	 * @param cards
	 *
	 */
	public void displayCardsPlayed(String playerName, LinkedList cards) {
		System.out.println(playerName);
		System.out.println(cards.toString());
	}

	/**
	 * Method displayGameTitleText
	 *
	 *
	 * @param title
	 *
	 */
	public void displayGameTitleText(String title) {
		System.out.println(title);
	}

	/**
	 * Method displayDeckActionText
	 *
	 *
	 * @param deckAction
	 *
	 */
	public void displayDeckActionText(String deckAction) {
		System.out.println(deckAction);
	}

	/**
	 * Method displayScoreAll
	 *
	 *
	 * @param players
	 */
	public void displayScoreAll(LinkedList<Player> players) {
		for(int i = 0; i<players.size(); i++){
			System.out.print(players.get(i).getName());
			if(i==players.size()) System.out.println();
			else System.out.print(" | ");
		}
		System.out.println("");
		for(int i = 0; i<players.size(); i++){
			System.out.print(players.get(i).getPoints());
			if(i==players.size()) System.out.println();
			else System.out.print(" | ");
		}
		System.out.println("");
	}

	/**
	 * Method displayScorePlayer
	 *
	 *
	 * @param player
	 */
	public void displayScorePlayer(Player player) {
		System.out.print(player.getName()+": "+player.getPoints());
	}

	/**
	 * Method userInputBoolean
	 *
	 *
	 * @param prompt
	 * @param yesDisplayText
	 * @param noDisplayText
	 * @param invalidText
	 *
	 * @return
	 *
	 */
	public boolean userInputBoolean(String prompt, String yesDisplayText, String noDisplayText, String invalidText) {
		System.out.println(prompt+" ("+yesDisplayText+"/"+noDisplayText+")");
		Scanner s = new Scanner(System.in);
		String input = "";
		while(true){
			input = s.nextLine();
			if(input!=null && yesDisplayText.toUpperCase().contains(input.toLowerCase())){
				return true;
			}
			else if(input!=null && input.contains(noDisplayText)){
				return false;
			}
			else {
				System.out.println(invalidText);
			}
		}
	}

	/**
	 * Method userInputInteger
	 *
	 * @param prompt
	 * @param min
	 * @param max
	 * @param invalidText
	 * @return
	 *
	 */
	public int userInputInteger(String prompt, int min, int max, String invalidText) {
		System.out.println(prompt);
		Scanner s = new Scanner(System.in);
		String input = "";
		int returnable;
		while(true){
			input = s.nextLine();
			try{
				returnable = Integer.parseInt(input);
				if(returnable>=min && returnable<=max){
					return returnable;
				} else {
					System.out.println(invalidText);
				}
			}
			catch(NumberFormatException e){
				System.out.println(invalidText);
			}
		}
	}

	/**
	 * Method userInputSelectCardFromHand
	 *
	 *
	 * @param prompt
	 * @param cards
	 * @param invalidText
	 *
	 * @return
	 *
	 */
	public int userInputSelectCardFromHand(String prompt, LinkedList cards, String invalidText) {
		System.out.println(prompt + "\n" + cards.toString());
		Scanner s = new Scanner(System.in);
		String input = "";
		int returnable;
		while(true){
			input = s.nextLine();
			try{
				returnable = Integer.parseInt(input);
				if(returnable<0 || returnable>=cards.size()){
					System.out.println(invalidText);
				} else {
					return returnable;
				}
			}
			catch(NumberFormatException e){
				System.out.println(invalidText);
			}
		}
	}

	/**
	 * Method userInputString
	 *
	 *
	 * @param prompt
	 *
	 * @return
	 *
	 */
	public String userInputString(String prompt) {
		System.out.println(prompt);
		Scanner s = new Scanner(System.in);
		String returnable = s.nextLine().trim();
		return returnable;
	}

	/**
     * Method langFile
     *
     * @param prompt
     * @param defaultLangFile
     * @return
     */
    public String langFile(String prompt, String defaultLangFile){
    	return defaultLangFile;
    }

    /**
     * Method deckFile
     *
     * @param prompt
     * @param defaultDeckFile
     * @return
     */
    public String deckFile(String prompt, String defaultDeckFile){
    	return defaultDeckFile;
    }
}
