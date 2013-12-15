package edu.wm.campustask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegistrationActivity extends Activity {
	
	EditText full_name_box;
	EditText user_name_box;
	EditText email_box;
	EditText pass_box;
	EditText conf_pass_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		//init Parse
		Parse.initialize(this, "9ghxzGJfF7V5PzvloHySpIri87S7H9bB0UuddqW7", "RzJg8zgyzKqCdLEPnXZYlv7MCGj6Zvtfn9b2Kud5"); 
		
		full_name_box = (EditText)findViewById(R.id.reg_name_input);
		user_name_box = (EditText)findViewById(R.id.reg_username_input);
		email_box = (EditText)findViewById(R.id.reg_email_input);
		pass_box = (EditText)findViewById(R.id.reg_password_input);
		conf_pass_box = (EditText)findViewById(R.id.reg_confirm_pass);
		
		full_name_box.setHintTextColor(Color.parseColor("#FFFFFF"));
		user_name_box.setHintTextColor(Color.parseColor("#FFFFFF"));
		email_box.setHintTextColor(Color.parseColor("#FFFFFF"));
		pass_box.setHintTextColor(Color.parseColor("#FFFFFF"));
		conf_pass_box.setHintTextColor(Color.parseColor("#FFFFFF"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	public void confirmReg(View view){
		String full_name = full_name_box.getText().toString();
		String username = user_name_box.getText().toString();
		String email = email_box.getText().toString();
		String password = pass_box.getText().toString();
		String conf_pass = conf_pass_box.getText().toString();
		
		//set up loading for registration
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("Attempting to register...");
		progress.show();
		
		//setup Toast for potential successfull registration
		final Toast tst = Toast.makeText(this,  "Registration successful. Please log in.", Toast.LENGTH_LONG);
		tst.setGravity(Gravity.TOP, 0, 120);
		
		//intent for possible successful reg
		final Intent cur_intent = new Intent(this, MainActivity.class);
		
		if(password.equals(conf_pass)){
			
			//creating entry for user in ParseDB
			ParseUser userObj = new ParseUser();
			userObj.setUsername(username);
			userObj.setPassword(password);
			userObj.setEmail(email);
			userObj.put("full_name", full_name);
			userObj.put("reputation", 0);
			userObj.put("tasks_completed", 0);
			userObj.put("status", "");
			userObj.put("domain", email.split("@")[1]);
			userObj.signUpInBackground(new SignUpCallback(){
				public void done(ParseException e){
					if(e == null){
						//cancel loading and continue
						progress.dismiss();
						startActivity(cur_intent);
						tst.show();
					}
					else{
						//sign up didn't work
						progress.dismiss();
						
						full_name_box.setText("");
						user_name_box.setText("");
						email_box.setText("");
						pass_box.setText("");
						conf_pass_box.setText("");
						
						Toast toast = Toast.makeText(getApplicationContext(), "Registration unsucessful. Try again.", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP, 0, 120);
						toast.show();
					}
				}
			});
		}
		else{
			
			progress.dismiss();
			
			full_name_box.setText("");
			user_name_box.setText("");
			email_box.setText("");
			pass_box.setText("");
			conf_pass_box.setText("");
			
			Toast toast = Toast.makeText(this, "Passwords do not match. Try again.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 0, 120);
			toast.show();
		}
	}

}
