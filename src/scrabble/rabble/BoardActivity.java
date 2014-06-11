package scrabble.rabble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import scrabble.rabble.board.Board;
import scrabble.rabble.board.Dictionary;
import scrabble.rabble.model.Tile;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BoardActivity extends ActionBarActivity {

	private int WIDTH = 13;
	private int HEIGHT = 13;
	private Board boardInstance;
	String serverName;
	int maxPlayers;
	int currentPlayers;
	int serverAvatar = -1;
	int numColumns = 13;
	ArrayList<Tile> boardTiles;
	FrameLayout base;
	RelativeLayout board;
	RelativeLayout serverDetails;
	MediaPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		base = (FrameLayout) findViewById(R.id.container);
		board = (RelativeLayout) findViewById(R.id.board_interface);
		serverDetails = (RelativeLayout) findViewById(R.id.server_details);
		base.removeView(board);
		
		//plays the theme song of my life
		player = MediaPlayer.create(this, R.raw.wallpaper);
		player.setLooping(true); // Set looping
		player.setVolume(100,100);
		player.start();
		
        //Text view sound stuff
		//final MediaPlayer mp2 = MediaPlayer.create(getApplicationContext(), R.raw.button2);
		//TextView textView = (TextView)findViewById(R.id.textView1);
		//textView.setOnClickListener(new View.OnClickListener(){
			
			//@Override
			//public void onClick(View v) {
				// TODO Auto-generated method stub
				//mp.start();
			//}

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
			View rootView = inflater.inflate(R.layout.fragment_board,
					container, false);
			return rootView;
		}
	}

	public void displayBoard() {
		base.addView(board);
		base.removeView(serverDetails);

		Dictionary dictionary = new Dictionary();

		AssetManager assetManager = this.getAssets();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					assetManager.open("dictionary.txt")));
			for (String line; (line = br.readLine()) != null;) {
				dictionary.addWord(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		boardInstance = new Board(WIDTH, HEIGHT, dictionary);
		boardInstance.generate();

		ArrayList<scrabble.rabble.board.Tile> tileArray = boardInstance
				.flatten();
		ArrayList<Tile> strings = new ArrayList<Tile>();

		Tile t;

		for (int i = 0; i < tileArray.size(); i++) {
			t = new Tile(tileArray.get(i).getChar());
			strings.add(t);
		}

		// while (strings.size() < 169)strings.add(new Tile('M'));

		int boardHeight = getBoardHeight();
		GridView gv = (GridView) findViewById(R.id.gridView);
		ViewGroup.LayoutParams lp = gv.getLayoutParams();
		lp.height = boardHeight;
		lp.width = boardHeight;
		gv.setLayoutParams(lp);
		// while(strings.size() < 169) strings.add(new Tile('M'));
		gv.setAdapter(new GridAdapter(strings, boardHeight / 13, this));

		/*
		 * int boardHeight = getBoardHeight(); ArrayList<Tile> strings = new
		 * ArrayList<Tile>(); while(strings.size() < 169) strings.add(new
		 * Tile('M')); GridView gv = (GridView) findViewById(R.id.gridView);
		 * ViewGroup.LayoutParams lp = gv.getLayoutParams(); lp.height =
		 * boardHeight; lp.width = boardHeight; gv.setLayoutParams(lp);
		 * ga.setItems(strings); ga.setViewSize(boardHeight / 13);
		 * gv.setAdapter(ga);
		 */
	}

	private int getBoardHeight() {
		int h;
		Display display = getWindowManager().getDefaultDisplay();
		h = display.getHeight(); // gets actual usable height
		h -= 40; // subtract 20 to get a boarder of 10 on each side
		return h;
	}

	public void createGame(View view) {
		EditText nameText = (EditText) findViewById(R.id.editText_gameName);
		EditText playersText = (EditText) findViewById(R.id.editText_maxPlayer);
		CharSequence text = "";
		if (nameText.getText().toString().length() == 0) {
			text = "Please enter your name";
			nameText.setText("");

		} else if (checkInput(playersText.getText().toString()) == false) {
			text = "Please enter the maximum number of players";
			playersText.setText("");
		} else if (serverAvatar == -1)
			text = "Please select an avatar";
		else {
			serverName = nameText.getText().toString();
			maxPlayers = Integer.parseInt(playersText.getText().toString());
			displayBoard();
		}
		if (text != "") {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	public void setServerAvatar(View view) {
		TextView temp = (TextView) view;

		for (int i = 0; i < 4; i++) {
			TextView tv = (TextView) findViewById(getResources().getIdentifier(
					"server_avatar" + Integer.toString(i + 1), "id",
					"scrabble.rabble"));
			tv.setBackgroundColor(Color.parseColor("#0066CC"));
		}
		for (int i = 0; i < 4; i++) {
			if (temp.getId() == getResources().getIdentifier(
					"server_avatar" + Integer.toString(i + 1), "id",
					"scrabble.rabble")) {
				serverAvatar = i + 1;
				temp.setBackgroundColor(Color.parseColor("#FFFF00"));

			}
		}
	}

	public boolean checkInput(String age) {
		boolean isValid = true;
		int x;
		try {
			x = Integer.parseInt(age.toString());
		} catch (Exception e) {
			isValid = false;
			return isValid;
		}
		if (x > 4)
			isValid = false;
		return isValid;
	}

	private final class GridAdapter extends BaseAdapter {

		ArrayList<Tile> mItems;
		int mCount;
		Context mContext;
		int vSize;

		private GridAdapter(ArrayList<Tile> item, int size, Context c) {
			mItems = item;
			mCount = item.size();
			vSize = size;
			mContext = c;
		}

		public void setViewSize(int i) {
			vSize = i;
		}

		public void setItems(ArrayList<Tile> items) {
			mItems = items;
			mCount = items.size();
		}

		@Override
		public int getCount() {
			return mCount;
		}

		@Override
		public Object getItem(final int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(final int position) {
			return position;
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			TextView textView;
			if (convertView == null) {
				textView = (TextView) LayoutInflater.from(mContext).inflate(
						R.layout.view, null);
				textView.setLayoutParams(new GridView.LayoutParams(vSize, vSize));
			} else {
				textView = (TextView) convertView;
			}
			textView.setText(Character
					.toString(mItems.get(position).getValue()));
			return textView;
		}
	}

	private TextView createView(Tile t) {
		TextView textView = (TextView) LayoutInflater.from(this).inflate(
				R.layout.view, null);
		textView.setText(String.valueOf(t.getValue()));
		return textView;
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