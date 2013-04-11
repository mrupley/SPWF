package com.example.helloandriod;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.RelativeLayout;

public class GameBoard {
	protected static Bitmap tileBlank, tileBlankLetter, tileCenter,
			tileDoubleLetter, tileDoubleWord, tileTripleLetter, tileTripleWord;

	public static int tileWidth, tileHeight;

	// Constants designated for all tiles
	protected static final int TILE_BLANK = 0;
	protected static final int TILE_CENTER = 27;
	protected static final int TILE_DOUBLE_LETTER = 28;
	protected static final int TILE_DOUBLE_WORD = 29;
	protected static final int TILE_TRIPLE_LETTER = 30;
	protected static final int TILE_TRIPLE_WORD = 31;

	public static final int GAME_BOARD_WIDTH = 15;
	public static final int GAME_BOARD_HEIGHT = 15;

	protected static final int DEFAULT_SPACING = 3;
	protected static final int DEFAULT_SPACING_HALF = (GAME_BOARD_HEIGHT - 1) / 2;

	protected static int[][] gameBoard;
	
	public static int gameBoardTop = 0;

	protected int m_xOrientation = 0;
	protected int m_yOrientation = 0;

	protected static Bitmap gameBoardBitmap;

	public GameBoard(Context context) {

		this.loadGameBoardImages(context);
		this.resizeImages();
		tileWidth = tileBlank.getWidth();
		tileHeight = tileBlank.getHeight();

		this.createBoard();
	}

	public static int getWidth() {
		if (gameBoardBitmap != null) {
			return gameBoardBitmap.getWidth();
		}
		return 0;
	}

	public static int getHeight() {
		if (gameBoardBitmap != null) {
			return gameBoardBitmap.getHeight();
		}
		return 0;
	}

	private void loadGameBoardImages(Context context) {
		tileBlank = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tile_blank);
		tileBlankLetter = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tile_blank_letter);
		tileCenter = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tile_center);
		tileDoubleLetter = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tile_double_letter);
		tileDoubleWord = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tile_double_word);
		tileTripleLetter = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tile_triple_letter);
		tileTripleWord = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tile_triple_word);
	}

	private void resizeImages() {
		tileBlank = getResizedBitmap(tileBlank, (int)(tileBlank.getWidth()* MainGamePanel.SCALE_MAXIMUM_FACTOR),
				(int)(tileBlank.getHeight()* MainGamePanel.SCALE_MAXIMUM_FACTOR));
		tileBlankLetter = getResizedBitmap(tileBlankLetter,
				(int)(tileBlankLetter.getWidth()* MainGamePanel.SCALE_MAXIMUM_FACTOR), (int)(tileBlankLetter.getHeight()* MainGamePanel.SCALE_MAXIMUM_FACTOR));
		tileCenter = getResizedBitmap(tileCenter, (int)(tileCenter.getWidth()* MainGamePanel.SCALE_MAXIMUM_FACTOR),
				(int)(tileCenter.getHeight()* MainGamePanel.SCALE_MAXIMUM_FACTOR));
		tileDoubleLetter = getResizedBitmap(tileDoubleLetter, (int)(tileDoubleLetter.getWidth()* MainGamePanel.SCALE_MAXIMUM_FACTOR),
				(int)(tileDoubleLetter.getHeight()* MainGamePanel.SCALE_MAXIMUM_FACTOR));
		tileDoubleWord = getResizedBitmap(tileDoubleWord, (int)(tileDoubleWord.getWidth()* MainGamePanel.SCALE_MAXIMUM_FACTOR),
				(int)(tileDoubleWord.getHeight()* MainGamePanel.SCALE_MAXIMUM_FACTOR));
		tileTripleLetter = getResizedBitmap(tileTripleLetter, (int)(tileTripleLetter.getWidth()* MainGamePanel.SCALE_MAXIMUM_FACTOR),
				(int)(tileTripleLetter.getHeight()* MainGamePanel.SCALE_MAXIMUM_FACTOR));
		tileTripleWord = getResizedBitmap(tileTripleWord, (int)(tileTripleWord.getWidth()* MainGamePanel.SCALE_MAXIMUM_FACTOR),
				(int)(tileTripleWord.getHeight()* MainGamePanel.SCALE_MAXIMUM_FACTOR));
	}

	public static void drawBoard(Canvas canvas, Paint myPaint) {
		if (gameBoardBitmap == null) {
			updateGameBoardBitmap(myPaint);
		}

		canvas.drawBitmap(gameBoardBitmap, 0, 0, myPaint);
	}

	private static void drawTileLetterAndValue(Canvas canvas, Paint myPaint,
			int width, int height, int letter) {
		myPaint.setColor(Color.BLACK);
		myPaint.setTextSize(128);

		canvas.drawText(String.valueOf(Character.toChars(letter + 64)),
				(width * tileWidth) + (tileWidth / 2) - 64,
				(height * tileHeight) + (tileHeight / 2) + 64, myPaint);
		myPaint.setTextSize(64);
		canvas.drawText(GameTile.friendValues.get(String.valueOf(Character
				.toChars(letter + 64))), (width * tileWidth) + (tileWidth / 2)
				+ 64, (height * tileHeight) + (tileHeight / 2) - 64, myPaint);
	}

	protected static Bitmap getTile(int x, int y) {
		switch (gameBoard[x][y]) {
		case TILE_BLANK:
			return tileBlank;
		case TILE_CENTER:
			return tileCenter;
		case TILE_DOUBLE_LETTER:
			return tileDoubleLetter;
		case TILE_DOUBLE_WORD:
			return tileDoubleWord;
		case TILE_TRIPLE_LETTER:
			return tileTripleLetter;
		case TILE_TRIPLE_WORD:
			return tileTripleWord;
		default: // it's a tile letter, return the letter value
			return tileBlankLetter;
		}
	}

	private void createBoard() {

		// TODO: get game board if it has been set up

		// Create a new board otherwise
		gameBoard = new int[GAME_BOARD_WIDTH][GAME_BOARD_HEIGHT];

		for (m_xOrientation = 0; m_xOrientation <= 1; m_xOrientation++) {
			for (m_yOrientation = 0; m_yOrientation <= 1; m_yOrientation++) {
				this.addTripleLetters();
				this.addDoubleWords();
				this.addDoubleLetters();
				this.addTripleWords();
			}
		}

		this.addCenterTile();
	}

	private static void updateGameBoardBitmap(Paint myPaint) {
		Canvas mCanvas;
		gameBoardBitmap = Bitmap
				.createBitmap(
						(int) (tileWidth * GAME_BOARD_WIDTH),
						(int) (tileHeight * GAME_BOARD_HEIGHT),
						Bitmap.Config.RGB_565);
		mCanvas = new Canvas(gameBoardBitmap);

		for (int i = 0; i < GAME_BOARD_WIDTH; i++) {
			for (int j = 0; j < GAME_BOARD_HEIGHT; j++) {
				mCanvas.drawBitmap(getTile(i, j), i * tileWidth, j * tileHeight, myPaint);
				// if the tile is in the range for a letter, draw the letter
				if (gameBoard[i][j] > TILE_BLANK
						&& gameBoard[i][j] < TILE_CENTER) {
					drawTileLetterAndValue(mCanvas, myPaint, i, j,
							gameBoard[i][j]);
				}
			}
		}
	}

	protected void addCenterTile() {
		gameBoard[GAME_BOARD_WIDTH / 2][GAME_BOARD_HEIGHT / 2] = TILE_CENTER;
	}

	protected void addTripleLetters() {
		// Diagonals
		populateSlot(2, 2, TILE_TRIPLE_LETTER);
		populateSlot(4, 4, TILE_TRIPLE_LETTER);

		populateSlot(1, 7, TILE_TRIPLE_LETTER);
	}

	protected void addDoubleWords() {
		populateSlot(4, 0, TILE_DOUBLE_WORD);

		populateSlot(6, 2, TILE_DOUBLE_WORD);
	}

	protected void addTripleWords() {
		populateSlot(7, 4, TILE_TRIPLE_WORD);
	}

	protected void addDoubleLetters() {
		populateSlot(3, 1, TILE_DOUBLE_LETTER);

		populateSlot(5, 3, TILE_DOUBLE_LETTER);

		populateSlot(6, 5, TILE_DOUBLE_LETTER);

	}

	private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	/*
	 * populates the slot for both positive negative offsets
	 * 
	 * @param xOffset the x offset from the center of the board
	 * 
	 * @param yOffset the y offset from the center of the board
	 * 
	 * @param tile the name of the tile to populate the board with.
	 */
	protected void populateSlot(int xOffset, int yOffset, int tile) {
		gameBoard[DEFAULT_SPACING_HALF - xOffset
				+ (m_xOrientation * (xOffset * 2))][DEFAULT_SPACING_HALF
				- yOffset + (m_yOrientation * (yOffset * 2))] = tile;
		gameBoard[DEFAULT_SPACING_HALF - yOffset
				+ (m_xOrientation * (yOffset * 2))][DEFAULT_SPACING_HALF
				- xOffset + (m_yOrientation * (xOffset * 2))] = tile;
	}
	
	public static void updateBoardTiles(){
        RelativeLayout.LayoutParams boardParams;
        float[] tileXY;
        for(int i = 0; i < GameTile.currentTiles; i++ ){
        	if(GameTile.tileImages[i] != null && GameTile.tileImages[i].getTag() != null){
        		tileXY = findTileLocation((int[])GameTile.tileImages[i].getTag());
        		boardParams = (RelativeLayout.LayoutParams) GameTile.tileImages[i].getLayoutParams();
        		boardParams.height = (int)(GameBoard.tileHeight * MainGamePanel.mScaleFactor);
        		boardParams.width = (int)(GameBoard.tileWidth * MainGamePanel.mScaleFactor);
        		boardParams.leftMargin = (int)tileXY[0];
        		boardParams.topMargin = (int)tileXY[1];
            	GameTile.tileImages[i].setLayoutParams(boardParams);
        	}
        }
	}
	
	//this returns the x and y coordinates of the tile corresponding to the absolute value on the board.
	public static int[] getTileXY(int X, int Y){
		//coordinates on the grid
		int[] xy = new int[2];
		
		//absolute x y
		float trueX = X - MainGamePanel.mPosX;
		float trueY = Y - MainGamePanel.mPosY - gameBoardTop;
		xy[0] = (int) ((trueX / GameBoard.tileWidth) / MainGamePanel.mScaleFactor);
		xy[1] = (int) ((trueY / GameBoard.tileHeight) / MainGamePanel.mScaleFactor);
		return xy;
	}
	
	//finds the absolute coordinates for the tile based on the x and y points on the grid
	private static float[] findTileLocation(int[] coords){
		float[] xy = new float[2];
		xy[0] = (GameBoard.tileWidth * MainGamePanel.mScaleFactor * coords[0]) + MainGamePanel.mPosX;
		xy[1] = (GameBoard.tileHeight * MainGamePanel.mScaleFactor * (coords[1])) + MainGamePanel.mPosY + gameBoardTop;
		return xy;
	}
}
