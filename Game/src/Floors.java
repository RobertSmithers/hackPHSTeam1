
/**
 * This is for creating the floor objects
 * 
 * @author Robert Smithers
 * @version 6/6/17
 */
import java.awt.*;
import java.util.*;
public class Floors
{
    private int GAME_WIDTH;
    private int GAME_HEIGHT;
    private Rectangle floorBlock;// = new Rectangle(GAME_WIDTH/2, GAME_HEIGHT/15, GAME_WIDTH/2, GAME_HEIGHT/15);;
    /**
     * Constructor to create the entire floor
     */
    public Floors(int GAME_WIDTH, int GAME_HEIGHT)
    {
        this.GAME_WIDTH = GAME_WIDTH;     //The scale set by the screen
        this.GAME_HEIGHT = GAME_HEIGHT;
        floorBlock = new Rectangle(0, GAME_HEIGHT/15, GAME_WIDTH, GAME_HEIGHT/15);
    }

    /**
     * This is used to draw the floor blocks in the game
     */
    public void drawBlocks()
    {
        //Because the floorBlock as a Rectangle starts at 0, height/15 with the top left as an anchor point, we need it to draw like it is different.
        StdDraw.setPenColor(0,153,76);
        StdDraw.filledRectangle( (double) floorBlock.x+GAME_WIDTH/2, (double) floorBlock.y, (double) floorBlock.width, (double) floorBlock.height);
        //StdDraw.picture((double) floorBlock.x+GAME_WIDTH/2, (double) floorBlock.y, "ground.png", (double) floorBlock.width, (double) GAME_HEIGHT/5);
    }
    
    public boolean missileCollisionAtPoint(Double x, Double y){
        if (floorBlock.contains(x,y)) {
            return true;
        }
        return false;
    }
    
    public boolean playerCollisionAtPoint(Double x, Double y){
        if (floorBlock.contains(x,y)) {
            return true;
        }
        return false;
    }
    
    public double getMinY(){
        return floorBlock.height*2+floorBlock.y;
    }
}
