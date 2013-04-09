package com.example.helloandriod;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat.sqlite";
    private static final int DATABASE_VERSION = 2;

    public static final String COLUMN_PLAYER = "player";
    public static final String COLUMN_PLAYER_PHRASE = "player_phrase";
    
    public static final String COLUMN_OPPONENT = "opponent";
    public static final String COLUMN_OPPONENT_PHRASE = "opponent_phrase";

    public static final String TABLE_NAME = "chatlog";
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME 
                                            + " (" + COLUMN_PLAYER + " TEXT, " + COLUMN_PLAYER_PHRASE + " TEXT, " + COLUMN_OPPONENT + " TEXT, " + COLUMN_OPPONENT_PHRASE +  " TEXT);";

    private static SQLiteDatabase db;
    private static ContentValues m_contentValues;
    private static String m_playerName;
    private static String m_opponentName = "default";
    
    ChatOpenHelper(Context context, String playerName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //sets the player name for reference in the database
        m_playerName = playerName;
        
        //m_opponentName = opponentName
    }
    
    ChatOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Some logic to i.e. add/remove columns...
    }
    
    public static void closeDatabase(){
    	if(db != null){
    		db.close();
    	}
    }
    
    public static void insertIntoDatabase(Context context, String Column, String phrase){
    	
    	//Opens the database if it does not exist
    	if(db == null || m_contentValues == null){
        	db = new ChatOpenHelper(context).getWritableDatabase();
        	m_contentValues = new ContentValues();
    	}

    	m_contentValues.put(COLUMN_PLAYER_PHRASE, phrase);
    	m_contentValues.put(COLUMN_PLAYER, m_playerName);
    	m_contentValues.put(COLUMN_OPPONENT, m_opponentName);
    	m_contentValues.put(COLUMN_OPPONENT_PHRASE, "Message Received. Good work!");
    	db.insert(TABLE_NAME, null, m_contentValues);

    	//contentValues.put(ChatOpenHelper.COLUMN_OPPONENT, TEST_DEFINITION);
    }
    
    
    /*
     * returns the list of messages in the chatlog between the two players
     * @param context - the context to pull the chat
     * @return ArrayList<String> the list of chat messages
     */
    public static ArrayList<String> query(Context context){
    	ArrayList<String> phrases = new ArrayList<String>();
    	m_playerName = ActivityLogin.getLogin(context);
    	final SQLiteDatabase db = new ChatOpenHelper(context).getWritableDatabase();
    	final Cursor c = db.query(TABLE_NAME, new String[]{ COLUMN_PLAYER_PHRASE } , COLUMN_PLAYER + " = ?" , new String[] {m_playerName} , null, null, null);
    	if (c != null) {
    	    if (c.moveToFirst()) {
    	        final int playerPhrase = c.getColumnIndex(COLUMN_PLAYER_PHRASE);
    	        for(int i = 0; i < c.getCount(); i++, c.move(1) ){
        	        final String phrase = c.getString(playerPhrase);
        	        phrases.add(phrase);
        	        Log.d("WORKSHOP", "Phrase:  '" + phrase 
                            + "' for player: '" + m_playerName + "'.");
    	        }

    	        
    	    }
    	    c.close();
    	}
    	return phrases;
    }
}