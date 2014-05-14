package scrabble.rabble.Server;

import scrabble.rabble.R;
import scrabble.rabble.Model.Player;
import scrabble.rabble.R.id;
import scrabble.rabble.R.string;

// Object contain back end logic for the player hands (client)

public class ClientLogic{

    public IsValidName checkName(Player p){
        IsValidName vn = new IsValidName();
        String name = p.name;
        if (name.length() >0 && name.length() <= 10){
			Boolean b = false;
			for(int i = 0;i < name.length();i++){
				String s = Character.toString(name.charAt(i));
                if(!s.matches("[A-Za-z]")) b= true;
			}
			if(b == false){
				String caps = "";
				for(int i = 0;i < name.length();i++){
					String c = Character.toString(name.charAt(i));
					if(i == 0) c = c.toUpperCase();
					caps += c;
				}
				vn.setName(caps);
                vn.setValid(true);
                return vn;
			}
			else{
                vn.setValid(false);
                return vn;
			}
		}
		else{
            vn.setValid(false);
            return vn;
		}
    }
}