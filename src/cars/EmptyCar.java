package cars;

import java.awt.Image;
import highwaysimulate.Constant;

public class EmptyCar extends Car{

	public EmptyCar() {
		super(Constant.INFINITE, 0, null);
	}
	
	public Image getImage(){
		return null;
	}
}
