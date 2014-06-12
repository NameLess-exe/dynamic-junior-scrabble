
package scrabble.rabble;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import scrabble.rabble.network.ClientService;
import scrabble.rabble.network.EventListener;
import scrabble.rabble.network.ServerService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	public final static String SERVER_NAME = "";
	ArrayList<String> servers;
	String selectedLobby;

	MediaPlayer player;
	private static final String TAG = "MainActivity";

    private ClientService clientService;
    private ServiceConnection clientServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            clientService = ((ClientService.LocalBinder) iBinder).getInstance();
            Log.v(TAG, "ClientService connected.");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            clientService = null;
            Log.v(TAG, "ClientService disconnected.");
        }
    };
    private ServerService serverService;
    private ServiceConnection serverServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serverService = ((ServerService.LocalBinder) iBinder).getInstance();
            Log.v(TAG, "ServerService connected.");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            clientService = null;
            Log.v(TAG, "ServerService disconnected.");
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		ArrayList<String> temp = new ArrayList<String>();
			temp.add("Server");
		updateServers(temp);
		/*
		clientService.registerListener(new EventListener() {
            @Override
            public void onConnect(Connection connection) {
                // The client has successfully connected to the server.
                Log.v(TAG, "Client connected.");

            }

            @Override
            public void onDisconnect(Connection connection) {
                // The client has been disconnected, either gracefully or forcefully.
                Log.v(TAG, "Client disconnected.");
            }

            @Override
            public void onDataReceived(Connection connection, Object object) {
                // So you have received an object. You should now test object to determine which type it is. 
            }

            @Override
            public void onDiscoverHost(List<InetAddress> inetAddresses) {
                // You have called .discover() and you have received a list of IPs that are found. These IPs
                // should correspond to the server's IP.
                Log.v(TAG, "Discovered Servers: " + inetAddresses);
            	for(int i = 0; i < inetAddresses.size();i++){
            		servers.add(inetAddresses.get(i).getHostAddress());
            	}
            	updateServers(servers);
            }
        });
		
		*/
		
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

	public void updateServers(final List<String> temp){
		LinearLayout list = (LinearLayout) findViewById(R.id.linearLayout_servers);
		for(int i = 0; i < temp.size();i++){
			TextView tv = new TextView(this);
			tv.setId(i);
			tv.setBackgroundResource(R.drawable.outline);
			tv.setTextSize(20);
			tv.setText("  " + temp.get(i));

			list.addView(tv);
			tv.setOnClickListener(new View.OnClickListener() {
			    @Override           
			    public void onClick(View v) {
			    	for(int i = 0;i < temp.size();i++){
			    		TextView t = (TextView) findViewById(i);
			    		t.setBackgroundResource(R.drawable.outline);
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
		 intent.putExtra(SERVER_NAME, selectedLobby);
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

