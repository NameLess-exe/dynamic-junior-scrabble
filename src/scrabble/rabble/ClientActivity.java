package scrabble.rabble;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.os.Build;

public class ClientActivity extends ActionBarActivity {
	RelativeLayout client;		// screen with main UI
	RelativeLayout details;		// screen with UI for getting player details
	FrameLayout baseLayout;
	Player myPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		baseLayout = (FrameLayout) findViewById(R.id.container);
		client = (RelativeLayout) findViewById(R.id.layout_client_screen);
		details = (RelativeLayout) findViewById(R.id.layout_getPlayer);
		baseLayout.removeView(client);												// Sets the UI to the first screen
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
	public void checkInput(View view){
		EditText ageText = (EditText) findViewById(R.id.editText_Age);
		EditText nameText = (EditText) findViewById(R.id.editText_Name);
		String age = ageText.getText().toString();
		String name = nameText.getText().toString();
        boolean isValid = true;
        try{
            int placeHolder = Integer.parseInt(age.toString());
            isValid =true;
        }
        catch(Exception e){isValid = false;}
        if (isValid == true){
        	myPlayer = new Player(name, Integer.parseInt(age));
        	//send myPlayer to the server, store it in tbe arraylist of the server
        	baseLayout.removeView(details);
        	baseLayout.addView(client);
        }
        else{
        	ageText.setHint("Enter a number only");
        	nameText.setHint("Enter your name");
        }
	}
}
//android:label="@string/title_activity_client" >