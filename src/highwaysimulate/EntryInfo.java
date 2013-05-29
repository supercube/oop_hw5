package highwaysimulate;

import cars.*;

public class EntryInfo extends Info{
	
	private Car[] entryCar;
	private boolean[] power;
	private int usedLane;
	private Info interInfo;
	public int tt;
	public EntryInfo(int usedLane, Info info){
		power = new boolean[Constant.MAX_LANE];
		interInfo = info;
		reset(usedLane);
	}
	public EntryInfo(Info info){
		this(Constant.MAX_LANE, info);
	}
	
	public boolean dash(Car car){
		tt++;
		for(int i = 0; i < usedLane; i++){
			if(power[i] && entryCar[i] == null){
				car.setPrevCar(new EmptyCar());
				car.setLane(i);
				entryCar[i] = car;
				power[i] = false;
				return true;
			}else{
				synchronized(entryCar[i]){
					if(power[i] && entryCar[i].getPos() >= 3){
						car.setPrevCar(entryCar[i]);
						entryCar[i].setNextCar(car);
						car.setLane(i);
						entryCar[i] = car;
						power[i] = false;
						return true;
					}
				}
			}
		}
		return false;
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
	
	public InterchangeInfo getInterInfo(){
		return (InterchangeInfo)interInfo;
	}
}
