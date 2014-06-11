package scrabble.rabble.board;

/**
 * Created by Leo on 5/21/2014.
 */
public enum Orientation {
    LEFT_RIGHT, TOP_DOWN;

    public Orientation tanget() {
        return this == LEFT_RIGHT ? TOP_DOWN : LEFT_RIGHT;
    }
}
