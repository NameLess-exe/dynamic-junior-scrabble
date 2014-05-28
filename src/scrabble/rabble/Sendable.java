package scrabble.rabble;

import java.io.Serializable;

/**
 * Created by Leo on 5/14/2014.
 */

public abstract class Sendable implements Serializable {
        public String toString() {
               // return new Gson().toJson(this);
        	return null; 			//fix this you fucking faggot
            }
    
        public byte[] toBytes() {
                return toString().getBytes();
            }
    
        public abstract Type getType();
    }