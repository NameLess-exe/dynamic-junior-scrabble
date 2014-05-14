package scrabble.rabble.Client;

import scrabble.rabble.R;
import scrabble.rabble.Model.Player;
import scrabble.rabble.R.id;
import scrabble.rabble.R.layout;
import scrabble.rabble.R.menu;
import scrabble.rabble.Server.ClientLogic;
import scrabble.rabble.Server.PlayerList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClientActivity extends ActionBarActivity {
	PlayerList pl = new PlayerList();											// server
	Player p = new Player();													// a server player
	ClientLogic cl = new ClientLogic();
	String playerName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout);			// removes the tile_button from the form initially
		rl.removeView(findViewById(R.id.tile_button));
		pl.addPlayer("Zoe");
		pl.addPlayer("Jeff");
		pl.addPlayer("David");
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
	
	// called when a name is entered and the button is pressed
	public void sendName(View view){ 
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout);
		EditText editText = (EditText) findViewById(R.id.enter_name);
		String name = editText.getText().toString();
		Player temp = new Player();
		temp.name = name;
		if ()
		if ()	
		TextView tv = (TextView) findViewById(R.id.enter_name);
		tv.setWidth(200);
		tv.setText("");
		tv.setHint("You may only enter letters");
		
				
	}
	public void generateClientUI(RelativeLayout l){
		// section should be inside the server, not the client code
		// name should be sent into the serve (code) where the new player is made, then sent back
		PlayerNode temp = server.getPlayers();
		pl.addPlayer(playerName);
		while(temp != null){
			//// may not work with client? 
			if(temp.player.identifier == server.numPlayers()){ // will be the last person who joined so will be num of players
				p = temp.player;					// sets the local player to the player of the same name in the server
				break;
			}
			else temp = temp.next;
		}
		///////////////////////////////////////////////////////////
		System.out.println(p.name);
		updateTiles(l, p);
	}
	
	// update the buttons on the client to match the player
	public void updateTiles(RelativeLayout l, Player pt){
		l.removeAllViews();
		PlayerNode temp = server.getPlayers();
		int x = 0;
		while (temp != null){
			Button b = new Button(this);
			b.setId(10+x);
			b.setY((server.numPlayers()-1)*100 - (100 * x));
			b.setWidth(200);
			b.setText(temp.player.name);								// button with the player name
			b.setOnClickListener(changeTiles);
			l.addView(b);
			temp=temp.next;
			x++;
		}

		for(int i = 0; i < 5; i++){
			Button b = new Button(this);						// buttons with the tile information
			b.setId(i);
			b.setX(200+ 100 * i);						// this layout can be changed, just a placeholder
			b.setText(pt.tiles[i]);
			if (pt.identifier != p.identifier) b.setEnabled(false);
			else b.setEnabled(true);
			b.setOnClickListener(onClickL);
			l.addView(b);
		}
	}
	
	View.OnClickListener changeTiles = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Button b = (Button) findViewById(v.getId());
			PlayerNode temp = server.getPlayers();
			while (temp != null){
				if (temp.player.name == b.getText().toString()){
					updateTiles((RelativeLayout) findViewById(R.id.layout), temp.player);
					break;
				}
				else temp = temp.next;
			}
		}
	};
	
	View.OnClickListener onClickL = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Button b = (Button) findViewById(v.getId());
			String[] s = new String[1];
			s[0] = b.getText().toString();
			if(server.tp.isEmpty() == false){
				p.replace(s, server.tp.getTiles(1));					// replaces the buttons text with a new character (tile)
			}
			else System.out.println("NO TILES LEFT");
			server.update(p);
		    updateTiles((RelativeLayout) findViewById(R.id.layout), p);
			System.out.println(b.getText().toString());
		}
	};
}
