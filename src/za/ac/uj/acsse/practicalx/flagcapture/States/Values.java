package za.ac.uj.acsse.practicalx.flagcapture.States;

public class Values 
{
	public static int SCREEN_WIDTH   				= 940;
	public static int SCREEN_HEIGHT  				= 680;
	public static int ACTOR_WIDTH	 				= 20;
	public static int ACTOR_HEIGHT	 				= 20;
	public static double FPS 	 					= 0;
	public static double SPEED_X 					= 0;
	public static double SPEED_Y 					= 0;
	public static int MAX_SPEED 					= 5;
	//Global game ticks counter, keeps track of how much time has elapsed since game was executed.
	public static int GAME_TICKS 					= 0;
	public static boolean DEBUG_INFO				= false;
	public static boolean BUILDING_DATA				= false;
	public static int SCREEN_BOUNDARY				= 100;
	public static int MINI_MAP_WIDTH				= 200;
	public static int MINI_MAP_HEIGHT				= 240;
	public static int MINI_MAP_X					= SCREEN_WIDTH-MINI_MAP_WIDTH;
	public static int MINI_MAP_Y					= SCREEN_HEIGHT-MINI_MAP_HEIGHT;
	public static double MINI_MAP_ACTOR_X			= MINI_MAP_X+MINI_MAP_WIDTH/2;
	public static double MINI_MAP_ACTOR_Y			= MINI_MAP_Y+MINI_MAP_HEIGHT/2;
	public static double MINI_MAP_SPEED				= 22.5;//og 35.2
	public static int MAX_HEIGHT_NEG				= -3540;
	public static int MAX_HEIGHT_POS				= 3500;
	public static int MAX_WIDTH_POS					= 3000;
	public static int MAX_WIDTH_NEG					= -3000;
	public static double BULLET_SPEED				= 15;
	public static int BULLET_FIRE_RATE				= 50;
	public static int AI_SPEED						= 5;
	public static int AI_FIRE_RATE					= 100;
	public static int BULLET_DAMAGE					= 7;
	public static int KNIFE_DAMAGE					= 3;
	public static int GAME_TIME						= 300;
	public static boolean PAUSED					= false;
	public static boolean STUCK						= false;
	public static boolean START						= true;
	public static boolean MULTIPLAYER				= false;
}
