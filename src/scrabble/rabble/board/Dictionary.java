package scrabble.rabble.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Leo on 5/21/2014.
 */
public class Dictionary implements Iterable<String> {
    private ArrayList<String> dictionary;
    private int pointer;


    public Dictionary() {
        this.dictionary = new ArrayList<String>();
        this.pointer = 0;
    }

	public void sort() {
        Collections.sort(dictionary, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() > o2.length() ? -1 : (o1.length() < o2.length() ? 1 : 0);
            }
        });
    }

    public void remove(String word){
        dictionary.remove(word);
    }

    public void addWord(String word) {
        dictionary.add(word.trim().toLowerCase());
    }

    public String get(int n) {
        return dictionary.get(n);
    }

    public String get() {
        if (pointer++ > dictionary.size())
            Collections.shuffle(dictionary);
        return dictionary.get(pointer);
    }

    public int size() {
        return dictionary.size();
    }

    @Override
    public Iterator<String> iterator() {
        return new DictionaryIterator();
    }

    private class DictionaryIterator implements Iterator<String> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < dictionary.size();
        }

        @Override
        public String next() {
            return dictionary.get(i++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
