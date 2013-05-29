package highwaysimulate;

import cars.*;

public class InterchangeInfo extends Info{
	private Car interCar;
	private int interPos;
	private boolean power;
	public InterchangeInfo(int interPos){
		reset(interPos);
		
	}
	public boolean dash(Car car) {
		
		if(power && interCar == null){
			car.setPrevCar(new EmptyCar());
			car.setLane(0);
			interCar = car;
			power = false;
			return true;
		}else{
			synchronized(interCar){
				if(power &&  interCar.getPos() >= 3 + interPos){
					car.setPrevCar(interCar);
					car.setNextCar(interCar.getNextCar());
					interCar.getNextCar().setPrevCar(car);
					interCar.setNextCar(car);
					car.setLane(0);
					interCar = car;
					power = false;
					return true;
				}
			}
		}
		return false;
	}
	
	protected void reset(int interPos){
		resetPower();
		this.interPos = interPos;
		interCar = null;
	}
	
	protected void resetPower(){
		power = true;
	}
	
	public void setInterCar(Car car){
		interCar.setNextCar(car);
		interCar = car;
	}
}
