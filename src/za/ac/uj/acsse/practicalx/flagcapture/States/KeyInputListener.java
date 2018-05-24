package za.ac.uj.acsse.practicalx.flagcapture.States;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.TimerTask;

import javax.swing.Timer;

import za.ac.uj.acsse.practicalx.flagcapture.Assets.Actor;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Building;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Bullet;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;

public class KeyInputListener extends TimerTask implements KeyListener ,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3575428464068294129L;
	
	public static boolean w_state 						= false;
	public static boolean a_state 						= false;
	public static boolean s_state 						= false;
	public static boolean d_state 						= false;
	
	public static boolean up_state						= false;
	public static boolean down_state					= false;
	public static boolean left_state					= false;
	public static boolean right_state					= false;

	public static String last_y_direction 				= "";
	public static String last_x_direction 				= "";
	
	private static double SPEED_DECREASE_STEP 			= 0.04;
	private static double SPEED_INCREASE_STEP 			= 0.07;
	
	private static boolean collision 					= false;
	private static Actor actor 							= Canvas.getActorInstance();	
	private Timer timer 								= null;
	
	public KeyInputListener()
	{
		//Every BULLET_FIRE_RATEms check if a new bullet should be instantiated or not
		timer = new Timer(Values.BULLET_FIRE_RATE,new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if(up_state  && !right_state && !down_state && !left_state)
					Canvas.getActorInstance().addBullet(new Bullet((int)Canvas.getActorInstance().getX()+(Values.ACTOR_WIDTH/2), (int)Canvas.getActorInstance().getY(),5,20,"U","RED"));
			
				if(down_state && !right_state && !up_state && !left_state)
					Canvas.getActorInstance().addBullet(new Bullet((int)Canvas.getActorInstance().getX()+(Values.ACTOR_WIDTH/2), (int)Canvas.getActorInstance().getY(),5,20,"D","RED"));
				
				if(left_state && !right_state && !up_state && !down_state)
					Canvas.getActorInstance().addBullet(new Bullet((int)Canvas.getActorInstance().getX(),(int) Canvas.getActorInstance().getY()+(Values.ACTOR_HEIGHT/2),20,5,"L","RED"));
				
				if(right_state && !down_state && !up_state && !left_state)
					Canvas.getActorInstance().addBullet(new Bullet((int)Canvas.getActorInstance().getX(),(int) Canvas.getActorInstance().getY()+(Values.ACTOR_HEIGHT/2),20,5,"R","RED"));
				
			}
		});
		timer.start();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_W:
				w_state = true;
				break;
			case KeyEvent.VK_S:
				s_state = true;
				break;
			case KeyEvent.VK_A:
				a_state = true;
				break;
			case KeyEvent.VK_D:
				d_state = true;
				break;
			case KeyEvent.VK_Q:
				boolean result = Values.DEBUG_INFO ? false : true;
				Values.DEBUG_INFO = result;
				break;
			case KeyEvent.VK_ESCAPE:
				result = Values.PAUSED?false:true;
				Values.PAUSED = result;
			case KeyEvent.VK_UP:
				up_state = true;
				break;
			case KeyEvent.VK_DOWN:
				down_state = true;
				break;
			case KeyEvent.VK_LEFT:
				left_state = true;
				break;
			case KeyEvent.VK_RIGHT:
				right_state = true;
				break;
			case KeyEvent.VK_E:
				result = Canvas.getActorInstance().isHasParticles() ? false : true;
				Canvas.getActorInstance().setHasParticles(result);
				break;
			case KeyEvent.VK_R:
				result = Values.BUILDING_DATA?false:true;
				Values.BUILDING_DATA = result;
				break;
		}
	}

	public static void keyStateListener()
	{		
		if(w_state||s_state)
			if (Values.SPEED_Y < Values.MAX_SPEED)
				Values.SPEED_Y += SPEED_INCREASE_STEP;
		
		if(a_state || d_state)
			if (Values.SPEED_X < Values.MAX_SPEED)
				Values.SPEED_X += SPEED_INCREASE_STEP;
			
		
		if (w_state) 
		{
			collision = actor.canCollide(0,(int) -Values.SPEED_Y)==-1?false:true;
			
			//if there's no collision then move the whole world
			if(!collision)
			{
				Values.MINI_MAP_ACTOR_Y -= Values.SPEED_Y/Values.MINI_MAP_SPEED;
				
				if(actor.getTop() > Values.SCREEN_BOUNDARY)
					actor.setY((int) -Values.SPEED_Y);
				else
					moveBuilding((int) Values.SPEED_Y,true);		
			}
		}

		if (s_state) 
		{
			collision = actor.canCollide(0,(int) Values.SPEED_Y)==-1?false:true;
			
			//if there's no collision then move the whole world
			if(!collision)
			{
				Values.MINI_MAP_ACTOR_Y += Values.SPEED_Y/Values.MINI_MAP_SPEED;
				
				if(actor.getBottom() < Values.SCREEN_HEIGHT-Values.SCREEN_BOUNDARY)
					actor.setY((int) Values.SPEED_Y);
				else
				{
					moveBuilding((int) -Values.SPEED_Y,true);
				}
			}
		}
		
		if (a_state) 
		{
			collision = actor.canCollide((int) -Values.SPEED_X,0)==-1?false:true;

			//if there's no collision then move the whole world
			if(!collision)
			{ 
				Values.MINI_MAP_ACTOR_X -= Values.SPEED_X/Values.MINI_MAP_SPEED;
				
				if(actor.getLeft() > Values.SCREEN_BOUNDARY)	
					actor.setX((int) -Values.SPEED_X);
				else
					moveBuilding((int) Values.SPEED_X,false);
			}
		}

		if (d_state) 
		{

			collision = actor.canCollide((int) Values.SPEED_X,0)==-1?false:true;
			
			//if there's no collision then move the whole world
			if(!collision)
			{
				Values.MINI_MAP_ACTOR_X += Values.SPEED_X/Values.MINI_MAP_SPEED;
				
				if(actor.getRight() < Values.SCREEN_WIDTH-Values.SCREEN_BOUNDARY)
					actor.setX((int) Values.SPEED_X);
				else
					moveBuilding((int) -Values.SPEED_X,false);
			}
		}

		// down and up are not active
		if (!w_state && !s_state)
		{
			// Reduce the speed
			if (Values.SPEED_Y > 0)
				Values.SPEED_Y -= SPEED_DECREASE_STEP;
			else
				Values.SPEED_Y = 0;

			if (last_y_direction.equals("UP") && Values.SPEED_Y>0)//Values.SPEED_Y>0 to avoid wasting resources when speed is 0
			{
				//Check for collision
				collision = actor.canCollide(0,(int) -Values.SPEED_Y)==-1?false:true;
				//if there's no collision then move the whole world
				if(!collision)
				{
					if(Values.MINI_MAP_ACTOR_Y-Values.SPEED_Y/Values.MINI_MAP_SPEED+20 >= Values.MINI_MAP_ACTOR_Y)
						Values.MINI_MAP_ACTOR_Y -= Values.SPEED_Y/Values.MINI_MAP_SPEED;
					
					if(actor.getTop() > Values.SCREEN_BOUNDARY)
						actor.setY((int) -Values.SPEED_Y);
					else
						moveBuilding((int) Values.SPEED_Y,true);
				}
				else
				{
					Values.SPEED_Y = 0;
				}
			}
			
			if (last_y_direction.equals("DOWN")&& Values.SPEED_Y>0)
			{
				//Check for collision
				collision = actor.canCollide(0,(int) Values.SPEED_Y)==-1?false:true;
				//if there's no collision then move the whole world
				if(!collision)
				{
					if(Values.MINI_MAP_ACTOR_Y+Values.SPEED_Y/Values.MINI_MAP_SPEED+20 <= Values.MINI_MAP_ACTOR_Y+Values.MINI_MAP_HEIGHT)
						Values.MINI_MAP_ACTOR_Y += Values.SPEED_Y/Values.MINI_MAP_SPEED;
					
					if(actor.getBottom() < Values.SCREEN_HEIGHT-Values.SCREEN_BOUNDARY)
						actor.setY((int) Values.SPEED_Y);
					else
						moveBuilding((int) -Values.SPEED_Y,true);
				}
				else
				{
					Values.SPEED_Y = 0;
				}
			}
			
			if(Values.SPEED_Y == 0)
			{
				last_y_direction = "";
			}
		}

		// left and right are not active
		if (!a_state && !d_state)
		{
			// Reduce the speed
			if (Values.SPEED_X > 0)
				Values.SPEED_X -= SPEED_DECREASE_STEP;
			else
				Values.SPEED_X = 0;
			
			if (last_x_direction.equals("LEFT"))
			{
				//Check for collision
				collision = actor.canCollide((int) -Values.SPEED_X,0)==-1?false:true;
				//if there's no collision then move the whole world
				if(!collision)
				{
					if(Values.MINI_MAP_ACTOR_X-Values.SPEED_X/Values.MINI_MAP_SPEED+20 >= Values.MINI_MAP_X)
						Values.MINI_MAP_ACTOR_X -= Values.SPEED_X/Values.MINI_MAP_SPEED;
					
					if(actor.getLeft() > Values.SCREEN_BOUNDARY)	
						actor.setX((int) -Values.SPEED_X);
					else
						moveBuilding((int) Values.SPEED_X,false);
				}
			}
			else if (last_x_direction.equals("RIGHT"))
			{
				//Check for collision
				collision = actor.canCollide((int) Values.SPEED_X,0)==-1?false:true;
				//if there's no collision then move the whole world
				if(!collision)
				{
					if(Values.MINI_MAP_ACTOR_X+Values.SPEED_X/Values.MINI_MAP_SPEED+20 <= Values.MINI_MAP_X+Values.MINI_MAP_WIDTH)
						Values.MINI_MAP_ACTOR_X += Values.SPEED_X/Values.MINI_MAP_SPEED;
					
					if(actor.getRight() < Values.SCREEN_WIDTH-Values.SCREEN_BOUNDARY)
						actor.setX((int) Values.SPEED_X);
					else
						moveBuilding((int) -Values.SPEED_X,false);
				}
			}
			
			if(Values.SPEED_X == 0)
			{
				last_x_direction = "";
			}
		}
	}
	
	private static void moveBuilding(int inc,boolean yaxis) throws ConcurrentModificationException
	{
		if(yaxis)
		{
			//Move the buildings
			for (Building building : Canvas.getBuildingList())
				if (building != null)
				{
					building.setY(inc);
				}
			
			//Move the bullets in relation to the buildings
			if(!Canvas.getActorInstance().getBullets().isEmpty())
			{
				try
				{
					//Move player bullets
					for(Bullet bullet:Canvas.getActorInstance().getBullets())
					{
						if(bullet!=null)
						{
							if(inc>=0)
								bullet.setY((int) Values.SPEED_Y);
							else
								bullet.setY((int) -Values.SPEED_Y);
						}
					}
					//Move AI bullets
					for(Bullet bullet:Canvas.getAiInstance().getBullets())
					{
						if(bullet!=null)
						{
							if(inc>=0)
								bullet.setY((int) Values.SPEED_Y);
							else
								bullet.setY((int) -Values.SPEED_Y);
						}
					}
				}
				catch(ConcurrentModificationException e)
				{
					e.printStackTrace();
					Main.log.println(e.getMessage());
				}
			}
			
			//Move the flags in relation to the buildings
			//Canvas.playerFlag.setY(inc);
			//Canvas.enemyFlag.setY(inc);
			//Move the AI in relation to the buildings
			Canvas.getAiInstance().setY(inc);
		}
		else
		{
			for (Building building : Canvas.getBuildingList())
				if (building != null)
				{
					building.setX(inc);
				}
			
			//Move the bullets in relation to the buildings
			if(!Canvas.getActorInstance().getBullets().isEmpty())
			{
				try
				{
					for(Bullet bullet:Canvas.getActorInstance().getBullets())
					{
						if(bullet != null)
						{
							if(inc>=0)
								bullet.setX((int) Values.SPEED_X);
							else
								bullet.setX((int) -Values.SPEED_X);
						}
					}
				}
				catch(ConcurrentModificationException e)
				{
					e.printStackTrace();
					Main.log.println(e.getMessage());
				}
			}
			//Move flags in relation to the buildings
			//Canvas.playerFlag.setX(inc);
			//Canvas.enemyFlag.setX(inc);
			//Move AI in relation to the buildings
			Canvas.getAiInstance().setX(inc);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{
			case KeyEvent.VK_W:
				w_state = false;
				last_y_direction = "UP";
				break;
			case KeyEvent.VK_S:
				s_state = false;
				last_y_direction = "DOWN";
				break;
			case KeyEvent.VK_A:
				a_state = false;
				last_x_direction = "LEFT";
				break;
			case KeyEvent.VK_D:
				d_state = false;
				last_x_direction = "RIGHT";
				break;
			case KeyEvent.VK_UP:
				up_state = false;
				break;
			case KeyEvent.VK_DOWN:
				down_state = false;
				break;
			case KeyEvent.VK_LEFT:
				left_state = false;
				break;
			case KeyEvent.VK_RIGHT:
				right_state = false;
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void run()
	{
		keyStateListener();
	}
}
