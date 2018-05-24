package za.ac.uj.acsse.practicalx.flagcapture.States;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Canvas;
import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;


public class GameOver extends JPanel
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1994945531370259901L;
	private JButton restart = new JButton("Restart");
	private JButton exit = new JButton("Exit");
	
	private JLabel gameover	= new JLabel("Game Over");
	private JLabel winner = new JLabel(".. Team Wins.");
	private JLabel loser = new JLabel(".. Team Loses.");
	
	private Font font = null;
	
	public GameOver()
	{
		Main.log.println("Game Over UI initialized.");
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		
		try 
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("8-BIT WONDER.TTF"));
			
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
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
		
		Timer timer = new Timer(1,new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				initUI();
			}
		});
		timer.start();
		
		exit.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
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
			public void actionPerformed(ActionEvent arg0) 
			{
				Main.log.println("Restarting.");
				Main.restart = true;
			}
		});
	}
	
	private void initUI()
	{	
		gameover.setForeground(Color.RED);
		gameover.setFont(new Font("8BIT WONDER",Font.PLAIN,38));
		winner.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		loser.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		restart.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		exit.setFont(new Font("8BIT WONDER",Font.PLAIN,12));
		
		winner.setForeground(Color.CYAN);
		winner.setText(Canvas.getWinner());
		loser.setForeground(Color.CYAN);
		loser.setText(Canvas.getLoser());
		
		gameover.setBounds(Values.SCREEN_WIDTH/2-165,100,400,80);
		winner.setBounds(Values.SCREEN_WIDTH/2-220,Values.SCREEN_HEIGHT-450,550,80);
		loser.setBounds(Values.SCREEN_WIDTH/2-220,Values.SCREEN_HEIGHT-420,550,80);
		restart.setBounds(Values.SCREEN_WIDTH/2-200, Values.SCREEN_HEIGHT-300,400,50);
		exit.setBounds(Values.SCREEN_WIDTH/2-200, Values.SCREEN_HEIGHT-200,400,50);
		
		add(gameover);
		add(winner);
		add(loser);
		add(restart);
		add(exit);
		
	}
}
