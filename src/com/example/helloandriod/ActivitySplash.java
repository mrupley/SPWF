package com.example.helloandriod;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class ActivitySplash extends Activity {
	
	private final int SPLASH_DISPLAY_LENGTH = 3000;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        
        this.doAsyncLoading();
        
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
               	Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
               	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);	
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
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
    
    private void doAsyncLoading(){
    	
    	//FIXME: implement an asyc runnable for this
    	GameTile.populateFriendValues();
    	GameTile.populateTileAmounts();
    }
    
}
