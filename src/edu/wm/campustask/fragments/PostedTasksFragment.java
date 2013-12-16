package edu.wm.campustask.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.wm.campustask.PostedTasksAdapter;
import edu.wm.campustask.R;

public class PostedTasksFragment extends Fragment {

	//global tmp_task for current selected dialog task -> for main activity
	ParseObject tmp_task;
    public PostedTasksFragment() {
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View prof_view = inflater.inflate(R.layout.fragment_posted_tasks, container, false);
        
        final ParseUser cur_user = ParseUser.getCurrentUser();
        String cur_domain = cur_user.getString("domain");
        
        ParseQuery<ParseObject> task_query = ParseQuery.getQuery("Tasks");
        task_query.whereEqualTo("domain", cur_domain);
        List<ParseObject> results = new ArrayList<ParseObject>();
        final ArrayList<String> title_list = new ArrayList<String>();
        final ArrayList<String> descript_list = new ArrayList<String>();
        final ArrayList<ParseUser> rel_user_list = new ArrayList<ParseUser>();
        final ArrayList<ParseObject> rel_task_list = new ArrayList<ParseObject>();
        try {
			results = task_query.find();
			for(int i=0; i < results.size(); i++){
				final ParseObject cur_task = results.get(i);
				
				ParseUser usr = (ParseUser) cur_task.getParseObject("user").fetch();
				if(!(usr.getUsername().equals(cur_user.getUsername())) && cur_task.getBoolean("complete") == false && cur_task.getBoolean("accepted") == false){
					title_list.add(cur_task.getString("title"));
					descript_list.add(cur_task.getString("description"));
					rel_user_list.add(usr);
					rel_task_list.add(cur_task);
				}
				/*
				cur_task.getParseObject("user").fetchIfNeededInBackground(new GetCallback<ParseUser>() {
					public void done(ParseUser usr, ParseException e) {
						//do stuff
						if(usr.getUsername().equals(cur_user.getUsername()) && cur_task.getBoolean("complete") == false){
							Log.v("username", usr.getUsername());
							title_list.add(cur_task.getString("title"));
							descript_list.add(cur_task.getString("description"));
						}
					}
				});*/
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		ListView lv = (ListView)prof_view.findViewById(R.id.posted_tasks_listview);
        lv.setAdapter(new PostedTasksAdapter(getActivity(), title_list, descript_list));
        
        final ArrayList<String> header_list = new ArrayList<String>();
        final ArrayList<String> body_list = new ArrayList<String>();
        header_list.add("TASK DESCRIPTION");
        header_list.add("POSTER");
        header_list.add("PAYMENT AMOUNT");
        header_list.add("DATE");
        header_list.add("TIME");
        
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     		@Override
     		public void onItemClick(AdapterView<?> argo0, View arg1, int position, long arg3) {
     			//do stuff on item click with given position
     			body_list.clear();
     			ParseUser cur_usr = rel_user_list.get(position);
     			ParseObject cur_task = rel_task_list.get(position);
     			tmp_task = cur_task;
     			
     			body_list.add(cur_task.getString("description"));
     	        body_list.add(cur_usr.getString("full_name"));
     	        body_list.add("$" + cur_task.getString("payment_amount"));
     	        body_list.add(cur_task.getString("date"));
     	        body_list.add(cur_task.getString("time"));
     			
     	        //((NavigationActivity)getActivity()).showTaskDetails(header_list, body_list);
     	        showTaskDetails(header_list, body_list);
     		}
		});
        
        return prof_view;
    }
    Dialog task_dialog;
    public void showTaskDetails(ArrayList<String> header_list, ArrayList<String> body_list){
    	task_dialog = new Dialog(getActivity());
		task_dialog.setContentView(R.layout.task_details_layout);
		task_dialog.setTitle("Task Details");
		
		ListView lv = (ListView)task_dialog.findViewById(R.id.active_tasks_dialog_list);
        lv.setAdapter(new PostedTasksAdapter(getActivity(), header_list, body_list));
        
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
    	tmp_task.put("accepted", true);
    	tmp_task.put("accepted_by", ParseUser.getCurrentUser());
    	tmp_task.saveInBackground();
    	task_dialog.dismiss();
    	Toast new_tst = Toast.makeText(getActivity(), "Task accepted!", Toast.LENGTH_LONG);
    	new_tst.setGravity(Gravity.TOP, 0, 120);
    	new_tst.show();
    }
    
}
