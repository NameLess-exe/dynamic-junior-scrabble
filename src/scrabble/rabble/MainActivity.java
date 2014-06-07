<<<<<<< HEAD
package scrabble.rabble;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	ArrayList<String> servers;
	String selectedLobby;
	MediaPlayer player;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		// get an ArrayList<string> of servers
		///////////////// TEST \\\\\\\\\\\\\\\\\\\\\
		servers = new ArrayList<String>();
		servers.add("Leo's server");
		servers.add("Scrabble Party!");
		servers.add("The Scrabble rabble");
		servers.add("The J Scrabs");
		servers.add("Random scrabble lobby");
		servers.add("Nameless lobby");
		updateServers();
		
		//plays the theme song of my life
		player = MediaPlayer.create(this, R.raw.wallpaper);
		player.setLooping(true); // Set looping
		player.setVolume(100,100);
		player.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public void updateServers(){
		LinearLayout list = (LinearLayout) findViewById(R.id.linearLayout_servers);
		for(int i = 0; i < servers.size();i++){
			TextView tv = new TextView(this);
			tv.setId(i);
			tv.setBackgroundResource(R.drawable.outline);
			tv.setTextSize(20);
			tv.setText("  " + servers.get(i));

			list.addView(tv);
			tv.setOnClickListener(new View.OnClickListener() {
			    @Override           
			    public void onClick(View v) {
			    	for(int i = 0;i < servers.size();i++){
			    		TextView temp = (TextView) findViewById(i);
			    		temp.setBackgroundResource(R.drawable.outline);
			    	}
			    	Button btn = (Button) findViewById(R.id.button_joinGame);
			    	btn.setEnabled(true);
			    	TextView temp = (TextView) v;
			    	temp.setBackgroundResource(R.drawable.outline_selected);
			    	selectedLobby = temp.getText().toString();
			    }
			});
		}
	}
	
	 public void joinGame(View view){
		 // request the server to join as a client, then execute 
		 Intent intent = new Intent(this,ClientActivity.class);
		 startActivity(intent);
	 }
	 
	 public void createGame(View view){
		Intent intent = new Intent(this, BoardActivity.class);
		startActivity(intent);
	 }
	 
	 @Override
	 public void onPause() {
		 super.onPause();
		 //pauses the epic beats
		 player.pause();
	 }
	 
	 @Override
	 public void onResume() {
		 super.onResume();
		 //resumes the beats
		 player.start();
	 }
}
=======
package scrabble.rabble;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	ArrayList<String> servers;
	String selectedLobby;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// get an ArrayList<string> of servers
		///////////////// TEST \\\\\\\\\\\\\\\\\\\\\
		servers = new ArrayList<String>();
		servers.add("Leo's server");
		servers.add("Scrabble Party!");
		servers.add("The Scrabble rabble");
		servers.add("The J Scrabs");
		servers.add("Random scrabble lobby");
		servers.add("Nameless lobby");
		updateServers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public void updateServers(){
		LinearLayout list = (LinearLayout) findViewById(R.id.linearLayout_servers);
		for(int i = 0; i < servers.size();i++){
			TextView tv = new TextView(this);
			tv.setId(i);
			tv.setBackgroundResource(R.drawable.outline);
			tv.setTextSize(20);
			tv.setText("  " + servers.get(i));

			list.addView(tv);
			tv.setOnClickListener(new View.OnClickListener() {
			    @Override           
			    public void onClick(View v) {
			    	for(int i = 0;i < servers.size();i++){
			    		TextView temp = (TextView) findViewById(i);
			    		temp.setBackgroundResource(R.drawable.outline);
			    	}
			    	Button btn = (Button) findViewById(R.id.button_joinGame);
			    	btn.setEnabled(true);
			    	TextView temp = (TextView) v;
			    	temp.setBackgroundResource(R.drawable.outline_selected);
			    	selectedLobby = temp.getText().toString();
			    }
			});
		}
	}
	
	 public void joinGame(View view){
		 // request the server to join as a client, then execute 
		 Intent intent = new Intent(this,ClientActivity.class);
		 startActivity(intent);
	 }
	 
	 public void createGame(View view){
		Intent intent = new Intent(this, BoardActivity.class);
		startActivity(intent);
	 }
}
>>>>>>> origin
