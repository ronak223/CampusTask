package edu.wm.campustask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	EditText user_name_box;
	EditText pass_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//init Parse
		Parse.initialize(this, "9ghxzGJfF7V5PzvloHySpIri87S7H9bB0UuddqW7", "RzJg8zgyzKqCdLEPnXZYlv7MCGj6Zvtfn9b2Kud5"); 
		ParseAnalytics.trackAppOpened(getIntent());
		
		TextView text = (TextView) findViewById(R.id.app_title_text);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/GIDDYUPSTD.OTF");
		text.setTypeface(tf, Typeface.BOLD);
		text.setTextColor(Color.parseColor("#DDDDDD"));
		
		user_name_box = (EditText)findViewById(R.id.username_input);
		pass_box = (EditText)findViewById(R.id.password_input);
		pass_box.setHintTextColor(Color.parseColor("#FFFFFF"));
		user_name_box.setHintTextColor(Color.parseColor("#FFFFFF"));
		
		/*
		ParseUser cur_user = ParseUser.getCurrentUser();
		if(cur_user != null){
			Intent intent = new Intent(this, NavigationActivity.class);
			startActivity(intent);
		}
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void toReg(View view){
		Intent intent = new Intent(this, RegistrationActivity.class);
		startActivity(intent);
	}
	
	public void loginUser(View view){
		String username = user_name_box.getText().toString();
		String password = pass_box.getText().toString();
		
		//loading dialog for signing in
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setTitle("Logging in...");
		progress.setMessage("Please wait...");
		progress.show();
		
		//create intent for transition to new main activity upon successful login
		final Intent intnt = new Intent(this, NavigationActivity.class);
		
		ParseUser.logInInBackground(username, password, new LogInCallback(){
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					//successfull login
					progress.dismiss();
					startActivity(intnt);
				}
				else {
					//login failed
					progress.dismiss();
					user_name_box.setText("");
					pass_box.setText("");
					Toast tst = Toast.makeText(getApplicationContext(), "Invalid credentials. Try again.", Toast.LENGTH_LONG);
					tst.setGravity(Gravity.TOP, 0, 120);
					tst.show();
				}
			}
		});
	}

}
