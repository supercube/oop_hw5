package highwaysimulate;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

public class Accident{
	private int pos, lane;
	public int accPad1, accPad2;
	protected static Image img;
	private static Random rnd;
	static {
		img = new TransparentIcon(Constant.URL,"Images/accident.png", Color.black).getIcon().getImage();
		rnd = new Random();
	}
	public Accident(int pos, int lane){
		this.pos = pos;
		this.lane = lane;
		accPad1 = rnd.nextInt(10) - 4;
		accPad2 = rnd.nextInt(10) - 4;
	}
	
	public Image getImage(){
		return img;
	}
	
	public int getPos(){
		return pos;
	}
	
	public int getLane(){
		return lane;
	}
}
