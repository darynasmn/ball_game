import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class BreakOut extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 3;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 1;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1)* BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static int NTURNS = 3;
	
    /** Speed of the moving bricks*/
	private static final int BRICK1_SPEED =3;
    private static final int BRICK2_SPEED =5;
    
    /** Height of hearts*/
    private static final int HEART_HEIGHT = 5;
    
    /** Amount of the bricks*/
	private static int NBRICKS = 5;
	
	/** Set variables for ball's speed*/
	double vx, vy=+3.0;
	
	/** To set score*/
	private int score =0;

	/** Method: run() */
	/** Runs the BreakOut program. */
	public void run() {
		setScreen();
		while(!gameOver){
	        ballMoved();
	        moveBrick1();
	        moveBrick2();
	        remove(scoreLabel);
	        setScore();
	        pause(7);
	        result();
	      }
	}
	/** A method that draws all the initial elements of the game */
	private void setScreen() {
		background();
		setPaddle();
		setBall();
		setBricks();
		addBricks();
		hearts();
		angle();
		setScore();
		addMouseListeners();
	}
	/** Method shows the result of the game*/
	private void result(){
		if(NBRICKS==0){
			gameOver = true;
			add(background);
			SoundClip winSound = new SoundClip("win.wav");
			winSound.setVolume(1);
			winSound.play();
			add( new GImage ("You_win.png"));
		}
		if(NTURNS==0){
			gameOver = true;
			add(background);
			SoundClip loseSound = new SoundClip("lose.wav");
			loseSound.setVolume(1);
			loseSound.play();
			add( new GImage ("You_lose.png"));
		}
	}
	/** Method adds the images of the hearts*/
	private void hearts(){
		heart1 = new GImage("heart.png",5,HEART_HEIGHT);
	    heart2 = new GImage("heart.png",heart1.getX()+26+5,HEART_HEIGHT);
	    heart3 = new GImage("heart.png",heart2.getX()+26+5,HEART_HEIGHT);
	    heart1.scale(0.035);
	    heart2.scale(0.035);
	    heart3.scale(0.035);
	    add(heart1);
	    add(heart2);
	    add(heart3);
	}
	/** Method checks the collision of ball with bottom.If it is true, the method removes the right heart*/ 
    private void collisionForHeart(){
	    if(NTURNS==3){
	      remove(heart3);
	      add(heart3);
	      pause(3);
	      remove(heart3);
	    }
	    if(NTURNS==2){
	      remove(heart2);
	      add(heart2);
	      pause(3);
	      remove(heart2);
	    }
	    if(NTURNS==1){
	      remove(heart1);
	      add(heart1);
	      pause(3);
	      remove(heart1);
	    }
    }
    /** Definer of which object ball collide with */
	private GObject getCollidingObject() {
		GObject collObj1 = getElementAt(ball.getX(), ball.getY());
		if (collObj1 != null)
			return collObj1;
		GObject collObj2 = getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
		if (collObj2 != null) 
			return collObj2;
		GObject collObj3 = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
		if (collObj3 != null) 
			return collObj3;
		GObject collObj4 = getElementAt(ball.getX()+ BALL_RADIUS, ball.getY() +  BALL_RADIUS);
		if (collObj4 != null)
			return collObj4;
	      return null;
	}
	/** Method checks the collision of ball with bricks*/
	private void checkForCollision() {
		if (ball != null) {
			GObject collider = getCollidingObject();
			if (collider != null && collider !=background && collider !=paddle && collider != scoreLabel && collider != heart1 && collider !=heart2 && collider!=heart3){
				SoundClip ball = new SoundClip("ball.wav");				
				ball.setVolume(1);
				ball.play();
				remove(collider);
				NBRICKS--;
				score += 10;
				vy = -vy;
			}
		}
	}
	/** Method shows the score of the game*/
	private void setScore(){
		scoreLabel = new GLabel("Score:" + score);
		scoreLabel.setFont("Arial-20");
		scoreLabel.setColor(Color.WHITE);
		add(scoreLabel,WIDTH-scoreLabel.getWidth(), 25);
	}
	/** Method checks the collision of ball with paddle*/
	private void paddleColision(){
        if(ball!=null){
          GObject obj = getElementAt((double)ball.getX()+BALL_RADIUS/2,(double)ball.getY()+BALL_RADIUS+1); 
          if (obj.equals(paddle))
        	  vy = -vy;
         }
      }
	/** Method checks the collision of ball with walls*/
	private void checkWallCollision(){
		if(ball != null){
			if(ball.getX()<0) 
				vx=-vx;
			if (ball.getX()+BALL_RADIUS>WIDTH)
				vx=-vx;
			if(ball.getY()<0)
				vy=-vy;
			if(ball.getY()+BALL_RADIUS>HEIGHT && NTURNS!=0){
				remove(ball);
				ball= null;
				SoundClip heart = new SoundClip("heart.wav");
			    heart.setVolume(1);
			    heart.play();
				NTURNS--;
				setBall();
				pause(300);
				
			}
		}
	}
    /** Method adds the moving bricks*/
	private void addBricks(){
	    moveBrick1 = new GRect(rgen.nextDouble(1,APPLICATION_WIDTH-1.3*BRICK_WIDTH),245,1.3*BRICK_WIDTH,1.5*BRICK_HEIGHT);  
	    moveBrick1.setFilled(true);
	    moveBrick1.setColor(Color.PINK);
	    add(moveBrick1);
	  
	    moveBrick2 = new GRect(rgen.nextDouble(1,APPLICATION_WIDTH-1.3*BRICK_WIDTH),320,1.3*BRICK_WIDTH,1.5*BRICK_HEIGHT);  
	    moveBrick2.setFilled(true);
	    moveBrick2.setColor(Color.WHITE);
	    add(moveBrick2);
   }
	/** Method moves the first moving brick*/
	private void moveBrick1(){
	   if(brickToLeft1){
	    	moveBrick1.move(-BRICK1_SPEED,0);
	    	if(moveBrick1.getX()<=0)
	    		brickToLeft1=false;
	    }else{
	    	moveBrick1.move(BRICK1_SPEED, 0);
	    	if(moveBrick1.getX()+50>=WIDTH)
	    		brickToLeft1 =true;
	   	}
    }
	/** Method moves the second moving brick*/
	private void moveBrick2(){
		if(brickToLeft2){
			moveBrick2.move(-BRICK2_SPEED,0);
			if(moveBrick2.getX()<=0)
				brickToLeft2=false;
	    }else{
	    	moveBrick2.move(BRICK2_SPEED, 0);
	    	if(moveBrick2.getX()+50>=WIDTH)
	    		brickToLeft2 =true;
	    }
	 }
	/** Method chooses the angle for ball*/
	private void angle(){
        vx = rgen.nextDouble(1.0, 3.0); 
        if (rgen.nextBoolean(0.5))	vx = -vx;
	}
	/** Method moves the ball*/
	private void ballMoved(){
        if(clicked==true)
          if (ball != null) {
            ball.move(vx, vy);
            collisionForAll();
     }
	}
	/** Method checks all collisions*/
      private void collisionForAll(){
        checkWallCollision();
        paddleColision();
        collisionForHeart();
        checkForCollision();
      }
    /** Method adds bricks*/
	private void setBricks() {
		for (int k = 0; k < NBRICKS_PER_ROW; k++)
			for (int j = 0; j < NBRICK_ROWS; j++) {
				double X = WIDTH / 2 - BRICK_SEP * 2- (BRICK_WIDTH * NBRICK_ROWS) / 2 - BRICK_SEP * (NBRICK_ROWS / 2 - 1);
				brick = new GRect(X + j * (BRICK_WIDTH + NBRICK_ROWS / 2), BRICK_Y_OFFSET	+ k * (BRICK_HEIGHT + BRICK_SEP), BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				if (k == 0 || k == 1) 
					brick.setColor(Color.RED);
				if (k == 2 || k == 3)
					brick.setColor(Color.ORANGE);
				if (k == 4 || k == 5)
					brick.setColor(Color.YELLOW);
				if (k == 6 || k == 7)
					brick.setColor(Color.GREEN);
				if (k == 8 || k == 9) 
					brick.setColor(Color.CYAN);
				add(brick);
		}
	}
	/** Method adds a ball*/
	private void setBall() {
		ball = new GOval(WIDTH / 2 - BALL_RADIUS / 2, HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS-2-200, BALL_RADIUS,BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.MAGENTA);
		add(ball);
	}
    /** to add mouseclecker*/
	public void mouseClicked(MouseEvent e) {
		clicked = true;
	}
    /** When you click on the mouse, a ball starts falling*/
	public void mouseMoved(MouseEvent e) {
		if (clicked == true)
		if (e.getX() < WIDTH - PADDLE_WIDTH)
		    paddle.setLocation(e.getX(), HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	}
    /** To add a paddle*/
	private void setPaddle() {
		paddle = new GRect((WIDTH - PADDLE_WIDTH) / 2, HEIGHT - PADDLE_Y_OFFSET	- PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.WHITE);
		add(paddle);
	}
    /** To add a background*/
	private void background() {
		this.setSize(WIDTH, HEIGHT);
		background = new GRect(0, 0, WIDTH, HEIGHT);
		background.setFilled(true);
		background.setColor(Color.BLACK);
		add(background);
	}
	/** private instance variables */ 
	private GRect paddle;
	private GOval ball;
	private GRect brick;
	private GRect background;
	private GLabel scoreLabel;
	private GRect moveBrick1;
    private GRect moveBrick2;
    private GImage heart1;
    private GImage heart2;
    private GImage heart3;
    private boolean brickToLeft1;
    private boolean brickToLeft2;
    private boolean clicked = false;
	private boolean gameOver = false;
	private RandomGenerator rgen = RandomGenerator.getInstance();
}
