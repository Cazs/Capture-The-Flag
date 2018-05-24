package za.ac.uj.acsse.practicalx.flagcapture.States;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import za.ac.uj.acsse.practicalx.flagcapture.GUI.Main;

public class Multiplayer extends JPanel
{
	private Font font = null;
	
	private JLabel user = new JLabel();
	private JTextField username = new JTextField();
	private JButton start = new JButton();
	private JButton exit = new JButton();
	private String usr = "";
	
	public Multiplayer()
	{
		this.setLayout(null);
		this.setBackground(Color.DARK_GRAY);
		
		try 
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("8-BIT WONDER.TTF"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
		} 
		catch (FontFormatException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		start.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Values.MULTIPLAYER = false;
				Values.START = false;
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
		
		javax.swing.Timer timer = new javax.swing.Timer(100,new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//usr = username.getText();
				initUI();
			}
		});  
		timer.start();
		//JOptionPane.showMessageDialog(null, Values.SCREEN_WIDTH);
		username.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		username.setBounds(Values.SCREEN_WIDTH/2-50,Values.SCREEN_HEIGHT/2-200,300,50);
		this.add(username);
	}
	
	private void initUI()
	{
		user.setForeground(Color.RED);
		user.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		//username.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		start.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		exit.setFont(new Font("8BIT WONDER",Font.PLAIN,26));
		
		user.setText("Username");
		//username.setText(usr);
		start.setText("Start");
		exit.setText("Exit");
		
		user.setBounds(Values.SCREEN_WIDTH/2-300,Values.SCREEN_HEIGHT/2-200,300,50);
		//username.setBounds(Values.SCREEN_WIDTH/2-100,Values.SCREEN_HEIGHT/2-200,300,50);
		start.setBounds(Values.SCREEN_WIDTH/2-150,Values.SCREEN_HEIGHT/2,300,50);
		exit.setBounds(Values.SCREEN_WIDTH/2-150,Values.SCREEN_HEIGHT/2+200,300,50);
		
		this.add(user);
		//this.add(username);
		this.add(start);
		this.add(exit);
		
		//this.repaint();
		//this.validate();
	}
}
