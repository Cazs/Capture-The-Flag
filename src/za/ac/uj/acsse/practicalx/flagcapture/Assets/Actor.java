package za.ac.uj.acsse.practicalx.flagcapture.Assets;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;
import za.ac.uj.acsse.practicalx.flagcapture.Model.GameAsset;
import za.ac.uj.acsse.practicalx.flagcapture.Model.IVisitor;
import za.ac.uj.acsse.practicalx.flagcapture.States.Values;


public class Actor  extends GameAsset
{
	private boolean hasParticles					= true;
	private String faction							= "";
	private boolean hasFlag							= false;
	private ArrayList<Bullet> bullets 				= new ArrayList<Bullet>();
	private Iterator<Bullet> iterator 				= null;
	private Bullet bullet							= null;
	private int health								= 0;
	private Random randomizer 						= new Random();
	private String id								= "";
	
	public Actor(int x,int y,boolean hasParticles,String faction)
	{
		super(x,y,Values.ACTOR_WIDTH,Values.ACTOR_HEIGHT,faction);
		this.hasParticles = hasParticles;
		this.faction = faction;
		health = 300;
	}
	//now returns the index of the building it collided with, else -1
	public int canCollide(int x_inc,int y_inc)
	{
		//distance between ai and player flag
		int px = Canvas.getBuildingList().get(0).getX()+Canvas.getBuildingList().get(0).getWidth()/2;
		int py = Canvas.getBuildingList().get(0).getY()+40;
		
		
		double t1 = Math.pow((px-Canvas.getAiInstance().getX()), 2);
		double t2 = Math.pow((py-Canvas.getAiInstance().getY()), 2);
		double pdistance = Math.sqrt(t1+t2);
		
		//distance between actor and enemy flag
		int ax = Canvas.getBuildingList().get(1).getX()+Canvas.getBuildingList().get(1).getWidth()/2;
		int ay = Canvas.getBuildingList().get(1).getY()-40;
		
		t1 = Math.pow((ax-Canvas.getActorInstance().getX()), 2);
		t2 = Math.pow((ay-Canvas.getActorInstance().getY()), 2);
		double edistance = Math.sqrt(t1+t2);
		
		//Check playerFlag collision
		if(flagCollisionRight(Canvas.playerFlag,x_inc) || flagCollisionLeft(Canvas.playerFlag,x_inc) || flagCollisionTop(Canvas.playerFlag,y_inc) || flagCollisionBottom(Canvas.playerFlag,y_inc))
		//if(pdistance<=20)
		{
			//Enemy to player flag collision
			if(!Canvas.playerFlag.getFaction().equals(faction))
			{
				Canvas.playerFlag.setAtBase(false);
				hasFlag = true;
			}
			//Has enemy flag and is at base
			else if(Canvas.playerFlag.getFaction().equals(faction) && hasFlag)
			{
				hasFlag = false;
				Canvas.enemyFlag.setAtBase(true);
				//Canvas.enemyFlag.spawnFlag(Canvas.buildings.get(1).getX()+Canvas.buildings.get(1).getWidth()/2, Canvas.buildings.get(1).getY()-80);
				Canvas.playerFlag.addPoint();
				Main.log.println(faction + " scores");
			}
		}
		//Check enemyFlag collision
		else if(flagCollisionRight(Canvas.enemyFlag,x_inc) || flagCollisionLeft(Canvas.enemyFlag,x_inc) || flagCollisionTop(Canvas.enemyFlag,y_inc) || flagCollisionBottom(Canvas.enemyFlag,y_inc))
		//if(edistance<=20)
		{
			//Player  to enemy flag collision
			if(!Canvas.enemyFlag.getFaction().equals(faction))
			{
				Canvas.enemyFlag.setAtBase(false);
				hasFlag = true;
			}
			//Enemy has player flag and is at base
			else if(hasFlag)
			{
				Canvas.playerFlag.setAtBase(true);
				//reset playerFlag
				hasFlag = false;
				//Canvas.playerFlag.spawnFlag(Canvas.buildings.get(0).getX()+Canvas.buildings.get(0).getWidth()/2, Canvas.buildings.get(0).getY()+60);
				Canvas.enemyFlag.addPoint();
				Main.log.println(faction + " scores");
			}
		}
		//Check building collision
		if(buildingCollisionRight(x_inc)==-1 && buildingCollisionLeft(x_inc)==-1 && buildingCollisionTop(y_inc)==-1 && buildingCollisionBottom(y_inc)==-1)
			return -1;//no collision
		
			//return the index of the building that was collided with
			if(buildingCollisionRight(x_inc)!=-1)
				return buildingCollisionRight(x_inc);
			
			if(buildingCollisionLeft(x_inc)!=-1)
				return buildingCollisionLeft(x_inc);
			
			if(buildingCollisionTop(y_inc)!=-1)
				return buildingCollisionTop(y_inc);
			
			if(buildingCollisionBottom(y_inc)!=-1)
				return buildingCollisionBottom(y_inc);
			
		Main.log.println("Unknown error when chcking for building and playerFlag collision.");
		return -10;//Unknown error
	}
	
	public void setHealth(int h){this.health = h;}
	
	public boolean hasFlag(){return hasFlag;}
	
	public String getFaction(){return faction;}
	
	public boolean isHasParticles() {return hasParticles;}
	
	public void setHasParticles(boolean hasParticles) {this.hasParticles = hasParticles;}
	
	private int buildingCollisionLeft(int x_inc)
	{
		int i = 0;
		for(Building b:Canvas.getBuildingList())
		{
			if(this.getRight()+x_inc >= b.getLeft() && this.getBottom() >= b.getTop() && this.getTop() <= b.getBottom() && this.getRight() <=b.getLeft())//this.getRight() < b.getRight())
			{
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private int buildingCollisionRight(int x_inc)
	{
		int i = 0;
		for(Building b:Canvas.getBuildingList())
		{
			if(this.getLeft()+x_inc <=  b.getRight() && this.getBottom() >=  b.getTop() && this.getTop() <= b.getBottom() && this.getLeft()>=b.getRight())
				return i;
			i++;
		}
		return -1;
	}
	
	private int buildingCollisionTop(int y_inc)
	{
		int i = 0;
		for(Building b:Canvas.getBuildingList())
		{
			if(this.getBottom()+y_inc >= b.getTop() && this.getLeft() <= b.getRight() && this.getRight() >= b.getLeft() && this.getBottom() < b.getBottom())
				return i;
			i++;
		}
		return -1;
	}
	
	private int buildingCollisionBottom(int y_inc)
	{
		int i = 0;
		for(Building b:Canvas.getBuildingList())
		{
			if(this.getTop()+y_inc <= b.getBottom() && this.getRight() >= b.getLeft() && this.getLeft() <= b.getRight() && this.getBottom() > b.getTop())
				return i;
			i++;
		}
		return -1;
	}
	
	private boolean flagCollisionLeft(Flag playerFlag,int x_inc)
	{
		if(this.getRight()+x_inc >= playerFlag.getLeft() && this.getBottom() >= playerFlag.getTop() && this.getTop() <= playerFlag.getBottom() && this.getRight() <=playerFlag.getLeft())
		{
			return true;
		}
		return false;
	}
	
	private boolean flagCollisionRight(Flag playerFlag, int x_inc)
	{
		if(this.getLeft()+x_inc <=  playerFlag.getRight() && this.getBottom() >=  playerFlag.getTop() && this.getTop() <= playerFlag.getBottom() && this.getLeft()>= playerFlag.getRight())
			return true;
		return false;
	}
	
	private boolean flagCollisionTop(Flag playerFlag,int y_inc)
	{
		if(this.getBottom()+y_inc >= playerFlag.getTop() && this.getLeft() <= playerFlag.getRight() && this.getRight() >= playerFlag.getLeft() && this.getBottom() < playerFlag.getBottom())
			return true;
		return false;
	}
	
	private boolean flagCollisionBottom(Flag playerFlag,int y_inc)
	{
		if(this.getTop()+y_inc <= playerFlag.getBottom() && this.getRight() >= playerFlag.getLeft() && this.getLeft() <= playerFlag.getRight() && this.getBottom() > playerFlag.getTop())
			return true;
		return false;
	}

	public void addBullet(Bullet bullet)
	{
		bullets.add(bullet);
	}
	
	public ArrayList<Bullet> getBullets()
	{
		return bullets;
	}

	public void checkBulletCollision(Graphics g)
	{
		iterator = bullets.iterator();
		while(iterator.hasNext())
		{
			bullet = iterator.next();
			if(bullet == null)
				return;
				
			switch(bullet.getDirection())
			{
			case "U":
				bullet.setY((int) -Values.BULLET_SPEED);
				break;
			case "D":
				bullet.setY((int) Values.BULLET_SPEED);
				break;
			case "L":
				bullet.setX((int) -Values.BULLET_SPEED);
				break;
			case "R":
				bullet.setX((int) Values.BULLET_SPEED);
				break;
			}

			g.drawRect((int)bullet.getX(), (int)bullet.getY(), (int)bullet.getWidth(), (int)bullet.getHeight());
			
			//bullet to building collision
			for(Building b:Canvas.getBuildingList())
			{
				if(bullet.getX()+bullet.getWidth() >= b.getLeft()-10 && bullet.getY()  <= b.getBottom() && bullet.getY()+bullet.getHeight() >= b.getTop() && bullet.getX()+bullet.getWidth() <= b.getRight())
				{
					switch(bullet.getDirection())
					{
					case "R":
						g.drawLine((int)bullet.getX()-5, (int)bullet.getY()-5,(int)bullet.getX()-10, (int)bullet.getY()-10);
						g.drawLine((int)bullet.getX()-5, (int)bullet.getY()+5,(int)bullet.getX()-10, (int)bullet.getY()+10);
						
						g.drawLine((int)bullet.getX(), (int)bullet.getY()-5,(int)bullet.getX()-5, (int)bullet.getY()-15);
						g.drawLine((int)bullet.getX(), (int)bullet.getY()+5,(int)bullet.getX()-5, (int)bullet.getY()+15);
						break;
					case "L":
						g.drawLine((int)bullet.getX()+30, (int)bullet.getY()-5,(int)bullet.getX()+40, (int)bullet.getY()-10);
						g.drawLine((int)bullet.getX()+30, (int)bullet.getY()+5,(int)bullet.getX()+40, (int)bullet.getY()+10);
						
						g.drawLine((int)bullet.getX()+40, (int)bullet.getY()-5,(int)bullet.getX()+50, (int)bullet.getY()-10);
						g.drawLine((int)bullet.getX()+40, (int)bullet.getY()+5,(int)bullet.getX()+50, (int)bullet.getY()+10);
						break;
					case "U":
						g.drawLine((int)bullet.getX(), (int)bullet.getY()+10, (int)bullet.getX()-10, (int)bullet.getY()+20);
						g.drawLine((int)bullet.getX()+(int)bullet.getWidth(), (int)bullet.getY()+10, (int)bullet.getX()+20, (int)bullet.getY()+20);
						
						//g.drawLine((int)bullet.getX()+10, (int)bullet.getY()+10, (int)bullet.getX()-3, (int)bullet.getY()+20);
						//g.drawLine((int)bullet.getX()+10+(int)bullet.getWidth(), (int)bullet.getY()+20, (int)bullet.getX()+20, (int)bullet.getY()+20);
						break;
					}
					if(iterator != null && bullet != null)
						iterator.remove();
					
					break;
				}
			}
			
			//Horizontal/X axis collision
			//AI bullet to Actor collision - NO FRIENDLY FIRE!
			if(bullet.getX() <= Canvas.getActorInstance().getRight() && bullet.getX() >= Canvas.getActorInstance().getLeft() && bullet.getY() >= Canvas.getActorInstance().getTop() && bullet.getBottom() <= Canvas.getActorInstance().getBottom())
			//if(bullet.getX() <= this.getRight() && bullet.getX() >= this.getLeft() && bullet.getY() >= this.getTop() && bullet.getBottom() <= this.getBottom())
			{
				if(!bullet.getFaction().equals(Canvas.getActorInstance().getFaction()))//Not from the same faction = Player got hit
				{
					Canvas.getActorInstance().decreaseHealth(Values.BULLET_DAMAGE);
					
					if(iterator != null && bullet != null)
						iterator.remove();
					
					break;
				}
			}
			//Actor bullet to AI collision - NO FRIENDLY FIRE!
			if(bullet.getX() <= Canvas.getAiInstance().getRight() && bullet.getX() >= Canvas.getAiInstance().getLeft() && bullet.getY() >= Canvas.getAiInstance().getTop() && bullet.getBottom() <= Canvas.getAiInstance().getBottom())
			{
				if(!bullet.getFaction().equals(Canvas.getAiInstance().getFaction()))//Not from the same faction = AI got hit
				{
					Canvas.getAiInstance().decreaseHealth(Values.BULLET_DAMAGE);
					
					if(iterator != null && bullet != null)
						iterator.remove();
					
					break;
				}
			}
			
			//Vertical/Y axis collision
			//Actor bullet to AI collision - NO FRIENDLY FIRE!
			if(bullet.getY() <= Canvas.getAiInstance().getBottom() && bullet.getY() >= Canvas.getAiInstance().getTop() && bullet.getX() >= Canvas.getAiInstance().getX() && bullet.getRight() <= Canvas.getAiInstance().getRight())
			{
				if(!bullet.getFaction().equals(Canvas.getAiInstance().getFaction()))//Not from the same faction = Player got hit
				{
					Canvas.getAiInstance().decreaseHealth(Values.BULLET_DAMAGE);//Decrease health
					
					if(iterator != null && bullet != null)
						iterator.remove();
					
					break;
				}
			}
			//AI bullet to Actor collision - NO FRIENDLY FIRE!
			if(bullet.getY() <= Canvas.getActorInstance().getBottom() && bullet.getY() >= Canvas.getActorInstance().getTop() && bullet.getX() >= Canvas.getActorInstance().getX() && bullet.getRight() <= Canvas.getActorInstance().getRight())
			{
				if(!bullet.getFaction().equals(Canvas.getActorInstance().getFaction()))//Not from the same faction = Player got hit
				{
					Canvas.getActorInstance().decreaseHealth(Values.BULLET_DAMAGE);//Decrease health
					
					if(iterator != null && bullet != null)
						iterator.remove();
					
					break;
				}
			}
		}
	}
	
	public void restoreHealth()
	{
		this.health = 300;
	}

	public void decreaseHealth(int dec)
	{
		if(this.health-dec < 0)
		{
			this.health = 0;//He dead yo!
		}
		else
		{
			this.health -= dec;
		}
	}
	
	public int getHealth(){return this.health;}

	public void spawnActor()
	{
		//My own little way of getting a random number between -300 and 300 - there are better methods
		int x = randomizer.nextInt(300);
		boolean neg_or_pos = randomizer.nextBoolean();
		
		//Spawn player, random number for different spawn point in the range(on the x axis) 170 and 770 
		if(neg_or_pos)//subtract
			this.setAbsX((Values.SCREEN_WIDTH/2)-x);
		else//add
			this.setAbsX((Values.SCREEN_WIDTH/2)+x);
		
		this.setAbsY(Values.SCREEN_HEIGHT/2);
		this.restoreHealth();
		
		Main.log.println("Spawned player");
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.paint(this);
	}

	public String getID(){return id;}
	
	public void setID(String id){this.id = id;}
}

