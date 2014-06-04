package scrabble.rabble;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Leo on 5/21/2014.
 */
public class Letter implements IntTile {
    private char letter;
    private Context context;

    public Letter(Context context, char letter) {
        this.letter = letter;
        this.context = context;
    }

    @Override
    public View getView() {
        Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.nothing, null);
        button.setText(String.valueOf(letter));
        return button;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public char getChar() {
        return letter;
    }
}
