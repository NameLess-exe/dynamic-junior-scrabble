package scrabble.rabble.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by Leo on 5/14/2014.
 */
public class Board {
    private static final long TIMEOUT = 50;
    private int width;
    private int height;
    private Tile[][] board;
    private Dictionary dictionary;
    private Random rnd;
    private HashSet<String> words;

    public Board(int width, int height, Dictionary dictionary) {
        this.width = width;
        this.height = height;
        this.board = new Tile[width][height];
        // board[]      is columns (top down), x coordinate, width
        // board[][]    is rows (left right, y coordinate, height
        for (Tile[] c : board)
            Arrays.fill(c, new Nothing());
        this.rnd = new Random();
        this.words = new HashSet<String>();
		dictionary.sort();
		this.dictionary = dictionary;
    }

    public Tile[][] get2dArray() {
        return board;
    }

    public void generate() {
        Orientation orientation = Orientation.values()[rnd.nextInt(Orientation.values().length)];
        Word word = new Word(dictionary.get(), orientation);
        int x1 = 0;
        int y1 = 0;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < TIMEOUT) {
            words.add(word.toString());
            if (legalPlacement(word, x1, y1))
                placeWord(word, x1, y1);

            // next word
            orientation = word.getOrientation().tanget();
            // loop this

            List<Integer> shuffled = fillFromTo(0, word.length());
            Collections.shuffle(shuffled);
            boolean found = false;
            pickIntersectingLetter:
            for (int pos1 : shuffled) {
                char letter = word.charAt(pos1);

                int letterX = x1 + (word.getOrientation() == Orientation.LEFT_RIGHT ? pos1 : 0);
                int letterY = y1 + (word.getOrientation() == Orientation.TOP_DOWN ? pos1 : 0);

                found = false;
                for (String i : dictionary) {
                    Word word2 = new Word(i, orientation);
                    if (word2.indexOf(letter) == -1)
                        continue;
                    // loop for every instance of letter in w TODO
                    int pos2 = word2.indexOf(letter);
                    if (pos2 == -1)
                        continue;
                    int tempx1 = letterX - (orientation == Orientation.LEFT_RIGHT ? pos2 : 0);
                    int tempy1 = letterY - (orientation == Orientation.TOP_DOWN ? pos2 : 0);
                    if (legalPlacement(word2, tempx1, tempy1)) {
                        word = word2;
                        x1 = tempx1;
                        y1 = tempy1;
                        found = true;
                        dictionary.remove(word.toString());
                        break pickIntersectingLetter;
                    }
                }
            }
            if (!found) {
                return;
            }
        }
    }

    private List<Integer> fillFromTo(int from, int to) {
        List<Integer> list = new ArrayList<Integer>();
        for (; from < to; from++)
            list.add(from);
        return list;
    }

    private boolean legalPlacement(Word word, int x1, int y1) {
        if (x1 < 0 || y1 < 0)
            return false;
        int x2 = x1 + (word.getOrientation() == Orientation.LEFT_RIGHT ? word.length() : 1);
        int y2 = y1 + (word.getOrientation() == Orientation.TOP_DOWN ? word.length() : 1);
        if (x2 > width || y2 > height)
            return false;
        Point point = new Point(x1, y1);
        if (word.getOrientation() == Orientation.LEFT_RIGHT) {
            if (point.getLeft() instanceof Letter)
                return false;
        }
        if (word.getOrientation() == Orientation.TOP_DOWN) {
            if (point.getTop() instanceof Letter)
                return false;
        }
        for (int x = x1, i = 0; x < x2; x++) {
            for (int y = y1; y < y2; y++, i++) {
                point = new Point(x, y);
                if (board[x][y] instanceof Letter) {
                    if (board[x][y].getChar() != word.charAt(i))
                        return false;
                } else if (word.getOrientation() == Orientation.LEFT_RIGHT) {
                    if (point.getTop() instanceof Letter)
                        return false;
                    if (point.getBottom() instanceof Letter)
                        return false;
                } else if (word.getOrientation() == Orientation.TOP_DOWN) {
                    if (point.getLeft() instanceof Letter)
                        return false;
                    if (point.getRight() instanceof Letter)
                        return false;
                }
            }
        }
        if (word.getOrientation() == Orientation.LEFT_RIGHT) {
            if (point.getRight() instanceof Letter)
                return false;
        } else if (word.getOrientation() == Orientation.TOP_DOWN) {
            if (point.getBottom() instanceof Letter)
                return false;
        }
        return true;
    }

    private void placeWord(Word word, int x1, int y1) {
        int x2 = x1 + (word.getOrientation() == Orientation.LEFT_RIGHT ? word.length() : 1);
        int y2 = y1 + (word.getOrientation() == Orientation.TOP_DOWN ? word.length() : 1);
        for (int x = x1, i = 0; x < x2; x++) {
            for (int y = y1; y < y2; y++, i++)
                board[x][y] = new Letter(word.charAt(i));
        }
    }

    public ArrayList<Tile> flatten() {
        ArrayList<Tile> ret = new ArrayList<Tile>();
        for (Tile[] c : board)
            ret.addAll(Arrays.asList(c));
        return ret;
    }

    private class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Tile getTop() {
            if (y - 1 >= 0)
                return board[x][y - 1];
            return null;
        }

        public Tile getLeft() {
            if (x - 1 >= 0)
                return board[x - 1][y];
            return null;
        }

        public Tile get() {
            return board[x][y];
        }

        public Tile getRight() {
            if (x + 1 < width)
                return board[x + 1][y];
            return null;
        }

        public Tile getBottom() {
            if (y + 1 < height)
                return board[x][y + 1];
            return null;
        }
    }
}
