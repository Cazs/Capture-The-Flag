package za.ac.uj.acsse.practicalx.flagcapture.States;

import java.io.Serializable;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;


public class SaveState implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6602106298754258596L;
	//private Actor player = null;
	//private Actor ai = null;
	private int playerscore = 0;
	private int aiscore = 0;
	
	private int gameticks = 0;
	
	private int playerhp = 0;
	private int aihp = 0;
	
	public SaveState()
	{
		playerscore = Canvas.playerFlag.getScore();
		aiscore = Canvas.enemyFlag.getScore();
		gameticks = Values.GAME_TICKS;
		playerhp = Canvas.getActorInstance().getHealth();
		aihp = Canvas.getAiInstance().getHealth();
	}
	
	public int getPlayerScore(){return playerscore;}
	
	public int getAiScore(){return aiscore;}
	
	public int getGameTicks(){return gameticks;}
	
	public int getPlayerHp(){return playerhp;}
	
	public int getAiHp(){return aihp;}
	
}
