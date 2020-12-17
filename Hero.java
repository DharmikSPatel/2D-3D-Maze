import java.awt.*;
import javax.swing.*;
import java.io.*;


public class Hero{
	private Location loc;
	private Location startLoc;
	private int size;
	private int dir;
	private Color fillColor;
	private Color outColor;
	private int moves;
	private int prevMoves;

	private String state;

	private boolean keyFound;
	private boolean chestOpened;
	private boolean onChestBlcok;

	private boolean q1Enabled;
	private boolean q1Solved;
	private boolean q2Enabled;
	private boolean q2Solved;

	private boolean win;

	//move counter
	//chest open big image
	//fix exit

	public Hero(Location loc, int size, int dir, Color c, Color c2){
		this.loc = loc;
		startLoc = loc;
		this.size = size;
		this.dir = dir;
		fillColor = c;
		outColor = c2;

		moves = 0;
		prevMoves = 0;
		state = "Maze Project";
		keyFound = false;
		q1Enabled = false;
		q1Solved = false;
		q2Solved = false;
		q2Enabled = false;

		win = false;
	}
	public boolean getChestOpened(){
		return chestOpened;
	}
	public boolean getOnChestBlcok(){
		return onChestBlcok;
	}
	public boolean getq1Enabled(){
		return q1Enabled;
	}
	public boolean getq2Enabled(){
		return q2Enabled;
	}
	public boolean getWin(){
		return win;
	}
	public String getState(){
		return state;
	}
	public void setState(String str){
		state = str;
	}
	public boolean getKeyFound(){
		return keyFound;
	}
	public Location getLoc(){
		return loc;
	}
	public int getSize(){
		return size;
	}
	public int getDir(){
		return dir;
	}
	public Color getFillColor(){
		return fillColor;
	}
	public Color getOutColor(){
		return outColor;
	}
	public int getMoves(){
		if(moves < 0)
			return 0;
		if(prevMoves > moves){
			moves = prevMoves;
			return prevMoves;
		}
		else{
			prevMoves = moves;
		}
		return moves;
	}
	public Rectangle getRect(){
		return new Rectangle(getLoc().getC(), getLoc().getR(), size, size);
	}


	public char[][] move(int keyCode, char[][] maze){
		int curRow = ((getLoc().getR())/size)-1;
		int curCol = ((getLoc().getC())/size)-1;
		state = "Maze Project";
		if(keyCode == 38 && !win){
			try{
				if(maze[curRow-1][curCol]!='*' && dir == 0){
					onChestBlcok = false;
					if(maze[curRow-1][curCol]=='k'){
						state = "Key Found";
						keyFound = true;
						maze[curRow-1][curCol] = ' ';
					}

					loc.setR(-size);
					moves++;
				}
				else if(maze[curRow][curCol+1]!='*' && dir == 1){
					if(maze[curRow][curCol+1]=='#'){
						state = "Winner! Winner! Chicken Dinner";
						win = true;
					}
					if(maze[curRow][curCol+1]=='c' && keyFound){
						state = "Chest Opened";
						onChestBlcok = true;
						chestOpened = true;
						maze[curRow][curCol+1] = 'd';
						maze[12][33] = ' ';
					}
					else if(maze[curRow][curCol+1]=='c'){
						state = "Locked! No Key Found";
						onChestBlcok = true;
						chestOpened = false;
					}
					else{
						onChestBlcok = false;
					}
					loc.setC(size);
					moves++;
				}
				else if(maze[curRow+1][curCol]!='*' && dir == 2){
					onChestBlcok = false;
					if(maze[curRow+1][curCol]=='k'){
						state = "Key Found";
						keyFound = true;
						maze[curRow+1][curCol] = ' ';
					}
					loc.setR(size);
					moves++;
				}
				else if(maze[curRow][curCol-1]==' ' && dir == 3){
					onChestBlcok = false;
					loc.setC(-size);
					moves++;
				}
			}catch(ArrayIndexOutOfBoundsException e){
				state = "Sorry, can't move there";
				moves--;
			}
		}
		//turn right
		else if(keyCode == 39 && !win){
			moves++;
			dir++;
			if(dir > 3){
				dir = 0;
			}


			if(dir == 3 && curRow == 15 && maze[curRow][curCol-1] == '0' && q1Solved == false){
				state = "In the morning: ";
				q1Enabled = true;
			}
			else if(dir == 3 && curRow == 5 && maze[curRow][curCol-1] == '0' && q2Solved == false){
				state = "Chips Ahoy...";
				q2Enabled = true;
			}
			else{
				q1Enabled = false;
				q2Enabled = false;
			}
		}
		//turn left
		else if(keyCode == 37 && !win){
			moves++;
			dir--;
			if(dir < 0){
				dir = 3;
			}
			if(dir == 3 && curRow == 15 && maze[curRow][curCol-1] == '0' && q1Solved == false){
				state = "In the morning: ";
				q1Enabled = true;
			}
			else if(dir == 3 && curRow == 5 && maze[curRow][curCol-1] == '0' && q2Solved == false){
				state = "Chips Ahoy...";
				q2Enabled = true;
			}
			else{
				q1Enabled = false;
				q2Enabled = false;
			}
		}
		return maze;
	}
	public char[][] correctAnswer(char[][] maze){
		if(q1Enabled){
			q1Solved = true;
			maze[14][33] = ' ';
		}
		else if(q2Enabled){
			q2Solved = true;
			maze[16][33] = ' ';
		}
		q1Enabled = false;
		q2Enabled = false;
		state = "Correct!";
		return maze;
	}
	public char[][] wrongAnswer(char[][] maze){

		if(q1Enabled && !q1Solved){
			loc = new Location(size, size*2);
			dir = 1;
		}
		if(q2Enabled && !q2Solved){
			loc = new Location(size, size*2);
			dir = 1;
		}
		q1Enabled = false;
		q2Enabled = false;
		state = "Wrong!";
		return maze;
	}

}