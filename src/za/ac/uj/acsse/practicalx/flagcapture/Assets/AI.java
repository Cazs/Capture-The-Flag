package za.ac.uj.acsse.practicalx.flagcapture.Assets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;
import za.ac.uj.acsse.practicalx.flagcapture.States.Values;

public class AI extends Actor
{
	private boolean x								= false;
	private boolean y								= false;
	
	private static boolean tcoll					= false;
	private static boolean bcoll					= false;
	private static boolean rcoll					= false;
	private static boolean lcoll					= false;
	private boolean ai_shoot_left					= false;
	private boolean ai_shoot_right					= false;
	private boolean ai_shoot_up						= false;
	private boolean ai_shoot_down					= false;
	private Timer ai_timer							= null;
	private double distance							= 0;
	private Random randomizer 						= new Random();

	public AI(int x, int y, boolean hasParticles, String faction)
	{
		super(x, y, hasParticles, faction);
		//Start AI fire rate timer
				ai_timer = new Timer(Values.AI_FIRE_RATE,new ActionListener() 
				{
					
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						if(ai_shoot_left)
							addBullet(new Bullet((int)getX(), (int)getY()+(Values.ACTOR_HEIGHT/2),20,5,"L","BLUE"));
						
						if(ai_shoot_right)
							addBullet(new Bullet((int)getX(), (int)getY()+(Values.ACTOR_HEIGHT/2),20,5,"R","BLUE"));
						
						if(ai_shoot_up)
							addBullet(new Bullet((int)getX()+(Values.ACTOR_WIDTH/2),(int) getY(),5,20,"U","BLUE"));
						
						if(ai_shoot_down)
							addBullet(new Bullet((int)getX()+(Values.ACTOR_WIDTH/2), (int)getY(),5,20,"D","BLUE"));
					}
				});
				Main.log.println("AI timer starting.");
				ai_timer.start();
	}

	private void followPlayer()
	{
		Main.log.println("AI Following player.");
		//Follow player
		if(this.getX() != Canvas.getActorInstance().getX())
		{
			if(Canvas.getActorInstance().getX()-this.getX()<0)
			{
				ai_shoot_left = true;
				x = false;
				
				if(this.canCollide(-Values.AI_SPEED, 0)==-1)//No collision
				{
					lcoll = false;
					
					if(!bcoll)
						this.setX(-Values.AI_SPEED);
					//return;
				}
				
				if(this.canCollide(-Values.AI_SPEED, 0)!=-1)//Left Collision
				{
					lcoll = true;
					this.setY(-Values.AI_SPEED-2);//move up
					//return;
				}
			}
			else
			{
				ai_shoot_left = false;
			}
			
			//if(this.getRight() < Canvas.getActorInstance().getRight())//Left())//AI to left of Actor
			if(Canvas.getActorInstance().getX()-this.getX()>0)
			{
				x = false;
				ai_shoot_right = true;
				
				if(this.canCollide(Values.AI_SPEED, 0)==-1)//No collision
				{
					rcoll = false;
					
					if(!tcoll)
						this.setX(Values.AI_SPEED);//move right
					//return;
				}
				
				if(this.canCollide(Values.AI_SPEED, 0)!=-1)//Right Collision
				{
					rcoll = true;
					this.setY(Values.AI_SPEED-2);//move down
					//return;
				}
			}
			else
			{
				ai_shoot_right = false;
			}
		}
		
		if(this.getY() != Canvas.getActorInstance().getY())
		{
			//if(this.getTop() > Canvas.getActorInstance().getTop())//getBottom())//AI below Actor
			if(Canvas.getActorInstance().getY()-this.getY()<0)
			{
				y = false;
				ai_shoot_up = true;
				
				if(this.canCollide(0,-Values.AI_SPEED)==-1)//No collision
				{
					bcoll = false;
					
					if(!rcoll)
						this.setY(-Values.AI_SPEED);//move up
					//return;
				}
				
				if(this.canCollide(0,-Values.AI_SPEED)!=-1)//Bottom Collision
				{
					bcoll = true;
					this.setX(Values.AI_SPEED-2);//move right
					//return;
				}
			}
			else
			{
				ai_shoot_up = false;
			}
			
			//if(this.getBottom() < Canvas.getActorInstance().getBottom())//Canvas.getActorInstance().getTop())//AI above Actor
			if(Canvas.getActorInstance().getY()-this.getY()>0)
			{
				y = false;
				ai_shoot_down = true;
				
				if(this.canCollide(0,Values.AI_SPEED)==-1)//No Collision
				{
					tcoll = false;
					
					if(!lcoll)
						this.setY(Values.AI_SPEED);//move down
					//return;
				}
				
				if(this.canCollide(0,Values.AI_SPEED)!=-1)//Top Collision
				{	
					tcoll = true;
					this.setX(-Values.AI_SPEED-2);//Move left
					//return;
				}
			}
			else
			{
				ai_shoot_down = false;
			}
		}
		
		if(distance <= 30)
		{
			Canvas.getActorInstance().decreaseHealth(Values.KNIFE_DAMAGE);
		}
	}
	
	private void goToBase()
	{
		int x = Canvas.getBuildingList().get(1).getX()+Canvas.getBuildingList().get(1).getWidth()/2;
		int y = Canvas.getBuildingList().get(1).getY()-100;
		
		Canvas.getEnemyFlagInstance().setAbsX(x);
		Canvas.getEnemyFlagInstance().setAbsY(y);
		
		if(this.getLeft() > Canvas.getEnemyFlagInstance().getRight())//AI to right of it's flag
		{	
			if(this.canCollide(-Values.AI_SPEED, 0)==-1)//No collision
			{
				lcoll = false;
				
				if(!bcoll)
					this.setX(-Values.AI_SPEED);
				//return;
			}
			
			if(this.canCollide(-Values.AI_SPEED, 0)!=-1)//Left Collision
			{
				lcoll = true;
				this.setY(-Values.AI_SPEED);//move up
				//return;
			}
		}
		
		if(this.getRight() < Canvas.getEnemyFlagInstance().getRight())//AI to left of it's flag
		{	
			if(this.canCollide(Values.AI_SPEED, 0)==-1)//No collision
			{
				rcoll = false;
				
				if(!tcoll)
					this.setX(Values.AI_SPEED);//move right
				//return;
			}
			
			if(this.canCollide(Values.AI_SPEED, 0)!=-1)//Right Collision
			{
				rcoll = true;
				this.setY(Values.AI_SPEED);//move down
				//return;
			}
		}
		
		if(this.getTop() > Canvas.getEnemyFlagInstance().getTop())//AI below it's flag
		{
			if(this.canCollide(0,-Values.AI_SPEED)==-1)//No collision
			{
				bcoll = false;
				
				if(!rcoll)
					this.setY(-Values.AI_SPEED);//move up
				//return;
			}
			
			if(this.canCollide(0,-Values.AI_SPEED)!=-1)//Bottom Collision
			{
				bcoll = true;
				this.setX(Values.AI_SPEED);//move right
				//return;
			}
		}
		
		if(this.getBottom() < Canvas.getEnemyFlagInstance().getBottom())//AI above it's flag
		{
			if(this.canCollide(0,Values.AI_SPEED)==-1)//No Collision
			{
				tcoll = false;
				
				if(!lcoll)
					this.setY(Values.AI_SPEED);//move down
				//return;
			}
			
			if(this.canCollide(0,Values.AI_SPEED)!=-1)//Top Collision
			{	
				tcoll = true;
				this.setX(-Values.AI_SPEED);//Move left
				//return;
			}
		}
	}
	
	private void findEnemyFlag()
	{
		Main.log.println("AI looking for enemy flag.");
		
		if(this.getLeft() > Canvas.getFlagInstance().getRight())//AI to right of enemy flag
		{	
			if(this.canCollide(-Values.AI_SPEED, 0)==-1)//No collision
			{
				lcoll = false;
				
				if(!bcoll)
					this.setX(-Values.AI_SPEED);
				//return;
			}
			
			if(this.canCollide(-Values.AI_SPEED, 0)!=-1)//Left Collision
			{
				lcoll = true;
				this.setY(-Values.AI_SPEED);//move up
				//return;
			}
		}
		
		if(this.getRight() < Canvas.getFlagInstance().getRight())//Left())//AI to left of enemy flag
		{	
			if(this.canCollide(Values.AI_SPEED, 0)==-1)//No collision
			{
				rcoll = false;
				
				if(!tcoll)
					this.setX(Values.AI_SPEED);//move right
				//return;
			}
			
			if(this.canCollide(Values.AI_SPEED, 0)!=-1)//Right Collision
			{
				rcoll = true;
				this.setY(Values.AI_SPEED);//move down
				//return;
			}
		}
		
		if(this.getTop() > Canvas.getFlagInstance().getTop())//AI below enemy flag
		{
			if(this.canCollide(0,-Values.AI_SPEED)==-1)//No collision
			{
				bcoll = false;
				
				if(!rcoll)
					this.setY(-Values.AI_SPEED);//move up
				//return;
			}
			
			if(this.canCollide(0,-Values.AI_SPEED)!=-1)//Bottom Collision
			{
				bcoll = true;
				this.setX(Values.AI_SPEED);//move right
				//return;
			}
		}
		
		if(this.getBottom() < Canvas.getFlagInstance().getBottom())//AI above enemy flag
		{
			if(this.canCollide(0,Values.AI_SPEED)==-1)//No Collision
			{
				tcoll = false;
				
				if(!lcoll)
					this.setY(Values.AI_SPEED);//move down
				//return;
			}
			
			if(this.canCollide(0,Values.AI_SPEED)!=-1)//Top Collision
			{	
				tcoll = true;
				this.setX(-Values.AI_SPEED);//Move left
				//return;
			}
		}
	}

	public void computeAI()
	{
		Main.log.println("AI Thinking.");
		
		//Calculate distance from player - Distance formula! :D
		double t1 = Math.pow(Canvas.getActorInstance().getX() - this.getX(),2);
		double t2 = Math.pow(Canvas.getActorInstance().getY() - this.getY(),2);
		distance = Math.sqrt(t1 + t2);
		
		if(!this.hasFlag())
		{
			if(distance <=150)
				followPlayer();
			else if(distance > 150)
			{
				findEnemyFlag();
				ai_shoot_up=false;
				ai_shoot_down=false;
				ai_shoot_left=false;
				ai_shoot_right=false;
			}
		}
		else if(this.hasFlag())
		{
			if(distance <=150)
				followPlayer();
			else if(distance > 150)
			{
				goToBase();
				ai_shoot_up=false;
				ai_shoot_down=false;
				ai_shoot_left=false;
				ai_shoot_right=false;
			}
		}
		
		//Follow and shoot down player if the AI is within range
		/*if(distance <= 100 )//&& !ai.hasFlag()
		{	
			if(this.hasFlag())
			{
				goToBase();
			}
			else
			{
				followPlayer();
			}
		}
		else if(distance > 100)
		{
			ai_shoot_up = false;
			ai_shoot_down = false;
			ai_shoot_left = false;
			ai_shoot_right = false;
			
			if(!this.hasFlag())
			{
				findEnemyFlag();
			}
			else
			{
				goToBase();
			}
		}*/
	}
	
	public double getDistance(){return distance;}
	
	@Override
	public void spawnActor()
	{
		int aix=0;
		//Random x coordinate in a certain range
		boolean chance = randomizer.nextBoolean(); 
		aix = chance?Canvas.getEnemyFlagInstance().getX()-randomizer.nextInt(300):(int)Canvas.getEnemyFlagInstance().getX()+randomizer.nextInt(300);
		
		//Re-instantiate AI in new location
		this.setAbsX(aix);
		this.setAbsY(Canvas.getBuildingList().get(1).getY()-50);
		
		this.restoreHealth();
		Main.log.println("Spawned AI");
	}
}