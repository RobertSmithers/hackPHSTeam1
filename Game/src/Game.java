/******************************************************************************
* Make the game
* 12/2/17
* Robert Smithers, Deven Roychowdhury, Arymon ioanfolanof
******************************************************************************/
import java.util.*;
import java.awt.Color;
import java.awt.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import crypto.CryptoException;
import crypto.CryptoUtils;


public class Game {

    final private File highscore = new File("highScore.txt");
    private PrintWriter writer;
    final private String key = "b2Hs0AkwpVme@duW";			//Used for the AES

    private ArrayList<Motion> missiles;
    public static final Font font = new Font("Arial", 1, 40);

    private static int round = 0;
    
    public Game()
    {
        missiles = new ArrayList<Motion>();
        try {

        	//Makes highScore.txt if they don't yet have it
        	if(!highscore.exists() && !highscore.isDirectory()) {
        		writer = new PrintWriter(highscore);			//Creates a printwriter with a blank file, only creating the username and password rows
        		writer.printf("%8s%25s\n", "Name", "Highscore", "");
        		writer.close();

        		//Encrypts the file after modification
        		try {
        			CryptoUtils.encrypt(key, highscore, highscore);
        		} catch (CryptoException ex) {
        			System.out.println(ex.getMessage());
        			ex.printStackTrace();
        		}
        	}
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
    }
    
    public void drawBackground(int GAME_WIDTH, int GAME_HEIGHT, String background) {
    		StdDraw.picture(GAME_WIDTH/2, GAME_HEIGHT/2, background, 300, 180);
    }
    
    public void drawScore(int GAME_WIDTH, int GAME_HEIGHT) {
    		StdDraw.setFont(font);
		StdDraw.text(GAME_WIDTH/2, GAME_HEIGHT*.9, Integer.toString(round));
    }
    
    public boolean drawTrajectory(int GAME_WIDTH, int GAME_HEIGHT, Motion path, int spawnHeight)
    {
        return path.drawPathDotted(GAME_WIDTH, GAME_HEIGHT, spawnHeight);
    }
    
    public int fireShot(boolean DEBUG, int GAME_WIDTH, int GAME_HEIGHT, Motion path, int spawnHeight, Player player1, Floors blocks, ArrayList<Motion> missiles, int startCounter, Game game, String background)
    {
        int val = path.fireShot(DEBUG, GAME_HEIGHT, GAME_WIDTH, spawnHeight, player1, blocks, missiles, startCounter, game, background);
        return val;
    }
    
    public void waitForShot(int SHOT_WAIT_TIME) {
        try        
        {
            Thread.sleep(SHOT_WAIT_TIME);
        } 
        
        catch(InterruptedException ex) 
        {
            Thread.currentThread().interrupt();
        }
    }
    
    public void gameOver(int GAME_WIDTH, int GAME_HEIGHT){
        
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.picture(GAME_WIDTH/2, GAME_HEIGHT/2, "gameOver.jpg", 100, 100);
        StdDraw.show();
    }
   
    public static void main(String[] args) {
        //"Don't change pls" variables
        boolean play = true;
        
        //Images and colors
        String background = "sky.jpg";
        
        //Game variables
        int TOTAL_MISSILES = 2;                                 //Total # of missiles at any given point
        int ANTIGRAVITY_AMOUNT = 4;                              //The more antigravity, the higher the player will bounce
        
        //Path variables
        boolean PATH_DEBUG = false;
        Color PATH_TEXT_COLORSCHEME = new Color(0,0,0);     //The color of the text on a debugged path
        double LINE_THICKNESS = 0.9;
        int SHOT_WAIT_TIME = 1500;             //(In millieconds, time before shot fires after seeing dotted line)
        
        //Window variables
        int GAME_WIDTH = 1440/5;         //First number is the number of pixels that will  be on the screen
        int GAME_HEIGHT = 900/5;
        
        //Player variables
        int size = GAME_HEIGHT/17;
        int RIGHT_SENSITIVIY = 3;       //Speed of player right movement
        int LEFT_SENSITIVITY = 3;       //Speed of player left movement
        
        StdDraw.setCanvasSize(GAME_WIDTH*3, GAME_HEIGHT*3);
        StdDraw.setXscale(0, GAME_WIDTH);
        StdDraw.setYscale(0, GAME_HEIGHT);
        Game game = new Game();
        
        //Spawn in a path for a shot
        int v;                          //Will be random
        int a;                          //Will be random
        int spawnHeight;                //Will be random
        int max = 150;
        
        //Make the blocks
        Floors blocks = new Floors(GAME_WIDTH, GAME_HEIGHT);
        
        //Make the player
        Player player1 = new Player(GAME_WIDTH*5/8, GAME_HEIGHT/4, size, GAME_WIDTH, GAME_HEIGHT, RIGHT_SENSITIVIY, LEFT_SENSITIVITY, ANTIGRAVITY_AMOUNT);
        
        int shots = 0;
        while (play)                //The game itself
        {
            shots = 0;
            
            //Clear the screen
            StdDraw.clear();
            
            //Draw background
            game.drawBackground(GAME_WIDTH, GAME_HEIGHT, background);
            
            //Draw Blocks
            blocks.drawBlocks();
            
            //Draw Player
            player1.drawPlayer(player1.direct);
            
            //Show the screen
            StdDraw.show();

            //play=false;
            if (game.missiles.size() < TOTAL_MISSILES) {                    //Fire a missile
            		game.missiles.clear();
            		round++;
            		if (round % 3 == 0) max += 30;
            		if (round % 2 == 0) {
            			SHOT_WAIT_TIME -= 75;
            			if (SHOT_WAIT_TIME <= 0) {
            				SHOT_WAIT_TIME = 50;
            			}
            		}
            		game.drawScore(GAME_WIDTH, GAME_HEIGHT);
                v = (int) (Math.random()*max);                                        //Up to 0 to 150 speed
                a = (int) (Math.random()*60);                                               //From 0 to 60 degrees
                spawnHeight = (int) (Math.random()*GAME_HEIGHT*6/10) + GAME_HEIGHT*3/10;    //Up to 9/10 full screen and down to 3/10 full screen
                
                Motion path = new Motion(v, a, PATH_TEXT_COLORSCHEME, LINE_THICKNESS);
                Motion path2 = new Motion(v+10, a+12, PATH_TEXT_COLORSCHEME, LINE_THICKNESS);
                //Store the missiles in an arrayList so they can be drawn at the same time
                
                //Draw the dotted path
                while ((!game.drawTrajectory(GAME_WIDTH, GAME_HEIGHT, path, spawnHeight)) || (!game.drawTrajectory(GAME_WIDTH, GAME_HEIGHT, path2, spawnHeight))) {
                		System.out.println("DO");
                		v = (int) (Math.random()*150);                                        //Up to 0 to 150 speed
                    a = (int) (Math.random()*60);                                               //From 0 to 60 degrees
                    spawnHeight = (int) (Math.random()*GAME_HEIGHT*6/10) + GAME_HEIGHT*3/10;    //Up to 9/10 full screen and down to 3/10 full screen
                    
                    path = new Motion(v, a, PATH_TEXT_COLORSCHEME, LINE_THICKNESS);
                    path2 = new Motion(v+10, a+12, PATH_TEXT_COLORSCHEME, LINE_THICKNESS);
                    //Store the missiles in an arrayList so they can be drawn at the same time
                }
         
                game.missiles.add(path);
                //System.out.println(game.missiles.size());
                game.missiles.add(path2);
                
                //Wait for number of seconds before firing on the path
                game.waitForShot(SHOT_WAIT_TIME);
                
                //Fire all of the missiles
                try {
	                shots = game.fireShot(PATH_DEBUG, GAME_WIDTH, GAME_HEIGHT, path, spawnHeight, player1, blocks, game.missiles, 0, game, background);
	                if (shots == 2017) {
	                		game.gameOver(GAME_WIDTH, GAME_HEIGHT);
	                		play = false;
	                	}
                } catch (IndexOutOfBoundsException e){
                		game.missiles.clear();
                }
            }
        }
    }
}