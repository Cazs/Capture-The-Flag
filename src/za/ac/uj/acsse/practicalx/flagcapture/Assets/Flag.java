package za.ac.uj.acsse.practicalx.flagcapture.Assets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;
import za.ac.uj.acsse.practicalx.flagcapture.Model.GameAsset;
import za.ac.uj.acsse.practicalx.flagcapture.Model.IVisitor;
import za.ac.uj.acsse.practicalx.flagcapture.States.Values;


public class Flag  extends GameAsset
{
	private Graphics g				= null;
	private boolean atBase			= true;
	private String faction			= "";
	private int score				= 0;
	
	public Flag(int x,int y, String faction)
	{
		super(x,y,50,50,faction);
		atBase = true;//when first instantiated, it will start off at base
		this.faction = faction;
		score = 0;
	}
	
	public static boolean validateFlag(String line)
	{
		Main.log.println("Validating flag.");
				
		Pattern pattern = Pattern.compile("(F)((-)\\d+|\\d+)(#)((-)\\d+|\\d+)(#)(RED|LIME|CYAN|MAGENTA|GREEN|BLUE)");
		Matcher matcher = pattern.matcher(line);
		return matcher.find();
	}
	
	public void resetPoints()						{score = 0;atBase=true;}
	
	public void spawnFlag(int x,int y)
	{
		this.setAtBase(true);
		this.setAbsX(x);
		this.setAbsY(y);
	}
	
	public void setG(Graphics g)					{this.g = g;}
	
	public void setAtBase(boolean state)			{this.atBase = state;}
	
	public boolean isAtBase()						{return atBase;}
	
	public String getFaction()						{return faction;}
	
	public void addPoint()							{this.score++;}
	
	public int getScore()							{return score;}
	
	public void setScore(int s)						{this.score = s;}
	
	public void drawFlag()
	{
		int x,y;
		
		if(atBase)
		{
			//AI flag coordinates
			if(faction.equals(Canvas.getAiInstance().getFaction()))
			{
				x = Canvas.getBuildingList().get(1).getX()+Canvas.getBuildingList().get(1).getWidth()/2;
				y = Canvas.getBuildingList().get(1).getY()-100;
				
				this.setAbsX(x);
				this.setAbsY(y);
				
				//draw flag border
				g.setColor(Color.MAGENTA);
				g.draw3DRect(x,y, 50, 50, true);
				
				g.drawString(x+","+y, x, y);
				//draw flag
				g.setColor(Color.GREEN);
				g.fill3DRect(x+30, y+10, 10, 10, true);
				g.drawLine(x+20, y+20, x+30, y+10);
			}
			//Player flag coordinates
			else if(faction.equals(Canvas.getActorInstance().getFaction()))
			{
				x = Canvas.getBuildingList().get(0).getX()+Canvas.getBuildingList().get(0).getWidth()/2;
				y = Canvas.getBuildingList().get(0).getY()+40;
				
				this.setAbsX(x);
				this.setAbsY(y);
				
				//draw flag border
				g.setColor(Color.MAGENTA);
				g.draw3DRect(x,y, 50, 50, true);
				
				if(Values.DEBUG_INFO)
					g.drawString(x+","+y, x, y);
				
				//draw flag
				g.setColor(Color.GREEN);
				g.fill3DRect(x+30, y+10, 10, 10, true);
				g.drawLine(x+20, y+20, x+30, y+10);
			}
		}
		//draw flag
		if(!atBase)
		{
			//JOptionPane.showMessageDialog(null, "Not at base.");
			//Player has AI flag
			if(Canvas.getActorInstance().hasFlag())
			{
				x = Canvas.getBuildingList().get(1).getX()+Canvas.getBuildingList().get(1).getWidth()/2;
				y = Canvas.getBuildingList().get(1).getY()-100;
				this.setAbsX(x);
				this.setAbsY(y);
				
				//draw flag border
				g.setColor(Color.MAGENTA);
				g.draw3DRect(x,y, 50, 50, true);
				
				//Draw actual flag
				if(!faction.equals(Canvas.getActorInstance().getFaction()))
				{
					g.setColor(Color.GREEN);
					g.fill3DRect(Canvas.getActorInstance().getX()+30, Canvas.getActorInstance().getY()+10, 10, 10, true);
					g.drawLine(Canvas.getActorInstance().getX()+20, Canvas.getActorInstance().getY()+20, Canvas.getActorInstance().getX()+30, Canvas.getActorInstance().getY()+10);
				}
			}
			
			//AI has player flag
			if(Canvas.getAiInstance().hasFlag())
			{
				x = Canvas.getBuildingList().get(0).getX()+Canvas.getBuildingList().get(0).getWidth()/2;
				y = Canvas.getBuildingList().get(0).getY()+40;
				this.setAbsX(x);
				this.setAbsY(y);
				
				//draw flag border
				g.setColor(Color.MAGENTA);
				g.draw3DRect(x,y, 50, 50, true);
				
				//Draw actual flag
				if(!faction.equals(Canvas.getAiInstance().getFaction()))
				{
					g.setColor(Color.GREEN);
					g.fill3DRect((int)Canvas.getAiInstance().getX()+30,(int) Canvas.getAiInstance().getY()+10, 10, 10, true);
					g.drawLine((int)Canvas.getAiInstance().getX()+20, (int)Canvas.getAiInstance().getY()+20,(int) Canvas.getAiInstance().getX()+30,(int) Canvas.getAiInstance().getY()+10);
				}
			}
		}
	}

	@Override
	public void accept(IVisitor visitor)
	{
		visitor.paint(this);
	}
}
