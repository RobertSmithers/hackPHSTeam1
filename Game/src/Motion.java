/******************************************************************************
* Draw Ballistic motion + variables
* 3/20/17
* Robert Smithers
******************************************************************************/
import java.awt.Font;
import java.util.*;
import java.awt.Color;

public class Motion {
    private double v;
    private double theta;
    private Color PATH_TEXT_COLOR;
    //All projectile values to be stored for other methods (only used for the DEBUG feature)
    private ArrayList<Double> xVals = new ArrayList<>();
    private ArrayList<Double> yVals = new ArrayList<>();
    private ArrayList<Double> vxVals = new ArrayList<>();
    private ArrayList<Double> vyVals = new ArrayList<>();
    private ArrayList<Double> axVals = new ArrayList<>();
    private ArrayList<Double> ayVals = new ArrayList<>();
    private ArrayList<Double> aVals = new ArrayList<>();
    private ArrayList<Double> vVals = new ArrayList<>();
    private ArrayList<Double> dtVals = new ArrayList<>();
    
    
    //CHANGEABLE VALUES FOR YOUR LIKING, RJ MR. MASTER CODER
    private double LINE_THICKNESS;                           //Change to adjust the thickness of the shooting line
    
    public Motion(double velocity, double angle, Color PATH_TEXT_COLOR, double LINE_THICKNESS) 
    {
        theta = Math.toRadians(angle);
        v = velocity;
        this.PATH_TEXT_COLOR = PATH_TEXT_COLOR;
        this.LINE_THICKNESS = LINE_THICKNESS;
    }
    
    public void drawPathDotted(int GAME_WIDTH, int GAME_HEIGHT, int spawnHeight) {
        double G = 9.8; // gravitational constant m/s^2
        double C = 0.002; // drag force coefficient
      
        
        double x = 0.0, y = 0.0 + spawnHeight; // position
        double vx = v * Math.cos(theta); // velocity in x direction
        double vy = v * Math.sin(theta); // velocity in y direction
        
        double ax = 0.0, ay = 0.0; double a = 0.0;// acceleration
        double dt = 0.02;//0.012; // time quantum
        
        ay = -G - (C * v * vy);
        
        // loop until ball hits ground
        while (y >= 0.0) {
            v = Math.sqrt(vx*vx + vy*vy);
            ax = -C * v * vx;
            a = Math.pow((Math.pow(ax,2)+Math.pow(ay,2)),(double)1/2);
            vx += ax * dt;
            vy += ay * dt;
            x += vx * dt;
            y += vy * dt;
            
            //Store values in arrayList
            xVals.add(x);
            yVals.add(y);
            vxVals.add(vx);
            vyVals.add(vy);
            axVals.add(ax);
            ayVals.add(ay);
            aVals.add(a);
            vVals.add(v);
            dtVals.add(dt);
            
            //Redraw the path
            if ((int) y == 0) {
                for (int i=0;i<xVals.size();i+=20){
                    StdDraw.setPenColor(255,0,0);
                    StdDraw.filledCircle(xVals.get(i), yVals.get(i), LINE_THICKNESS);
                }
                StdDraw.show(5);
            }
        }
        StdDraw.show();
        }
        
    public int fireShot(boolean debugPath, int GAME_HEIGHT, int GAME_WIDTH, int spawnHeight, Player player1, Floors blocks, ArrayList<Motion> missiles, int startCounter, Game game, String background) {
        int ret = 0;     //Return
        
        double t = 0.0; // time
        StdDraw.setPenColor(0,0,0);
        Font font = new Font("Bitstream Vera Sans", Font.PLAIN, 10);
        StdDraw.setFont(font);
        
        // Get current time
        long start = System.currentTimeMillis();
        
        int f = startCounter;
        
        // loop until ball hits ground
        while (yVals.get(f) >= 0.0 && !blocks.missileCollisionAtPoint(xVals.get(f), yVals.get(f)) && !player1.collisionAtPoint(xVals.get(f), yVals.get(f))) {   //the missile y value is greater than 0 and not intersecting with the player or a block.
            if (StdDraw.hasNextKeyTyped()) {            //Checking if the player needs some movement
                if (StdDraw.isKeyPressed(32) || StdDraw.isKeyPressed(87)) {                             //If the user presses "space"
                    player1.movePlayer("up", blocks);
                }
                
                if (StdDraw.isKeyPressed(65)) {                             //If the user presses "a"
                    player1.movePlayer("left", blocks);
                }
                
                if (StdDraw.isKeyPressed(68)) {                             //If the user presses "d"
                    player1.movePlayer("right", blocks);
                }
                
                
            }
            player1.fall(blocks);
            
            //Remove Text and draw blocks, player
            StdDraw.setPenColor(255,255,255);
            StdDraw.clear();
            game.drawBackground(GAME_WIDTH, GAME_HEIGHT, background);
            blocks.drawBlocks();
            player1.drawPlayer(player1.direct);
            
            
            if (debugPath) {
                if (f!=0) {
                    //Remove them old lines
                    StdDraw.setPenColor(255,255,255);
                    StdDraw.line(xVals.get(f-1), yVals.get(f-1), xVals.get(f-1) + vxVals.get(f-1)*2, yVals.get(f-1));     //The vx line
                    StdDraw.line(xVals.get(f-1), yVals.get(f-1), xVals.get(f-1), yVals.get(f-1) + vyVals.get(f-1)*2);     //The vy line
                }
                //Draw v lines
                StdDraw.setPenColor(0,0,255);
                StdDraw.line(xVals.get(f), yVals.get(f), xVals.get(f)+vxVals.get(f)*2,yVals.get(f));     //The vx line
                StdDraw.line(xVals.get(f), yVals.get(f), xVals.get(f),yVals.get(f)+vyVals.get(f)*2);     //The vy line
            }

            for (int value=0; value < missiles.size(); value++) {
                (missiles.get(value)).drawPath(f);
            }
            
            // Get time
            long mark = System.currentTimeMillis();
            t = (mark-start)/1000;
            if (debugPath) {
                //Draw Text
                StdDraw.setPenColor(PATH_TEXT_COLOR);
                StdDraw.text(GAME_WIDTH*2/3,GAME_HEIGHT*9/10, "t = "+(int)t);
                StdDraw.text(GAME_WIDTH*2/3,GAME_HEIGHT*88/100, "dt = "+dtVals.get(f));
                StdDraw.text(xVals.get(f)-GAME_WIDTH/60,yVals.get(f)+GAME_HEIGHT*10/300, "vy = "+(int) (double) vyVals.get(f));
                StdDraw.text(xVals.get(f)-GAME_WIDTH/60,yVals.get(f)+GAME_HEIGHT*10/450, "y = "+(int) (double) yVals.get(f));
                StdDraw.text(xVals.get(f)-GAME_WIDTH/60,yVals.get(f)+GAME_HEIGHT*10/900, "ay = "+(int) (double) ayVals.get(f));
                
                StdDraw.text(xVals.get(f)+GAME_WIDTH/200,yVals.get(f)+GAME_HEIGHT*10/135, "v = "+(int) (double) vVals.get(f));
                StdDraw.text(xVals.get(f)+GAME_WIDTH/200,yVals.get(f)+GAME_HEIGHT*10/150, "a = "+(int) (double) aVals.get(f));
                
                StdDraw.text(xVals.get(f)+GAME_WIDTH/60,yVals.get(f)+GAME_HEIGHT*10/300, "vx = "+(int) (double) vxVals.get(f));;
                StdDraw.text(xVals.get(f)+GAME_WIDTH/60,yVals.get(f)+GAME_HEIGHT*10/450, "x = "+(int) (double) xVals.get(f));
                StdDraw.text(xVals.get(f)+GAME_WIDTH/60,yVals.get(f)+GAME_HEIGHT*10/900, "ax = "+(int) (double) axVals.get(f));
            }
            
            StdDraw.show(10);
            f+=1;
            }
        
        //Concurrent error, just a reminder, no for each with remove method
        for (int i=0; i < missiles.size(); i++){                                    //Remove any missiles that collide
            if (missiles.get(i).yVals.get(f) <= blocks.getMinY() || blocks.missileCollisionAtPoint(missiles.get(i).xVals.get(f), missiles.get(i).yVals.get(f)) || player1.collisionAtPoint(missiles.get(i).xVals.get(f), missiles.get(i).yVals.get(f))) {    
                //missiles.get(i).yVals.clear();           //Removes those values to not take up space   
                //missiles.get(i).xVals.clear();           //Removes those values to not take up space
                if (debugPath) {
                    System.out.println(missiles.size() + "----x:"+missiles.get(i).xVals.get(f)+"-----y:"+missiles.get(i).yVals.get(f));
                    player1.playerInfo();
                }
                
                if (player1.collisionAtPoint(missiles.get(i).xVals.get(f), missiles.get(i).yVals.get(f))) return 2017;
                missiles.remove(missiles.get(i));
                ret = i;
               
               }
            if (missiles.isEmpty()) i = missiles.size();
         }

        if (!missiles.isEmpty()) {
            for (int i2 = 0; i2 < missiles.size(); i2++){                                    //Draw the rest of the missiles
               if (f <= missiles.get(i2).xVals.size()) {    //So the xVals.get(f) -> the f value can be too large if the other missile has values that are too large. This prevents that. We also know that the yVals will be okay because they should be equal to xVals.size() (They are coordinates)
                   if (missiles.get(i2).yVals.get(f) > blocks.getMinY() && !blocks.missileCollisionAtPoint(missiles.get(i2).xVals.get(f), missiles.get(i2).yVals.get(f)) && !player1.collisionAtPoint(missiles.get(i2).xVals.get(f), missiles.get(i2).yVals.get(f))) {
                       //System.out.println("REDRAWING, HEADS UP");
                       missiles.get(i2).fireShot(debugPath, GAME_HEIGHT, GAME_WIDTH, spawnHeight, player1, blocks, missiles, f, game, background);           //This is being run while the for statement 2 above is going
                       i2 = missiles.size();
                    }
                }
            }
        }
        return ret;
    }
    
    public void drawPath(int f) {
        //Redraw the path
        for (int i=0;i<f-1;i++){
            if (i<=255) StdDraw.setPenColor(Math.abs(255-i),0+i,0+i);       //Makes the little gradient from red to turqouise
            else StdDraw.setPenColor(0,255,255);                            //Otherwise it stays turqouise at the max vals
            try {
	            	StdDraw.filledCircle(xVals.get(i), yVals.get(i), LINE_THICKNESS);
	            	StdDraw.setPenColor(0,0,0);
	            	StdDraw.filledCircle(xVals.get(f-1), yVals.get(f-1), LINE_THICKNESS*20/5);
            } catch (IndexOutOfBoundsException e) {
            		continue;
            }
        }
    }
}