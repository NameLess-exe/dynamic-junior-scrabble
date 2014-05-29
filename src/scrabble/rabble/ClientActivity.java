package scrabble.rabble;

import java.util.ArrayList;

import scrabble.rabble.model.Player;
import scrabble.rabble.model.PlayerList;
import scrabble.rabble.model.Sendable;
import scrabble.rabble.model.Tile;
import scrabble.rabble.model.TilePool;
import scrabble.rabble.model.Type;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClientActivity extends ActionBarActivity {
	RelativeLayout client; // screen with main UI
	RelativeLayout details; // screen with UI for getting player details
	FrameLayout baseLayout;
	Player myPlayer;
	TilePool tempTilePool;

	// ////////// TEST \\\\\\\\\\\\
	PlayerList alp;
	// /////////////\\\\\\\\\\\\\\\

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
		ArrayList<String> words = new ArrayList<String>() {
			{
				add("HELLO");
				add("SCRABBLE");
				add("GAME");
				add("DEMONSTRATION");
			}
		};
		tempTilePool = new TilePool(words);
		baseLayout = (FrameLayout) findViewById(R.id.container);
		client = (RelativeLayout) findViewById(R.id.layout_client_screen);
		details = (RelativeLayout) findViewById(R.id.layout_getPlayer);
		baseLayout.removeView(client); // Sets the UI to the first screen
		Player temp = new Player();
		temp.setIdentifier(25);
		dispatch((Sendable) temp);
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
		Player temp = new Player();
		CharSequence text = "";
		if (checkInput(ageText.getText().toString()) == false) {
			text = "Please enter numbers only";
			ageText.setText("");
		} else if (nameText.getText().toString().length() == 0) {
			text = "Please enter your name";
			nameText.setText("");
		} else if (myPlayer.getAvatar() == -1)
			text = "Please select an avatar";
		else {
			myPlayer.setName(temp.getName());
			myPlayer.setAge(temp.getAge());

			// network.send((Sendable) myPlayer.getTiles().get(i));

			// //////////// TEST \\\\\\\\\\\\\\\\
			myPlayer.setTurn(true);
			myPlayer.setTiles(tempTilePool.getTiles(5));
			Player p1 = new Player();
			p1.setName("Tom");
			p1.setTiles(tempTilePool.getTiles(5));
			Player p2 = new Player();
			p2.setName("Jojo");
			p2.setTiles(tempTilePool.getTiles(5));
			Player p3 = new Player();
			p3.setName("Leo");
			p3.setTiles(tempTilePool.getTiles(5));
			alp = new PlayerList();
			alp.addPlayer(p1);
			alp.addPlayer(myPlayer);
			alp.addPlayer(p2);
			alp.addPlayer(p3);
			dispatch((Sendable) alp);
			// //////////////\\\\\\\\\\\\\\\\\\\\\
		}
		if (text != "") {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
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

	public void updateUi(PlayerList playerList) {
		int numPlayers = playerList.size();
		// Retrieves the correct player object matching the client
		for (int i = 0; i < numPlayers; i++) {
			if (myPlayer.getIdentifier() == playerList.get(i).getIdentifier()) {
				myPlayer = playerList.removePlayer(i); // update the player
				break;
			}
		}
		// Change the colour of the player's layout if it is their turn
		TextView temp = (TextView) findViewById(R.id.textView_myTurn);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.player_1);
		if (myPlayer.getTurn() == true) {
			temp.setText(R.string.text_my_turn);
			layout.setBackgroundColor(Color.parseColor("#FFFF00")); // Yellow
		} else {
			temp.setText(R.string.text_not_my_turn);
			layout.setBackgroundColor(Color.parseColor("#99CCFF")); // Blue
		}
		temp = (TextView) findViewById(R.id.textView_myScore);
		temp.setText("Score: " + myPlayer.getPoints());
		ArrayList<Tile> playerTiles = myPlayer.getTiles();
		// Reset the textViews holding each tile
		for (int i = 0; i < 5; i++) {
			TextView tv = (TextView) findViewById(getResources().getIdentifier(
					"textView_tile" + Integer.toString(i + 1), "id",
					"scrabble.rabble"));
			tv.setText(" ");
		}
		// Set the textViews to display the new tiles
		for (int i = 0; i < playerTiles.size(); i++) {
			TextView tv = (TextView) findViewById(getResources().getIdentifier(
					"textView_tile" + Integer.toString(i + 1), "id",
					"scrabble.rabble"));
			tv.setText(Character.toString(playerTiles.get(i).getValue()));
		}
		// Update the other players
		for (int i = 0; i < playerList.size(); i++) {
			layout = (RelativeLayout) findViewById(getResources()
					.getIdentifier("player_" + Integer.toString(i + 2), "id",
							"scrabble.rabble"));
			// Set the colour/background if it their turn
			if (playerList.get(i).getTurn() == true) {
				layout.setBackgroundColor(Color.parseColor("#FFFF00")); // Yellow
			} else
				layout.setBackgroundColor(Color.parseColor("#99CCFF"));// Blue

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
			for (int x = 0; x < playerTiles.size(); x++) {
				TextView tv = (TextView) findViewById(getResources()
						.getIdentifier(
								"textView_P" + Integer.toString(i + 1) + "T"
										+ Integer.toString(x + 1), "id",
								getPackageName()));
				tv.setText(Character.toString(playerTiles.get(x).getValue()));
			}
		}
	}

	public boolean checkInput(String age) {
		boolean isValid = true;
		try {
			Integer.parseInt(age.toString());
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}

	public void setAvatar(View view) {
		TextView temp = (TextView) view;

		for (int i = 0; i < 9; i++) {
			TextView tv = (TextView) findViewById(getResources().getIdentifier(
					"player_avatar" + Integer.toString(i + 1), "id",
					"scrabble.rabble"));
			tv.setBackgroundColor(Color.parseColor("#0066CC"));
		}
		for (int i = 0; i < 9; i++) {
			if (temp.getId() == getResources().getIdentifier(
					"player_avatar" + Integer.toString(i + 1), "id",
					"scrabble.rabble")) {
				myPlayer.setAvatar(i + 1);
				temp.setBackgroundColor(Color.parseColor("#FFFF00"));

			}
		}
	}

	public void tilePressed(View view) {
		if (myPlayer.getTurn() == false)
			return;
		TextView temp = (TextView) view;
		boolean valid = true;
		int i = 0;
		while(valid == true){
			if (temp.getId() == getResources().getIdentifier(
					"textView_tile" + Integer.toString(i + 1), "id",
					"scrabble.rabble")) {
				// temp.setBackgroundColor(Color.parseColor("#0000FF"));

				// network.send((Sendable) myPlayer.getTiles().get(i));

				myPlayer.removeTile(myPlayer.getTiles().get(i));
				myPlayer.showTiles();
				myPlayer.setTurn(false);
				alp.addPlayer(myPlayer);
				alp.get(0).setTurn(true);
				valid = false;
			}
			i++;
			if (i>myPlayer.getTiles().size()) valid = false;
		}
		dispatch((Sendable) alp);
	}

	public void dispatch(Sendable o) {
		if (o.getType() == Type.PLAYER) {
			myPlayer = (Player) o;
		} else if (o.getType() == Type.PLAYERLIST) {
			PlayerList p = (PlayerList) o;
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