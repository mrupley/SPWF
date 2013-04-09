package com.example.helloandriod;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityChat extends Activity {
	
    String m_playerName;
    TextView m_textView;
    ListView m_listView;
    EditText m_sendText;
    String m_message;
    ChatAdapter m_chat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        
        m_playerName = ActivityLogin.getLogin(getBaseContext());
        
        m_textView   = (TextView)findViewById(R.id.TextView1);
        m_textView.append(" " + m_playerName);
        
        m_listView = (ListView) findViewById(R.id.mylist);
        
        m_sendText   = (EditText)findViewById(R.id.editText1);
        m_chat = new ChatAdapter(this);
        m_listView.setAdapter( m_chat ); 
    	
        this.insertIntoChatLog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    public void closeApp(View v) {
    	ChatOpenHelper.closeDatabase();
    	finish();
    }
    
    private void insertIntoChatLog(){
    	new ChatOpenHelper(this, m_playerName);
    	ArrayList<String> messages = ChatOpenHelper.query(this);
    	for(int i = 0; i < messages.size(); i++){
    		m_chat.add(messages.get(i));
    		m_chat.add("Message Received. Good work!");
    	}
    }
    
    public String setLogin(Bundle savedInstanceState){
    	String login = null;
    	Bundle extras;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
            	login = null;
            } else {
            	login = extras.getString("LOGIN_ID");
            }
        } else {
        	login = (String) savedInstanceState.getSerializable("LOGIN_ID");
        }
        
        return login;
    }
    
    public void sendChat(View v){
    	m_message = m_sendText.getText().toString();
    	m_sendText.setText("");
    	
    	//resets the input manager for the keyboard
    	InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        m_chat.add(m_message);
        m_chat.add("Message Received. Good work!");
        ChatOpenHelper.insertIntoDatabase(getBaseContext(), m_playerName, m_message);
    }
    
}
