
/**
 * This is the player that moves around and plays the game
 * 
 * @author Robert Smithers
 * @version 6/5/17
 */
import java.awt.*;
public class Player
{
    private int GAME_WIDTH;
    private int GAME_HEIGHT;
    private int xStart;
    private int yStart;
    private double ay = -9.8;
    private double vy;
    private double dt = 0.02;//0.012; // time quantum
    private Rectangle playerBody;
    
    //FINAL VARS FOR FUN
    private final int LEFT_SENSITIVITY;
    private final int RIGHT_SENSITIVITY;
    private final int ANTIGRAVITY_AMOUNT;
    
    public String direct = "right";
    
    /**
     * Constructor
     */
    public Player(int xStart, int yStart, int size, int GAME_WIDTH, int GAME_HEIGHT, int RIGHT_SENSITIVITY, int LEFT_SENSITIVITY, int ANTIGRAVITY_AMOUNT)
    {
        this.GAME_WIDTH = GAME_WIDTH;
        this.GAME_HEIGHT = GAME_HEIGHT;
        this.RIGHT_SENSITIVITY = RIGHT_SENSITIVITY;
        this.LEFT_SENSITIVITY = LEFT_SENSITIVITY;
        this.ANTIGRAVITY_AMOUNT = ANTIGRAVITY_AMOUNT;
        playerBody = new Rectangle(xStart, yStart, size, size); 
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void drawPlayer(String direction)
    {
        //more right, more up
        //contact is above and to the
    		StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.square(playerBody.x + playerBody.width, playerBody.y, 24);
        StdDraw.setPenColor(100, 100, 100);
        if (direction.equals("right")) {
        		StdDraw.picture(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, "tankR.png", 48, 27.6); 
        		direct = direction;
        }//Outside Image    playerbody.width is one side (all same length)
        else if (direction.equals("left")) {
        		StdDraw.picture(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, "tankL.png", 48, 27.6);       //Outside Image    playerbody.width is one side (all same length)
        		direct = direction;
        }
        else 
        		throw new RuntimeException("Error: invalid input for direction");
         
//         StdDraw.setPenColor(90, 90, 90);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*9/10);   //yaxis is flipped is StdDraw compared to Rectangle
//         StdDraw.setPenColor(80, 80, 80);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*8/10);
//         StdDraw.setPenColor(70, 70, 70);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*7/10);
//         StdDraw.setPenColor(60, 60, 60);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*6/10);
//         StdDraw.setPenColor(50, 50, 50);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*5/10);  
//         StdDraw.setPenColor(40, 40, 40);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*4/10); 
//         StdDraw.setPenColor(30, 30, 30);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*3/10); 
//         StdDraw.setPenColor(20, 20, 20);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*2/10); 
//         StdDraw.setPenColor(10, 10, 10);
//         StdDraw.filledSquare(playerBody.x + playerBody.width/2, playerBody.y - playerBody.width/2, playerBody.width*1/10); 
        
        //System.out.println("player middle coordinate (StdDraw) = x: "+ playerBody.x + ", y: "+ playerBody.y);
        StdDraw.setPenColor(0, 0, 0);            //Inside square
    }
    
    public void playerInfo() {
        System.out.println("player top left (rect) = x: " + playerBody.x + ", y: "+ playerBody.y + "player top right = x: " + (playerBody.x + playerBody.width));
        /**
         * 1----x:296.58919996768606-----y:38.05955633462662
         * player top left (rect) = x: 277, y: 109
         */
    }
    
    public void movePlayer(String direction, Floors blocks)
    {
        if (direction.equals("up"))
        {
            if (!isInAir(blocks)) {
                vy = ANTIGRAVITY_AMOUNT;
                playerBody.y+=5;
            }
        }
        
        if (direction.equals("right"))
        {
        		drawPlayer(direction);    
        		playerBody.x+= RIGHT_SENSITIVITY;
        }
        
        if (direction.equals("left"))
        {
        		drawPlayer(direction); 
        		playerBody.x-= LEFT_SENSITIVITY;
        }
        
    }
    
    public void fall(Floors blocks)
    {
        if (collisionWithBlock(blocks) != "bottomEdge" && playerBody.y > blocks.getMinY()) {               //As long as the bottom is not touching something
            //Then it falls down
            vy += ay * dt;
            playerBody.y += vy * dt;
            playerBody.y+=vy;
            blocks.drawBlocks();
            drawPlayer(direct);
            StdDraw.show();
        }
    }
    
    public boolean isInAir(Floors blocks)
    {
        if (playerBody.y <= blocks.getMinY()) {
            return false;
        }
        return true;
    }
    
    public boolean collisionAtPoint(Double x, Double y){
        if (playerBody.contains(x,y)) {
            return true;
        }
        return false;
    }
    
    public String collisionWithBlock(Floors blocks){
        double topLeftx = 0.0; double topLefty = 0.0;
        double topRightx = playerBody.width; double topRighty = 0;
        double bottomRightx = playerBody.width; double bottomRighty = playerBody.width;
        double bottomLeftx = 0.0; double bottomLefty = playerBody.width;
        
        if (blocks.playerCollisionAtPoint(topLeftx, topLefty) && blocks.playerCollisionAtPoint(topRightx, topRighty)) {
            return "topEdge";       //the top edge is colliding with the block
        }
        if (blocks.playerCollisionAtPoint(topLeftx, topLefty) && blocks.playerCollisionAtPoint(bottomLeftx, bottomLefty)) {
            return "leftEdge";      //The left edge is colliding with the block
        }
        if (blocks.playerCollisionAtPoint(bottomLeftx, bottomLefty) && blocks.playerCollisionAtPoint(bottomRightx, bottomRighty)) {
            return "bottomEdge";    //The bottom edge is colliding with the blocks
        }
        if (blocks.playerCollisionAtPoint(bottomRightx, bottomRighty) && blocks.playerCollisionAtPoint(topRightx, topRighty)) {
            return "rightEdge";     ////The right edge is colliding with the block
        }
        return null;
    }
}
