
package com.zalgebar.SushiGo1;

public abstract class Card {
	private final boolean DEBUG = SushiGoGame.DEBUG;

	String cardName;
	double pointValue;

	/**
	 * Method Card
	 */
	public Card() {
		this.cardName = "";
		this.pointValue = 0;
	}

	/**
	 * Method getValue
	 * @return
	 */
	public double getValue() {
		return this.pointValue;
	}

	/**
	 * Method getName
	 * @return
	 */
	public String getName() {
		return this.cardName;
	}

	@Override
	public String toString(){
		return (this.cardName+" :: "+this.pointValue);
	}
}
