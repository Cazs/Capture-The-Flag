package za.ac.uj.acsse.practicalx.flagcapture.Assets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Particle
{
	private int random_particle_size 				= 0;
	private Random randomiser 						= new Random();
	private Color colour							= null;
	
	//each instance draws four particles
	public Particle(Graphics g,int x,int y)//x and y coordinates of the actor
	{
		//Random numbers will determine the colours,size and location of each particle
		colour = new Color(randomiser.nextInt(255),randomiser.nextInt(255),randomiser.nextInt(255));
		g.setColor(colour.brighter());
		random_particle_size = randomiser.nextInt(5);
		if(randomiser.nextInt(2)== 0)
			g.drawRect(x+randomiser.nextInt(10), y+5+randomiser.nextInt(10), random_particle_size+1, random_particle_size+1);
		else
			g.fillRect(x+randomiser.nextInt(10), y+5+randomiser.nextInt(10), random_particle_size+1, random_particle_size+1);
		
		colour = new Color(randomiser.nextInt(255),randomiser.nextInt(255),randomiser.nextInt(255));
		g.setColor(colour.brighter());
		random_particle_size = randomiser.nextInt(5);
		if(randomiser.nextInt(2)== 0)
			g.drawRect(x+randomiser.nextInt(10), y-15+randomiser.nextInt(10), random_particle_size+1, random_particle_size+1);
		else
			g.fillRect(x+randomiser.nextInt(10), y-15+randomiser.nextInt(10), random_particle_size+1, random_particle_size+1);
		
		colour = new Color(randomiser.nextInt(255),randomiser.nextInt(255),randomiser.nextInt(255));
		g.setColor(colour.brighter());
		random_particle_size = randomiser.nextInt(5);
		if(randomiser.nextInt(2)== 0)
			g.drawRect(x+5+randomiser.nextInt(10), y+randomiser.nextInt(10),random_particle_size+1, random_particle_size+1);
		else
			g.fillRect(x+5+randomiser.nextInt(10), y+randomiser.nextInt(10),random_particle_size+1, random_particle_size+1);
		
		colour = new Color(randomiser.nextInt(255),randomiser.nextInt(255),randomiser.nextInt(255));
		g.setColor(colour.brighter());
		random_particle_size = randomiser.nextInt(5);
		if(randomiser.nextInt(2)== 0)
			g.drawRect(x-15+randomiser.nextInt(10), y+randomiser.nextInt(10),random_particle_size+1, random_particle_size+1);
		else
			g.fillRect(x-15+randomiser.nextInt(10), y+randomiser.nextInt(10),random_particle_size+1, random_particle_size+1);
	}
}
