package cars;

import highwaysimulate.Constant;
import highwaysimulate.Highway;
import highwaysimulate.Info;
import highwaysimulate.TransparentIcon;

import java.awt.Color;
import java.awt.Image;

public class Truck extends Car{
		private static Image img = new TransparentIcon(Constant.URL,"Images/truck.png", Color.white).getIcon().getImage();
		public Truck(int pos, Highway highway, Info info){
			super(pos, highway, info);
			length = 3;
		}
		
		public Image getImage(){
			return img;
		}
	}
