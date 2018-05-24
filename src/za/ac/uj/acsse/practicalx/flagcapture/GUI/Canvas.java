package za.ac.uj.acsse.practicalx.flagcapture.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import za.ac.uj.acsse.practicalx.flagcapture.Assets.Actor;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Building;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Bullet;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Flag;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.MiniMap;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.Particle;
import za.ac.uj.acsse.practicalx.flagcapture.Server.PollServer;
import za.ac.uj.acsse.practicalx.flagcapture.Server.UpdateServer;
import za.ac.uj.acsse.practicalx.flagcapture.States.KeyInputListener;
import za.ac.uj.acsse.practicalx.flagcapture.States.Start;
import za.ac.uj.acsse.practicalx.flagcapture.States.Values;
import za.ac.uj.acsse.practicalx.flagcapture.Assets.AI;


public class Canvas extends JPanel
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7865665478736055847L;
	private static long id							= 0; 
	private static Graphics g 	  		  			= null;
	private Random randomizer 						= new Random();
	private Font font								= null;		
	//initial position of player is off the grid to avoid collisions when moving the buildings down
	private static Actor actor 						= null;
	private File building_file 						= null;
	public static ArrayList<Building> buildings		= new ArrayList<Building>();
	private static MiniMap minimap					= null;
	public static Flag playerFlag					= null;
	public static Flag enemyFlag					= null;
	public static int flag1x						= 0;
	public static int flag1y						= 0;
	public static int flag2x						= 0;
	public static int flag2y						= 0;
	private int mcounter							= 0;
	//Winner
	private static String winner					= "";
	private static String loser						= "";
	//Drawer
	//private DrawVisitor dv							= new DrawVisitor();
	//private ArrayList<GameAsset> assets				= new ArrayList<GameAsset>();
	//AI
	private static AI ai 							= null;
	private static Actor player2					= new Actor(-5000,-5000,true,"BLUE");
	//private static UpdateServer update 				= new UpdateServer();
	//private static PollServer poll					= new PollServer();
	
	public static Actor getPlayer2Instance(){return player2;}
	
	public Canvas(String building_path)
	{
		player2.setID("John_Doe");
		/*
			try 
			{
				//update.sendGetRequest();
				//update.sendPostRequest();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
				System.err.println(e1.getMessage());
			}
		*/
		
		//instantiate
		actor = new Actor(-5000,-5000,true,"RED");
		ai = new AI(Values.SCREEN_WIDTH/2,Values.SCREEN_HEIGHT/2,false,"BLUE");
		ai.setID("Joe_Soap");
		//Load font
		try 
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("8-BIT WONDER.TTF"));
			
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
		 
		    Main.log.println("Canvas font file loaded.");
		}
		catch (FileNotFoundException e) 
		{
			Main.log.println(e.getMessage());
		}
		catch (FontFormatException e) 
		{
			Main.log.println(e.getMessage());
		}
		catch (IOException e) 
		{
			Main.log.println(e.getMessage());
		}
		
		building_file = new File(building_path);
		
		if(!building_file.exists())
		{
			Main.log.println("Level not loaded, file not found or corrupt.");
		
			JOptionPane.showMessageDialog(null,"Level file not found.","The level file could not be found, please make sure level01.dat is in the root directory of the program",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			return;
		}
		else
		{
			Main.log.println("Reading level.");
			readLevel();
		}
		
		//Minimap stuff
		minimap = new MiniMap();
		Values.MINI_MAP_ACTOR_X = (Values.MINI_MAP_X)+Values.MINI_MAP_WIDTH/2-20;
		Values.MINI_MAP_ACTOR_Y = (Values.MINI_MAP_Y)+Values.MINI_MAP_HEIGHT/2-40;
		
		moveMapUp();
		actor.spawnActor();
		ai.spawnActor();
		//enemyFlag.spawnFlag(buildings.get(1).getX()+buildings.get(1).getWidth()/2,buildings.get(1).getY()-150);
		
		//int x = Canvas.getBuildingList().get(0).getX()+Canvas.getBuildingList().get(0).getWidth()/2;
		//int y = Canvas.getBuildingList().get(0).getY()+40;
		//playerFlag.spawnFlag(Values.SCREEN_WIDTH/2,Values.SCREEN_HEIGHT/2);
		//playerFlag.spawnFlag(buildings.get(0).getX()+buildings.get(0).getWidth()/2, buildings.get(0).getY()+60);
	}
	
	public void revertToDefault() throws IOException
	{
		moveMapUp();
		//Respawn player
		actor.spawnActor();
		//Respawn AI
		ai.spawnActor();
		//Respawn playerFlag
		playerFlag.resetPoints();
		//playerFlag.setAtBase(true);
		//playerFlag.spawnFlag(buildings.get(0).getX()+buildings.get(0).getWidth()/2, buildings.get(0).getY()+60);
		//Respawn enemyFlag
		enemyFlag.resetPoints();
		//enemyFlag.setAtBase(true);
		//enemyFlag.spawnFlag(buildings.get(1).getX()+buildings.get(0).getWidth()/2, buildings.get(1).getY()-80);
		
		Main.log.println("Game reverted to default state.");
	}
	
	private void readLevel()
	{
		int flags = 0;
		buildings = new ArrayList<Building>();
		
		Scanner reader = null;
		try
		{
			reader = new Scanner(building_file);
			String line = "";
			while(reader.hasNext())
			{
				line = reader.next();
				StringTokenizer tokenizer = new StringTokenizer(line,"#");
				if(Building.validate(line))
				{
					String xx = tokenizer.nextToken();
					int x = Integer.valueOf(xx.substring(1, xx.length()));
					int y = Integer.valueOf(tokenizer.nextToken());
					int w = Integer.valueOf(tokenizer.nextToken());
					int h = Integer.valueOf(tokenizer.nextToken());
					String number = tokenizer.nextToken();
					
					@SuppressWarnings("unused")
					String colour = tokenizer.nextToken();
					
					boolean is3D = Boolean.valueOf(tokenizer.nextToken());
					
					buildings.add(new Building(x,y,w,h,Color.CYAN,is3D,number));
				}
				else if(Flag.validateFlag(line))
				{
					//Read and store both flags
					String xx = tokenizer.nextToken();
					int x = Integer.valueOf(xx.substring(1, xx.length()));
					int y = Integer.valueOf(tokenizer.nextToken());
					String faction = tokenizer.nextToken();
					
					if(flags == 0)
					{
						playerFlag = new Flag(x,y,faction);
						//flag1x = x;
						//flag1y = y;
					}
					else
					{
						enemyFlag = new Flag(x,y,faction);
						//flag2x = x;
						//flag2y = y;
					}
					flags++;
				}
			}
			Main.log.println("Reading level.");
		}
		catch(FileNotFoundException e)
		{
			Main.log.println(e.getMessage());
		}
	}
	
	public static ArrayList<Building> getBuildingList(){return buildings;}
	
	@Override
	protected void paintComponent(final Graphics g)
	{
		Canvas.g = g;
		
		//Clear and redraw
		Canvas.g.setColor(Color.BLACK);
		Canvas.g.fillRect(0, 0, getWidth(), getHeight());
		
		//Update Window Dimensions
		Values.SCREEN_HEIGHT = getHeight();//new width
		Values.SCREEN_WIDTH = getWidth();
		
		//Update Minimap Dimension
		Values.MINI_MAP_X = Values.SCREEN_WIDTH-Values.MINI_MAP_WIDTH;
		Values.MINI_MAP_Y = Values.SCREEN_HEIGHT-Values.MINI_MAP_HEIGHT;
		
		//Draw time
		Font f = g.getFont();
		g.setFont(new Font("8BIT WONDER",Font.PLAIN,36));
		g.setColor(Color.ORANGE);
		g.drawString(""+(Values.GAME_TIME-(Values.GAME_TICKS/1000)), (Values.SCREEN_WIDTH/2)-40,30);
		
		
		//Draw scores
		g.setFont(new Font("8BIT WONDER",Font.PLAIN,28));
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(Values.SCREEN_WIDTH-310, 10, 300, 50);
		g.setColor(Color.RED);
		g.fillRect(Values.SCREEN_WIDTH-309, 11, 150, 48);
		g.setColor(Color.BLUE);
		g.fillRect(Values.SCREEN_WIDTH-160, 11, 148, 48);
		g.setColor(Color.BLACK);
		g.drawString(""+playerFlag.getScore(),Values.SCREEN_WIDTH-309+70 , 45);
		g.drawString(""+enemyFlag.getScore(),Values.SCREEN_WIDTH-100 , 45);
		//reset font back to original
		g.setFont(new Font(f.getName(),Font.PLAIN,12));
		
		//Minimap
		if(mcounter==0)
		{
			Values.MINI_MAP_ACTOR_X = Values.MINI_MAP_X+Values.MINI_MAP_WIDTH/2-20;
			Values.MINI_MAP_ACTOR_Y = Values.MINI_MAP_Y-40;//+Values.MINI_MAP_HEIGHT/2-40;
			mcounter++;
		}
		
		minimap.setGraphics(g);
		minimap.drawMap();
		
		//populate array
		/*for(Building building:buildings)
		{
			assets.add(building);
		}
		assets.add(playerFlag);
		assets.add(enemyFlag);
		assets.add(actor);
		assets.add(ai);
		for(GameAsset ga: assets)
		{
			ga.accept(dv);
		}*/
		//Buildings
		for(Building building:buildings)
		{
			building.setGraphics(g);
			building.drawBuilding();
		}
		
		/***Begin Flags***/
		playerFlag.setG(g);
		playerFlag.drawFlag();
		
		enemyFlag.setG(g);
		enemyFlag.drawFlag();
		/***End Flags***/
		/*******************Player2 Begin********************/
		//Draw player2
		g.setColor(Color.GREEN);
		g.fillRect(player2.getX(), player2.getY(), Values.ACTOR_WIDTH, Values.ACTOR_HEIGHT);
		/*******************Player2 End********************/
		
		/*******************Player Begin********************/
		//Draw player
		if(actor.getHealth()==0)
		{
			moveMapUp();
			//if the player dies and he has the enemy playerFlag, take it back to its base
			if(actor.hasFlag())
			{
				enemyFlag.setAtBase(true);
			}
			//Respawn player
			actor.spawnActor();
		}
		
		Canvas.g.setColor(Color.CYAN);
		Canvas.g.fillRect(actor.getX(),actor.getY(), Values.ACTOR_WIDTH, Values.ACTOR_HEIGHT);
		
		/*try
		{
			//String response = update.updateServer(actor.getID(), actor.getX(), actor.getY());
			String response = poll.pollServer(actor.getID());
			//if(!response.equals("SUCCESSFUL"))
			//{
				//Main.log.println("Could not update player data on server, force quiting");
				//System.exit(0);
			//}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(0);
		}*/
		
		//Draw healthbar
		g.setColor(Color.LIGHT_GRAY);
		//g.setClip(new Rectangle(10,10));
		g.drawRect(10, 10, 300, 50);
		g.setColor(Color.GREEN);
		g.fillRect(10+1, 10+1, actor.getHealth()-2, 48);
		
		//Go through all bullets and update their location then render them
		actor.checkBulletCollision(g);
		
		//Draw little sparks
		if(actor.isHasParticles())
		{
			for(int i=0;i<5;i++)
			{
				new Particle(g,(int)actor.getX()+randomizer.nextInt(10),(int)actor.getY()+randomizer.nextInt(10));//alter the coordinates a bit
			}
		}
		/*******************Player End********************/
		
		/*******************AI Begin*********************/
		//Draw AI
		//If the AI has no health, respawn him at his base
		if(ai.getHealth()==0)
		{
			//if the AI dies and he has the playerFlag, reset it back to its original place
			if(ai.hasFlag())
			{
				playerFlag.setAtBase(true);
				//playerFlag.spawnFlag(buildings.get(0).getX()+buildings.get(0).getWidth()/2, buildings.get(0).getY()+60);
			}
			//Respawn AI
			ai.spawnActor();
		}
		Canvas.g.setColor(Color.MAGENTA);
		Canvas.g.fillRect((int)ai.getX(), (int)ai.getY(), Values.ACTOR_WIDTH, Values.ACTOR_HEIGHT);
		//Update AI location on DB
		/*try
		{
			//String response = update.updateServer(ai.getID(), ai.getX(), ai.getY());
			//response = poll.pollServer(ai.getID());
			//if(!response.equals("SUCCESSFUL"))
			//{
				//Main.log.println("Could not update player data on server, force quiting");
				//System.exit(0);
			//}
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			System.exit(0);
		}*/
		
		//Move AI
		ai.computeAI();
		
		//Go through all AI bullets, check their collision, update their location then render them
		ai.checkBulletCollision(g);
		
		//Draw AI healthbar
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect((int)ai.getX()-40, (int)ai.getY()-20, 100, 10);
		g.setColor(Color.GREEN);
		double health = (ai.getHealth()/300f);
		g.fillRect((int)ai.getX()-40,(int) ai.getY()-20, (int) (health*100), 10);
		/*******************AI End*********************/
		
		if(Values.DEBUG_INFO)
		{
			Canvas.g.drawString("["+actor.getX()+","+actor.getY()+"]",(int) actor.getX()-10, (int)actor.getY());
			addString("FPS: " + Values.FPS,0,10);
			addString("W state :" + KeyInputListener.w_state,0,20);
			addString("A state :" + KeyInputListener.a_state,0,30);
			addString("S state :" + KeyInputListener.s_state,0,40);
			addString("D state :" + KeyInputListener.d_state,0,50);
			addString("Current speed on x axis :" + Values.SPEED_X,0,60);
			addString("Current speed on y axis :" + Values.SPEED_Y,0,70);
			addString("Max speed: "+ Values.MAX_SPEED,0,80);
			addString("Coordinates: [x: " + actor.getX() + " y: " + actor.getY() + "]",0,90);
			addString("Number of bullets fired by player:"+actor.getBullets().size(),0,100);
			addString("Number of bullets fired by AI:"+ai.getBullets().size(),0,110);
			addString("Distance betweeen player and AI: "+ ai.getDistance(),0,120);
			
			int px = Canvas.getBuildingList().get(0).getX()+Canvas.getBuildingList().get(0).getWidth()/2;
			int py = Canvas.getBuildingList().get(0).getY()+40;
			
			double t1 = Math.pow((px-ai.getX()), 2);
			double t2 = Math.pow((py-ai.getY()), 2);
			double result = Math.sqrt(t1+t2);
			
			//double t1 = Math.pow((playerFlag.getX()-enemyFlag.getX()), 2);
			//double t2 = Math.pow((playerFlag.getY()-enemyFlag.getY()), 2);
			addString("Distance between AI and player flag: "+result,0,130);
		}
	}
	
	public static boolean checkGameState()
	{
		if(Values.GAME_TIME-(Values.GAME_TICKS/1000) <= 0)
		{
			if(playerFlag != null && enemyFlag != null)
			{
				if(playerFlag.getScore() > enemyFlag.getScore())
				{
					winner = playerFlag.getFaction()+" team wins the match with "+playerFlag.getScore()+" points";// ("+enemyFlag.getFaction()+" team got "+enemyFlag.getScore()+" points)";
					loser = enemyFlag.getFaction()+" team loses the match with "+enemyFlag.getScore()+" points";
				}
				
				if(enemyFlag.getScore() > playerFlag.getScore())
				{
					winner = enemyFlag.getFaction()+" team wins the match with "+enemyFlag.getScore()+" points";// ("+playerFlag.getFaction()+" team got "+playerFlag.getScore()+" points)";
					loser = playerFlag.getFaction()+" team loses the match with "+playerFlag.getScore()+" points";
				}
				
				if(playerFlag.getScore() == enemyFlag.getScore())
				{
					winner = "DRAW RED Team got "+playerFlag.getScore()+" and BLUE Team got "+enemyFlag.getScore()+" points" ;
					loser = "";//"DRAW RED Team got "+playerFlag.getScore()+" and BLUE Team got "+enemyFlag.getScore()+" points" ;
				}
			}
			Main.log.println("Game over.");
			return true;
		}
		return false;
	}
	
	public static String getLoser()				{return loser;}
	
	public static String getWinner()			{return winner;}
	
	public static Actor getActorInstance()		{return actor;}
		
	public static Actor getAiInstance()			{return ai;}
	
	public static Flag getFlagInstance()		{return playerFlag;}
	
	public static Flag getEnemyFlagInstance()	{return enemyFlag;}
	
	private static void addString(String str,int x,int y)
	{
		g.setColor(Color.GREEN);
		g.drawString(str, x, y);
	}
	
	public static void moveMapUp()
	{
		//Move the map down so the player can spawn by their base 
		boolean top = false;
		
		while(!top)
		{
			if(buildings.get(0).getY() >= Values.SCREEN_HEIGHT/2-20)
			{
				top = true;
			}
			else
			{
				//Move buildings
				for(Building building:buildings)
				{
					building.setY(1);
				}
				//Move AI bullets
				for(Bullet b:ai.getBullets())
				{
					b.setY(1);
				}
				//Move player bullets
				for(Bullet b:actor.getBullets())
				{
					b.setY(1);
				}
				//Move AI
				ai.setY(1);
			}
		}
		
		Main.log.println("Map shifted.");
	}
}
