package scrabble.rabble.Server;

public class IsValidName{

    private boolean isValid;
    private String name;
    
    public void setValid(boolean b){
        isValid = b;
    }
    
    public boolean getValid(){
        return isValid;
    }
    
    public void setName(String b){
        name = b;
    }
    
    public String getName(){
        return name;
    }
}