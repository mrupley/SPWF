package com.example.helloandriod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class MainGamePanel extends SurfaceView{
	
	

public static final float SCALE_MAXIMUM_FACTOR = 0.375f; //this is used to scale the maximum image size... saving tons of memory by creating a smaller bitmap for the board.
public static float SCALE_MINIMUM_FACTOR = SCALE_MAXIMUM_FACTOR;
public static final float SCALE_TRUE_FACTOR = 1.0f;
private ScaleGestureDetector mScaleDetector;
public static float mScaleFactor = SCALE_MINIMUM_FACTOR;
private float mscaleDifference = 1.0f;

public static float mPosX;
public static float mPosY;

private float xMin = 0, yMin = 0;

private int gameWidth = 1;
private int gameHeight = 1;

private GameThread gameThread;  // For our thread needed to do logical processing without holding up the UI thread
private SurfaceHolder holder; // For our CallBacks.. (One of the areas I don't understand!)

GestureDetector gestureDetector;
 
public MainGamePanel(Context context, AttributeSet attrs) {
 
	super( context, attrs );
	mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	gameThread = new GameThread(this); //Create the GameThread instance for our logical processing
	holder = getHolder();
	new GameBoard(context);
	gestureDetector = new GestureDetector(context, new GestureListener());

	holder.addCallback(new SurfaceHolder.Callback() {

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);   

        while (retry) { 
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
            int width, int height) {
        // The code to resize the screen ratio when it flips from landscape to portrait and vice versa

    }
});
}
 
public MainGamePanel(Context context, AttributeSet attrs, int defStyle) {
 
super( context, attrs, defStyle );
}

@Override
public boolean onTouchEvent(MotionEvent ev) {
	
    // Let the ScaleGestureDetector inspect all events.
    mScaleDetector.onTouchEvent(ev);
		   
	return gestureDetector.onTouchEvent(ev);
}

@Override
protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
{
    super.onSizeChanged(xNew, yNew, xOld, yOld);

	gameWidth = this.getWidth();
	gameHeight = this.getHeight();

	SCALE_MINIMUM_FACTOR = (float)(gameWidth / (float)(GameBoard.tileWidth * GameBoard.GAME_BOARD_WIDTH));
}

private class GestureListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onDown(MotionEvent ev) {
        return true;
    }
    
    @Override
    public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){

        if (!mScaleDetector.isInProgress()) {
            mPosX -= distanceX;
            mPosY -= distanceY;
            this.validateXYPosition();
            GameBoard.updateBoardTiles();
            invalidate();
        }
    	return true;
    }
    
    //sets the board within the boundaries.
    private void validateXYPosition(){
    	xMin = (gameWidth -(gameWidth * mscaleDifference));
    	yMin = (gameHeight -(gameHeight * mscaleDifference));
    	
        if(mPosX > 0){
        	mPosX = 0;
        }
        if(mPosX < xMin){
        	mPosX = xMin;
        }
        if(mPosY > 0){
        	mPosY = 0;
        }
        if(mPosY < yMin){
        	mPosY = yMin;
        }
    }
    
    // event when double tap occurs
    @Override
    public boolean onDoubleTap(MotionEvent ev) {
    	if (!mScaleDetector.isInProgress()) {
	    	//Log.d("DEBUG", "scale: "+ mScaleFactor + " X: "+mPosX + " " + ev.getX() +" Y: "+mPosY + " " + ev.getY());

	    	

	    	mPosX -= ((ev.getX() * (SCALE_TRUE_FACTOR / mScaleFactor)) - (gameWidth/2));
	    	mPosY -= ((ev.getY() * (SCALE_TRUE_FACTOR / mScaleFactor)) - (gameHeight/2));
	    	
	    	mScaleFactor = SCALE_TRUE_FACTOR;
	    	mscaleDifference = mScaleFactor / SCALE_MINIMUM_FACTOR;
    	}

        return true;
    }
}

@Override
public void draw(Canvas canvas) {
    canvas.drawColor(Color.parseColor("#a7bccb") );
    canvas.save();
    //Log.d("DEBUG", "X: "+mPosX+" Y: "+mPosY);
    //Log.d("DEBUG", "scale: "+ mScaleFactor);
    canvas.translate(mPosX, mPosY);
    canvas.scale(mScaleFactor, mScaleFactor);
    Paint myPaint = new Paint();
    GameBoard.drawBoard(canvas, myPaint);
    }

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleFactor *= detector.getScaleFactor();

        // Don't let the object get too small or too large.
        mScaleFactor = Math.max(SCALE_MINIMUM_FACTOR, Math.min(mScaleFactor, SCALE_TRUE_FACTOR));
        
		mscaleDifference = mScaleFactor / SCALE_MINIMUM_FACTOR;
		GameBoard.updateBoardTiles();
        invalidate();
        return true;
    }
}

}
