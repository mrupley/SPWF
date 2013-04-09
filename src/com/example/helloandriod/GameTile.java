package com.example.helloandriod;

import java.util.Hashtable;

import android.content.Context;
import android.widget.ImageButton;

public class GameTile extends GameBoard{

	static Hashtable<String, String> friendValues = new Hashtable<String, String>();
	static Hashtable<String, String> tileAmounts = new Hashtable<String, String>();
	
    public static ImageButton[] tileImages;
    
    public static int currentTiles = 7;
    
	public GameTile(Context context) {
		super(context);
	}

	//adds a tile to the existing game board
	public void addTile(int xPosition, int yPosition, int tile){
		
	}
	
	public static void populateFriendValues(){
			friendValues.put( "A", "1" );
			friendValues.put( "B", "4" );
			friendValues.put( "C", "4" );
			friendValues.put( "D", "2" );
			friendValues.put( "E", "1" );
			friendValues.put( "F", "4" );
			friendValues.put( "G", "3" );
			friendValues.put( "H", "3" );
			friendValues.put( "I", "1" );
			friendValues.put( "J", "10" );
			friendValues.put( "K", "5" );
			friendValues.put( "L", "2" );
			friendValues.put( "M", "4" );
			friendValues.put( "N", "2" );
			friendValues.put( "O", "1" );
			friendValues.put( "P", "4" );
			friendValues.put( "Q", "10" );
			friendValues.put( "R", "1" );
			friendValues.put( "S", "1" );
			friendValues.put( "T", "1" );
			friendValues.put( "U", "2" );
			friendValues.put( "V", "5" );
			friendValues.put( "W", "4" );
			friendValues.put( "X", "8" );
			friendValues.put( "Y", "3" );
			friendValues.put( "Z", "10" );		
	}

	public static void populateTileAmounts() {
			tileAmounts.put( "A", "9" );
			tileAmounts.put( "B", "2" );
			tileAmounts.put( "C", "2" );
			tileAmounts.put( "D", "5" );
			tileAmounts.put( "E", "13" );
			tileAmounts.put( "F", "2" );
			tileAmounts.put( "G", "3" );
			tileAmounts.put( "H", "4" );
			tileAmounts.put( "I", "8" );
			tileAmounts.put( "J", "1" );
			tileAmounts.put( "K", "1" );
			tileAmounts.put( "L", "4" );
			tileAmounts.put( "M", "2" );
			tileAmounts.put( "N", "5" );
			tileAmounts.put( "O", "8" );
			tileAmounts.put( "P", "2" );
			tileAmounts.put( "Q", "1" );
			tileAmounts.put( "R", "6" );
			tileAmounts.put( "S", "5" );
			tileAmounts.put( "T", "7" );
			tileAmounts.put( "U", "4" );
			tileAmounts.put( "V", "2" );
			tileAmounts.put( "W", "2" );
			tileAmounts.put( "X", "1" );
			tileAmounts.put( "Y", "2" );
			tileAmounts.put( "Z", "1" );
}
}
