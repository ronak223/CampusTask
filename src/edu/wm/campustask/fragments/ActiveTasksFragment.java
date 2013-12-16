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

public class ActiveTasksFragment extends Fragment {
	
	ParseObject tmp_task;
	ParseObject tmp_task2;
	ParseUser accept_by;
    public ActiveTasksFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View prof_view = inflater.inflate(R.layout.fragment_active_task, container, false);
        
        ListView lv_left = (ListView)prof_view.findViewById(R.id.accepted_tasks_list);
        ListView lv_right = (ListView)prof_view.findViewById(R.id.your_posted_tasks_list);
        
        ArrayList<String> accept_titles = new ArrayList<String>();
        ArrayList<String> accept_descripts = new ArrayList<String>();
        ArrayList<String> posted_titles = new ArrayList<String>();
        ArrayList<String> posted_descripts = new ArrayList<String>();
        final ArrayList<ParseUser> rel_user_list = new ArrayList<ParseUser>();
        final ArrayList<ParseObject> rel_task_list = new ArrayList<ParseObject>();
        final ArrayList<ParseUser> rel_user_list2 = new ArrayList<ParseUser>();
        final ArrayList<ParseObject> rel_task_list2 = new ArrayList<ParseObject>();
        List<ParseObject> results = new ArrayList<ParseObject>();
        
        ParseUser cur_user = ParseUser.getCurrentUser();
        
        ParseQuery<ParseObject> task_query = ParseQuery.getQuery("Tasks");
        
        try{
        	results = task_query.find();
        	for(int i=0; i < results.size(); i++){
				final ParseObject cur_task = results.get(i);
				
				//for getting tasks we posted
				ParseUser usr = (ParseUser) cur_task.getParseObject("user").fetch();
				
				if(usr.getUsername().equals(cur_user.getUsername()) && cur_task.getBoolean("complete") == false){
					posted_titles.add(cur_task.getString("title"));
					posted_descripts.add(cur_task.getString("description"));
					if(cur_task.getBoolean("accepted") == false){
						rel_user_list.add(null);
					}
					else{
						ParseUser accept_by = (ParseUser) cur_task.getParseObject("accepted_by").fetch();
						rel_user_list.add(accept_by);
					}
					rel_task_list.add(cur_task);
				}
				else if(cur_task.getBoolean("accepted") == true && cur_task.getBoolean("complete") == false){
					ParseUser accept_by = (ParseUser) cur_task.getParseObject("accepted_by").fetch();
					if(accept_by.getUsername().equals(cur_user.getUsername())){
						accept_titles.add(cur_task.getString("title"));
						accept_descripts.add(cur_task.getString("description"));
						rel_user_list2.add((ParseUser) cur_task.getParseObject("user"));
						rel_task_list2.add(cur_task);
					}
					else{
						
					}
				}
        	}
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        lv_left.setAdapter(new PostedTasksAdapter(getActivity(), accept_titles, accept_descripts));
        lv_right.setAdapter(new PostedTasksAdapter(getActivity(), posted_titles, posted_descripts));
		
        //the following is all for your POSTED TASKS (right-side) list view
        final ArrayList<String> header_list = new ArrayList<String>();
        final ArrayList<String> body_list = new ArrayList<String>();
        header_list.add("TASK DESCRIPTION");
        header_list.add("ACCEPTED BY");
        header_list.add("PAYMENT AMOUNT");
        header_list.add("DATE");
        header_list.add("TIME");
        
        lv_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> argo0, View arg1, int position, long arg3) {
        		body_list.clear();
     			ParseUser cur_usr = rel_user_list.get(position);
     			ParseObject cur_task = rel_task_list.get(position);
     			tmp_task = cur_task;
     			accept_by = cur_usr;
     			
     			body_list.add(cur_task.getString("description"));
     			if(cur_usr != null){
     				body_list.add(cur_usr.getString("full_name"));
     			} else{
     				body_list.add("Task not accepted yet");
     			}
     	        body_list.add("$" + cur_task.getString("payment_amount"));
     	        body_list.add(cur_task.getString("date"));
     	        body_list.add(cur_task.getString("time"));
     			
     	        //((NavigationActivity)getActivity()).showTaskDetails(header_list, body_list);
     	        dispTaskDetails(header_list, body_list);
        	}
        });
        
        //the following is all for your ACCEPTED TASKS (left-side) list view
        
        final ArrayList<String> header_list2 = new ArrayList<String>();
        final ArrayList<String> body_list2 = new ArrayList<String>();
        header_list2.add("TASK DESCRIPTION");
        header_list2.add("POSTER");
        header_list2.add("PAYMENT AMOUNT");
        header_list2.add("DATE");
        header_list2.add("TIME");
        
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     		@Override
     		public void onItemClick(AdapterView<?> argo0, View arg1, int position, long arg3) {
     			//do stuff on item click with given position
     			body_list2.clear();
     			ParseUser cur_usr2 = rel_user_list2.get(position);
     			ParseObject cur_task2 = rel_task_list2.get(position);
     			tmp_task2 = cur_task2;
     			
     			body_list2.add(cur_task2.getString("description"));
     	        body_list2.add(cur_usr2.getString("full_name"));
     	        body_list2.add("$" + cur_task2.getString("payment_amount"));
     	        body_list2.add(cur_task2.getString("date"));
     	        body_list2.add(cur_task2.getString("time"));
     			
     	        //((NavigationActivity)getActivity()).showTaskDetails(header_list, body_list);
     	        showTaskDetails(header_list2, body_list2);
     		}
		});
        return prof_view;
    }
    
    //the following is for dialog of POSTED TASKS
    Dialog your_tasks_dialog;
    public void dispTaskDetails(ArrayList<String> header_list, ArrayList<String> body_list){
    	your_tasks_dialog = new Dialog(getActivity());
		your_tasks_dialog.setContentView(R.layout.your_tasks_dialog_layout);
		your_tasks_dialog.setTitle("Complete Task?");
		
		ListView lv = (ListView)your_tasks_dialog.findViewById(R.id.your_tasks_dialog_list);
        lv.setAdapter(new PostedTasksAdapter(getActivity(), header_list, body_list));
        
        Button close_button = (Button)your_tasks_dialog.findViewById(R.id.close_your_task_dialog_button);
        close_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismissTaskDetailsDialog(v);
			}
		});
        
        Button accept_task = (Button)your_tasks_dialog.findViewById(R.id.complete_task_dialog_button);
        accept_task.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				completeTask(v);
			}
		});
        if(accept_by == null){
        	accept_task.setVisibility(View.GONE);
        }
        else{
        	accept_task.setVisibility(View.VISIBLE);
        }
        
        your_tasks_dialog.show();
    }

  
    public void dismissTaskDetailsDialog(View view){
    	your_tasks_dialog.dismiss();
    }
    
    public void completeTask(View view){
    	ParseUser puser = ParseUser.getCurrentUser();
    	
    	tmp_task.put("complete", true);
    	tmp_task.put("completed_by", accept_by);
    	tmp_task.saveInBackground();
    	
    	int cur_rep = puser.getInt("reputation");
    	int cur_task_comp = puser.getInt("tasks_completed");
    	int new_rep = cur_rep + 5;
    	int new_task_comp = cur_task_comp + 1;
    	
    	puser.put("reputation", new_rep);
    	puser.put("tasks_completed", new_task_comp);
    	puser.saveInBackground();
    	
    	int cur_acc_rep = accept_by.getInt("reputation");
    	int cur_acc_tasks = accept_by.getInt("tasks_completed");
    	int new_cur_acc_rep = cur_acc_rep + 10;
    	int new_cur_acc_tasks = cur_acc_tasks + 1;
    	
    	accept_by.put("reputation", new_cur_acc_rep);
    	accept_by.put("tasks_completed", new_cur_acc_tasks);
    	accept_by.saveInBackground();
    	
    	your_tasks_dialog.dismiss();
    	Toast new_tst = Toast.makeText(getActivity(), "Task completed!", Toast.LENGTH_LONG);
    	new_tst.setGravity(Gravity.TOP, 0, 120);
    	new_tst.show();
    }
    
    //the following is for ACCEPTED TASKS
    Dialog task_dialog;
    public void showTaskDetails(ArrayList<String> header_list, ArrayList<String> body_list){
    	task_dialog = new Dialog(getActivity());
		task_dialog.setContentView(R.layout.accepted_tasks_dialog_layout);
		task_dialog.setTitle("Task Details");
		
		ListView lv = (ListView)task_dialog.findViewById(R.id.accepted_tasks_dialog_list);
        lv.setAdapter(new PostedTasksAdapter(getActivity(), header_list, body_list));
        
        Button close_button = (Button)task_dialog.findViewById(R.id.close_accepted_tasks_dialog_button);
        close_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismissTaskDetailsDialogTwo(v);
			}
		});
        
        task_dialog.show();
    }
    
    //PostedTaskFragment -- dismissing task details dialog
    public void dismissTaskDetailsDialogTwo(View view){
    	task_dialog.dismiss();
    }
}
