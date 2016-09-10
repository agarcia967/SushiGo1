package com.zalgebar.SushiGo1;

import com.zalgebar.INIManager;
import java.util.LinkedList;

public class MainTester {

	/**
	 * Method main
	 *
	 *
	 * @param args
	 *
	 */
	public static void main(String[] args) {
		SushiGoGame game = new SushiGoGame(new SushiGoTextUI());
		game.DEBUG = true;
		game.play();
	}
}
