package highwaysimulate;

import java.net.URL;

public class Constant{

	public static final int NO_PARAMETER = 5;
	public static final int MAX_LANE = 4;
	public static final int GRIDSIZE = 5;
	public static final int LANEWIDTH = 30;
	public static final int LANEHEIGHT = 10;
	public static final int INTERVAL = 1000;
	public static final int INFINITE = 2147483647;
	public static final URL URL = createURL("http://www.csie.ntu.edu.tw/~b99902008/oop_hw5/");
	public static enum Status{
		WAIT, BEFOREINTER, AFTERINTER, DONE
	}
	private static final URL createURL(String url){
		try{
			return new URL(url);
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
}
