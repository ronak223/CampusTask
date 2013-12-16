package edu.wm.campustask;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.wm.campustask.fragments.ActiveTasksFragment;
import edu.wm.campustask.fragments.PostTaskFragment;
import edu.wm.campustask.fragments.PostedTasksFragment;
import edu.wm.campustask.fragments.ProfileFragment;

public class NavigationActivity extends Activity {

	//setting up nav_drawer
	private String[] navigation_selections;
	private DrawerLayout nav_drawer_layout;
	private ListView nav_drawer_list;
	//global PostedTasksFrag for reference in helper methods
	Fragment pt_frag;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		
		//init Parse
		Parse.initialize(this, "9ghxzGJfF7V5PzvloHySpIri87S7H9bB0UuddqW7", "RzJg8zgyzKqCdLEPnXZYlv7MCGj6Zvtfn9b2Kud5"); 
		
		//setting up the actual slide-out navigation drawer
		navigation_selections = getResources().getStringArray(R.array.nav_drawer_array);
	    nav_drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    nav_drawer_list = (ListView) findViewById(R.id.left_drawer);
	    
	    // Set the adapter for the list view
        nav_drawer_list.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navigation_selections));
        // Set the list's click listener
        nav_drawer_list.setOnItemClickListener(new DrawerItemClickListener());   
        
        //starting on Profile activity
        FragmentManager tmp_fragManager = getFragmentManager();
        Fragment cur_frag = new ProfileFragment();
		tmp_fragManager.beginTransaction().replace(R.id.content_frame, cur_frag).commit();
		// update selected item and title, then close the drawer
        nav_drawer_list.setItemChecked(0, true);
        setTitle(navigation_selections[0]);
        nav_drawer_layout.closeDrawer(nav_drawer_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigation, menu);
		return true;
	}
	
	/* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    //for navigating between fragments
    private void selectItem(int position) {
		FragmentManager fragManager = getFragmentManager();
		
    	//profile position
    	if(position == 0){
    		Fragment cur_frag = new ProfileFragment();
    		fragManager.beginTransaction().replace(R.id.content_frame, cur_frag).commit();
    		// update selected item and title, then close the drawer
            nav_drawer_list.setItemChecked(position, true);
            setTitle(navigation_selections[position]);
            nav_drawer_layout.closeDrawer(nav_drawer_list);
            
    	}
    	//active tasks position
    	else if(position == 1){
    		Fragment cur_frag = new ActiveTasksFragment();
    		fragManager.beginTransaction().replace(R.id.content_frame, cur_frag).commit();
    		//update selected item and title, then close the drawer
    		nav_drawer_list.setItemChecked(position, true);
    		setTitle(navigation_selections[position]);
    		nav_drawer_layout.closeDrawer(nav_drawer_list);
    		
    	}
    	//available tasks position
    	else if(position == 2){
    		//Fragment cur_frag = new PostedTasksFragment();
    		pt_frag = new PostedTasksFragment();
    		fragManager.beginTransaction().replace(R.id.content_frame, pt_frag).commit();
    		//update selected item and title, then close the drawer
    		nav_drawer_list.setItemChecked(position, true);
    		setTitle(navigation_selections[position]);
    		nav_drawer_layout.closeDrawer(nav_drawer_list);
    	}
    	//post a task position
    	else if(position == 3){
    		Fragment cur_frag = new PostTaskFragment();
    		fragManager.beginTransaction().replace(R.id.content_frame, cur_frag).commit();
   		 	//update selected item and title, then close the drawer
    		nav_drawer_list.setItemChecked(position, true);
    		setTitle(navigation_selections[position]);
    		nav_drawer_layout.closeDrawer(nav_drawer_list);
    	}
    }
    
    //ProfileFragment -- logging out user on button click
    public void logoutUser(View view){
    	ParseUser.logOut();
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
    
    //ProfileFragment -- updating user status on button click
    public void updateStatus(View view){
    	TextView status = (TextView)findViewById(R.id.status_edit_text);

    	ParseUser curUser = ParseUser.getCurrentUser();
    	curUser.put("status", status.getText().toString());
    	curUser.saveInBackground();
    	
    	status.setText("");
    	Toast tst = Toast.makeText(this, "Status updated successully.", Toast.LENGTH_LONG);
    	tst.setGravity(Gravity.TOP, 0, 120);
    	tst.show();
    }
    
    //PostTaskFragment -- creating new task on button click
    public void postTask(View view){
    	//retrieving all string from post task form
    	EditText title = (EditText)findViewById(R.id.task_title_input);
    	EditText pay_amount = (EditText)findViewById(R.id.payment_input);
    	EditText date = (EditText)findViewById(R.id.task_date_input);
    	EditText time = (EditText)findViewById(R.id.task_time_input);
    	EditText contact_number = (EditText)findViewById(R.id.number_input);
    	EditText description = (EditText)findViewById(R.id.task_description_input);
    	
    	//building DB object for task
    	ParseObject cur_task = new ParseObject("Tasks");
    	cur_task.put("user", ParseUser.getCurrentUser());
    	cur_task.put("title", title.getText().toString());
    	cur_task.put("payment_amount", pay_amount.getText().toString());
    	cur_task.put("date", date.getText().toString());
    	cur_task.put("time", time.getText().toString());
    	cur_task.put("contact_number", contact_number.getText().toString());
    	cur_task.put("description", description.getText().toString());
    	cur_task.put("complete", false);
    	cur_task.put("accepted", false);
    	cur_task.put("domain", ParseUser.getCurrentUser().getString("domain"));
    	cur_task.saveInBackground();
    	Toast tst = Toast.makeText(this, "Task has been created.", Toast.LENGTH_LONG);
    	tst.setGravity(Gravity.TOP, 0, 120);
    	tst.show();
    	
    	title.setText("");
    	pay_amount.setText("");
    	date.setText("");
    	time.setText("");
    	contact_number.setText("");
    	description.setText("");
    }
    
    //PostTaskFragment -- showing date and time picker dialog on button click
    Dialog dialog;
    public void showDateTimePickerDialog(View view){
    	dialog = new Dialog(this);
		dialog.setContentView(R.layout.date_time_layout);
		dialog.setTitle("Choose time and date of task");
		
		//setting onclick listener for date/time dialog buttons
        Button closeButton = (Button)dialog.findViewById(R.id.close_dialog_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismissDateTimeDialog(v);
			}
		});
        Button setButton = (Button)dialog.findViewById(R.id.set_time_from_dialog_button);
        setButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setDateTime(v);
				
			}
		});
        
		dialog.show();
		
    }
    
    //PostTaskFragment -- dismissing dialog is cancel button is clicked
    public void dismissDateTimeDialog(View view){
    	dialog.dismiss();
    }
    
    //PostTaskFragment -- confirming date and time on OK button click
    public void setDateTime(View view){
    	DatePicker dp = (DatePicker)dialog.findViewById(R.id.datePicker);
    	TimePicker tp = (TimePicker)dialog.findViewById(R.id.timePicker);
    	
    	int cur_month = dp.getMonth() + 1;
    	EditText date_input = (EditText)findViewById(R.id.task_date_input);
    	EditText time_input = (EditText)findViewById(R.id.task_time_input);
    	date_input.setText("" + cur_month + "/" + dp.getDayOfMonth() + "/" + dp.getYear());
    	
    	int offset_time = tp.getCurrentHour();
    	String cur_minute = "0";
    	if(tp.getCurrentMinute() < 10){
    		cur_minute = "0" + tp.getCurrentMinute();
    	}
    	else{
    		cur_minute = "" + tp.getCurrentMinute();
    	}
    	time_input.setText("" + offset_time + ":" + cur_minute);
    	
    	dialog.dismiss();
    }
    
    /*
    //PostedTaskFragment -- creating dialog for task details
    Dialog task_dialog;
    public void showTaskDetails(ArrayList<String> header_list, ArrayList<String> body_list){
    	task_dialog = new Dialog(this);
		task_dialog.setContentView(R.layout.task_details_layout);
		task_dialog.setTitle("Task Details");
		
		ListView lv = (ListView)task_dialog.findViewById(R.id.active_tasks_dialog_list);
        lv.setAdapter(new PostedTasksAdapter(this, header_list, body_list));
        
        Button close_button = (Button)task_dialog.findViewById(R.id.close_task_dialog_button);
        close_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismissTaskDetailsDialog(v);
			}
		});
        
        Button accept_task = (Button)task_dialog.findViewById(R.id.accept_task_dialog_button);
        accept_task.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				acceptTask(v);
			}
		});
        
        task_dialog.show();
    }
    
    //PostedTaskFragment -- dismissing task details dialog
    public void dismissTaskDetailsDialog(View view){
    	task_dialog.dismiss();
    }
    
    //PostedTaskFragment -- accepting task
    public void acceptTask(View view){
    	
    }
    */

}
