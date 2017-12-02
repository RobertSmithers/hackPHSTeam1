/******************************************************************************
* Make the game
* 5/18/17
* Robert Smithers
******************************************************************************/
import java.awt.Font;
import java.util.*;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
public class Game {
    private double v;
    private double theta;
    private boolean play;
    private ArrayList<Motion> missiles;
    public Game() 
    {
        missiles = new ArrayList<Motion>();
        play = true;
    }
    
    public void drawTrajectory(int GAME_WIDTH, int GAME_HEIGHT, Motion path, int spawnHeight)
    {
        path.drawPathDotted(GAME_WIDTH, GAME_HEIGHT, spawnHeight);
    }
    
    public int fireShot(boolean DEBUG, int GAME_WIDTH, int GAME_HEIGHT, Motion path, int spawnHeight, Player player1, Floors blocks, ArrayList<Motion> missiles, int startCounter)
    {
        int val = path.fireShot(DEBUG, GAME_HEIGHT, GAME_WIDTH, spawnHeight, player1, blocks, missiles, startCounter);
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
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(GAME_WIDTH*2/3,GAME_HEIGHT*88/100, "Well, looks like you lost...");
        StdDraw.text(GAME_WIDTH*2/3,GAME_HEIGHT*40/100, "Care to try again?");
        StdDraw.text(GAME_WIDTH*2/3,GAME_HEIGHT*40/100, "Bwahahahaha");
        StdDraw.show();
        play = false;
    }
   
    public static void main(String[] args) {
        //"Don't change pls" variables
        boolean play = true;
        
        //Game variables
        int TOTAL_MISSILES = 2;                                 //Total # of missiles at any given point
        int ANTIGRAVITY_AMOUNT = 6;                              //The more antigravity, the higher the player will bounce
        
        //Path variables
        boolean PATH_DEBUG = false;
        Color PATH_TEXT_COLORSCHEME = new Color(0,0,0);     //The color of the text on a debugged path
        double LINE_THICKNESS = 0.9;
        int SHOT_WAIT_TIME = 1500;             //(In millieconds, time before shot fires after seeing dotted line)
        
        //Window variables
        int GAME_WIDTH = 1500/3;         //First number is the number of pixels that will  be on the screen
        int GAME_HEIGHT = 900/3;
        
        //Player variables
        int size = GAME_HEIGHT/20;
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
 
        //Make the blocks
        Floors blocks = new Floors(GAME_WIDTH, GAME_HEIGHT);
        
        //Make the player
        Player player1 = new Player(GAME_WIDTH*5/8, GAME_HEIGHT/4, size, GAME_WIDTH, GAME_HEIGHT, RIGHT_SENSITIVIY, LEFT_SENSITIVITY, ANTIGRAVITY_AMOUNT);
        
        while (play)                //The game itself
        {
            int shots = 0;
            
            //Clear the screen
            StdDraw.clear();
            
            //Draw Background
            blocks.drawBlocks();
            
            //Draw Player
            player1.drawPlayer();
            
            //Show the screen
            StdDraw.show();
            
            //player1.movePlayer("up", blocks);
            //System.out.println("W was pressed");
            //play=false;
            if (game.missiles.size() < TOTAL_MISSILES) {                    //Fire a missile
                v = (int) (Math.random()*150);                                        //Up to 0 to 150 speed
                a = (int) (Math.random()*60);                                               //From 0 to 60 degrees
                spawnHeight = (int) (Math.random()*GAME_HEIGHT*6/10) + GAME_HEIGHT*3/10;    //Up to 9/10 full screen and down to 3/10 full screen
                
                Motion path = new Motion(v, a, PATH_TEXT_COLORSCHEME, LINE_THICKNESS);
                Motion path2 = new Motion(v+10, a+12, PATH_TEXT_COLORSCHEME, LINE_THICKNESS);
                //Store the missiles in an arrayList so they can be drawn at the same time
                game.missiles.add(path);
                //System.out.println(game.missiles.size());
                game.missiles.add(path2);
                
                //Draw the dotted path
                game.drawTrajectory(GAME_WIDTH, GAME_HEIGHT, path, spawnHeight);
                game.drawTrajectory(GAME_WIDTH, GAME_HEIGHT, path2, spawnHeight);
                
                //Wait for number of seconds before firing on the path
                game.waitForShot(SHOT_WAIT_TIME);
                
                //Fire all of the missiles
                shots = game.fireShot(PATH_DEBUG, GAME_WIDTH, GAME_HEIGHT, path, spawnHeight, player1, blocks, game.missiles, 0);
                //if (shots == 2017) game.gameOver(GAME_WIDTH, GAME_HEIGHT);
            }
            
            //This game over method is not working like it should be. It should go black and print out text and that should be it, but it decides to redraw everything else
            if (shots == 2017) {
                StdDraw.clear(StdDraw.BLACK);
                game.gameOver(GAME_WIDTH, GAME_HEIGHT);
                StdDraw.show();
            }
        }
    }
    
    /**
     * 
     * Idea: Something like the screen moves up, your character has to jump, avoid the incoming shots 
     * 
     * The arcs of the trajectory should be continued, using the preexisting BallisticMotion idea.
     */
}