
package com.agarcia967.SushiGo1;

public class Nigiri extends Card {
	private static final boolean DEBUG = SushiGoGame.DEBUG;

	private Wasabi wasabi = null;

	/**
	 * Method Nigiri
	 *
	 *
	 */
	public Nigiri() {
		this(NigiriType.EGG);
		return;
	}
	public Nigiri(NigiriType nigiriType) {
		// TODO: Add your code here
		switch (nigiriType){
			case EGG:
				this.cardName = SushiGoGame.cardNames.get("name.Nigiri.Egg");
				this.pointValue = 1;
				return;
			case SALMON:
				this.cardName = SushiGoGame.cardNames.get("name.Nigiri.Salmon");
				this.pointValue = 2;
				return;
			case SQUID:
				this.cardName = SushiGoGame.cardNames.get("name.Nigiri.Squid");
				this.pointValue = 3;
				return;
			default:
				System.out.println("--------------ERROR:Cannot instantiate a Nigiri of type "+nigiriType+".");
				System.exit(0);
		}
	}

	/**
	 * Method setWasabi
	 * @param1 the Wasabi card with which to pair
	 */
	void setWasabi(Wasabi newWasabi){
		if(this.wasabi==null){
			System.out.print("--------------ERROR:Cannot attach this Nigiri to this Wasabi.");
			System.out.print("       This Nigiri is already paired to another Wasabi.");
			System.exit(0);
		}
		if(newWasabi.getNigiri()!=null){
			System.out.print("--------------ERROR:Cannot attach this Nigiri to this Wasabi.");
			System.out.print("       Wasabi that was passed is already paired to another Nigiri.");
			System.exit(0);
		}
		this.wasabi = newWasabi;
		newWasabi.setNigiri(this);
	}

	public Wasabi getWasabi(){
		return this.wasabi;
	}

	public double getValue(){
		if(this.wasabi==null) return this.pointValue;
		else return (this.getValue()*3);
	}

	public boolean isPaired(){
		if (this.wasabi!=null) return true;
		return false;
	}
}
