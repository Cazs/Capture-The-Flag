package za.ac.uj.acsse.practicalx.flagcapture.GUI;

/**
 * 8BIT WONDER.TTF Font from http://www.dafont.com/8bit-wonder.font
 */

import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import za.ac.uj.acsse.practicalx.flagcapture.Server.PollServer;
import za.ac.uj.acsse.practicalx.flagcapture.Server.UpdateServer;
import za.ac.uj.acsse.practicalx.flagcapture.States.GameOver;
import za.ac.uj.acsse.practicalx.flagcapture.States.KeyInputListener;
import za.ac.uj.acsse.practicalx.flagcapture.States.Multiplayer;
import za.ac.uj.acsse.practicalx.flagcapture.States.Paused;
import za.ac.uj.acsse.practicalx.flagcapture.States.SaveState;
import za.ac.uj.acsse.practicalx.flagcapture.States.Start;
import za.ac.uj.acsse.practicalx.flagcapture.States.Values;

public class Main extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3321476605429177105L;
	
	private static int frame_time					= 0;
	private static double ticks						= 0;
	private static int current						= 0;
	private static int previous						= 0;
	private static double frames					= 0;
	private final static int REFRESH_DELAY			= 25;
	//private static javax.swing.Timer refresh_timer= null;
	private static javax.swing.Timer screen_timer	= null;
	private static Canvas canvas 	 				= null;
	
	private JMenuBar menubar						= new JMenuBar();
	private JMenu	multi							= new JMenu("Multiplayer");
	private JTextField username						= new JTextField();
	private JButton start_multi						= new JButton("Start!");
	
	private JMenu	file							= new JMenu("Menu");
	private JMenuItem open							= new JMenuItem("Open a save file");
	private JMenuItem exit							= new JMenuItem("Exit");
	private static GameOver gameover 				= null;
	private static Paused paused					= null;
	private static boolean go						= false;
	public static boolean restart					= false;
	public static boolean p_restart					= false;
	private static boolean canvas_removed			= false;;
	public static boolean start_added				= false;
	public static boolean multiplayer_added			= false;
	public static boolean multiplayer				= false;
	
	public static PrintWriter log 					= null;
	private static Thread loop_thread				= null;
	private static Start start_screen				= null;
	private static Multiplayer multiplayer_screen			= null;
	private static PollServer poll					= new PollServer();
	private static UpdateServer update				= new UpdateServer();
	public static boolean start						= true;
	private static javax.swing.Timer server_timer			= null;
	
	public Main()
	{
		//Set the size of the screen to the actual size of the physical screen
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		
		Values.SCREEN_WIDTH = (int) screen_size.getWidth();
		Values.SCREEN_HEIGHT = (int) screen_size.getHeight();
		
		try 
		{
			log = new PrintWriter("log.log");
		} 
		catch (FileNotFoundException e1) 
		{
			System.exit(0);
			e1.printStackTrace();
		}
		
		username.setColumns(5);
		multi.add(new JLabel("Enter username below"));
		multi.add(username);
		multi.add(start_multi);
		
		file.add(open);
		file.add(exit);
		
		menubar.add(file);
		menubar.add(multi);
		
		this.setJMenuBar(menubar);
		
		canvas = new Canvas("./level01.dat");//Level
		
		start_multi.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(!username.getText().isEmpty())
				{
					Canvas.getActorInstance().setID(username.getText());
					server_timer.start();
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Please enter a valid username","Invalid Username",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		exit.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				log.println("Exiting.");
				log.flush();
				log.close();
				System.exit(0);
			}
		});
		
		open.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				
				ObjectInputStream ois;
				try 
				{
					JFileChooser chooser = new JFileChooser();
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.addChoosableFileFilter(new FileNameExtensionFilter("binary files", "bin"));
					int result = chooser.showOpenDialog(getContentPane());
					
					if(result == JFileChooser.APPROVE_OPTION)
					{
						
						ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(chooser.getSelectedFile().getAbsolutePath())));
						
						SaveState ss = (SaveState) ois.readObject();
						
						Canvas.getActorInstance().setHealth(ss.getPlayerHp());
						Canvas.getAiInstance().setHealth(ss.getAiHp());
						Canvas.playerFlag.setScore(ss.getPlayerScore());
						Canvas.enemyFlag.setScore(ss.getAiScore());
						Values.GAME_TICKS = ss.getGameTicks();
						
						ois.close();
						
						getContentPane().validate();
						getContentPane().repaint();
						
						log.println("Game save loaded.");
					}
				} 
				catch
				(FileNotFoundException e) 
				{
					log.println("Game save:" + e.getMessage());
					JOptionPane.showMessageDialog(null,"Could not load save file because it is either corrupt or missing, loading new game","Save file corrupt or missing.",JOptionPane.ERROR_MESSAGE);
				}
				catch (IOException e) 
				{
					log.println("Game save:" + e.getMessage());
					JOptionPane.showMessageDialog(null,"Could not load save file because it is either corrupt or missing, loading new game","Save file corrupt or missing.",JOptionPane.ERROR_MESSAGE);
				}
				catch (ClassNotFoundException e) 
				{
					log.println("Game save:" + e.getMessage());
					JOptionPane.showMessageDialog(null,"Could not load save file because it is either corrupt or missing, loading new game","Save file corrupt or missing.",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);//Maximize window
		this.setUndecorated(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		//setResizable(false);
		
		start_screen = new Start();
		multiplayer_screen = new Multiplayer();
		
		add(canvas);
		
		screen_timer = new javax.swing.Timer(100,new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent ev) 
			{
				
				if(start)
				{
					canvas_removed = true;
					getContentPane().removeAll();
					getContentPane().add(start_screen);
					getContentPane().repaint();
					getContentPane().validate();
					Values.GAME_TICKS = 0;
					
					if(multiplayer)
					{
						//getContentPane().removeAll();
						getContentPane().remove(start_screen);
						getContentPane().add(multiplayer_screen);
						getContentPane().repaint();
						getContentPane().validate();
						multiplayer_added = true;
					}
				}
				else if(restart)
				{
					getContentPane().removeAll();
					restart = false;
					getContentPane().add(canvas);
					getContentPane().validate();
					getContentPane().repaint();
					
					Values.GAME_TICKS = 0;
					
					try 
					{
						canvas.revertToDefault();
					}
					catch (IOException e) 
					{
						log.println("Restart: "+e.getMessage());
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(null,"Game has been restarted", "Restart",JOptionPane.INFORMATION_MESSAGE);
					//System.out.println("Restarted");
				}
				else if(go)//Game over
				{
					getContentPane().removeAll();//(canvas);
					getContentPane().add(gameover);
					getContentPane().validate();
					getContentPane().repaint();
					//return;
				}
				//load screens
				else if(Values.PAUSED)
				{
					if(p_restart)//if restarted from pause menu
					{
						Values.PAUSED = false;
						canvas_removed = false;
						getContentPane().removeAll();//(paused);
						restart = false;
						getContentPane().add(canvas);
						getContentPane().validate();
						getContentPane().repaint();
						//refresh_timer.restart();
						//loop_thread.yield();
						Values.GAME_TICKS = 0;
						//loop_thread.start();
						try 
						{
							canvas.revertToDefault();
						}
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						JOptionPane.showMessageDialog(null,"Game has been restarted", "Restart",JOptionPane.INFORMATION_MESSAGE);
						p_restart = false;
						//return;
					}
					else
					{
						canvas_removed= true;
						//remove canvas and repaint
						getContentPane().removeAll();//(canvas);
						getContentPane().repaint();
						getContentPane().validate();
						
						//add paused menu and repaint
						getContentPane().add(paused);
						getContentPane().repaint();
						getContentPane().validate();
						//loop_thread.yield();
						//refresh_timer.stop();
						//game_timer.cancel();
						KeyInputListener.up_state = false;
					}
					System.out.println(KeyInputListener.up_state);
					//return;
				}
				else
				{
					//getContentPane().setFocusable(true);
					//System.out.println(getContentPane().isFocusCycleRoot());
					if(canvas_removed)
					{
						getContentPane().removeAll();
						getContentPane().add(canvas);
						getContentPane().repaint();
						getContentPane().validate();
						//JOptionPane.showMessageDialog(null, "Starting.");
						canvas_removed = false;
					}
				}
				/**
				 * For some reason, it becomes impossible to unpause, I assume the keylistener stops
				 * listening here because this loses foucs. Below is how I solved it.
				 */
				
				if(Values.STUCK)  
				{
					getContentPane().removeAll();//(paused);
					
					paused = new Paused();
					
					getContentPane().add(paused);
					
					getContentPane().validate();
					getContentPane().repaint();
					
					Values.STUCK = false;
				}
			}
		});
		screen_timer.start();
	}
	
	public static void main(String[] args)
	{
		final Main main = new Main();
		/****Start server polling and updating****/
		server_timer = new javax.swing.Timer(1000,new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String response = update.updateServer(Canvas.getActorInstance().getID(), Canvas.getActorInstance().getX(), Canvas.getActorInstance().getY());
					response = poll.pollServer(Canvas.getPlayer2Instance().getID());
					
					int x = Integer.valueOf(response.substring(0, response.indexOf(",")));
					int y = Integer.valueOf(response.substring(response.indexOf(",")+1,response.length()));
					
					System.out.println(x);
					System.out.println(y);
					
					Canvas.getPlayer2Instance().setAbsX(x);
					Canvas.getPlayer2Instance().setAbsY(y);
					
					//if(!response.equals("SUCCESSFUL"))
					//{
						//Main.log.println("Could not update player data on server, force quiting");
						//System.exit(0);
					//}
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
					log.println(e1.getMessage());
					System.exit(0);
				}
			}
		});
		//server_timer.start();
		/****End server polling and updating****/
		
		log.println("Starting game.");
		
		paused = new Paused();
		gameover = new GameOver();
		
		//key listener and timertask [listen for key presses at a specified interval]
		final KeyInputListener listen = new KeyInputListener();
		
		//Add key listener and set visibility
		main.addKeyListener(listen);
		main.setVisible(true);
		main.setFocusable(true);
		
		/*********************BEGINNIG OF MY GAME LOOP*******************/
		loop_thread = new Thread(new Runnable() 
		{
			
			@Override
			public void run() 
			{
				while(true)
				{
					try 
					{
						if(!Values.PAUSED)
						{
							if(Canvas.checkGameState())
							{
								go = true;
								Values.GAME_TICKS=0;
								//refresh_timer.stop();
							}
							else
							{
								go = false;
							}
							current = Values.GAME_TICKS;
							frame_time = current - previous;
							if(ticks+frame_time >= 1000)
							{
								//Count amount of frames rendered in one second
								Values.FPS = frames/(ticks/1000);
								ticks = 0;
								frame_time = 0;
								frames = 0;
							}
							else
							{
								ticks += frame_time;
							}
							canvas.setDoubleBuffered(true);
							//canvas.repaint();
							frames++;
							previous = Values.GAME_TICKS;
							Values.GAME_TICKS += REFRESH_DELAY;
							canvas.repaint();
						}
						Thread.sleep(REFRESH_DELAY);
					} 
					catch (InterruptedException e) 
					{
						log.println(e.getMessage());
					}
				}
			}
		});
		loop_thread.start();
		/**************************END OF MY GAME LOOP*******************/
		
		//Listen for key press events every 10 milliseconds
		Thread t_key = new Thread(new Runnable() 
		{	
			@Override
			public void run()
			{
				Timer key_timer = new Timer();
				key_timer.scheduleAtFixedRate(listen, 0, 10);
			}
		});
		t_key.start();
	}
}
