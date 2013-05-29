package cars;

import java.awt.Image;

import highwaysimulate.*;

public abstract class Car implements Runnable{
		
	private int pos;			// at head
	private int length;			// from head to tail
	private int speed;
	private int acc_count;		// acceleration count down 2 -> 1 -> 0
	private int slow_count; 	// slow down 1 -> 0
	private int limit;			// highway speedlimit
	private int lane;
	private Constant.Status status;
	private Info info;
	protected Car prevCar;
	protected Car nextCar;
	
	public Car(int pos, int limit, Info info){
		this.pos = pos;
		this.limit = limit;
		this.info = info;
		prevCar = null;
		nextCar = null;
		speed = 0;
		acc_count = 0;
		slow_count = 0;
		lane = 0;
		length = 1;
		status = Constant.Status.WAIT;
	}
	
	public void run(){
		switch(status){
			case WAIT:
				synchronized(info){
					if(info instanceof EntryInfo){
						synchronized(this){
							prevCar = info.dash(this);
							if(prevCar == null)
								return;
							dash();
							status = Constant.Status.BEFOREINTER;
						}
					}
				}
				break;
			case BEFOREINTER:
			case AFTERINTER:
			
				break;
			case DONE:
				return;
			default:;
		}
	}
	
	public int move(){
		pos += speed;
		
		if(acc_count > 0){
			acc_count--;
			if(acc_count == 0 && speed < limit)
				speed++;
		}else if(slow_count > 0){
			slow_count--;
			if(slow_count == 0 && speed > 0)
				speed--;
		}
		return this.pos;
	}
	public boolean dash(){
		if(prevCar instanceof EmptyCar){
			speed = limit;
		}else{
			speed = (prevCar.pos-this.pos)/2;
			if(speed > limit)
				speed = limit;
		}
		if(speed == 0)
			return false;
		
		pos += speed;
		return true;
	}
	
	public void AdjustSpeed(){
		
		if(acc_count == 0 && slow_count == 0){
			if((prevCar == null || prevCar.pos-this.pos > 2*speed) && speed < limit){
				acc_count = 2;
			}else if((prevCar != null && prevCar.pos-this.pos < 2*speed) && speed > 0){
				slow_count = 1;
			}
		}
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
	
	public int getLane(){
		return lane;
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
	public abstract Image getImage(); 
}