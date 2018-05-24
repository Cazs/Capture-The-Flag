package za.ac.uj.acsse.practicalx.flagcapture.Assets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;
import za.ac.uj.acsse.practicalx.flagcapture.Model.GameAsset;
import za.ac.uj.acsse.practicalx.flagcapture.Model.IVisitor;
import za.ac.uj.acsse.practicalx.flagcapture.States.Values;


public class Building extends GameAsset
{
	private Graphics g						= null;
	private String number					= "";
	private Color colour					= null;
	private int z							= 13;
	private double distance					= 0;
	private boolean	above					= false;
	private boolean	below					= false;
	private boolean	left					= false;
	private boolean	right					= false;
	private boolean is3D					= true;
	
	public Building(int x,int y,int w,int h,Color colour,boolean is3D,String number)
	{	
		super(x,y,w,h,"");
		this.number = number;
		this.colour = colour;
		this.is3D = is3D;
		
		calculateDistance();
	}
	
	public static boolean validate(String line)
	{
		Main.log.println("Validating building.");
		
		Pattern pattern = Pattern.compile("(B)((-)\\d+|\\d+)(#)((-)\\d+|\\d+)(#)\\d+(#)\\d+(#)\\w+(#)(RED|LIME|CYAN|MAGENTA)(#)(TRUE|FALSE)");
		Matcher matcher = pattern.matcher(line);
		return matcher.find();
	}
	
	
	public void calculateDistance()
	{
		Actor actor = Canvas.getActorInstance();
		
		if(actor.getBottom() <= this.getTop())		//above
			above = true;
		else
			above = false;
		
		if(actor.getTop() >= this.getBottom())		//below
			below = true;
		else
			below = false;
		
		if(actor.getRight() <= this.getLeft())		//left
			left = true;
		else
			left = false;
		
		if(actor.getLeft() >= this.getRight())		//right
			right = true;
		else
			right = false;
		
		//a more accurate distance, could've used the center of the building instead
		if(above && left)
			distance = Math.sqrt(Math.pow(getX()-actor.getX(),2)+Math.pow(getY()-actor.getY(),2));//in pixels
		if(below && left)
			distance = Math.sqrt(Math.pow(getX()-actor.getX(),2)+Math.pow(getY()+getHeight()-actor.getY(),2));//in pixels
		if(above && right)
			distance = Math.sqrt(Math.pow(getX()+getWidth()-actor.getX(),2)+Math.pow(getY()-actor.getY(),2));//in pixels
		if(below && right)
			distance = Math.sqrt(Math.pow(getX()+getWidth()-actor.getX(),2)+Math.pow(getY()-actor.getY(),2));//in pixels
		if(above && !left && !right)//above only
			distance = Math.sqrt(Math.pow(getX()-actor.getX(),2)+Math.pow(getY()-actor.getY(),2));//in pixels
		if(below && !left && !right)//below only
			distance = Math.sqrt(Math.pow(getX()-actor.getX(),2)+Math.pow(getY()-actor.getY(),2));//in pixels
		if(left && !above && !below)
			distance = Math.sqrt(Math.pow(getX()-actor.getX(),2)+Math.pow(getY()-actor.getY(),2));//in pixels
		if(right && !above && !below)
			distance = Math.sqrt(Math.pow(getX()-actor.getX(),2)+Math.pow(getY()-actor.getY(),2));//in pixels
		
		if(distance >= 325)
			z = 13;
		if(distance >= 300 && distance < 325)
			z = 12;
		if(distance >= 275 && distance < 300)
			z = 11;
		if(distance >= 250 && distance < 275)
			z = 10;
		if(distance >= 225 && distance < 250)
			z = 9;
		if(distance >= 200 && distance < 225)
			z = 8;
		if(distance >= 175 && distance < 200)
			z = 7;
		if(distance >= 150 && distance < 175)
			z = 6;
		if(distance >= 125 && distance < 150)
			z = 5;
		if(distance >= 100 && distance < 125)
			z = 4;
		if(distance >= 75 && distance < 100)
			z = 3;
		if(distance >= 50 && distance < 75)
			z = 2;
		if(distance >= 25 && distance < 50)
			z = 1;
		if(distance < 25)
			z = 0;
	}
	
	public void drawBuilding()
	{
		calculateDistance();
		
		g.setColor(Color.CYAN);
		//draw layout of building
		g.draw3DRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(),true);
		g.drawString(""+number, (int)getX()-20, (int) (getY()+(getHeight()/2)));
		
		if(Values.BUILDING_DATA)
		{
			g.setColor(Color.GREEN);
			g.drawString("["+getX()+","+getY()+"]", (int)getX()-20, (int)getY());
			g.drawString("["+getX()+","+getLeft()+"]", (int) (getX()-20), (int) (getBottom()+10));
			g.drawString("["+getRight()+","+getY()+"]", (int) (getRight()-20), (int) getY());
			g.drawString("["+getRight()+","+getBottom()+"]", (int)getRight()+10,(int)getBottom()+10);
			
			g.setColor(Color.RED);
			g.drawLine((int)getLeft()-10, (int)getTop(), (int)getRight()+10, (int)getTop());
			g.drawLine((int)getLeft()-10,(int) getBottom(), (int)getRight()+10,(int) getBottom());
			g.drawLine((int)getLeft(), (int)getTop()-10, (int)getLeft(), (int)getBottom()+10);
			g.drawLine((int)getRight(), (int)getTop()-10, (int)getRight(), (int)getBottom()+10);
		}
		
		if(is3D)
		{
			if(right)
				isRight();
			if(left)
				isLeft();
			if(above && !right && !left)//above only
				isAbove();
			if(below && !right && !left)//below only
				isBelow();
			if(left && !above && !below)
				isLeftOnly();
			if(right && !above && !below)
				isRightOnly();
		}
	}
	
	private void isLeft()
	{
		Color c = Color.GRAY; 
		//shade rect
		g.setColor(c.brighter());
		//g.fillRect(getX(), getY(), getWidth(), getHeight());
		//outline rect
		g.setColor(colour.brighter());
		g.draw3DRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(),true);
		
		if(above)
		{
			g.drawLine((int)getX()-z, (int)getY()-z,(int) ( getX()+getWidth()-z),(int) getY()-z);//horizontal line
			g.drawLine((int)getX()-z, (int)getY()-z, (int)getX()-z, (int) (getY()+getHeight()-z));//vertical line
			
			g.drawLine((int)getX()-z, (int)getY()-z, (int)getX(),(int) getY());//top-left diagonal line
			g.drawLine((int)(getX()+getWidth()-z),(int) getY()-z, (int) (getX()+getWidth()),(int) getY());//top-right diagonal line
			g.drawLine((int)(getX()-z),(int) (getY()+getHeight()-z),(int) getX(), (int)(getY()+getHeight()));//bottom-left diagonal line
		}
		
		if(below)
		{
			g.drawLine(getX()-z, getY()+getHeight()+z, getX()+getWidth()-z, getY()+getHeight()+z);//horizontal line
			g.drawLine(getX()-z, getY()+z, getX()-z, getY()+getHeight()+z);//vertical line
			
			g.drawLine(getX()-z, getY()+z, getX(), getY());//top-left diagonal line
			g.drawLine(getX()+getWidth()-z, getY()+getHeight()+z, getX()+getWidth(), getY()+getHeight());//bottom-right diagonal line
			g.drawLine(getX()-z, getY()+getHeight()+z, getX(), getY()+getHeight());//bottom-left diagonal line
		}
	}
	
	private void isRight()
	{
		//shade rect
		g.setColor(Color.GRAY);
		//g.fillRect(getX(), getY(), getWidth(), getHeight());
		//outline rect
		g.setColor(colour.brighter());
		g.draw3DRect(getX(), getY(), getWidth(), getHeight(),true);
		
		if(above)
		{
			g.drawLine(getX()+z, getY()-z, getX()+getWidth()+z, getY()-z);//horizontal line
			g.drawLine(getX()+z+getWidth(), getY()-z, getX()+z+getWidth(), getY()+getHeight()-z);//vertical line
			
			g.drawLine(getX()+z, getY()-z, getX(), getY());//top-left diagonal line
			g.drawLine(getX()+getWidth()+z, getY()-z, getX()+getWidth(), getY());//top-right diagonal line
			g.drawLine(getX()+z+getWidth(), getY()+getHeight()-z, getX()+getWidth(), getY()+getHeight());//bottom-left diagonal line
		}
		
		if(below)
		{
			g.drawLine(getX()+z, getY()+getHeight()+z, getX()+getWidth()+z, getY()+getHeight()+z);//horizontal line
			g.drawLine(getX()+z+getWidth(), getY()+z, getX()+z+getWidth(), getY()+getHeight()+z);//vertical line
			
			g.drawLine(getX()+z+getWidth(), getY()+z, getX()+getWidth(), getY());//top-left diagonal line
			g.drawLine(getX()+getWidth()+z, getY()+getHeight()+z, getX()+getWidth(), getY()+getHeight());//bottom-right diagonal line
			g.drawLine(getX()+z, getY()+getHeight()+z, getX(), getY()+getHeight());//bottom-left diagonal line
		}
	}
	
	private void isAbove()
	{
		Color c = Color.CYAN;
		g.setColor(c.brighter());
		g.draw3DRect(getX(), getY(), getWidth(), getHeight(), false);
		
		//draw straights
		g.drawLine(getX()+z, getY()-z, getX()-z+getWidth(), getY()-z);//horizontal line
		//draw diagonals
		g.drawLine(getX()+z, getY()-z, getX(), getY());//top-left
		g.drawLine(getX()-z+getWidth(), getY()-z, getX()+getWidth(), getY());//top-right
	}
	
	private void isBelow()
	{
		Color c = Color.CYAN;
		g.setColor(c.brighter());
		g.draw3DRect(getX(), getY(), getWidth(), getHeight(), false);
		
		//draw straights
		g.drawLine(getX()+z, getY()+z+getHeight(), getX()-z+getWidth(), getY()+getHeight()+z);//horizontal line
		//draw diagonals
		g.drawLine(getX()-z+getWidth(), getY()+z+getHeight(), getX()+getWidth(), getY()+getHeight());//bottom-right
		g.drawLine(getX()+z, getY()+z+getHeight(), getX(), getY()+getHeight());//bottom-left
	}
	
	private void isLeftOnly()
	{
		Color c = Color.CYAN;
		g.setColor(c.brighter());
		g.draw3DRect(getX(), getY(), getWidth(), getHeight(), false);
		//vertical
		g.drawLine(getX()-z, getY()+z, getX()-z, getY()+getHeight()-z);
		//diagonals
		g.drawLine(getX()-z, getY()+z, getX(), getY());
		g.drawLine(getX()-z, getY()+getHeight()-z, getX(), getY()+getHeight());
	}
	
	private void isRightOnly()
	{
		Color c = Color.CYAN;
		g.setColor(c.brighter());
		g.draw3DRect(getX(), getY(), getWidth(), getHeight(), false);
		//verticals
		g.drawLine(getX()+getWidth()+z, getY()+z, getX()+getWidth()+z, getY()+getHeight()-z);
		//diagonals
		g.drawLine(getX()+getWidth()+z, getY()+z, getX()+getWidth(), getY());
		g.drawLine(getX()+getWidth()+z, getY()+getHeight()-z, getX()+getWidth(), getY()+getHeight());
	}
	
	public void setGraphics(Graphics g)	{this.g = g;}
	
	public boolean get3D()				{return this.is3D;}

	@Override
	public void accept(IVisitor visitor) 
	{
		visitor.paint(this);
	}
}
