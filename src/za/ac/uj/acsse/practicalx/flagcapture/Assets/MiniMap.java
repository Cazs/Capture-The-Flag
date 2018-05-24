package za.ac.uj.acsse.practicalx.flagcapture.Assets;

import java.awt.Color;
import java.awt.Graphics;

import za.ac.uj.acsse.practicalx.flagcapture.States.Values;


public class MiniMap 
{
	private Graphics g		= null;
	
	public void drawMap()
	{
		g.setColor(Color.RED);
		g.drawRect(Values.MINI_MAP_X-18, Values.MINI_MAP_Y-40, Values.MINI_MAP_WIDTH, Values.MINI_MAP_HEIGHT);
		//draw player position on minimap
		g.fill3DRect((int)Values.MINI_MAP_ACTOR_X, (int)Values.MINI_MAP_ACTOR_Y, 10, 10, true);
	}
	
	public void setGraphics(Graphics g)
	{
		this.g = g;
	}
}
