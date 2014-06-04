package scrabble.rabble;

/**
 * Created by Leo on 5/21/2014.
 */
public enum Orientation {
    LEFT_RIGHT, TOP_DOWN;

    public static Orientation tangentTo(Orientation orientation) {
        return orientation == LEFT_RIGHT ? TOP_DOWN : LEFT_RIGHT;
    }

    public Orientation tanget() {
        return tangentTo(this);
    }
}
