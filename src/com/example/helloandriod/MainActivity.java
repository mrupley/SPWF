package com.example.helloandriod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    FriendAdapter m_friend;
    ListView m_listView;
    public static String versionName = "BETA";
    public static int versionCode = 0;
    
    private String marketURI = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        
        
        this.setupYourMove();
        
    	try {
    	    versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    	    versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
    	} catch (NameNotFoundException e) {
    		
    	}
    	new getJSON().execute("http://pastebin.com/raw.php?i=Bb5GRpDT");
    	this.loadCrosspromoImage();
    	
    	TextView gameVersionLogo = (TextView) findViewById(R.id.gameVersion);
    	gameVersionLogo.setText(getString(R.string.app_name) + " ®, @" + versionName + "(build #" + String.valueOf(versionCode) + "), © 2013 Zynga Inc.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    private void loadCrosspromoImage(){
    	
    }
    
    public void closeApp(View v) {
    	finish();
    }
    
    public void startGame(View v){
    	Intent intent = new Intent(this, ActivityGame.class);
        startActivity(intent);
    }
    
    //logs the user out of the game, prepares for a new login.
    public void logout(View v){

    	SharedPreferences login = getSharedPreferences("Login_ID", Activity.MODE_PRIVATE);
    	
    	//resets the login name
        SharedPreferences.Editor editor = login.edit();
        editor.putString("LOGIN", null);
        editor.commit();
        
        //return to the login screen
    	Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }
    
    //Links to the external game marketplace of the crosspromo game
    public void goToCrosspromoPage(View v){
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse("market://details?id=" + marketURI));
    	startActivity(intent);
    }
    
    //puts your friends on the list of people to play against
    private void setupYourMove(){
    	m_listView = (ListView) findViewById(R.id.mylist);
    	m_friend = new FriendAdapter(this);
    	m_listView.setAdapter( m_friend );
    	
    	//TODO: pull in a list of real friends and make this dynamic
    	m_friend.add("Alex");
    	m_friend.add("Nicolas");
    	m_friend.add("Surith");
    	m_friend.add("Jason");
    }
    
    //need to take this class out of the main class... should be loaded externally
    class RetreiveBitmapTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            try {
    			URL url = new URL(urls[0]);
    	    	Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap bmp) {
        	ImageButton imgAvatar = (ImageButton) findViewById(R.id.crosspromoButton1);
        	imgAvatar.setImageBitmap(bmp);
        }
     }
    
    
    //need to take this class out of the main class... should be loaded during the splash screen or some other idle time
    class getJSON extends AsyncTask<String, Void, String[]> {
    	
    	protected String[] doInBackground(String... urls) {
	    	DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
	    	HttpPost httppost = new HttpPost(urls[0]);
	    	// Depends on your web service
	    	httppost.setHeader("Content-type", "application/json");
	        try {
		    	InputStream inputStream = null;
		    	String result = null;
		    	HttpResponse response = httpclient.execute(httppost);           
		    	HttpEntity entity = response.getEntity();
		
		    	inputStream = entity.getContent();
		    	
		    	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    	StringBuilder sb = new StringBuilder();
		
		    	String line = null;
		    	while ((line = reader.readLine()) != null)
		    	{
		    	    sb.append(line + "\n");
		    	}
		    	result = sb.toString();
		
		        // getting JSON string from url
		    	JSONObject jObject = new JSONObject(result);
		
	
	        	String[] JSON = {jObject.getString("image"), jObject.getString("app")};
	        	return JSON;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return null;
    	}
	        protected void onPostExecute(String[] s) {
	        	marketURI = s[1];
	        	new RetreiveBitmapTask().execute(s[0]);

	        }
    }
}
