package cars;

import java.awt.Image;
import java.util.Random;

import highwaysimulate.*;

public abstract class Car implements Runnable{
		
	private int pos;			// at head
	private int speed;
	private int acc_count;		// acceleration count down 2 -> 1 -> 0
	private int slow_count; 	// slow down 1 -> 0
	private Highway highway;
	private int lane;
	private Constant.Status status;
	private Info info;
	private boolean adjust;
	private boolean justDashed;
	private boolean accident = false;
	protected int length;		// from head to tail
	protected Car prevCar;
	protected Car nextCar;
	public int accPad1, accPad2;
	
	private static Random rnd = new Random();

	public Car(int pos, Highway highway, Info info){
		this.pos = pos;
		this.highway = highway;
		this.info = info;
		prevCar = null;
		nextCar = null;
		speed = 0;
		acc_count = 0;
		slow_count = 0;
		lane = 0;
		length = 1;
		status = Constant.Status.WAIT;
		adjust = true;
		justDashed = false;
		accident = false;
		accPad1 = 0;
		accPad2 = 0;
	}
	
	public void run(){
		if(accident || status == Constant.Status.WAIT)
			return;
		if(!adjust){
			adjust = !adjust;
			if(justDashed){
				justDashed = !justDashed;
				return;
			}
			switch(status){
				case WAIT:
					return;
				case BEFOREINTER:
					move();
					if(pos > highway.getInterPos()){
						if(lane == 0){
							synchronized(((EntryInfo)info).getInterInfo()){
								((EntryInfo)info).getInterInfo().setInterCar(this);
							}
						}
						status = Constant.Status.AFTERINTER;
					}
					break;
				case AFTERINTER:
					move();
					if(pos >= highway.getLength()){
						pos = Constant.INFINITE;
						status = Constant.Status.DONE;
					}
					break;
				case DONE:
					return;
				default:;
			}
		}else{
			adjust = !adjust;
			if(status == Constant.Status.BEFOREINTER || status == Constant.Status.AFTERINTER){
				adjustSpeed();
			}
		}
	}
	
	public int move(){
		pos += speed;
		
		if(acc_count > 0){
			acc_count--;
			if(acc_count == 0 && speed < highway.getLimit())
				speed++;
		}else if(slow_count > 0){
			slow_count--;
			if(slow_count == 0 && speed > 0)
				speed--;
		}
		return this.pos;
	}
	public boolean dash(){
		if(!info.dash(this))
			return false;
		
		if(prevCar == null)
			return false;
		
		if(prevCar instanceof EmptyCar){
			speed = highway.getLimit();
		}else{
			speed = (prevCar.getPos()-getHead())/highway.getSafeFactor();
			if(speed > highway.getLimit())
				speed = highway.getLimit();
		}
		if(speed == 0)
			return false;
		
		status = Constant.Status.BEFOREINTER;
		pos += speed;
		if(pos > highway.getInterPos()){
			if(lane == 0 && info instanceof EntryInfo){
				((EntryInfo)info).getInterInfo().setInterCar(this);
			}
			status = Constant.Status.AFTERINTER;
		}
		justDashed = true;
		return true;
	}
	
	public synchronized void adjustSpeed(){
		
		if(acc_count == 0 && slow_count == 0){
			int distance = prevCar.getPos()-getHead();
			if(prevCar != null && prevCar.isAccident()){
				slow_count = 1;
			}else if((prevCar instanceof EmptyCar || distance > highway.getSafeFactor()*speed) && speed < highway.getLimit() && distance > 1){
				acc_count = 2;
			}else if((prevCar != null && distance < highway.getSafeFactor()*speed) && speed > 0 && !(speed == 1 && distance > 2)){
				slow_count = 1;
			}
		}
	}
	
	public boolean checkAccident(){
		if(!accident && status != Constant.Status.WAIT && status != Constant.Status.DONE && prevCar != null && getHead() >= prevCar.getPos()){
			highway.addAccident(new Accident(pos, lane));
			accident = true;
			prevCar.setAccident();
			if(nextCar != null){
				nextCar.setPrevCar(prevCar);
			}
			if(getHead() >= highway.getInterPos()){
				if(info instanceof EntryInfo){
					((EntryInfo)info).getInterInfo().setInterCar(prevCar);
				}else{
					((InterchangeInfo)info).setInterCar(prevCar);
				}
			}
			accPad1 = rnd.nextInt(10) - 4;
			accPad2 = rnd.nextInt(10) - 4;
			prevCar.accPad1 = rnd.nextInt(10) - 4;
			prevCar.accPad2 = rnd.nextInt(10) - 4;
			return true;
		}
		return false;
	}
	
	protected void setAccident(){
		accident = true;
	}
	
	protected void setSpeed(int speed){
		this.speed = speed;
	}
	
	protected void setPos(int pos){
		this.pos = pos;
	}
	
	public int getPos(){
		return pos;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getHead(){
		return pos+length-1;
	}
	
	public int getLane(){
		return lane;
	}
	
	public Car getPrevCar(){
		return prevCar;
	}
	
	public Car getNextCar(){
		return nextCar;
	}
	
	public Constant.Status getStatus(){
		return status;
	}
	
	public void setPrevCar(Car prevCar){
		this.prevCar = prevCar;
	}
	public void setNextCar(Car nextCar){
		this.nextCar = nextCar;
	}
	public void setLane(int lane){
		this.lane = lane;
	}
	
	public boolean isAccident(){
		return accident;
	}
	
	public abstract Image getImage(); 
}