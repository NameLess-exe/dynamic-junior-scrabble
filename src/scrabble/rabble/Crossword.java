package scrabble.rabble;

import android.content.Context;

import java.util.*;

/**
 * Created by Leo on 5/14/2014.
 */
public class Crossword implements Iterable<IntTile> {
    private int width;
    private int height;
    private IntTile[][] crossword;
    private Dictionary dictionary;
    private Random rnd;
    private HashSet<String> words;
    private Context context;

    public Crossword(int width, int height, Dictionary dictionary, Context context) {
        this.width = width;
        this.height = height;
        this.crossword = new IntTile[width][height];
        // crossword[]      is columns (top down), x coordinate, width
        // crossword[][]    is rows (left right, y coordinate, height
        for (IntTile[] c : crossword)
            Arrays.fill(c, new Nothing(context));
        this.dictionary = dictionary;
        this.rnd = new Random();
        this.words = new HashSet<String>();
        this.context = context;
    }

    public void generate() {
        Orientation orientation = Orientation.values()[rnd.nextInt(Orientation.values().length)];
        Word word = new Word(dictionary.get(), orientation);
        int x1 = width / 2;
        int y1 = height / 2;
        for (int a = 0; a < 3; a++) {
            words.add(word.toString());
            if (legalPlacement(word, x1, y1))
                placeWord(word, x1, y1);
            else
                System.out.println("illegal word placement");

            // next word
            orientation = word.getOrientation().tanget();
            // loop this
            int pos1 = rnd.nextInt(word.length());
            char letter = word.charAt(pos1);
            int letterX = x1 + (word.getOrientation() == Orientation.LEFT_RIGHT ? pos1 : 0);
            int letterY = y1 + (word.getOrientation() == Orientation.TOP_DOWN ? pos1 : 0);
            Point letterPos = new Point(letterX, letterY);
            //int spaceBefore = word.getOrientation() == Orientation.LEFT_RIGHT ? letterPos.freeSpaceLeft() : letterPos.freeSpaceTop();
            //int spaecAfter = word.getOrientation() == Orientation.LEFT_RIGHT ? letterPos.freeSpaceTop() : letterPos.freeSpaceBottom();

            boolean found = false;
            for (Iterator<String> i = dictionary.iterator(); i.hasNext(); ) {
                word = new Word(i.next(), orientation);
                if (words.contains(word.toString()))
                    continue;
                // loop for every instance of letter in w
                int pos2 = word.indexOf(letter);
                if (pos2 == -1)
                    continue;
                x1 = letterX - (orientation == Orientation.LEFT_RIGHT ? pos2 : 0);
                y1 = letterY - (orientation == Orientation.TOP_DOWN ? pos2 : 0);
                if (legalPlacement(word, x1, y1)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("no valid words found, quitting");
                return;
            }
        }
    }

    private boolean legalPlacement(Word word, int x1, int y1) {
        int x2 = word.getOrientation() == Orientation.LEFT_RIGHT ? word.length() : 1;
        int y2 = word.getOrientation() == Orientation.TOP_DOWN ? word.length() : 1;

        for (int x = x1, i = 0; x1 < x2; x++) {
            for (int y = y1; y1 < y2; y++, i++) {
                Point point = new Point(x, y);
                if (crossword[x][y] instanceof Letter && ((Letter) crossword[x][y]).getChar() != word.charAt(i)) // if words cross over the intersecting letters must match
                    return false;
                else if (point.freeSpaceLeft() > 2 && point.freeSpaceTop() > 2 && point.freeSpaceRight() > 2 && point.freeSpaceBottom() > 2)
                    return false;
                else if (!(((point.getTop() == null || point.getTop() instanceof Nothing) && (point.getBottom() == null || point.getBottom() instanceof Nothing))
                        && ((point.getLeft() == null || point.getLeft() instanceof Nothing) && (point.getRight() == null || point.getRight() instanceof Nothing))))
                    return false; // this if is redundant
            }
        }
        return true;
    }

    private void placeWord(Word word, int x1, int y1) {
        System.out.printf("placing '%1$s' at %2$s,%3$s%n", word, x1, y1);
        int x2 = x1 + (word.getOrientation() == Orientation.LEFT_RIGHT ? word.length() : 1);
        int y2 = y1 + (word.getOrientation() == Orientation.TOP_DOWN ? word.length() : 1);
        for (int x = x1, i = 0; x < x2; x++) {
            for (int y = y1; y < y2; y++, i++)
                crossword[x][y] = new Letter(context, word.charAt(i));
        }
    }

    private class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public IntTile getTopLeft() {
            if (x - 1 > 0 && y - 1 > 0)
                return crossword[x - 1][y - 1];
            return null;
        }

        public IntTile getTop() {
            if (y - 1 > 0)
                return crossword[x][y - 1];
            return null;
        }

        public IntTile getTopRight() {
            if (x + 1 < width && y - 1 > 0)
                return crossword[x + 1][y - 1];
            return null;
        }

        public IntTile getLeft() {
            if (x - 1 > 0)
                return crossword[x - 1][y];
            return null;
        }

        public IntTile get() {
            return crossword[x][y];
        }

        public IntTile getRight() {
            if (x + 1 < width)
                return crossword[x + 1][y];
            return null;
        }

        public IntTile getBottomLeft() {
            if (x - 1 > 0 && y + 1 < height)
                return crossword[x - 1][y + 1];
            return null;
        }

        public IntTile getBottom() {
            if (y + 1 < height)
                return crossword[x][y + 1];
            return null;
        }

        public IntTile getBottomRight() {
            if (x + 1 < width && y + 1 < height)
                return crossword[x + 1][y + 1];
            return null;
        }

        public int freeSpaceLeft() {
            int i;
            for (i = x - 1; i > 0 && crossword[i][y] instanceof Nothing; i--) {
            }
            return x - i;
        }

        public int freeSpaceTop() {
            int i;
            for (i = y - 1; i > 0 && crossword[x][i] instanceof Nothing; i--) {
                System.out.println(i);
            }
            return y - i;
        }

        public int freeSpaceRight() {
            int i;
            for (i = x + 1; i < width && crossword[i][y] instanceof Nothing; i++) {
                System.out.println(i);
            }
            return i - x;
        }

        public int freeSpaceBottom() {
            int i;
            for (i = y + 1; i < height && crossword[x][i] instanceof Nothing; i++) {
                System.out.println(i);
            }
            return i - y;
        }
    }

    public ArrayList<IntTile> flatten() {
        ArrayList<IntTile> ret = new ArrayList<IntTile>();
        for (IntTile[] c : crossword)
            ret.addAll(Arrays.asList(c));
        return ret;
    }


    public IntTile[][] getCrossword() {
        return crossword;
    }

    @Override
    public Iterator iterator() {
        return new CrosswordIterator();
    }

    private class CrosswordIterator implements Iterator<IntTile> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < width * height;
        }

        @Override
        public IntTile next() {
            return crossword[i % width][i++ / height];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
