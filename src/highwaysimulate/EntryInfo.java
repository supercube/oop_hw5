package highwaysimulate;

import cars.*;

public class EntryInfo extends Info{
	
	private Car[] entryCar;
	private boolean[] power;
	private int usedLane;
	public int tt;
	public EntryInfo(int usedLane){
		power = new boolean[Constant.MAX_LANE];
		reset(usedLane);
	}
	public EntryInfo(){
		this(Constant.MAX_LANE);
	}
	
	public Car dash(Car car){
		tt++;
		for(int i = 0; i < usedLane; i++){
			if(power[i] && entryCar[i] == null){
				car.setLane(i);
				entryCar[i] = car;
				power[i] = false;
				return new EmptyCar();
			}else{
				synchronized(entryCar[i]){
					if(power[i] && entryCar[i].getPos() >= 3){
						entryCar[i].setNextCar(car);
						car.setLane(i);
						Car tmp = entryCar[i];
						entryCar[i] = car;
						power[i] = false;
						return tmp;
					}
				}
			}
		}
		return null;
	}
	
	protected void reset(int usedLane){
		this.usedLane = usedLane;
		resetPower();
		entryCar = new Car[Constant.MAX_LANE];
		for(int i = 0; i < Constant.MAX_LANE; i++){
			entryCar[i] = null;
		}
	}
	
	protected void resetPower(){
		for(int i = 0; i < usedLane; i++)
			power[i] = true;
		tt = 0;
	}
}
