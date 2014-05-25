package scrabble.rabble;

import android.provider.Settings.Secure;

public class ClientLogic {
	public Player checkInput(String name, String age) {
		boolean isValid = true;
		Player p = new Player();
		try {
			Integer.parseInt(age.toString());
			if(name.length()>0){
			isValid = true;
			}
			else{
				return null;
			}
		} catch (Exception e) {
			isValid = false;
		}
		if (isValid == true) {
			p.setName(name);
			p.setAge(Integer.parseInt(age));
			// send myPlayer to the server, store it in the arraylist of the server
			return p;
		} else {
			return null;
		}
	}
}
