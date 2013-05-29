package highwaysimulate;

import cars.Car;
import cars.EmptyCar;

public class InterchangeInfo extends Info{
	private Car interCar;
	private int interPos;
	private boolean power;
	public InterchangeInfo(int interPos){
		reset(interPos);
		
	}
	public Car dash(Car car) {
		
		if(power && interCar == null){
			car.setLane(0);
			interCar = car;
			power = false;
			return new EmptyCar();
		}else{
			synchronized(interCar){
				if(power &&  interCar.getPos() >= 3 + interPos){
					interCar.setNextCar(car);
					car.setLane(0);
					Car tmp = interCar;
					interCar = car;
					power = false;
					return tmp;
				}
			}
		}
		return null;
	}
	
	protected void reset(int interPos){
		resetPower();
		this.interPos = interPos;
		interCar = null;
	}
	
	protected void resetPower(){
		power = true;
	}

}
