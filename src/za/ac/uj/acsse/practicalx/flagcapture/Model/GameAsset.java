package za.ac.uj.acsse.practicalx.flagcapture.Model;

public abstract class GameAsset  implements IVisitable
{
	private int x 									= 0;
	private int y									= 0;
	private int w									= 0;
	private int h									= 0;
	private String faction							= "";
	
	public GameAsset(int x,int y,int w,int h,String faction)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.faction = faction;
	}
	
	public int getX() 
	{
		return x;
	}

	public void setX(int x) 
	{
		this.x += x;
	}

	public int getY() 
	{
		return y;
	}

	public void setY(int y) 
	{
		this.y += y;
	}

	public int getWidth()
	{
		return w;
	}

	public void setWidth(int w)
	{
		this.w = w;
	}

	public int getHeight()
	{
		return h;
	}

	public void setHeight(int h)
	{
		this.h = h;
	}

	public String getFaction() 
	{
		return faction;
	}

	public void setFaction(String faction) 
	{
		this.faction = faction;
	}

	public int getRight()
	{
		return (x+w);
	}
	
	public int getBottom()
	{
		return y+h;
	}

	public int getLeft(){return x;}
		
	public int getTop(){return y;}
	
	public void setAbsX(int x){this.x = x;}
	
	public void setAbsY(int y){this.y = y;}

	@Override
	public abstract void accept(IVisitor visitor);
}
