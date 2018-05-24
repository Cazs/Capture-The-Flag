package za.ac.uj.acsse.practicalx.flagcapture.Model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import za.ac.uj.acsse.practicalx.flagcapture.Assets.Actor;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Building;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Bullet;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Flag;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Particle;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.States.Values;

public class DrawVisitor implements IVisitor
{
	private Graphics g = null;
	private Random randomizer 						= new Random();
	
	public void setGraphics(Graphics g)
	{
		this.g = g;
	}
	
	@Override
	public void paint(Actor actor) 
	{
		//Draw player
		if(actor.getHealth()==0)
		{
			//if the player dies and he has the enemy flag, take it back to its base
			if(actor.hasFlag())
			{
				Canvas.enemyFlag.setAtBase(true);
				Canvas.enemyFlag.setAbsX((int) (Canvas.buildings.get(1).getX()+Canvas.buildings.get(0).getWidth()/2));
				Canvas.enemyFlag.setAbsY((int)Canvas.buildings.get(1).getY()-80);
			}
			//Respawn player
			//actor = new Actor(px,flag.getY(),true,"RED");//reinstantiate ai in new location
			Canvas.moveMapUp();
			//spawnPlayer();
			actor.setAbsX((Values.SCREEN_WIDTH/2));// = new Actor((Values.SCREEN_WIDTH/2)-x,Values.SCREEN_HEIGHT/2,true,"RED");
			actor.setAbsY((Values.SCREEN_HEIGHT/2));
			actor.restoreHealth();
			//Draw little sparkles
			if(actor.isHasParticles())
			{
				for(int i=0;i<5;i++)
				{
					//alter the coordinates a bit
					new Particle(g,(int)actor.getX()+randomizer.nextInt(10),(int)actor.getY()+randomizer.nextInt(10));
				}
			}
		}
		
		g.setColor(Color.CYAN);
		g.fillRect((int)actor.getX(),(int) actor.getY(), Values.ACTOR_WIDTH, Values.ACTOR_HEIGHT);
		
		//Draw healthbar
		g.setColor(Color.LIGHT_GRAY);
		//g.setClip(new Rectangle(10,10));
		g.drawRect(10, 10, 300, 50);
		g.setColor(Color.GREEN);
		g.fillRect(10+1, 10+1, actor.getHealth()-2, 48);
		
		//go through all bullets and update their location then render them
		actor.checkBulletCollision(g);
	}

	@Override
	public void paint(Building building)
	{
		//for(Building b:Canvas.getBuildingList())
		{
			building.setGraphics(g);
			building.drawBuilding();
		}
	}

	@Override
	public void paint(Flag flag) 
	{
		flag.setG(g);
		flag.drawFlag();
		
		Canvas.enemyFlag.setG(g);
		Canvas.enemyFlag.drawFlag();
	}

	/*@Override
	public void paint(Particle particle) 
	{
		//Draw little sparkles
		if(Canvas.getActorInstance().isHasParticles())
		{
			for(int i=0;i<5;i++)
			{
				//alter the coordinates a bit
				new Particle(g,(int)Canvas.getActorInstance().getX()+randomizer.nextInt(10),(int)Canvas.getActorInstance().getY()+randomizer.nextInt(10));
			}
		}
	}*/

	@Override
	public void paint(Bullet bullet) 
	{
		//go through all bullets and update their location then render them
		Canvas.getActorInstance().checkBulletCollision(g);
	}
}
