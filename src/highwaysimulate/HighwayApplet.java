package highwaysimulate;

import java.applet.*;
import java.awt.*;
import java.net.URL;

import javax.swing.*;

public class HighwayApplet extends JApplet{
	
	private Container c;
	private JPanel showPanel, infoPanel;
	public void init(){
		try{
			super.init();
			c = getContentPane();
			showPanel = new Highway(this);
			showPanel.setBounds(0, 0, 1200, 400);
			infoPanel = new InfoPanel((Highway)showPanel);
			infoPanel.setBounds(0, 400, 1200, 100);
			c.setLayout(null);
			c.add(showPanel);
			c.add(infoPanel);
			c.setSize(1200, 500);
			c.setPreferredSize(new Dimension(1200, 500));
			setSize(1200, 500);
			setPreferredSize(new Dimension(1200, 500));
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
	public void start(){
		
	}
}
