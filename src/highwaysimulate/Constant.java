package highwaysimulate;

import java.net.URL;

public class Constant{

	public static final int NO_PARAMETER = 5;
	public static final int GRIDSIZE = 10;
	public static final int INTERVAL = 50;
	public static final URL URL = createURL("http://www.csie.ntu.edu.tw/~b99902008/oop_hw5/");
	
	private static final URL createURL(String url){
		try{
			return new URL(url);
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
}
