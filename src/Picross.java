import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Picross extends GraphicsProgram {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int APPLICATION_WIDTH = 320;
	public int APPLICATION_HEIGHT = 240;
	
	int BOARD_WIDTH  = 10;
	int BOARD_HEIGHT = 10;
	
	int[][] level = { 	{0,0,0,0,0,0,0,0,0,0},
						{1,1,1,1,1,1,1,0,0,0},
						{0,1,1,0,0,0,1,1,1,0},
						{0,1,1,0,0,1,1,1,0,0},
						{0,1,1,1,1,1,1,0,0,0},
						{0,1,1,0,0,0,1,1,1,0},
						{0,1,1,0,0,0,0,1,1,1},
						{0,1,1,0,0,0,1,1,1,0},
						{1,1,1,1,1,1,1,1,0,0},
						{0,0,0,0,0,0,0,0,0,0} };
	
	int[][] colNums = {	{2,1,1}, 
						{1,8}, 
						{1,8}, 
						{3,1,1,1}, 
						{3,1,1,1},
						{3,1,2,1}, 
						{2,5,2}, 
						{2,2,4}, 
						{2,1,3}, 
						{1,1} };
	
	String[] rowNums 	= { "0", "7", "23", "23",   "6",  "23", "23", "23",  "8", "0" };
	
	GRect[][] tiles 	= new GRect[BOARD_WIDTH][BOARD_HEIGHT]; 
	
	GLabel[] rowLabels 	= new GLabel[10];
	GCompound[] colLabels 	= new GCompound[10];
	
	Color TILE_BORDER_COLOR = Color.DARK_GRAY;
	Color TILE_SELECTED_BORDER_COLOR = Color.RED;
	
	Color TILE_FILL_COLOR_FULL 		= Color.BLUE;
	Color TILE_FILL_COLOR_EMPTY 	= Color.WHITE;
	Color TILE_FILL_COLOR_UNTRIED 	= Color.GRAY;
	Color LABEL_COLOR_DEFAULT 		= Color.BLACK;
	Color LABEL_COLOR_ACTIVE		= Color.white;
	Color GAME_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	
	int TILE_BLOCK_SIZE = 10;
	int TILE_PADDING = 2;
	
	int TOP_NUMBER_HEIGHT = 100;
	int SIDE_NUMBER_WIDTH = 100;
	
	int GAME_WIDTH  = BOARD_WIDTH  * (TILE_BLOCK_SIZE + TILE_PADDING) + SIDE_NUMBER_WIDTH;
	int GAME_HEIGHT = BOARD_HEIGHT * (TILE_BLOCK_SIZE + TILE_PADDING) + TOP_NUMBER_HEIGHT;
	int GAME_MARGIN = 20;
	
	
	int cursorRow = 0;
	int cursorCol = 0;
	
	// Main Method to make this a real Java Application
	public static void main(String[] args) {
		new Picross().start(args);
	}
	
	public void run() {
		
		initGameScreen();
		
		add(new GLabel("Picross PiGrrl Edition", 20, 20 ) );
		
		addKeyListeners();
		
		drawLevel();
	    moveCursor(0, 0);
	    
		while (true) {
			// Game Running Loop
		}
	      
	}

	
	public void initGameScreen(){
		//this.resize(GAME_WIDTH + GAME_MARGIN, GAME_HEIGHT + GAME_MARGIN);
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		setBackground(GAME_BACKGROUND_COLOR);
	}
	
	
	// Handle keyboard input
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				moveCursor( 0, 1);
				break;
			case KeyEvent.VK_LEFT:
				moveCursor( 0,-1);
				break;
			case KeyEvent.VK_UP:
				moveCursor(-1, 0);
				break;
			case KeyEvent.VK_DOWN:
				moveCursor( 1, 0);
				break;
			case KeyEvent.VK_SPACE:
				if(e.isShiftDown())
					clearTile();
				else
					markTile();
				break;
			default:
				break;
		}
	}
	
	
	public void clearTile(){
		if (level[cursorRow][cursorCol] == 0) {
			tiles[cursorRow][cursorCol].setFillColor(TILE_FILL_COLOR_EMPTY);
		}
	}
	
	
	public void markTile(){
		if (level[cursorRow][cursorCol] == 1) {
			tiles[cursorRow][cursorCol].setFillColor(TILE_FILL_COLOR_FULL);
		}
	}
	
	
	public void moveCursor(int rowChange, int colChange) {

		// Check that move isn't out of bounds!
		int newRow = cursorRow + rowChange;
		int newCol = cursorCol + colChange;
		
		if ( (newRow >= BOARD_WIDTH) | newRow < 0 | newCol >= BOARD_HEIGHT | newCol < 0) {
			// Shit don't work! Break outta here!
			return;
		}
		
		rowLabels[cursorRow].setColor(LABEL_COLOR_DEFAULT);
		colLabels[cursorCol].setColor(LABEL_COLOR_DEFAULT);
		tiles[cursorRow][cursorCol].setColor(TILE_BORDER_COLOR);
		
			cursorRow = newRow;
			cursorCol = newCol;
		
		tiles[cursorRow][cursorCol].setColor(TILE_SELECTED_BORDER_COLOR);
		rowLabels[cursorRow].setColor(LABEL_COLOR_ACTIVE);
		colLabels[cursorCol].setColor(LABEL_COLOR_ACTIVE);
		
	}
	
	
	// drawLevel() uses the local global level[][] variable and draws 
	// the completed board
	public void drawLevel() {
		for (int row = 0; row < BOARD_WIDTH; row++) {
			for (int col = 0; col < BOARD_HEIGHT; col++) {
				tiles[row][col] = makeTile(level[row][col]);
				tiles[row][col].setLocation( tileLocation(row, col) );
				add(tiles[row][col]);
				
				if (row==0) {
					colLabels[col] = makeColLabel(col);
					add(colLabels[col]);
				}
				
			}
			rowLabels[row] = makeRowLabel(row);
			add(rowLabels[row]);
		}
	}
	
	
	public GLabel makeRowLabel(int row){
		GLabel l = new GLabel(rowNums[row]);
		
		double y = TOP_NUMBER_HEIGHT + (TILE_BLOCK_SIZE + TILE_PADDING)*(row+1) + l.getY();
		double x = SIDE_NUMBER_WIDTH - l.getWidth();
		
		l.setLocation(x, y);
		l.setColor(LABEL_COLOR_DEFAULT);
		
		return l;
	}

	
	public GCompound makeColLabel(int col) {
		GCompound l = new GCompound();
		
		for (int i = 1; i <= colNums[col][0]; i++) {
			GLabel tl = new GLabel(String.valueOf(colNums[col][i]) );
			
			l.add(tl, 0, (i) * tl.getHeight() );
			
		}
				
		double y = TOP_NUMBER_HEIGHT - l.getHeight();
		double x = SIDE_NUMBER_WIDTH + (TILE_BLOCK_SIZE + TILE_PADDING)*(col);
		
		l.setLocation(x, y);
		l.setColor(LABEL_COLOR_DEFAULT);
		
		return l;
	}
	
	
	public GPoint tileLocation(int row, int col){
		GPoint tp = new GPoint();
		
		double windowX = col * (TILE_BLOCK_SIZE + TILE_PADDING) + SIDE_NUMBER_WIDTH;
		double windowY = row * (TILE_BLOCK_SIZE + TILE_PADDING) + TOP_NUMBER_HEIGHT;
		
		tp.setLocation(windowX, windowY);
		
		return tp;
	}
	
	
	
	public GRect makeTile(int isFilled){
		GRect myTile = new GRect(0,0,TILE_BLOCK_SIZE, TILE_BLOCK_SIZE);
			myTile.setColor(TILE_BORDER_COLOR);
			myTile.setFillColor(TILE_FILL_COLOR_UNTRIED);
			myTile.setFilled(true);
		return myTile;
	}
	
	
}
