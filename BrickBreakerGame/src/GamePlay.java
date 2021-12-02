import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JPanel;

public class GamePlay extends JPanel implements KeyListener , ActionListener{

	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;//this to change the number of bricks
	
	private Timer timer;
	private int delay = 10; //this to change the delay betweent the ball and the paddle
	
	private int playerX = 310;
	
	
	Random number = new Random();
	private int ballposX = number.nextInt(120) + 1;//120;
	private int ballposY = number.nextInt(350) + 1;//350;
	
	//to change the speed of the ball
	private int ballXdirection= -3;
	private int ballYdirection = -6;
	
	private BrickGenerator brick;
	
	
	public GamePlay() {
		brick = new BrickGenerator(3,7); //this
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//show score in the Frame
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(" "+score, 590, 30);
		//BrickGenerator
		brick.draw((Graphics2D)g);
		
		//the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		//message for when we finish our bricks
		if(totalBricks <=0) {
			play = false;
			ballXdirection = 0;
			ballYdirection = 0;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won ", 260,300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
			
		}
		
		
		//Message for gameOver
		if(ballposY > 570) {
			play = false;
			ballXdirection = 0;
			ballYdirection = 0;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Scores : "+score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
			
			
		}
		
		g.dispose();
		
	
	}
	
	//will call the method paint(Graphics g) and redraw again 
	// in order to see change of the paddle (moveRight, moveLeft)
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		//when we play the arrow keys the ball should move inside the borders.
		if(play) {
			//with this condition we create a rectangle around the ball 
			//so we can detect the intersection between 2 different objects.
			//( intersection between the ball and the paddle)
			if(new Rectangle(ballposX, ballposY,20,20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdirection = - ballYdirection;
			}
			
			//intersection between the ball and the bricks
			A:for(int i =0 ; i<brick.map.length; i++) {
				for(int j = 0; j<brick.map[0].length; j++) {
					if(brick.map[i][j]>0) {//detect the intersection
						int brickX = j * brick.brickWidth +80;
						int brickY = i * brick.brickHeight+50;
						int brickWidth = brick.brickWidth;
						int brickHeight = brick.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY,20,20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							brick.setBrickValue(0, i, j);
							totalBricks--;
							score +=5;
							
							//for the left and the right intersection
							if(ballposX + 19 <= brickRect.x || ballposX +1 >= brickRect.x +brickRect.width) {
								ballXdirection = - ballXdirection;
							}
							else {
								ballYdirection = - ballYdirection;
							}
							
							break A;
						}
					}
				}
			}
			
			ballposX +=ballXdirection;
			ballposY += ballYdirection;
			if(ballposX <0) {
				ballXdirection = - ballXdirection;
			}
			if(ballposY <0) {
				ballYdirection = - ballYdirection;
			}
			if(ballposX >670) {
				ballXdirection = - ballXdirection;
			}
		}
		
		repaint();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	//to detect arrow keys
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				//it doesn't go outside the panel
				if(playerX >= 600) {
					playerX = 600;
				}
				else {
					moveRight();
				}
			}
	        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
	        	if(playerX <= 10) {
					playerX = 10;
				}
				else {
					moveLeft();
				}
			}
	        
	        //press enter - restart the frame
	        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	        	if(!play) {
	        		play = true;
	        		ballposX = number.nextInt(120) + 1;
	        		ballposY = number.nextInt(350) + 1;
	        		ballXdirection = -3;
	        		ballYdirection = -6;
	        		playerX = 310;
	        		score = 0;
	        		totalBricks =21;
	        		brick = new BrickGenerator(3,7); //this to change the number of bricks
	        		
	        	}
	        }
			
		}
		
		

		private void moveLeft() {
			play = true;
			playerX -= 30; //move to the left side
			
		}

		private void moveRight() {
			play = true;
			playerX +=30; //move to the right side
			
		}


}
