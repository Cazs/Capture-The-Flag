package za.ac.uj.acsse.practicalx.flagcapture.States;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;

public class Start extends JPanel 
{
	private JButton start = new JButton();
	private JButton exit = new JButton();
	private JButton multiplayer = new JButton();
	
	private Font font = null;
	
	public Start()
	{
		this.setLayout(null);
		
		try 
		{
			font = Font.createFont(Font.TRUETYPE_FONT,new File("8-BIT WONDER.TTF"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
			
			 Main.log.println("Canvas font file loaded.");
		} 
		catch (FontFormatException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		
		this.setBackground(Color.DARK_GRAY);
		
		start.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				Main.start = false;
				//Values.START = false;
			}
		});
		
		multiplayer.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Values.MULTIPLAYER = true;
				//Values.START = false;
				Main.multiplayer = true;
			}
		});
		
		multiplayer.setEnabled(false);
		
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
		
		javax.swing.Timer timer = new javax.swing.Timer(200,new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				initUI();
			}
		});  
		timer.start();
	}
	
	private void initUI()
	{
		start.setFont(new Font("8BIT WONDER",Font.PLAIN,24));
		multiplayer.setFont(new Font("8BIT WONDER",Font.PLAIN,24));
		exit.setFont(new Font("8BIT WONDER",Font.PLAIN,24));
		
		start.setText("Start");
		multiplayer.setText("Multiplayer");
		exit.setText("Exit");
		
		start.setBounds(Values.SCREEN_WIDTH/2-150,Values.SCREEN_HEIGHT/2-200,300,50);
		multiplayer.setBounds(Values.SCREEN_WIDTH/2-150,Values.SCREEN_HEIGHT/2,300,50);
		exit.setBounds(Values.SCREEN_WIDTH/2-150,Values.SCREEN_HEIGHT/2+200,300,50);
		
		this.add(start);
		this.add(multiplayer);
		this.add(exit);
		
		this.repaint();
		this.validate();
	}
}
