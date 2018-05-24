package za.ac.uj.acsse.practicalx.flagcapture.Assets;

import za.ac.uj.acsse.practicalx.flagcapture.Model.GameAsset;
import za.ac.uj.acsse.practicalx.flagcapture.Model.IVisitor;

public class Bullet extends GameAsset
{
	private String direction						= "";
	
	public Bullet(int x,int y, int w,int h, String direction,String faction)
	{
		super(x,y,w,h,faction);
		this.direction = direction;
	}
	
	public String getDirection()
	{
		return direction;
	}

	@Override
	public void accept(IVisitor visitor) 
	{
		visitor.paint(this);
	}

}
