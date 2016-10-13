
package com.agarcia967.SushiGo1;

import java.util.*;

public class Player {
	private static final boolean DEBUG = SushiGoGame.DEBUG;

	private String playerName;
	private LinkedList<Maki> makiList;
	private LinkedList<Tempura> tempuraList;
	private LinkedList<Sashimi> sashimiList;
	private LinkedList<Dumplings> dumplingList;
	private LinkedList<Pudding> puddingList;
	private LinkedList<Nigiri> nigiriList;
	private LinkedList<Wasabi> wasabiList;
	private Chopsticks myChopsticks;
	private double roundScore;
	private double totalScore;

	private LinkedList<Card> hand;

	/**
	 * Method Player
	 *
	 *
	 */
	public Player() {
		this("");
	}
	public Player(String name){
		this.playerName = name;
		this.totalScore = 0;
		this.newRound();
		puddingList = new LinkedList<Pudding>();
	}

	public String getName(){
		return this.playerName;
	}

	/**
	 * Method newRound
	 * Remove all cards except for Puddings from play and prepare for a new round
	 * @return
	 */
	void newRound(){
		makiList = new LinkedList<Maki>();
		tempuraList = new LinkedList<Tempura>();
		sashimiList = new LinkedList<Sashimi>();
		dumplingList = new LinkedList<Dumplings>();
		nigiriList = new LinkedList<Nigiri>();
		wasabiList = new LinkedList<Wasabi>();
		myChopsticks = null;
	}

	Wasabi nextUnPairedWasabi(){
		ListIterator<Wasabi> listIterator = wasabiList.listIterator();
		while (listIterator.hasNext()) {
			if(listIterator.next().isPaired()==false) return listIterator.previous();
		}
		return null;
	}

	int getNumMaki(){
		if(DEBUG) System.out.print("Totaling Maki... ");
		int returnable = 0;
		ListIterator<Maki> listIterator = makiList.listIterator();
		while (listIterator.hasNext()) {
			returnable+=listIterator.next().getNumRolls();
		}
		if(DEBUG) System.out.println("Total Maki: "+returnable);
		return returnable;
	}

	int getNumPudding(){
		if(DEBUG) System.out.print("Total Pudding: "+puddingList.size());
		return puddingList.size();
	}

	void playCard(Card newCard){
		//if(DEBUG) System.out.println("> void takeCard(Card)");
		this.playCard(newCard,false);
	}

	void playCard(Card newCard, boolean useWasabi){
		//if(DEBUG) System.out.println("> void takeCard(Card, boolean)");
		if(DEBUG) System.out.print("Adding "+newCard.getName()+"... ");
		if(newCard instanceof Maki){
			makiList.addLast((Maki)newCard);
			if(DEBUG) System.out.print("Added to Maki list. ");
		}
		else if(newCard instanceof Tempura){
			tempuraList.addLast((Tempura)newCard);
			if(DEBUG) System.out.print("Added to Tempura list. ");
		}
		else if(newCard instanceof Sashimi){
			sashimiList.addLast((Sashimi)newCard);
			if(DEBUG) System.out.print("Added to Sashimi list. ");
		}
		else if(newCard instanceof Dumplings){
			dumplingList.addLast((Dumplings)newCard);
			if(DEBUG) System.out.print("Added to Dumpling list. ");
		}
		else if(newCard instanceof Pudding){
			puddingList.addLast((Pudding)newCard);
			if(DEBUG) System.out.print("Added to Pudding list. ");
		}
		else if(newCard instanceof Wasabi){
			wasabiList.addLast((Wasabi)newCard);
			if(DEBUG) System.out.print("Added to Pudding list. ");
		}
		else if(newCard instanceof Nigiri){
			Wasabi myWasabi = nextUnPairedWasabi();
			if(myWasabi!=null){
				if(DEBUG) System.out.print("\n -Nigiri value: "+newCard.getValue()+" Use wasabi? ");
				if(useWasabi){
					if(DEBUG) System.out.print("Yes. \n");
					((Nigiri)newCard).setWasabi(myWasabi);
					myWasabi.setNigiri((Nigiri)newCard);
				}
				else{
					if(DEBUG) System.out.print("No. \n");
				}
			}
			nigiriList.addLast((Nigiri)newCard);
			if(DEBUG) System.out.print("Added to Nigiri list. ");
		}
		else if(newCard instanceof Chopsticks){
			myChopsticks = (Chopsticks)newCard;
			if(DEBUG) System.out.print("Added Chopsticks to hand. ");
		}
		else{
			System.out.println("--------------ERROR: Class '" + newCard.getClass().getSimpleName() + "' not recognized.");
			System.exit(0);
		}
		if(DEBUG) System.out.println("Done.");
	}

	/**
	 * Method takeCard(Card,Card)
	 *
	 * @return Returns the reference to the Chopsticks that was used by the player.
	 */
	Chopsticks playCard(Card card1, Card card2){
		//if(DEBUG) System.out.println("> void takeCard(Card, Card)");
		if(myChopsticks==null){
			System.out.println("--------------ERROR: Player does not have a Chopsticks.");
			System.exit(0);
		}
		this.playCard(card1);
		this.playCard(card2);

		Chopsticks returnable = myChopsticks;
		myChopsticks = null;
		return returnable;
	}

	private double getPointsNigiri(){
		double returnable = 0;
		ListIterator<Nigiri> listIterator = nigiriList.listIterator();
		while (listIterator.hasNext()) {
			returnable+=listIterator.next().getValue();
		}
		if(DEBUG) System.out.println(" > Total Nigiri Points: "+returnable);
		return returnable;
	}

	public double getPoints(){
		return this.roundScore;
	}

	double getPoints(double makiPoints){
		if(DEBUG) System.out.println("\nCalculating Player's Points...");
		double returnable = 0;

		returnable+=makiPoints;
		if(DEBUG) System.out.println(" > Total Maki Points: "+makiPoints);

		double tempuraValue = 0;
		if(tempuraList!=null && tempuraList.getFirst()!=null) tempuraValue=tempuraList.getFirst().getValue();
		returnable += (tempuraList.size()/2)*tempuraValue;
		if(DEBUG) System.out.println(" > Total Tempura Points: "+(tempuraList.size()/2)*tempuraValue);

		double sashimiValue = 0;
		if(sashimiList!=null && sashimiList.getFirst()!=null) sashimiValue=sashimiList.getFirst().getValue();
		returnable += (sashimiList.size()/3)*sashimiValue;
		if(DEBUG) System.out.println(" > Total Sashimi Points: "+((sashimiList.size()/3)*sashimiValue));

		double temp = returnable;
		if(dumplingList.size()==1) returnable+=1;
		else if(dumplingList.size()==2) returnable+=3;
		else if(dumplingList.size()==3) returnable+=6;
		else if(dumplingList.size()==4) returnable+=10;
		else if(dumplingList.size()>=5) returnable+=15;
		if(DEBUG) System.out.println(" > Total Dumplings Points: "+(returnable-temp));

		returnable+=getPointsNigiri();

		if(DEBUG) System.out.println("Total Player Points: "+returnable+"\n");
		this.roundScore = returnable;
		this.totalScore += returnable;
		return returnable;
	}

	double getPointsDessert(double makiPoints, double puddingPoints){
		if(DEBUG) System.out.println(" > Total Pudding Points: "+puddingPoints);
		double returnable = getPoints(makiPoints)+puddingPoints;
		if(DEBUG) System.out.println("Total Dessert Points: "+returnable+"\n");
		this.totalScore += puddingPoints;
		this.roundScore = returnable;
		return returnable;
	}
}
