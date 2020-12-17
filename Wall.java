import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Wall{
	int[] rows;    //y
	int[] cols;    //x
	String type;
	Color startColor;
	Color endColor;
	int size;

	public Wall(int[] rows, int[] cols, String type, int sR, int sG, int sB, int size){
		this.rows = rows;
		this.cols = cols;
		this.type = type;
		this.size = size;
		startColor = new Color(sR, sG, sB);
		try{
			endColor = new Color(sR - size, sG - size, sB - size);
		}catch(Exception e){
			endColor = startColor;
		}
		if(type.equals("dd"))
		System.out.println(type+" "+startColor.toString()+" "+endColor.toString());
	}
	public GradientPaint getPaint(){
		int startRow = 0;
		int startCol = 0;
		int endRow = 0;
		int endCol = 0;
		if(type.equals("left")){
			startRow = (rows[0] + rows[3])/2;
			startCol = cols[0];
			endRow = startRow;
			endCol = startCol+size;
		}
		else if(type.equals("right")){
			startRow = (rows[0] + rows[3])/2;
			startCol = cols[0];
			endRow = startRow;
			endCol = startCol-size;
		}
		else if(type.equals("top")){
			startRow = rows[0];
			startCol = (cols[0] + cols[1])/2;
			endRow = startRow+size;
			endCol = startCol;
		}
		else if(type.equals("bottom")){
			startRow = rows[0];
			startCol = (cols[0] + cols[1])/2;
			endRow = startRow-size;
			endCol = startCol;
		}
		else if(type.equals("topLeft")){
			startRow = rows[0];
			startCol = (cols[0] + cols[1])/2;
			endRow = startRow+size;
			endCol = startCol;
		}
		else if(type.equals("topRight")){
			startRow = rows[1];
			startCol = (cols[0] + cols[1])/2;
			endRow = startRow+size;
			endCol = startCol;
		}
		else if(type.equals("botRight")){
			startRow = rows[2];
			startCol = (cols[0] + cols[1])/2;
			endRow = startRow-size;
			endCol = startCol;
		}
		else if(type.equals("botLeft")){
			startRow = rows[2];
			startCol = (cols[0] + cols[1])/2;
			endRow = startRow-size;
			endCol = startCol;
		}

		return new GradientPaint(startCol, startRow, startColor, endCol, endRow, endColor);
	}
	public String getType(){
		return type;
	}
	public Polygon getPoly(){
		if(type.equals("topRight") || type.equals("topLeft") || type.equals("botLeft") || type.equals("botRight"))
			return new Polygon(cols, rows, 3);
		else
			return new Polygon(cols, rows, 4);
	}
}