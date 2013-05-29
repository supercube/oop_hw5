package highwaysimulate;

import cars.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Highway extends JPanel implements ActionListener{
	private int length, usedLane;
	private int num_car_entry, num_car_interchange, interPos;
	private Car[] entryCar;
	private Car[] interCar;
	private int[] status;
	private int[] tmpstatus;
	private int nextEnteringEntryCar, nextEnteringInterCar, endEntryCar, endInterCar;
	private Car influencedCar;
	private int time;
	private Timer timer;
	private Info entryInfo, interInfo;
	protected static Image background, lane;
	protected static Image[] smallLane;
	static{
		background = new TransparentIcon(Constant.URL,"Images/highway.png", Color.black).getIcon().getImage();
		lane = new TransparentIcon(Constant.URL,"Images/lane.png", Color.black).getIcon().getImage();
		smallLane = new Image[Constant.LANEWIDTH/Constant.GRIDSIZE-1];
		for(int i = 1; i <= Constant.LANEWIDTH/Constant.GRIDSIZE-1; i++){
			smallLane[i-1] = new TransparentIcon(Constant.URL,"Images/lane" + i + ".png", Color.black).getIcon().getImage();
		}
	}
	public Highway(int length, int lane, int num_car_entry, int num_car_interchange, int interPos){
		try{
			timer = new Timer(Constant.INTERVAL, this);
			entryInfo = new EntryInfo(lane);
			interInfo = new InterchangeInfo(interPos);
			set(length, lane, num_car_entry, num_car_interchange, interPos);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public Highway(){
		this(10, 1, 5, 0, 5);
	}
	
	public boolean set(int length, int lane, int num_car_entry, int num_car_interchange, int interPos){
		if(length <= 0 || length > 240 || num_car_entry < 0 || num_car_interchange < 0 || interPos <= 1 || interPos > length)
			return false;
		
		this.length = length;
		this.usedLane = lane;
		this.num_car_entry = num_car_entry;
		this.num_car_interchange = num_car_interchange;
		this.interPos = interPos;
		((EntryInfo)entryInfo).reset(lane);
		((InterchangeInfo)interInfo).reset(interPos);
		entryCar = new Car[num_car_entry + 1];
		interCar = new Car[num_car_interchange + 1];
		for(int i = 0; i < num_car_entry; i++)
			entryCar[i] = new Sedan(0, 4, entryInfo);
		for(int i = 0; i < num_car_interchange; i++)
			interCar[i] = new Sedan(interPos, 4, interInfo);
		
		time = 0;
		
		repaint();
		timer.start();
		try{
			Thread.yield();
		}catch(Exception e){
			System.out.println(e);
		}
		return true;
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
		entryInfo.resetPower();
		interInfo.resetPower();
		for(int i = 0; i < num_car_entry; i++){
			new Thread(entryCar[i]).start();
		}
		for(int i = 0; i < num_car_interchange; i++){
			new Thread(interCar[i]).start();
		}
		time++;
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(background, 0, 0, 1200, 500, null);
		int i;
		int rate = Constant.LANEWIDTH/Constant.GRIDSIZE;
		for(i = 0; i < length/rate; i++){
			for(int j = 0; j < usedLane + 1; j++){
				g.drawImage(lane, i * Constant.LANEWIDTH, 100 + j*20, Constant.LANEWIDTH, Constant.LANEHEIGHT, null);
			}
		}
		for(int k = 0; k < length - i*rate; k++){
			for(int j = 0; j < usedLane + 1; j++){
				g.drawImage(smallLane[k], i * Constant.LANEWIDTH + k * Constant.GRIDSIZE, 100 + j*20, Constant.GRIDSIZE, Constant.LANEHEIGHT, null);
			}
		}
		int count = 0;
		for(i = 0; i < num_car_entry; i++){
			if(entryCar[i].getStatus() == Constant.Status.BEFOREINTER || entryCar[i].getStatus() == Constant.Status.AFTERINTER){
				g.drawImage(entryCar[i].getImage(), entryCar[i].getPos()*Constant.GRIDSIZE, 110 + entryCar[i].getLane()*20, Constant.GRIDSIZE, Constant.GRIDSIZE, null);
				count++;
				System.out.print(entryCar[i].getLane() + " at " + entryCar[i].getPos() + ", ");
			}
		}
		System.out.println(": " + count + ", " + ((EntryInfo)entryInfo).tt);
		g.drawString(Integer.toString(time), 20, 20);
		
	}
}
