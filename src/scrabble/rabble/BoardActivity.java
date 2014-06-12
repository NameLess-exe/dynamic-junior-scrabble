package scrabble.rabble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import scrabble.rabble.board.Board;
import scrabble.rabble.board.Dictionary;
import scrabble.rabble.model.Player;
import scrabble.rabble.model.PlayerList;
import scrabble.rabble.model.Sendable;
import scrabble.rabble.model.Tile;
import scrabble.rabble.model.TilePool;
import scrabble.rabble.model.Type;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BoardActivity extends ActionBarActivity {

	private int WIDTH = 13;
	private int HEIGHT = 13;
	private Board boardInstance;
	PlayerList alp;
	String serverName;
	int maxPlayers;
	int currentPlayers;
	int serverAvatar = -1;
	int numColumns = 13;
	ArrayList<Tile> boardTiles;
	FrameLayout base;
	RelativeLayout board;
	RelativeLayout serverDetails;
	ArrayList<Tile> boardState = new ArrayList<Tile>();
	ArrayList<Tile> boardComplete = new ArrayList<Tile>();

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

		// //////////// TEST \\\\\\\\\\\\\\\\
		Player myPlayer = new Player();
		myPlayer.setPoints(1);
		myPlayer.setName("Matt");
		Player p1 = new Player();
		p1.setName("Tom");
		Player p2 = new Player();
		p2.setName("Jojo");
		p2.setPoints(4);
		Player p3 = new Player();
		p3.setName("Leo");
		p3.setPoints(3);
		alp = new PlayerList();
		alp.addPlayer(p1);
		alp.addPlayer(myPlayer);
		alp.addPlayer(p2);
		alp.addPlayer(p3);
		// //////////////\\\\\\\\\\\\\\\\\\\\\
		removeExtraPlayers(maxPlayers);
		updatePlayers(alp);

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

		Tile t;

		for (int i = 0; i < tileArray.size(); i++) {
			try {
				t = new Tile(tileArray.get(i).getChar());
			} catch (Exception e) {
				t = new Tile('-');
			}
			boardComplete.add(t);
			boardState.add(new Tile('-'));
		}

		// while (strings.size() < 169)strings.add(new Tile('M'));

		int boardHeight = getBoardHeight();
		GridView gv = (GridView) findViewById(R.id.gridView);
		ViewGroup.LayoutParams lp = gv.getLayoutParams();

		lp.height = boardHeight;
		lp.width = boardHeight;
		gv.setLayoutParams(lp);

		// while(strings.size() < 169) strings.add(new Tile('M'));
		gv.setAdapter(new GridAdapter(boardComplete, boardHeight / 13, this));

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				TextView tv = (TextView) v;
				if (tv.getText() != "") {
					Tile t = new Tile(tv.getText().toString().charAt(0));
					String string = "";
					if (t.getValue() != '-') {
						Tile temp = boardState.get(position);
						if (temp.getValue() == '-') {
							boardState.set(position, t);
							string = "Tile placed";
							int currentPosition = position;
							boolean wordCompleted = true;
							int direction = -1;
							boolean canContinue = true;
							if (currentPosition % 13 == 0) {// not on the last
															// line
								if (boardComplete.get(currentPosition + 1)
										.getValue() == '-')
									canContinue = false;
							} else if (currentPosition % 12 == 0) {
								if (boardComplete.get(currentPosition - 1)
										.getValue() == '-')
									canContinue = false;
							} else if (boardComplete.get(currentPosition + 1)
									.getValue() == '-'
									&& boardComplete.get(currentPosition - 1)
											.getValue() == '-') { // check the
																	// one above
								canContinue = false;
							}

							if (canContinue == true) {
								while (wordCompleted == true) {
									currentPosition += direction;
									if (currentPosition >= 0 && boardComplete.get(currentPosition)
											.getValue() != '-') {
										if (boardState.get(currentPosition)
												.getValue() == '-')
											wordCompleted = false;
									} else {
										if (direction == 1) {
											wordCompleted();
											wordCompleted = false; // break the
																	// loop
										} else {
											direction = 1;
											currentPosition = position;
										}
									}
								}
							}
							wordCompleted = true;
							direction = -13;
							currentPosition = position;
							canContinue = true;
							if (currentPosition < 13) {// not on the last line
								if (boardComplete.get(currentPosition + 13)
										.getValue() == '-')
									canContinue = false;
							} else if (currentPosition > 155) {
								if (boardComplete.get(currentPosition - 13)
										.getValue() == '-')
									canContinue = false;
							} else if (boardComplete.get(currentPosition + 13)
									.getValue() == '-'
									&& boardComplete.get(currentPosition - 13)
											.getValue() == '-') { // check the
																	// one above
								canContinue = false;
							}

							if (canContinue == true) {
								while (wordCompleted == true) {
									currentPosition += direction;

									 if (currentPosition >= 0 && boardComplete.get(currentPosition)
											.getValue() != '-') {
										if (boardState.get(currentPosition)
												.getValue() == '-')
											wordCompleted = false;
									} else {
										if (direction == 13) {
											wordCompleted();
											wordCompleted = false; // break the
																	// loop
										} else {
											direction = 13;
											currentPosition = position;
										}
									}
								}
							}
						} else
							string = "Cannot place";
					} else
						string = "Cannot place";

					// /////////SEND TILE TO THE SERVER\\\\\\\\\\\\\\\\
					Context context = getApplicationContext();

					int duration = Toast.LENGTH_LONG;
					Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
					// /////////////////////////////////////////////////Character.toString(t.getValue())
				}
			}
		});

	}

	private void wordCompleted() {
		Player p = alp.get(0);
		p.setPoints(p.getPoints() + 1);
		updatePlayers(alp);
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
			int duration = Toast.LENGTH_SHORT;
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
			  //textView.setBackground();
				textView.setLayoutParams(new GridView.LayoutParams(vSize, vSize));
			} else {
				textView = (TextView) convertView;
			}

			String s;
			if (mItems.get(position).getValue() == '-') {
				s = "";
				textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			} else {
				s = Character.toString(mItems.get(position).getValue());
				textView.setText(s);
			}
			return textView;
		}
	}

	private void removeExtraPlayers(int size) {
		int temp = 4; // set to the last player
		LinearLayout ll = (LinearLayout) findViewById(R.id.linear_players);
		while (temp > size) {
			ll.removeView((RelativeLayout) findViewById(getResources()
					.getIdentifier("server_player" + Integer.toString(temp),
							"id", "scrabble.rabble")));
			temp--;
		}
	}

	public void updatePlayers(PlayerList players) {
		Player p;
		for (int i = 0; i < maxPlayers; i++) {

			p = players.get(i);
			TextView tv = (TextView) findViewById(getResources().getIdentifier(
					"display_name" + Integer.toString(i + 1), "id",
					"scrabble.rabble"));
			tv.setText(p.getName());

			tv = (TextView) findViewById(getResources().getIdentifier(
					"display_score" + Integer.toString(i + 1), "id",
					"scrabble.rabble"));
			tv.setText("Score:  " + Integer.toString(p.getPoints()));

			tv = (TextView) findViewById(getResources().getIdentifier(
					"display_avatar" + Integer.toString(i + 1), "id",
					"scrabble.rabble"));
			ViewGroup.LayoutParams lp = tv.getLayoutParams();
			lp.width = 60;
			lp.height = 60;
			tv.setLayoutParams(lp);
			tv.setBackgroundColor(Color.parseColor("#0066CC"));

		}
	}
}