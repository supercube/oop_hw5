package highwaysimulate;

import cars.*;

public class EntryInfo extends Info{
	
	private Car[] entryCar;
	private boolean[] power;
	private Info interInfo;
	private Highway highway;
	
	public EntryInfo(Highway highway, Info info){
		this.highway = highway;
		power = new boolean[Constant.MAX_LANE];
		interInfo = info;
		reset();
	}
	
	public boolean dash(Car car){
		int lane = highway.getUsedLane();
		int safeFactor = highway.getSafeFactor();
		for(int i = 0; i < lane; i++){
			if(power[i] && entryCar[i] == null){
				car.setPrevCar(new EmptyCar());
				car.setLane(i);
				entryCar[i] = car;
				power[i] = false;
				return true;
			}else{
				if(power[i] && entryCar[i].getPos() >= (safeFactor+1) + car.getLength() - 1){
					car.setPrevCar(entryCar[i]);
					entryCar[i].setNextCar(car);
					car.setLane(i);
					entryCar[i] = car;
					power[i] = false;
					return true;
				}
			}
		}
		return false;
	}
	
	protected void reset(){
		resetPower();
		entryCar = new Car[Constant.MAX_LANE];
		for(int i = 0; i < Constant.MAX_LANE; i++){
			entryCar[i] = null;
		}
	}
	
	protected void resetPower(){
		int tmp = highway.getUsedLane();
		for(int i = 0; i < tmp; i++)
			power[i] = true;
	}
	
	protected Car getEntryCar0(){
		return entryCar[0];
	}
	
	public InterchangeInfo getInterInfo(){
		return (InterchangeInfo)interInfo;
	}
}
