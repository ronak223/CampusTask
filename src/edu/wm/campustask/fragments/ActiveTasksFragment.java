package edu.wm.campustask.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.wm.campustask.PostedTasksAdapter;
import edu.wm.campustask.R;

public class ActiveTasksFragment extends Fragment {
	
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
				}
				else if(cur_task.getBoolean("accepted") == true){
					ParseUser accept_by = (ParseUser) cur_task.getParseObject("accepted_by").fetch();
					if(accept_by.getUsername().equals(cur_user.getUsername())){
						accept_titles.add(cur_task.getString("title"));
						accept_descripts.add(cur_task.getString("description"));
					}
				}
        	}
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        lv_left.setAdapter(new PostedTasksAdapter(getActivity(), accept_titles, accept_descripts));
        lv_right.setAdapter(new PostedTasksAdapter(getActivity(), posted_titles, posted_descripts));
        

        return prof_view;
    }
}
