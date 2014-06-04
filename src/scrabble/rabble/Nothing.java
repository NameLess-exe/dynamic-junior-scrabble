package scrabble.rabble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Leo on 5/21/2014.
 */
public class Nothing implements IntTile {
    private Context context;

    public Nothing(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {
        Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.nothing, null);
        return button;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
