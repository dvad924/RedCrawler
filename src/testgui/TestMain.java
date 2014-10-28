package testgui;

import redcrawl.utils.TimeKeeper;


public class TestMain {
	public static void main(String[] args){
		TimeKeeper tk = TimeKeeper.getTimeKeeper();
		
		while(true){
			if(tk.timeAvailable()){
				System.out.println(tk.toString());
				tk.noteLastRequest();
			}
		}
		
	}
}
