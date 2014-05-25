package scrabble.rabble;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import android.provider.Settings.Secure;

public class ClientActivity extends ActionBarActivity {
	RelativeLayout client; // screen with main UI
	RelativeLayout details; // screen with UI for getting player details
	FrameLayout baseLayout;
	ClientLogic logic;
	Player myPlayer;
	boolean firstUpdate = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		logic = new ClientLogic();
		baseLayout = (FrameLayout) findViewById(R.id.container);
		client = (RelativeLayout) findViewById(R.id.layout_client_screen);
		details = (RelativeLayout) findViewById(R.id.layout_getPlayer);
		baseLayout.removeView(client); // Sets the UI to the first screen
		myPlayer = new Player();
		myPlayer.setIdentifier(25);;
		
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
		Player temp = logic.checkInput(nameText.getText().toString(), ageText
				.getText().toString());
		if (myPlayer == null) {
			ageText.setText("");
			ageText.setHint("Enter a number only");
			nameText.setHint("Enter your name");
		} else {
			myPlayer.setName(temp.getName());
			myPlayer.setAge(temp.getAge());
			myPlayer.setTurn(false);
			Player p1 = new Player();
			p1.setName("Jeff");
			Player p2 = new Player();
			p2.setName("Jenny");
			Player p3 = new Player();
			p3.setName("Kenny");
			ArrayList<Player> alp = new ArrayList<Player>();
			alp .add(p1);
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
		int toRemove = 4 - numPlayers; // get the number of players to remove
		int temp = 3; // set to the last player
		for (int i = 0; i < toRemove; i++) {
			client.removeView((LinearLayout) findViewById(getResources()
					.getIdentifier("layout_player" + temp, "id",
							"scrabble.rabble")));
			client.removeView((TextView) findViewById(getResources()
					.getIdentifier("textView_P" + temp + "Name", "id",
							"scrabble.rabble")));
			client.removeView((TextView) findViewById(getResources()
					.getIdentifier("textView_P" + temp + "Score", "id",
							"scrabble.rabble")));
			client.removeView((ImageView) findViewById(getResources()
					.getIdentifier("avatar" + temp, "id", "scrabble.rabble")));
			temp--;
		}
	}

	public void updateUi(ArrayList<Player> playerList) {
		int numPlayers = playerList.size();
		for (int i = 0; i < numPlayers; i++) {
			if (myPlayer.getIdentifier() == playerList.get(i).getIdentifier()){
				myPlayer = playerList.remove(i);				// update the player
				break;
			}
		}
		
		TextView temp = (TextView) findViewById(R.id.textView_myTurn);
		if (myPlayer.getTurn() == true) temp.setText(R.string.text_my_turn);
		else temp.setText(R.string.text_not_my_turn);
		for (int i = 0;i < playerList.size();i++){
			temp = (TextView) findViewById(getResources().getIdentifier("textView_P" + Integer.toString(i + 1) + "Name", "id", "scrabble.rabble"));
			temp.setText(playerList.get(i).getName());
			temp = (TextView) findViewById(getResources().getIdentifier("textView_P" + Integer.toString(i + 1) + "Score", "id", "scrabble.rabble"));
			temp.setText("Score: "+ Integer.toString(playerList.get(i).getPoints()));
		}
	}

	
	public void dispatch(Object o) {
		Log.d("Type", o.getClass().getName());
		if (o.getClass().getName().toString() == "Player") {
			myPlayer = (Player) o;
		}
		else if (o.getClass().getName().toString() == "java.util.ArrayList") {
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