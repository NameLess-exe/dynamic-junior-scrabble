package scrabble.rabble;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Leo on 5/20/2014.
 */
public class Board {
    private static final String TAG = "Board";

    private Dictionary dictionary = new Dictionary();
    private Context context;
    private Tile[][] board; // board[] is x, columns, width. board[][] is y, rows, height.

    public Board(Context context) {
        this.context = context;
    }

    public void populate(String filename) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            for (String line = null; (line = br.readLine()) != null; )
                dictionary.add(line);
            /*
            Collections.sort(dictionary, new Comparator<String>() {
                @Override
                public int compare(String s, String s2) {
                    return s.length() > s2.length() ? 1 : (s.length() < s2.length() ? -1 : 0);
                }
            });
            */
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // Consume exception, not much we can do. Memory leak here?
                }
            }
        }
    }

    public ArrayList generateBoard(int width, int height) {
        Random random = new Random();
        board = new Tile[width][height];
        for (Tile[] row : board)
            Arrays.fill(row, new Nothing());

        int words = 1;

        for (int i = 0; i < words; i++) {
            Word.Orientation orientation = Word.Orientation.random();
            Word word = new Word(dictionary.get(random.nextInt(dictionary.size())), orientation);
            // Coordinates of word relative to board
            int x1 = 0;
            int y1 = 0;
            int x2 = x1 + word.getWidth();
            int y2 = y1 + word.getHeight();
            Log.i(TAG, word.toString());
            for (int y = y1; y < y2; y++) {
                for (int x = x1; x < x2; x++) {
                    board[x][y] = new Letter(word.getLetter(x + y));
                }
            }

            // Pick intersecting letter
            char letter = word.getLetter(random.nextInt(word.length()));
            
            // Pick another word that contains the intersection letter
            ArrayList<Integer> wordsContaining = dictionary.wordsContaining(letter);
            int index = wordsContaining.get(random.nextInt(wordsContaining.size()));
            Word word2 = new Word(dictionary.get(index), orientation == Word.Orientation.LEFT_RIGHT ? Word.Orientation.TOP_DOWN : Word.Orientation.LEFT_RIGHT);
        }

        ArrayList<Tile> ret = new ArrayList<Tile>();
        for (Tile[] row : board) {
            for (Tile t : row)
                ret.add(t);
        }

        return ret;
    }

    public interface Tile {
        View getView();
    }

    private static class Dictionary {
        private LinkedList<String> dictionary = new LinkedList<String>();
        private HashMap<Character, ArrayList<Integer>> contain = new HashMap<Character, ArrayList<Integer>>();

        public void add(String word) {
            dictionary.add(word);
            for (char c : word.toCharArray()) {
                if (contain.get(c) == null)
                    contain.put(c, new ArrayList<Integer>());
                contain.get(c).add(dictionary.size() - 1);
            }
        }

        public String get(int n) {
            return dictionary.get(n);
        }

        public ArrayList<Integer> wordsContaining(char c) {
            return contain.get(c);
        }

        public int size() {
            return dictionary.size();
        }
    }

    private static class Word {
        private String word;
        private Orientation orientation;
        private int width;
        private int height;

        private Word(String word, Orientation orientation) {
            this.word = word;
            this.orientation = orientation;
            width = orientation == Orientation.LEFT_RIGHT ? word.length() : 1;
            height = orientation == Orientation.TOP_DOWN ? word.length() : 1;
        }

        public int length() {
            return word.length();
        }

        public Orientation orientation() {
            return orientation;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public char getLetter(int n) {
            return word.charAt(n);
        }

        public String getWord() {
            return word;
        }

        @Override
        public String toString() {
            return word;
        }

        private enum Orientation {
            LEFT_RIGHT, TOP_DOWN;

            public static Orientation random() {
                Random random = new Random();
                Orientation[] orientations = Orientation.values();

                return orientations[random.nextInt(orientations.length)];
            }
        }
    }

    private class Nothing implements Tile {
        @Override
        public View getView() {
            return LayoutInflater.from(context).inflate(R.layout.nothing, null);
        }
    }

    private class Letter implements Tile {
        private char letter;

        private Letter(char letter) {
            this.letter = letter;
        }

        @Override
        public View getView() {
            Button button = (Button) LayoutInflater.from(context).inflate(R.layout.letter, null);
            button.setText(String.valueOf(letter));
            return button;
        }
    }
}
