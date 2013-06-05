package highwaysimulate;

import cars.*;

public class InterchangeInfo extends Info{
	private Car interCar;
	private Highway highway;
	private boolean power;
	public InterchangeInfo(Highway highway){
		this.highway = highway;
		reset();
	}
	public synchronized boolean dash(Car car) {
		if(power && interCar.getPos() >= (highway.getSafeFactor()+1) + highway.getInterPos() + car.getLength() - 1 && !interCar.isAccident()){
			car.setPrevCar(interCar);
			car.setNextCar(interCar.getNextCar());
			if(interCar.getNextCar() != null)
				interCar.getNextCar().setPrevCar(car);
			interCar.setNextCar(car);
			car.setLane(0);
			interCar = car;
			power = false;
			return true;
		}
		return false;
	}
	
	protected void reset(){
		resetPower();
		interCar = new EmptyCar();
	}
	
	protected void resetPower(){
		power = true;
	}
	
	protected Car getInterCar(){
		return interCar;
	}
	
	public void setInterCar(Car car){
		interCar = car;
	}
}
