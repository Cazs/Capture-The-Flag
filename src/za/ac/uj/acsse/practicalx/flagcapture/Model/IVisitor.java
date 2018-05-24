package za.ac.uj.acsse.practicalx.flagcapture.Model;

import za.ac.uj.acsse.practicalx.flagcapture.Assets.Actor;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Building;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Bullet;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Flag;

public interface IVisitor 
{
	public void paint(Actor actor);
	public void paint(Building building);
	public void paint(Flag flag);
	//public void paint(Particle particle);
	public void paint(Bullet bullet);
}
