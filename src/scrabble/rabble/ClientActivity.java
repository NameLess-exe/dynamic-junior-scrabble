package scrabble.rabble;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

public class ClientActivity extends ActionBarActivity {
	RelativeLayout client; // screen with main UI
	RelativeLayout details; // screen with UI for getting player details
	FrameLayout baseLayout;
	Player myPlayer;
	TilePool tempTilePool;
	
	boolean firstUpdate = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		EditText nameText = (EditText) findViewById(R.id.editText_Name);
		EditText ageText = (EditText) findViewById(R.id.editText_Age);
		nameText.setText("Matt");
		ageText.setText("18");
		ArrayList<String> words = new ArrayList<String>() {{
		    add("HELLO");
		    add("SCRABBLE");
		    add("GAME");
		    add("DEMONSTRATION");
		}};
		tempTilePool = new TilePool(words);
		baseLayout = (FrameLayout) findViewById(R.id.container);
		client = (RelativeLayout) findViewById(R.id.layout_client_screen);
		details = (RelativeLayout) findViewById(R.id.layout_getPlayer);
		baseLayout.removeView(client); // Sets the UI to the first screen
		myPlayer = new Player();
		myPlayer.setIdentifier(25);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_client,
					container, false);
			return rootView;
		}
	}

	public void joinGame(View view) {
		EditText nameText = (EditText) findViewById(R.id.editText_Name);
		EditText ageText = (EditText) findViewById(R.id.editText_Age);
		Player temp = checkInput(nameText.getText().toString(), ageText
				.getText().toString());
		if (myPlayer == null) {
			ageText.setText("");
			ageText.setHint("Enter a number only");
			nameText.setHint("Enter your name");
		} else {
			myPlayer.setName(temp.getName());
			myPlayer.setAge(temp.getAge());
			myPlayer.setTurn(true);
			myPlayer.setTiles(tempTilePool.getTiles(5));
			Player p1 = new Player();
			p1.setName("Jeff");
			p1.setTiles(tempTilePool.getTiles(5));
			Player p2 = new Player();
			p2.setName("Jenny");
			p2.setTiles(tempTilePool.getTiles(5));
			Player p3 = new Player();
			p3.setName("Kenny");
			p3.setTiles(tempTilePool.getTiles(5));

			ArrayList<Player> alp = new ArrayList<Player>();
			alp.add(p1);
			alp.add(myPlayer);
			alp.add(p2);
			alp.add(p3);
			addUi(alp.size());
			updateUi(alp);
		}
	}

	public void addUi(int numPlayers) {
		baseLayout.removeAllViews();
		baseLayout.addView(client);
		int temp = 4; // set to the last player
		while (temp > numPlayers) {
			client.removeView((RelativeLayout) findViewById(getResources()
					.getIdentifier("player_" + Integer.toString(temp), "id",
							"scrabble.rabble")));
			temp--;
		}
	}

	public void updateUi(ArrayList<Player> playerList) {
		int numPlayers = playerList.size();
		for (int i = 0; i < numPlayers; i++) {
			if (myPlayer.getIdentifier() == playerList.get(i).getIdentifier()) {
				myPlayer = playerList.remove(i); // update the player
				break;
			}
		}
		TextView temp = (TextView) findViewById(R.id.textView_myTurn);
		if (myPlayer.getTurn() == true) {
			temp.setText(R.string.text_my_turn);
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.player_1);
			layout.setBackgroundColor(Color.parseColor("#FFFF00"));
		} else
			temp.setText(R.string.text_not_my_turn);

		ArrayList<Tile> playerTiles = myPlayer.getTiles();
		for(int i = 0;i < playerTiles.size();i++){
			TextView tv = (TextView) findViewById(getResources().getIdentifier("textView_tile"+Integer.toString(i + 1), "id", "scrabble.rabble"));
			tv.setText(Character.toString(playerTiles.get(i).getValue()));
		}
		Log.d("Test", Integer.toString(playerList.size()));
		for (int i = 0; i < playerList.size(); i++) {

			playerList.get(i).showTiles();
			if (playerList.get(i).getTurn() == true) {
				RelativeLayout layout = (RelativeLayout) findViewById(getResources().getIdentifier("player_" + Integer.toString(i + 2), "id", "scrabble.rabble"));
				layout.setBackgroundColor(Color.parseColor("#FFFF00"));
			}
			temp = (TextView) findViewById(getResources().getIdentifier(
					"textView_P" + Integer.toString(i + 1) + "Name", "id",
					"scrabble.rabble"));
			temp.setText(playerList.get(i).getName()); // set the player name in
														// each layout
			temp = (TextView) findViewById(getResources().getIdentifier(
					"textView_P" + Integer.toString(i + 1) + "Score", "id",
					"scrabble.rabble"));
			temp.setText("Score: "
					+ Integer.toString(playerList.get(i).getPoints()));
			playerTiles = playerList.get(i).getTiles();
			for(int x = 0;x < playerTiles.size();x++){
				TextView tv = (TextView) findViewById(getResources().getIdentifier("textView_P"+Integer.toString(i + 1)+"T"+Integer.toString(x + 1), "id", getPackageName()));
				tv.setText(Character.toString(playerTiles.get(x).getValue()));
			}
		}
		
	}

	public Player checkInput(String name, String age) {
		boolean isValid = true;
		Player p = new Player();
		try {
			Integer.parseInt(age.toString());
			if (name.length() > 0) {
				isValid = true;
			} else {
				return null;
			}
		} catch (Exception e) {
			isValid = false;
		}
		if (isValid == true) {
			p.setName(name);
			p.setAge(Integer.parseInt(age));
			// send myPlayer to the server, store it in the arraylist of the
			// server
			return p;
		} else {
			return null;
		}
	}

	public void dispatch(Object o) {
		Log.d("Type", o.getClass().getName());
		if (o.getClass().getName().toString() == "Player") {
			myPlayer = (Player) o;
		} else if (o.getClass().getName().toString() == "java.util.ArrayList") {
			ArrayList<Player> p = (ArrayList<Player>) o;
			if (firstUpdate == true) {
				firstUpdate = false;
				addUi(p.size());
				updateUi(p);
			} else {
				updateUi(p);
			}
		} else {
			Log.e("Object type error",
					"Object passed was not an ArrayList<Player>");
		}
	}
}

// client = (RelativeLayout)
// findViewById(getResources().getIdentifier("layout_client_screen",
// "id","scrabble.rabble"));
// android:label="@string/title_activity_client" >