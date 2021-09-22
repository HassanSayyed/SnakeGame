import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.Stack;

import javax.swing.JPanel;



public class GamePanel extends JPanel implements ActionListener {
	

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH* SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 85;
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	//intial snake length
	int bodyParts = 5;
	
	int lettersEaten =0;
	String currentLetter ;
	//coordinate of letter
	int letterX;
	int letterY;
	
	char direction = 'R';
	
	boolean running = false;
	
	Timer timer;
	Random random;
	
	Stack<String> letterStack ;
	
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.blue);
		this.setFocusable(true);
		
		this.addKeyListener(new MyKeyAdapter());
		
		
		
		startGame();
	}
	
	
	public void startGame() {

		letterStack = new Stack<String>();
		letterStack.push("null");// un reachable element "game won"
		letterStack.push("d");
		letterStack.push("e");
		letterStack.push("y");
		letterStack.push("a");
		letterStack.push("S");
		letterStack.push("n");
		letterStack.push("a");
		letterStack.push("s");
		letterStack.push("a");
		letterStack.push("H");
		
		
		
		newLetter();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		draw(g);
	}
	

	
	public void draw(Graphics g) {
		
		
		if (running & !letterStack.isEmpty()) {
		//draw V & H lines to show grid 
	//	for(int i=0; i< SCREEN_HEIGHT/UNIT_SIZE ; i++) {
	//		g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
	//		g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH	, i*UNIT_SIZE);
	//	}
		
		
		
		
		//draw the letter
    	g.setFont(new Font("TimesRoman", Font.BOLD, 27));
    	g.setColor(Color.white);
		g.drawString(currentLetter, letterX , letterY +25 );
		
		
		//draw the snake
		for (int i=0; i< bodyParts;i++) {
			if(i==0) {
				//head of snake
				g.setColor(Color.black);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}else {
				g.setColor(Color.white);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			
		g.setColor(Color.gray);	
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: "+lettersEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+lettersEaten))/2, g.getFont().getSize() +10);	

		} 
		}
		else if (letterStack.isEmpty()) {
			gameWon(g);
		
		}else {
			gameOver(g);
		}
	
	}
	
	public void newLetter() {
		currentLetter = letterStack.pop();
		
		letterX = random.nextInt((int) (SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		letterY = random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
		
	}
	
	
	public void move() {
		for (int i= bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		
		switch (direction) {
		case 'U': y[0] = y[0] - UNIT_SIZE; break;
		case 'D' :y[0] = y[0] + UNIT_SIZE; break; 
		case 'R' :x[0] = x[0] + UNIT_SIZE; break; 
		case 'L' :x[0] = x[0] - UNIT_SIZE; break; 

		default:
			throw new IllegalArgumentException("Unexpected value Wrong direction: " + direction);
		}
		
	}
	
	public void checkLetter() {
		if ( (x[0] == letterX && y[0] == letterY)  ) {
			bodyParts++;
			lettersEaten++;
			
			//re-generate new letter 
			newLetter();
			
			
		}
		
	}
	
	public void checkCollision() {
		//check for collision with it's body
		for (int i = bodyParts;i>0;i--) {
			if(x[0]==x[i] && y[0]==y[i]) {
				running = false;
			}
		}
		//check for snake's head-border collision
		if (x[0]<0 || x[0]>SCREEN_WIDTH || y[0]<0 || y[0]>SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver (Graphics g) {
		
		g.setColor(Color.black);
		g.setFont(new Font("Ink Free",Font.BOLD,80));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		g.drawString("Score: "+lettersEaten, (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2 + 70, SCREEN_HEIGHT/2 + 90);

	}
	
public void gameWon (Graphics g) {
		
		g.setColor(Color.black);
		g.setFont(new Font("Ink Free",Font.BOLD,60));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Completed", (SCREEN_WIDTH - metrics.stringWidth("Game Completed"))/2 , SCREEN_HEIGHT/2);
		g.drawString("Guess my Name?", (SCREEN_WIDTH - metrics.stringWidth("Game Completed"))/2 , SCREEN_HEIGHT/2 + 100);
		

	}
	
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT: 
				if (direction != 'R') direction = 'L'; break;
			case KeyEvent.VK_RIGHT: 
				if (direction != 'L') direction = 'R'; break;	
			case KeyEvent.VK_UP: 
				if (direction != 'D') direction = 'U'; break;
			case KeyEvent.VK_DOWN: 
				if (direction != 'U') direction = 'D'; break;
			
			default:
				System.out.print("Wrong Input ==> "+  e.getKeyCode());
			}
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (running) {
			move();
			checkLetter();
			checkCollision();
		}
		
		repaint();
		
	}

}
