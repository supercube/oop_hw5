package highwaysimulate;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.*;

public class Highway extends JPanel implements ActionListener{
	private int length;
	private int num_car_entry;
	private int num_car_interchange;
	private Car[] entryCar;
	private Car[] interCar;
	private int[] status;
	private int[] tmpstatus;
	private boolean accident;
	private int entryptr, interptr; // the next car to get on highway
	private int endentryptr, endinterptr; 
	private int influencedCar;
	private int tmp;
	private Image background, lane, lane1, lane2;
	private Applet app;
	private Timer timer;
	public Highway(Applet app, int length, int lane, int num_car_entry, int num_car_interchange, int interchange){
		try{
			timer = new Timer(Constant.INTERVAL, this);
			this.app = app;
			background = app.getImage(Constant.URL,"Images/highway.png");
			this.lane = new TransparentIcon(Constant.URL,"Images/lane.png", Color.black).getIcon().getImage();
			this.lane1 = new TransparentIcon(Constant.URL,"Images/lane1.png", Color.black).getIcon().getImage();
			this.lane2 = new TransparentIcon(Constant.URL,"Images/lane2.png", Color.black).getIcon().getImage();
			set(length, lane, num_car_entry, num_car_interchange, interchange);
		}catch(Exception e){
			System.out.println(e);
		}
		timer.start();
	}
	
	public Highway(Applet app){
		this(app, 10, 1, 5, 0, 5);
	}
	
	public boolean set(int length, int lane, int num_car_entry, int num_car_interchange, int interchange){
		if(length <= 0 || length > 120 || num_car_entry < 0 || num_car_interchange < 0 || interchange > length)
			return false;
		
		this.length = length;
		this.num_car_entry = num_car_entry;
		this.num_car_interchange = num_car_interchange;
		entryCar = new Car[num_car_entry + 1];
		interCar = new Car[num_car_interchange + 1];
		status = new int[length+1]; // from 1 to the length
		tmpstatus = new int[length+1]; // from 1 to the length
		for(int i = 1; i <= num_car_entry; i++)
			entryCar[i] = new Sedan(); //(1);
		for(int i = 1; i <= num_car_interchange; i++)
			interCar[i] = new Sedan(); //(Constant.interpos);
		this.accident = false;
		this.entryptr = 1;
		this.interptr = 1;
		this.endentryptr = 1;
		this.endinterptr = 1;
		this.influencedCar = 1;
		repaint();
		return true;
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(background, 0, 0, 1200, 500, null);
		int i;
		for(i = 0; i < length/3; i++){
			g.drawImage(lane, i * Constant.GRIDSIZE*3, 200, Constant.GRIDSIZE*3, Constant.GRIDSIZE, null);
			g.drawImage(lane, i * Constant.GRIDSIZE*3, 220, Constant.GRIDSIZE*3, Constant.GRIDSIZE, null);
		}
		if(length - i*3 >= 1){
			g.drawImage(lane1, i * Constant.GRIDSIZE*3, 200, Constant.GRIDSIZE, Constant.GRIDSIZE, null);
			g.drawImage(lane1, i * Constant.GRIDSIZE*3, 220, Constant.GRIDSIZE, Constant.GRIDSIZE, null);
		}
		if(length - i*3 == 2){
			g.drawImage(lane2, i * Constant.GRIDSIZE*3+10, 200, Constant.GRIDSIZE, Constant.GRIDSIZE, null);
			g.drawImage(lane2, i * Constant.GRIDSIZE*3+10, 220, Constant.GRIDSIZE, Constant.GRIDSIZE, null);
			
		}
	}
}
