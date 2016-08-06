
package com.zalgebar.SushiGo1;

public class Maki extends Card {
	private static final boolean DEBUG = SushiGoGame.DEBUG;

	private int numRolls;

	public Maki(){
		this(MakiType.ONE);
	}

	/**
	 * Method Maki
	 *
	 *
	 */
	public Maki(MakiType makiType) {
		this.pointValue = 0;
		switch (makiType){
			case ONE:
				this.cardName = SushiGoGame.cardNames.get("name.Maki.1");
				return;
			case DUO:
				this.cardName = SushiGoGame.cardNames.get("name.Maki.2");
				return;
			case TRIO:
				this.cardName = SushiGoGame.cardNames.get("name.Maki.3");
				return;
			default:
				System.out.println("--------------ERROR:Cannot instantiate a Maki of type "+makiType+".");
				System.exit(0);
		}
		return;
	}

	public int getNumRolls(){
		return numRolls;
	}
}
