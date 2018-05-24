package za.ac.uj.acsse.practicalx.flagcapture.States;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;


public class Paused extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7416471186883852782L;
	private JTextField notif1 = new JTextField("The game has been paused press ESC again to return to the game");
	private JTextField notif2 = new JTextField("If you quit now (without saving first) all game progress will be lost");
	
	private JButton exit = new JButton("Exit Game");
	private JButton restart = new JButton("Restart Game");
	private JButton save = new JButton("Save Game");
	
	private JLabel paused = new JLabel("Paused");
	private JLabel red = new JLabel("RED Team currenlty has 0 points");
	private JLabel blue = new JLabel("Blue Team currenlty has 0 points");
	private JLabel time = new JLabel("000");
	
	private Font font = null;
	
	public Paused()
	{
		Main.log.println("Paused UI initialized.");
		try 
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("8-BIT WONDER.TTF"));
			
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
		    
		    Main.log.println("Paused font file loaded.");
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
		
		//this.addKeyListener(new KeyInputListener());
		//this.setFont(new Font("8BIT WONDER",Font.PLAIN,38));
		
		Timer timer = new Timer(1,new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try 
				{
					initUI();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		});
		
		exit.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.log.println("Exiting.");
				Main.log.flush();
				Main.log.close();
				System.exit(0);
			}
		});
		
		restart.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.p_restart = true;
			}
		});
		
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ObjectOutputStream dos = null;
				try
				{
					SaveState ss = new SaveState();
					
					JFileChooser chooser = new JFileChooser();
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.addChoosableFileFilter(new FileNameExtensionFilter("binary files", "bin"));
					int result = chooser.showSaveDialog(getRootPane());
					
					if(result == JFileChooser.APPROVE_OPTION)
					{
						if(chooser.getSelectedFile().getName().contains(".bin"))
						{
							String path = chooser.getSelectedFile().getAbsolutePath();
							dos = new ObjectOutputStream(new FileOutputStream(path));//BufferedOutputStream(
							dos.writeObject(ss);
							dos.flush();
							dos.close();
							
							Main.log.println("Game state saved.");
							JOptionPane.showMessageDialog(null,"Game data saved.", "Saved",JOptionPane.INFORMATION_MESSAGE);
						}else{
							Main.log.println("Game state not saved.");
							JOptionPane.showMessageDialog(null,"Game data was not saved, the extension of the file must be .bin","Not Saved",JOptionPane.ERROR_MESSAGE);
						}
					}
				} 
				catch (FileNotFoundException ex) 
				{
					Main.log.println(ex.getMessage());
				} catch (IOException ex) {
					Main.log.println(ex.getMessage());
				}
				Values.STUCK = true;
			}
		});
		
		timer.start();
	}
	
	private void initUI() throws IOException
	{
		this.setLayout(null);
		
		notif1.setFocusable(false);
		notif2.setFocusable(false);
		
		notif1.setEditable(false);
		notif2.setEditable(false);
		
		this.setBackground(Color.ORANGE);
		
		exit.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		save.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		restart.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		
		paused.setFont(new Font("8BIT WONDER",Font.PLAIN,38));
		notif1.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		notif2.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		red.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		blue.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		time.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		
		if(Canvas.playerFlag != null && Canvas.enemyFlag != null)
		{
			blue.setText("BLUE Team currenlty has "+Canvas.enemyFlag.getScore()+" points");
			red.setText("RED Team currenlty has "+Canvas.playerFlag.getScore()+" points");
		}
		time.setText("Remaining time is "+(Values.GAME_TIME-(Values.GAME_TICKS/1000)));
		
		notif1.setHorizontalAlignment(SwingConstants.CENTER);
		notif2.setHorizontalAlignment(SwingConstants.CENTER);
		
		notif1.setBounds(0,0,Values.SCREEN_WIDTH,20);
		paused.setBounds(Values.SCREEN_WIDTH/2-100,100,400,80);
		red.setBounds(Values.SCREEN_WIDTH/2-150,Values.SCREEN_HEIGHT-520,400,80);
		blue.setBounds(Values.SCREEN_WIDTH/2-150,Values.SCREEN_HEIGHT-450,450,80);
		time.setBounds(Values.SCREEN_WIDTH/2-100,Values.SCREEN_HEIGHT-400,450,80);
		restart.setBounds(Values.SCREEN_WIDTH/2-200, Values.SCREEN_HEIGHT-300,400,50);
		save.setBounds(Values.SCREEN_WIDTH/2-200, Values.SCREEN_HEIGHT-200,400,50);
		exit.setBounds(Values.SCREEN_WIDTH/2-200, Values.SCREEN_HEIGHT-100,400,50);
		notif2.setBounds(0,Values.SCREEN_HEIGHT-40,Values.SCREEN_WIDTH,20);
		
		this.add(notif1);
		this.add(paused);
		this.add(red);
		this.add(blue);
		this.add(time);
		this.add(restart);
		this.add(save);
		this.add(exit);
		this.add(notif2);
		
		this.repaint();
		this.validate();
	}
}
