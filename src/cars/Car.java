package cars;

import java.awt.Image;

import highwaysimulate.*;

public abstract class Car implements Runnable{
		
	private int pos;			// at head
	private int length;			// from head to tail
	private int speed;
	private int acc_count;		// acceleration count down 2 -> 1 -> 0
	private int slow_count; 	// slow down 1 -> 0
	private Highway highway;
	private int lane;
	private Constant.Status status;
	private Info info;
	private boolean mode;
	private boolean justDashed;
	protected Car prevCar;
	protected Car nextCar;

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
		mode = false;
		justDashed = false;
	}
	
	public void run(){
		if(mode){
			mode = !mode;
			if(justDashed){
				justDashed = !justDashed;
				return;
			}
			switch(status){
				case WAIT:
					
					break;
				case BEFOREINTER:
					move();
					if(pos > highway.getInterPos()){
						//((EntryInfo)info).getInterInfo().setInterCar(this);
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
			mode = !mode;
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
		if(info instanceof EntryInfo){
			if(!info.dash(this))
				return false;
		}
		
		if(prevCar == null)
			return false;
		
		if(prevCar instanceof EmptyCar){
			speed = highway.getLimit();
		}else{
			speed = (prevCar.pos-this.pos)/2;
			if(speed > highway.getLimit())
				speed = highway.getLimit();
		}
		if(speed == 0)
			return false;
		
		status = Constant.Status.BEFOREINTER;
		pos += speed;
		justDashed = true;
		return true;
	}
	
	public void adjustSpeed(){
		
		if(acc_count == 0 && slow_count == 0){
			if((prevCar instanceof EmptyCar || prevCar.pos-this.pos > 2*speed) && speed < highway.getLimit()){
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
	
	public int getLength(){
		return length;
	}
	public int getLane(){
		return lane;
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
	public abstract Image getImage(); 
}