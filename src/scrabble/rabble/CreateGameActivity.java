package scrabble.rabble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class CreateGameActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_game,
					container, false);
			return rootView;
		}
	}
	
	public void joinGame(View view){
		/*
		Intent intent = new Intent(this, INSERT_NAME_HERE.class);
		EditText edit1 = (EditText) findViewById(R.id.editText_gameName);
		EditText edit2 = (EditText) findViewById(R.id.editText_maxPlayer);
		String sName = edit1.getText().toString();
		String sPlayers = edit2.getText().toString();
		intent.putExtra(serverName, sName);
		intent.putExtra(maxPlayers, sPlayers);
	   // public final static String serverName = "scrabble.rabble.SERVER_NAME";
	   // public final static String maxPlayers = "scrabble.rabble.NUM_PLAYERS";
		*/
	}

}
