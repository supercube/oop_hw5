package highwaysimulate;

import cars.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.*;

public class Highway extends JPanel implements ActionListener{
	private int length, usedLane;
	private int num_car_entry, num_car_interchange, interPos;
	private Car[] entryCar;
	private Car[] interCar;
	private int nextEnteringEntryCar, nextEnteringInterCar;
	private int time;
	private Random rnd;
	private Timer timer;
	private Info entryInfo, interInfo;
	private int limit;
	private int safeFactor;
	private int interImageId;
	private ArrayList<Accident> accidents;
	protected static Image background, lane;
	protected static Image[] interArrow;
	static{
		background = new TransparentIcon(Constant.URL,"Images/highway.png", Color.black).getIcon().getImage();
		lane = new TransparentIcon(Constant.URL,"Images/lane.png", Color.black).getIcon().getImage();
		interArrow = new Image[3];
		for(int i = 0; i < 3; i++){
			interArrow[i] = new TransparentIcon(Constant.URL,"Images/interArrow" + i + ".png", Color.white).getIcon().getImage();
		}
	}
	public Highway(int length, int lane, int num_car_entry, int num_car_interchange, int interPos, int limit, int safeFactor){
		try{
			timer = new Timer(Constant.INTERVAL, this);
			rnd = new Random();
			interInfo = new InterchangeInfo(this);
			entryInfo = new EntryInfo(this, interInfo);
			accidents = new ArrayList<Accident>(0);
			set(length, lane, num_car_entry, num_car_interchange, interPos, limit, safeFactor);
		}catch(Exception e){
			 e.printStackTrace(); 
		}
	}
	
	public Highway(){
		this(100, 4, 20, 0, 50, 4, 2);
	}
	
	public boolean set(int length, int lane, int num_car_entry, int num_car_interchange, int interPos, int limit, int safeFactor){
		if(length <= 0 || length > 240 || lane <= 0 || lane > Constant.MAX_LANE || num_car_entry < 0 || num_car_interchange < 0 || interPos <= 1 || interPos > length || safeFactor < 2)
			return false;
		
		this.length = length;
		this.usedLane = lane;
		this.num_car_entry = num_car_entry;
		this.num_car_interchange = num_car_interchange;
		this.interPos = interPos;
		this.limit = limit;
		this.safeFactor = safeFactor;
		interImageId = -1;
		accidents.clear();
		((EntryInfo)entryInfo).reset();
		((InterchangeInfo)interInfo).reset();
		entryCar = new Car[num_car_entry + 1];
		interCar = new Car[num_car_interchange + 1];
		for(int i = 0; i < num_car_entry; i++){
			switch(rnd.nextInt(3)){
				case 0:
					entryCar[i] = new Sedan(0, this, entryInfo);
					break;
				case 1:
					entryCar[i] = new Van(0, this, entryInfo);
					break;
				case 2:
					entryCar[i] = new Truck(0, this, entryInfo);
					break;
				default:
					entryCar[i] = new Sedan(0, this, entryInfo);
			}
			
		}
		for(int i = 0; i < num_car_interchange; i++){
			switch(rnd.nextInt(3)){
				case 0:
					interCar[i] = new Sedan(interPos, this, interInfo);
					break;
				case 1:
					interCar[i] = new Van(interPos, this, interInfo);
					break;
				case 2:
					interCar[i] = new Truck(interPos, this, interInfo);
					break;
				default:
					interCar[i] = new Sedan(interPos, this, interInfo);
			}
		}
		nextEnteringEntryCar = 0;
		nextEnteringInterCar = 0;
		time = 0;
		repaint();
		
		/* initial dash */
		dashAndCheckAccident();
		Car tmpCar1 = ((EntryInfo)entryInfo).getEntryCar0();
		Car tmpCar2 = ((InterchangeInfo)interInfo).getInterCar();
		if( tmpCar1 != null && tmpCar1 != tmpCar2){
			tmpCar1.setPrevCar(tmpCar2);
			tmpCar2.setNextCar(tmpCar1);
		}
		adjustSpeedOrMove();
		try{
			Thread.sleep(Constant.INTERVAL);
		}catch(Exception e){
			System.out.println(e);
		}
		adjustSpeedOrMove();
		
		time = 2;
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
		if(time % 2 == 0){
			dashAndCheckAccident();
		}
		adjustSpeedOrMove();
		time++;
	}
	
	private void dashAndCheckAccident(){
		entryInfo.resetPower();
		interInfo.resetPower();
		while(nextEnteringEntryCar < num_car_entry && entryCar[nextEnteringEntryCar].dash()){
			nextEnteringEntryCar++;
		}
		while(nextEnteringInterCar < num_car_interchange && interCar[nextEnteringInterCar].dash()){
			nextEnteringInterCar++;
		}
		for(int i = 0; i < num_car_entry; i++){
			entryCar[i].checkAccident();
		}
		for(int i = 0; i < num_car_interchange; i++){
			interCar[i].checkAccident();
		}
	}
	
	private void adjustSpeedOrMove(){
		for(int i = 0; i < num_car_entry; i++){
			new Thread(entryCar[i]).start();
		}
		for(int i = 0; i < num_car_interchange; i++){
			new Thread(interCar[i]).start();
		}
	}
	
	private Image getInterImage(){
		interImageId = (interImageId+1)%3;
		return interArrow[interImageId];
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(background, 0, 100, length*Constant.GRIDSIZE, Constant.LANEHEIGHT + 2*usedLane*Constant.LANEHEIGHT, null);
		
		g.drawImage(getInterImage(), interPos*Constant.GRIDSIZE, 100, 10, 10, null);
		
		int i;
		int rate = Constant.LANEWIDTH/Constant.GRIDSIZE;
		for(i = 0; i < length/rate; i++){
			for(int j = 0; j < usedLane + 1; j++){
				g.drawImage(lane, i * Constant.LANEWIDTH, 100 + j*20, Constant.LANEWIDTH, Constant.LANEHEIGHT, null);
			}
		}
		
		for(int j = 0; j < usedLane + 1; j++){
			g.drawImage(lane, i * Constant.LANEWIDTH, 100 + j*20, i * Constant.LANEWIDTH + (length-i*rate)*Constant.GRIDSIZE, 110 + j*20, 0, 0, (length-i*rate)*Constant.GRIDSIZE, 10, null);
							//   dx1					dy1				dx2														dy2		 sx1 sy1   sx2							   sy2
		}
		
		for(i = 0; i < num_car_entry; i++){
			if(entryCar[i].getStatus() == Constant.Status.BEFOREINTER || entryCar[i].getStatus() == Constant.Status.AFTERINTER){
				g.drawImage(entryCar[i].getImage(), entryCar[i].getPos()*Constant.GRIDSIZE + entryCar[i].accPad1, 110 + entryCar[i].getLane()*20 + entryCar[i].accPad2, Constant.GRIDSIZE*entryCar[i].getLength(), Constant.GRIDSIZE, null);
			}
		}
		for(i = 0; i < num_car_interchange; i++){
			if(interCar[i].getStatus() == Constant.Status.BEFOREINTER || interCar[i].getStatus() == Constant.Status.AFTERINTER){
				g.drawImage(interCar[i].getImage(), interCar[i].getPos()*Constant.GRIDSIZE + interCar[i].accPad1, 110 + interCar[i].getLane()*20 + interCar[i].accPad2, Constant.GRIDSIZE*interCar[i].getLength(), Constant.GRIDSIZE, null);
				
			}
		}
		
		Iterator<Accident> iterator = accidents.iterator();
		Accident tmp;
		while(iterator.hasNext()){
			tmp = iterator.next();
			g.drawImage(tmp.getImage(), tmp.getPos()*Constant.GRIDSIZE + tmp.accPad1, 110 + tmp.getLane()*20 + tmp.accPad2, Constant.GRIDSIZE, Constant.GRIDSIZE, null);
		}
		g.drawString("time: " + Integer.toString(time/2), 20, 20);
	}
	
	public void addAccident(Accident accident){
		accidents.add(accident);
	}
	public int getLimit(){
		return limit;
	}
	public int getInterPos(){
		return interPos;
	}
	public int getLength(){
		return length;
	}
	public int getUsedLane(){
		return usedLane;
	}
	public int getSafeFactor(){
		return safeFactor;
	}
}
