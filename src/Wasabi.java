
package com.agarcia967.SushiGo1;

public class Wasabi extends Card {
	
	private Nigiri nigiri = null;
	
	/**
	 * Method Wasabi
	 *
	 *
	 */
	public Wasabi() {
		this.cardName = SushiGoGame.cardNames.get("Wasabi");
		this.pointValue = 0;
	}
	
	/**
	 * Method Tempura
	 * @param1 the Nigiri card with which to pair
	 * @return returns 0 if successful
	 *		   returns 1 if this already paired
	 *		   returns 2 if other already paired
	 */
	void setNigiri(Nigiri newNigiri){
		
		if(this.getNigiri()!=null){
			System.out.print("--------------ERROR:Cannot attach this Nigiri to this Wasabi.");
			System.out.print("       This Wasabi is already paired to another Nigiri.");
			System.exit(0);
		}
		if(newNigiri.getWasabi()!=null){
			System.out.print("--------------ERROR:Cannot attach this Nigiri to this Wasabi.");
			System.out.print("       Nigiri that was passed is already paired to another Wasabi.");
			System.exit(0);
		}
		this.nigiri = newNigiri;
		newNigiri.setWasabi(this);
	}

	public Nigiri getNigiri(){
		return this.nigiri;
	}
	
	public boolean isPaired(){
		if (this.nigiri!=null) return true;
		return false;
	}
}
