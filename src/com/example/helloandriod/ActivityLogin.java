package com.example.helloandriod;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class ActivityLogin extends Activity {

	EditText m_loginText;
	SharedPreferences login;
	String loginID;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        
        m_loginText   = (EditText)findViewById(R.id.editText1);
        
        login = getSharedPreferences("Login_ID", Activity.MODE_PRIVATE);
        loginID = login.getString("LOGIN", null);
        if(loginID != null){
        	Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    public void closeApp(View v) {
    	finish();
    }
    
    public static String getLogin(Context context){
    	String loginID = null;
        SharedPreferences login = context.getSharedPreferences("Login_ID", Activity.MODE_PRIVATE);
        loginID = login.getString("LOGIN", null);
        
        return loginID;
    }
    
    
    public void loginToMain(View v){
    	
    	//stores the login name to shared preferences
        SharedPreferences.Editor editor = login.edit();
        editor.putString("LOGIN", m_loginText.getText().toString());
        editor.commit();
        
        
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    
}
