package cars;

import highwaysimulate.Constant;
import highwaysimulate.Highway;
import highwaysimulate.Info;
import highwaysimulate.TransparentIcon;

import java.awt.Color;
import java.awt.Image;

public class Van extends Car{
	private static Image img = new TransparentIcon(Constant.URL,"Images/van.png", Color.white).getIcon().getImage();
	public Van(int pos, Highway highway, Info info){
		super(pos, highway, info);
		length = 2;
	}
	
	public Image getImage(){
		return img;
	}
}
