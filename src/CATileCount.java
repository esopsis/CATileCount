import java.awt.*;
import java.awt.image.BufferedImage;
import java.applet.*;
import java.util.Scanner;
import java.util.Random;

/*
CATileCount.java
by Eric J.Parfitt (ejparfitt@gmail.com)

I wrote this program to help with some tiles I designed which emulate a
rule 110 cellular automaton.  I wrote this to determine how many of 
each kind of tile I might need.  This code runs a rule 110 cellular
automaton, and colors each pixel a different color depending on which of
seven tiles would correspond to that pixel.  It also tallies the number
of each tile that are found in each step of the evolution.

Version: 1.0 alpha
*/

public class CATileCount extends Applet implements Runnable {
	//Variables
	private boolean[] ruleDigit = {false, true, true, false, true, true, true, false};	//Rule 110
	//private boolean[] ruleDigit = {false, false, false, true, true, true, true, false};		//Rule 30
	//private boolean[] ruleDigit = {false, false, true, true, false, true, true, false};		//Rule 54
	private BufferedImage drawing;
	private Graphics2D g;
	private Thread myThread; 
	private int arrayH = 400;
	private int numStripes = 12;
	private int incFact = 32 * numStripes;
	private int xMax = 400;
	private Random random = new Random();
	
	boolean cellA = false;
	boolean cellB = false;
	boolean cellC = false;
	private boolean[] row = new boolean[xMax];
	private boolean[] oldRow = new boolean[xMax];
	private boolean[] oldRow2 = new boolean[xMax];
	private boolean[][] array = new boolean[arrayH][xMax];
	private boolean[] tempRow = new boolean[row.length];
	
	Scanner scan = new Scanner(System.in);


	// More variables
	
	Color redColor;
	Color weirdColor;
	Color bgColor;
	Color whiteColor;
	Color blackColor;
	Color greenColor;
	Color yellowColor;
	Color orangeColor;
	Color purpleColor;
	private Color[][] colorAr = new Color[arrayH][xMax];
	private Color[] colors= new Color[xMax];
	private double xSize = 800;
	private double ySize = 600;
	private int stepNumber = 1;
	int initRowL;
	private int typeA;
	private int typeB;
	private int typeC;
	private int typeD;
	private int typeE;
	private int y;
	private int rY;
	private int bR;
	private int rB;
	private int rYB;
	private int w;
	private int bY;
	public void init() 
	{
		//random initial conditions
		
		for(int i = 0; i < row.length; i++) {
			row[i] = random.nextBoolean();
		}
		
		drawing = new BufferedImage((int) xSize, (int) ySize, BufferedImage.TYPE_4BYTE_ABGR);
		g = drawing.createGraphics();
		resize((int) xSize, (int) ySize);

		// Colors
		initRowL = row.length;
		redColor = Color.red;
		weirdColor = new Color(60,60,122);
		bgColor = Color.white;
		whiteColor = Color.white;
		blackColor = Color.black;
		greenColor = Color.green;
		yellowColor = Color.yellow;
		orangeColor = Color.orange;;
		purpleColor = new Color(255,0,255);
		System.out.println(incFact);

		setBackground(bgColor);
	}
	

	public void stop()
	{
		myThread = null;
	}

	public void start() {
		if(myThread == null) {
			myThread = new Thread(this, "curvechange");
			myThread.start();
		}
	}
	//Gets keys and updates graphics
	public void run() {
		while(true) {
			//speed
			try {Thread.sleep(0);}
			catch(Exception e) {}
			update(getGraphics());
			
		}
	}
	public void update(Graphics gr) {
		paint(gr);
	}
	// Draws stuff on screen
	public void paint(Graphics gr) {
		g.setColor(bgColor);
		g.fillRect(0, 0, (int) xSize, (int) ySize);
		
		int tot = y + rY + bR + rB + rYB + w + bY;
		if (tot == 0) {
			tot = -1;
		}
		System.out.println("y = " + y + " rY = " + rY + " bR = " + bR + " rB = " + rB + " rYB = " + rYB + " w = " + w + " bY = " + bY + " total = " + (tot));
		
		for(int i = 0; i < row.length && i < xMax; i++) {
			array[arrayH - 1][i] = row[i];
			colorAr[arrayH - 1][i] = colors[i];
		}
		
		for (int i = 0; i < array.length; i++) {
			y = 0;
			rY = 0;
			bR = 0;
			rB = 0;
			rYB = 0;
			w = 0;
			bY = 0;
			for (int j = 0; j < array[0].length; j++) {
				if(j+2 < row.length) {
					if(oldRow[j+1] == false) {
						typeA++;
						w++;
						colors[j] = Color.red;
					}
					else {
						if(oldRow[j] == true) {
							if(oldRow[j+2] == true) {
								typeB++;
								if(oldRow2[j+1] == true) {
									bR++;
									colors[j] = Color.orange;
								}
								else {
									bY++;
									colors[j] = Color.yellow;
								}
							}
							else {
								typeE++;
								rB++;
								colors[j] = Color.green;
							}
						}
						else{
							if(oldRow[j+2] == true) {
								typeC++;
								if (oldRow2[j+1] == true) {
									rYB++;
									colors[j] = Color.cyan;
								}
								else {
									y++;
									colors[j] = Color.blue;
								}
							}
							else {
								typeD++;
								rY++;
								colors[j] = purpleColor;
							}
						}
					}
				}
				g.setColor(colorAr[i][j]);
				g.drawRect((int) (xSize / 2 - xMax / 2) + j, 50 + i, 0, 0);
				if (i + 1 < array.length) {
					array[i][j] = array[i + 1][j];
					colorAr[i][j] = colorAr[i + 1][j];
				}
			}
		}
		for(int i = 0; i < row.length; i++){
			oldRow2[i] = oldRow[i];
			oldRow[i] = row[i];
			if (i - 1 >=0 && i + 1 < row.length) {
				cellA = row[i - 1];
				cellB = row[i];
				cellC = row[i + 1];
			}
			if (i == row.length - 1) {
				cellA = row[row.length - 2];
				cellB = row[row.length - 1];
				cellC = row[0];
			}
			if (i == 0) {
				cellA = row[row.length - 1];
				cellB = row[0];
				cellC = row[1];
			}
			
			if (cellA == false && cellB == false &&  cellC == false) {
				tempRow[i] = ruleDigit[7];
			}
			if (cellA == false && cellB == false && cellC == true) {
				tempRow[i] = ruleDigit[6];
			}
			if (cellA == false && cellB == true && cellC == false) {
				tempRow[i] = ruleDigit[5];
			}
			if (cellA == false && cellB == true && cellC == true) {
				tempRow[i] = ruleDigit[4];
			}
			if (cellA == true && cellB == false && cellC == false) {
				tempRow[i] = ruleDigit[3];
			}
			if (cellA == true && cellB == false && cellC == true) {
				tempRow[i] = ruleDigit[2];
			}
			if (cellA == true && cellB == true && cellC == false) {
				tempRow[i] = ruleDigit[1];
			}
			if (cellA == true && cellB == true && cellC == true) {
				tempRow[i] = ruleDigit[0];
			}
		}
		for (int i = 0; i < tempRow.length; i++) {
			row[i] = tempRow[i];
		}
		tempRow = new boolean[row.length];
		
		//Increments step number
		stepNumber ++;
		gr.drawImage(drawing, 0,0, this);
	}
}
	
