package scrabble.rabble.board;

/**
 * Created by Leo on 5/21/2014.
 */
public class Letter implements Tile {
    private char letter;

    public Letter(char letter) {
        this.letter = letter;
    }

    @Override
    public char getChar() {
        return letter;
    }

}
