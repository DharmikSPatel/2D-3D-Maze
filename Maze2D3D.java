import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Maze2D3D extends JPanel implements KeyListener{
	JFrame frame;

	/*
	Extra #1
	Gradient Painting in 3D mode

	Extra #2
	-Must answer all the "questions" at the question
	 walls correctly to open the brick doors near the exit.
	-If you answer wrong, then the hero is transported
	 back to the start.
	-Once the hero is transported back to the start,
	 you must complete the question again.
	-Question blocks can only exist in left walls.
	-Only works in 2D mode

	Extra #3
	-Must collect the key to open the chest. The chest
	 opens one of the brick doors near the exit.
	-keys can only exist in vertical hallways,
	 longer than 3 block
	-chests can only exist at the end of horizontal hallways,
	-Only works in 2D mode
	*/

	Hero hero;
	//2D
	Color background = new Color(57,88,107);
	Color heroFill = new Color(249,215,128);
	Color heroOut = new Color(239,248,225);
	Color wallFill = new Color(37,50,66);
	Color wallOut = new Color(239,248,225);
	int glow = 2;
	int size2d = 20; //maze is 25

	//3D
	int size3d = 50;
	int fov = 5; //max is 5
	ArrayList<Wall> walls = new ArrayList<Wall>();
	int startR = 250;
	int startG = 250;
	int startB = 255;
	int dir = 1;
	char[][] maze;
	String screen = "2d";

	//Extras
	Image bigCloseChest;
	String bigCloseChestPath = "bigCloseChest.png";
	Image bigOpenChest;
	String bigOpenChestPath = "bigOpenChest.png";
	Image key;
	String keyPath = "key.png";
	Image keyFade;
	String keyFadePath = "keyfade.png";
	Image closeChest;
	String closeChestPath = "closeChest.png";
	Image openChest;
	String openChestPath = "openChest.png";
	Image q1;
	String q1Path = "q1.png";
	Image q2;
	String q2Path = "q2.png";
	Image brick;
	String brickPath = "brick.png";
	Image q1Brick;
	String q1BrickPath = "q1Brick.png";
	Image q2Brick;
	String q2BrickPath = "q2Brick.png";
	int textWidth;



	public Maze2D3D(){            //col, row
		hero = new Hero(new Location(size2d,size2d*2), size2d, dir, heroFill, heroOut);
		setBoard();
		frame = new JFrame("A-Mazing Program");
		frame.add(this);
		frame.setSize(1200,900);
		frame.addKeyListener(this); //need this for keyboard
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //close line
		try{
			bigCloseChest = ImageIO.read(new File(bigCloseChestPath));
			bigCloseChest = bigCloseChest.getScaledInstance(500, -1, Image.SCALE_FAST);
			bigOpenChest = ImageIO.read(new File(bigOpenChestPath));
			bigOpenChest = bigOpenChest.getScaledInstance(500, -1, Image.SCALE_FAST);
			key = ImageIO.read(new File(keyPath));
			key = key.getScaledInstance(size2d, -1,Image.SCALE_FAST);
			keyFade = ImageIO.read(new File(keyFadePath));
			keyFade = keyFade.getScaledInstance(size2d, -1,Image.SCALE_FAST);
			closeChest = ImageIO.read(new File(closeChestPath));
			closeChest = closeChest.getScaledInstance(size2d, -1,Image.SCALE_FAST);
			openChest = ImageIO.read(new File(openChestPath));
			openChest = openChest.getScaledInstance(size2d, -1,Image.SCALE_FAST);
			q1 = ImageIO.read(new File(q1Path));
			q1 = q1.getScaledInstance(500, -1, Image.SCALE_FAST);
			q2 = ImageIO.read(new File(q2Path));
			q2 = q2.getScaledInstance(500, -1, Image.SCALE_FAST);
			brick = ImageIO.read(new File(brickPath));
			brick = brick.getScaledInstance(size2d, -1, Image.SCALE_FAST);
			q1Brick = ImageIO.read(new File(q1BrickPath));
			q1Brick = q1Brick.getScaledInstance(size2d, -1, Image.SCALE_FAST);
			q2Brick = ImageIO.read(new File(q2BrickPath));
			q2Brick = q2Brick.getScaledInstance(size2d, -1, Image.SCALE_FAST);
		}
		catch(Exception e){
			System.out.println("Image Load Error");
		}

		frame.setVisible(true);



	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(background);
		g2.fillRect(0,0,frame.getWidth(), frame.getHeight());
		g2.setFont(new Font("Arial", Font.BOLD, 24));
		if(screen.equals("2d")){
			paint2DMaze(g2);
			paint2DHero(g2);
			g2.setColor(Color.WHITE);
			textWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 24)).stringWidth(hero.getState());
			g2.drawString(hero.getState(), ((maze[0].length+2)*size2d/2)-textWidth/2, ((maze.length+3)*size2d));

			g2.drawString("Moves: "+hero.getMoves(), size2d-1, ((maze.length+3)*size2d));

			textWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 24)).stringWidth("Keys:    ");
			g2.drawString("Keys:    ", (((maze[0].length+1)*size2d)-textWidth), ((maze.length+3)*size2d));


			if(hero.getq1Enabled() == true){
				g2.drawImage(q1, ((maze[0].length+2)*size2d/2)-(q1.getWidth(null)/2), ((maze.length+3)*size2d), this);
			}
			if(hero.getq2Enabled() == true){
				g2.drawImage(q2, ((maze[0].length+2)*size2d/2)-(q1.getWidth(null)/2), ((maze.length+3)*size2d), this);
			}
			if(hero.getKeyFound()){
				g2.drawImage(key, ((maze[0].length)*size2d), (int)(((maze.length+2.1)*size2d)), this);
			}
			else{
				g2.drawImage(keyFade, ((maze[0].length)*size2d), (int)(((maze.length+2.1)*size2d)), this);
			}

			if(hero.getOnChestBlcok() && hero.getChestOpened()){
				g2.drawImage(bigOpenChest, ((maze[0].length+2)*size2d/2)-(q1.getWidth(null)/2), ((maze.length+3)*size2d), this);
			}
			else if(hero.getOnChestBlcok() && !hero.getChestOpened()){
				g2.drawImage(bigCloseChest, ((maze[0].length+2)*size2d/2)-(q1.getWidth(null)/2), ((maze.length+3)*size2d), this);
			}

		}
		else{
			paint3DMaze(g2);
			g2.setColor(Color.WHITE);
			textWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 24)).stringWidth("Moves: "+hero.getMoves());
			g2.drawString("Moves: "+hero.getMoves(), (size3d*16/2)-(textWidth/2), size3d*15);
			if(hero.getState().equals("Winner! Winner! Chicken Dinner") || hero.getState().equals("Sorry, can't move there")){
				textWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 24)).stringWidth(hero.getState());
				g2.drawString(hero.getState(), (size3d*16/2)-(textWidth/2), size3d*16);
			}
		}
	}
	public void keyPressed(KeyEvent e){ //down then USETHIS
		if(!hero.getWin()){
			maze = hero.move(e.getKeyCode(), maze);
		}
		if(e.getKeyCode() == 32 && screen.equals("2d")){
			screen = "3d";
		}
		else if(e.getKeyCode() == 32 && screen.equals("3d")){
			screen = "2d";
		}

		if(e.getKeyCode() == 49 && (hero.getq1Enabled() || hero.getq2Enabled())){
			maze = hero.correctAnswer(maze);
		}
		else if(e.getKeyCode() == 50 && (hero.getq1Enabled() || hero.getq2Enabled())){
			maze = hero.wrongAnswer(maze);
		}

		repaint();
	}
	public void keyReleased(KeyEvent e){
	}
	public void keyTyped(KeyEvent e){ //down and up then
	}
	public Wall getLeftWall(int f){
		int[] rows = {100+(size3d*f), 150+(size3d*f), 650-(size3d*f), 700-(size3d*f)};
		int[] cols = {100+(size3d*f), 150+(size3d*f), 150+(size3d*f), 100+(size3d*f)};
		return new Wall(rows, cols, "left", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}
	public Wall getRightWall(int f){
		int[] rows = {100+(size3d*f), 150+(size3d*f), 650-(size3d*f), 700-(size3d*f)};
		int[] cols = {700-(size3d*f), 650-(size3d*f), 650-(size3d*f), 700-(size3d*f)};
		return new Wall(rows, cols, "right", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}
	public Wall getTopWall(int f){
		int[] rows = {100+(size3d*f), 100+(size3d*f), 150+(size3d*f), 150+(size3d*f)};
		int[] cols = {100+(size3d*f), 700-(size3d*f), 650-(size3d*f), 150+(size3d*f)};
		return new Wall(rows, cols, "top", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}
	public Wall getBottomWall(int f){
		int[] rows = {700-(size3d*f), 700-(size3d*f), 650-(size3d*f), 650-(size3d*f)};
		int[] cols = {100+(size3d*f), 700-(size3d*f), 650-(size3d*f), 150+(size3d*f)};
		return new Wall(rows, cols, "bottom", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}
	public Wall getFrontWall(int f){
		int[] rows = {100+(size3d*f), 100+(size3d*f), 700-(size3d*f), 700-(size3d*f)};
		int[] cols = {100+(size3d*f), 700-(size3d*f), 700-(size3d*f), 100+(size3d*f)};
		return new Wall(rows, cols, "front", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}


	public Wall getFrontRight(int f){
		int[] rows = {150+(size3d*f), 150+(size3d*f), 650-(size3d*f), 650-(size3d*f)};
		int[] cols = {650-(size3d*f), 700-(size3d*f), 700-(size3d*f), 650-(size3d*f)};
		return new Wall(rows, cols, "frontR", startR-size3d*(f+1), startG-size3d*(f+1), startB-size3d*(f+1), size3d);
	}
	public Wall getFrontLeft(int f){
		int[] rows = {150+(size3d*f), 150+(size3d*f), 650-(size3d*f), 650-(size3d*f)};
		int[] cols = {100+(size3d*f), 150+(size3d*f), 150+(size3d*f), 100+(size3d*f)};
		return new Wall(rows, cols, "frontL", startR-size3d*(f+1), startG-size3d*(f+1), startB-size3d*(f+1), size3d);
	}


	public Wall getRightTop(int f){
		int[] rows = {150+(size3d*f), 100+(size3d*f), 150+(size3d*f)};
		int[] cols = {650-(size3d*f), 700-(size3d*f), 700-(size3d*f)};
		return new Wall(rows, cols, "topRight", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}
	public Wall getLeftTop(int f){
		int[] rows = {100+(size3d*f), 150+(size3d*f), 150+(size3d*f)};
		int[] cols = {100+(size3d*f), 150+(size3d*f), 100+(size3d*f)};
		return new Wall(rows, cols, "topLeft", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}
	public Wall getRightBot(int f){
		int[] rows = {650-(size3d*f), 650-(size3d*f), 700-(size3d*f)};
		int[] cols = {650-(size3d*f), 700-(size3d*f), 700-(size3d*f)};
		return new Wall(rows, cols, "botRight", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}
	public Wall getLeftBot(int f){
		int[] rows = {650-(size3d*f), 650-(size3d*f), 700-(size3d*f)};
		int[] cols = {100+(size3d*f), 150+(size3d*f), 100+(size3d*f)};
		return new Wall(rows, cols, "botLeft", startR-size3d*f, startG-size3d*f, startB-size3d*f, size3d);
	}


	public static void main(String[]args){
		MazeProject app = new MazeProject();
	}
	public void paint2DHero(Graphics2D g2){
		g2.setColor(hero.getFillColor());
		g2.fill(hero.getRect());
		g2.setColor(hero.getOutColor());
		g2.draw(hero.getRect());
	}
	public void paint2DMaze(Graphics2D g2){
		int tempX = size2d;
		int tempY = size2d;
		for(int i = 0; i < maze.length; i++){
			for(int j = 0; j < maze[i].length; j++){
				char c = maze[i][j];
				if (((i == 12 || i == 14 || i == 16) && j == 33) && (c != ' ')){
					g2.drawImage(brick, tempX, tempY, this);
					g2.setColor(wallOut);
					g2.drawRect(tempX, tempY, size2d, size2d);
				}
				else if(c == '*'){
					g2.setColor(Color.BLUE);
					g2.fillRect(tempX+glow, tempY+glow, size2d, size2d);
					g2.setColor(wallFill);
					g2.fillRect(tempX, tempY, size2d, size2d);
					g2.setColor(wallOut);
					g2.drawRect(tempX, tempY, size2d, size2d);

				}
				else if (c == '0'){
					if(i==15){
						g2.drawImage(q1Brick, tempX, tempY, this);
						g2.setColor(wallOut);
						g2.drawRect(tempX, tempY, size2d, size2d);
					}
					else if(i==5){
						g2.drawImage(q2Brick, tempX, tempY, this);
						g2.setColor(wallOut);
						g2.drawRect(tempX, tempY, size2d, size2d);
					}
				}
				else if (c == 'k'){
					g2.drawImage(key, tempX, tempY+1, this);
				}
				else if (c == 'c'){
					g2.drawImage(closeChest, tempX, tempY, this);
				}
				else if (c == 'd'){
					g2.drawImage(openChest, tempX, tempY, this);
				}
				tempX+=size2d;
			}
			tempX=size2d;
			tempY+=size2d;
		}
	}
	public void paint3DMaze(Graphics2D g2){ //makes sure to draw the frontwalls last
		int row = hero.getLoc().getR()/size2d-1;
		int col = hero.getLoc().getC()/size2d-1;
		int dir = hero.getDir();
		switch (dir) {
			case 0:
				for(int f = 0; f < fov; f++){
					if((row - f) >= 0 && (col - 1) >= 0 && maze[row-f][col-1] != ' '){
						walls.add(getLeftWall(f));
					}
					else{
						walls.add(getFrontLeft(f));
						walls.add(getLeftTop(f));
						walls.add(getLeftBot(f));
					}
					if((row - f) >= 0 && (col + 1) < maze[0].length && maze[row-f][col+1] != ' '){
						walls.add(getRightWall(f));
					}
					else{
						walls.add(getFrontRight(f));
						walls.add(getRightTop(f));
						walls.add(getRightBot(f));
					}
					if((row - f) >= 0 && maze[row-f][col] == '*'){
						walls.add(getFrontWall(f));
					}
					walls.add(getTopWall(f));
					walls.add(getBottomWall(f));
				}
				break;
			case 1:
				for(int f = 0; f < fov; f++){
					if((row - 1) >= 0 && (col + f) < maze[0].length && maze[row-1][col+f] == '*'){
						walls.add(getLeftWall(f));
					}
					else{
						if((row - 1) >= 0 && (col + f) < maze[0].length){
							walls.add(getFrontLeft(f));
							walls.add(getLeftTop(f));
							walls.add(getLeftBot(f));
						}
					}

					if((row + 1) < maze.length && (col + f) < maze[0].length && maze[row+1][col+f] != ' '){
						walls.add(getRightWall(f));
					}
					else{
						if((row + 1) < maze.length && (col + f) < maze[0].length){
							walls.add(getFrontRight(f));
							walls.add(getRightTop(f));
							walls.add(getRightBot(f));
						}
					}
					if((col + f) < maze[0].length && maze[row][col+f] == '*'){
						walls.add(getFrontWall(f));
					}
					if((col + f) < maze[0].length){
						walls.add(getTopWall(f));
						walls.add(getBottomWall(f));
					}
				}
				break;

			case 2:
				for(int f = 0; f < fov; f++){
					if((row + f) < maze.length && (col + 1) < maze[0].length && maze[row+f][col+1] == '*'){
						walls.add(getLeftWall(f));
					}
					else{
						walls.add(getFrontLeft(f));
						walls.add(getLeftTop(f));
						walls.add(getLeftBot(f));
					}
					if((row + f) < maze.length && (col - 1) >= 0 && maze[row+f][col-1] != ' '){
						walls.add(getRightWall(f));
					}
					else{
						walls.add(getFrontRight(f));
						walls.add(getRightTop(f));
						walls.add(getRightBot(f));
					}
					if((row + f) < maze.length && maze[row+f][col] == '*'){
						walls.add(getFrontWall(f));
					}
					walls.add(getTopWall(f));
					walls.add(getBottomWall(f));
				}
				break;
			case 3:
				for(int f = 0; f < fov; f++){
					if((row + 1) < maze.length && (col - f) >= 0 && maze[row+1][col-f] == '*'){
						walls.add(getLeftWall(f));
					}
					else{
						if((row + 1) < maze.length && (col - f) >= 0){
							walls.add(getFrontLeft(f));
							walls.add(getLeftTop(f));
							walls.add(getLeftBot(f));
						}
					}
					if((row - 1) >= 0 && (col - f) >= 0 && maze[row-1][col-f] == '*'){
						walls.add(getRightWall(f));
					}
					else{
						if((row - 1) >= 0 && (col - f) >= 0){
							walls.add(getFrontRight(f));
							walls.add(getRightTop(f));
							walls.add(getRightBot(f));
						}
					}
					if((col - f) >= 0 && (maze[row][col-f] == '*' || maze[row][col-f] == '0')){
						walls.add(getFrontWall(f));
					}
					if((col - f) >= 0){
						walls.add(getTopWall(f));
						walls.add(getBottomWall(f));
					}
				}
				break;
			}
		for(int i = 0; i < walls.size(); i++){
			if(!(walls.get(i).getType().equals("front"))){
				g2.setPaint(walls.get(i).getPaint());
				g2.fill(walls.get(i).getPoly());
				g2.setColor(Color.BLACK);
				g2.draw(walls.get(i).getPoly());
			}
		}
		boolean noFrontWall = true;
		for(int i = 0; i < walls.size(); i++){
			if((walls.get(i).getType().equals("front"))){
				g2.setPaint(walls.get(i).getPaint());
				g2.fill(walls.get(i).getPoly());
				g2.setColor(Color.BLACK);
				g2.draw(walls.get(i).getPoly());
				noFrontWall = false;
				break;
			}
		}
		if(dir == 0 && noFrontWall){
			Wall frontWall = getFrontWall(fov-1);
			g2.setPaint(frontWall.getPaint());
			g2.fill(frontWall.getPoly());
			g2.setColor(Color.BLACK);
			g2.draw(frontWall.getPoly());
		}
		else if(noFrontWall &&!((row == 17 && (col == 33||col == 34))||(row ==1 && (col == 0 || col == 1)))){
			Wall frontWall = getFrontWall(fov-1);
			g2.setPaint(frontWall.getPaint());
			g2.fill(frontWall.getPoly());
			g2.setColor(Color.BLACK);
			g2.draw(frontWall.getPoly());
		}

		walls.clear();
	}
	public void print2DArray(char[][] maze){
		for(int i = 0; i < maze.length; i++){
			for(int j = 0; j < maze[0].length; j++){
				System.out.print(maze[i][j]);
			}
			System.out.println();
		}
	}
	public void print1DArray(int[] a){
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i]+", ");
		}
		System.out.println();
	}
	public void setBoard(){
		File fileName = new File("Maze.txt");
		//find the number of row and col of the maze in the textfile
		try{
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			String text;
			char[] line;
			int row = 0;
			int col = 0;
			while((text=input.readLine())!=null){
				col = text.length();
				row++;
			}
			maze = new char[row][col];
		}catch(IOException ioe){
			System.out.println("File not found.");
		}
		//conver the text file to char[][]
		try{
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			String text;
			char[] line;
			int row = 0;
			while((text=input.readLine())!=null){
				maze[row] = text.toCharArray();
				row++;
			}
		}catch(IOException ioe){
			System.out.println("File not found.");
		}

	}
}