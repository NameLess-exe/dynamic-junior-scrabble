package scrabble.rabble;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClientActivity extends ActionBarActivity {
	TilePool tp = new TilePool();												// server tile pool
	Player p = new Player();													// a server player
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
		tp.getPool();															// load a tile pool
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
		if (name.length() >0 && name.length() <= 10){
			rl.removeAllViews();
			showHand(rl,name);
		}
		else{
			TextView tv = (TextView) findViewById(R.id.enter_name);
			tv.setWidth(200);
			tv.setText("");
			tv.setHint(getString(R.string.enter_name));
		}
				
	}
	public void showHand(RelativeLayout l, String name){
		// section should be inside the server, not the client code
		// name should be sent into the serve (code) where the new player is made, then sent back
		System.out.println("Hello");
		p.construct(tp.getTiles(5));
		p.name = name;
		///////////////////////////////////////////////////////////

		updateTiles(l, p);
	}
	
	// update the buttons on the client to match the player
	public void updateTiles(RelativeLayout l, Player p){
		l.removeAllViews();
		Button b = new Button(this);
		b.setText(p.name);								// button with the player name
		l.addView(b);
		for(int i = 0; i < 5; i++){
			b = new Button(this);						// buttons with the tile information
			b.setId(20+i);
			b.setX(200+ 100 * i);						// this layout can be changed, just a placeholder
			b.setText(p.tiles[i]);
			b.setOnClickListener(onClickL);
			l.addView(b);
		}
	}
	// solution: change, call a refresh
	View.OnClickListener onClickL = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Button b = (Button) findViewById(v.getId());
			String[] s = new String[1];
			s[0] = b.getText().toString();
			if(tp.isEmpty() == false){
				p.replace(s, tp.getTiles(1));					// replaces the buttons text with a new character (tile)
			}
			else System.out.println("NO TILES LEFT");
		    updateTiles((RelativeLayout) findViewById(R.id.layout), p);
			System.out.println(b.getText().toString());
		}
	};
}
