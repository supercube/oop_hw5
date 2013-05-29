package cars;

import java.awt.Color;
import java.awt.Image;

import highwaysimulate.Constant;
import highwaysimulate.Info;
import highwaysimulate.TransparentIcon;

public class Sedan extends Car{
	private static Image img = new TransparentIcon(Constant.URL,"Images/sedan.png", Color.white).getIcon().getImage();
	public Sedan(int pos, int limit, Info info){
		super(pos, limit, info);
	}
	
	public Image getImage(){
		return img;
	}
}
