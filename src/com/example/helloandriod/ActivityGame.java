package com.example.helloandriod;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityGame extends Activity implements Shaker.Callback{
	

    private int _xDelta;
    private int _yDelta;
    
    protected int m_windowWidth, m_windowHeight;
    
    protected boolean tilesCreated = false;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//TODO: change if less than 7 tiles
    	GameTile.currentTiles = 7;
    	GameTile.tileImages = new ImageButton[GameTile.currentTiles+1]; 
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        
        
        new Shaker(getBaseContext(), 1.25d, 3000, this);
        
        ImageView tileBox = (ImageView)findViewById(R.id.imageRack);
        
        final ViewTreeObserver obs = tileBox.getViewTreeObserver();
        obs.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw () {
            	if(!tilesCreated){
                	createTileButtons(GameTile.currentTiles);
                	tilesCreated = true;
            	}

                return true;
           }
        });
        

        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    public void closeApp(View v) {
    	finish();
    }
    
    public void createTileButtons(int numberTiles){
    	DisplayMetrics displaymetrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    	m_windowHeight = displaymetrics.heightPixels;
    	m_windowWidth = displaymetrics.widthPixels;
    	
    	for(int i = 1; i <= numberTiles; i++){
    		
    		switch(i){
    		case 1:
    			GameTile.tileImages[i] = (ImageButton)findViewById(R.id.tileButton1);
    	    	break;
    		case 2:
    			GameTile.tileImages[i] = (ImageButton)findViewById(R.id.tileButton2);
    	    	break;
    		case 3:
    			GameTile.tileImages[i] = (ImageButton)findViewById(R.id.tileButton3);
    	    	break;
    		case 4:
    			GameTile.tileImages[i] = (ImageButton)findViewById(R.id.tileButton4);
    	    	break;
    		case 5:
    			GameTile.tileImages[i] = (ImageButton)findViewById(R.id.tileButton5);
    	    	break;
    		case 6:
    			GameTile.tileImages[i] = (ImageButton)findViewById(R.id.tileButton6);
    	    	break;
    		case 7:
    			GameTile.tileImages[i] = (ImageButton)findViewById(R.id.tileButton7);
    	    	break;
    		}
    		LayoutParams params = new LayoutParams(
    		        200,      
    		        200
    		);

    		ImageView tileBox = (ImageView)findViewById(R.id.imageRack);
        	
    		params.leftMargin = ((m_windowWidth / 7) * (i-1));
        	params.topMargin = tileBox.getTop() + 10;
    		params.height = (m_windowWidth / 7);
    		params.width = (m_windowWidth / 7);
    		params.alignWithParent = true;
    		GameTile.tileImages[i].setLayoutParams(params);
    		GameTile.tileImages[i].setId(i);
    		GameTile.tileImages[i].setOnTouchListener(new Button.OnTouchListener(){
	    	    @Override
	    	    public boolean onTouch(View v, MotionEvent event) {
	    	        final int X = (int) event.getRawX();
	    	        final int Y = (int) event.getRawY();
	    	        switch (event.getAction() & MotionEvent.ACTION_MASK) {
	    	            case MotionEvent.ACTION_DOWN:
	    	                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
	    	                _xDelta = X - lParams.leftMargin;
	    	                _yDelta = Y - lParams.topMargin;
	    	                
	    	                lParams.height = (int)((m_windowWidth / 7) * 1.2);
	    	                lParams.width = (int)((m_windowWidth / 7) * 1.2);
	    	                break;
	    	            case MotionEvent.ACTION_UP:
	    	            	//put the tile on the board or back where it was
	    	            	ImageView tileBox = (ImageView)findViewById(R.id.imageRack);
	    	            	
	    	            	//sets the tile on the board
	    	            	if(Y > (int)findViewById(R.id.chatButton).getHeight() && Y < tileBox.getTop()){
		    	            	setOnBoard(v.getId(), X, Y);
	    	            	} else if(Y >= tileBox.getTop() && Y <= tileBox.getBottom()){ //swap tiles with one another
	    	            		for(int i = 1; i <= GameTile.currentTiles; i++){
	    	            			if((X >= ((m_windowWidth / 7) * (i-1))) && (X <= (m_windowWidth / 7) * i)){
	    	            				int currentTileId = v.getId();
	    	            				swapTiles(v.getId(), i);
	    	            		        
	    	            				setTile(currentTileId, false);
	    	            				setTile(i, true); //the current tile after the swap
	    	            			}
	    	            		}
	    	            	} else{ //return tile to position
	    	            		setTile(v.getId(), false);
	    	            	}

	    	            	//sets tile back to the rack.
	    	            	//setTile(v.getId());
	    	            	
	    	                break;
	    	            case MotionEvent.ACTION_POINTER_DOWN:
	    	                break;
	    	            case MotionEvent.ACTION_POINTER_UP:
	    	                break;
	    	            case MotionEvent.ACTION_MOVE:
	    	                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
	    	                layoutParams.leftMargin = X - _xDelta;
	    	                layoutParams.topMargin = Y - _yDelta;
	    	                layoutParams.rightMargin = -250;
	    	                layoutParams.bottomMargin = -250;
	    	                v.setLayoutParams(layoutParams);
	    	                break;
	    	        }
	    	        
	    	        return true;
	    	    }
	    	});
    	}
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	return true;
    }
    
    public void goToChat(View v){
    	Intent intent = new Intent(this, ActivityChat.class);
        startActivity(intent);
        finish();
    }

    public void recallTiles(View v){
    	this.recallTiles();
    }


	@Override
	public void shakingStarted() {
		shuffleTiles();
	}


	@Override
	public void shakingStopped() {
		
	}
	
	private void shuffleTiles(){
	    int rand; 

	    Random r = new Random();

	    for(int i=GameTile.currentTiles;i>0;i--) 
	    { 
	        rand= r.nextInt(i) + 1;

	        swapTiles(i, rand);
	        
	    }
	    
	    for(int i = 1; i <= GameTile.currentTiles; i++){
	    	setTile(i, true);
	    }
	}
	
	private void setTile(int index, boolean recall){
		if(GameTile.tileImages[index].getTag() == null || recall){
			GameTile.tileImages[index].setId(index);
			GameTile.tileImages[index].setTag(null);
	    	RelativeLayout.LayoutParams upParams = (RelativeLayout.LayoutParams) GameTile.tileImages[index].getLayoutParams();
	    	upParams.leftMargin = ((m_windowWidth / 7) * (GameTile.tileImages[index].getId()-1));
	    	ImageView tileBox = (ImageView)findViewById(R.id.imageRack);
	    	upParams.topMargin = tileBox.getTop() + 10;
	    	upParams.height = (m_windowWidth / 7);
	    	upParams.width = (m_windowWidth / 7);
	    	GameTile.tileImages[index].setLayoutParams(upParams);
		}
	}
	
	private void setOnBoard(int index, int X, int Y){
		RelativeLayout.LayoutParams boardParams = (RelativeLayout.LayoutParams) GameTile.tileImages[index].getLayoutParams();
		Button chatButton = (Button)findViewById(R.id.chatButton);
		chatButton.getBottom();
		boardParams.height = (int)(GameBoard.tileHeight * MainGamePanel.mScaleFactor);
		boardParams.width = (int)(GameBoard.tileWidth * MainGamePanel.mScaleFactor);
		boardParams.leftMargin = (int)(X-MainGamePanel.mPosX);
		boardParams.topMargin = (int)(Y-MainGamePanel.mPosY);
    	GameTile.tileImages[index].setTag(1);
    	GameTile.tileImages[index].setLayoutParams(boardParams);
	}
	
	private void swapTiles(int currentTile, int newTile){
	    ImageButton tempButton;
		tempButton = GameTile.tileImages[currentTile];
		
        GameTile.tileImages[currentTile] = GameTile.tileImages[newTile];
        GameTile.tileImages[currentTile].setId(currentTile);
        
        if(GameTile.tileImages[currentTile].getTag() != null){
            
        }

        
        GameTile.tileImages[newTile] = tempButton;
        GameTile.tileImages[newTile].setId(newTile);
        
        if(GameTile.tileImages[newTile].getTag() != null){
        	
        }
	}
	
	private void recallTiles(){
		for(int i = 1; i <= GameTile.currentTiles; i ++){
			setTile(i, true);
		}
	}
    
}
