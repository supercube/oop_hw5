package highwaysimulate;

import java.awt.Color;
import java.awt.Image;

public class Accident{
	public int pos, lane;
	
	protected static Image img;
	static {
		img = new TransparentIcon(Constant.URL,"Images/accident.png", Color.black).getIcon().getImage();

	}
	public Accident(int pos, int lane){
		this.pos = pos;
		this.lane = lane;
	}
}
