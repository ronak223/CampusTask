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

public class PostedTasksFragment extends Fragment {
	
    public PostedTasksFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View prof_view = inflater.inflate(R.layout.fragment_posted_tasks, container, false);
        
        //String[] tmp = {"data1", "data2", "data3"};
        //String[] descript = {"Lorem ipsum blah blah blah blah blah blah", "Lorem ipsum blah blah blah blah blah blah22222", "Lorem ipsum blah blah blah blah blah blah33333333"};
        
        ParseUser cur_user = ParseUser.getCurrentUser();
        String cur_domain = cur_user.getString("domain");
        
        ParseQuery<ParseObject> task_query = ParseQuery.getQuery("Tasks");
        task_query.whereEqualTo("domain", cur_domain);
        List<ParseObject> results = new ArrayList<ParseObject>();
        List<String> title_list = new ArrayList<String>();
        List<String> descript_list = new ArrayList<String>();
        try {
			results = task_query.find();
			for(int i=0; i < results.size(); i++){
				ParseObject cur_task = results.get(i);
				ParseUser usr = (ParseUser) cur_task.getParseObject("user");
				if(usr.getObjectId() == cur_user.getObjectId()){
					title_list.add(cur_task.getString("title"));
					descript_list.add(cur_task.getString("description"));
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String[] tmp_titles = new String[title_list.size()];
        String[] tmp_descripts = new String[descript_list.size()];
		ListView lv = (ListView)prof_view.findViewById(R.id.posted_tasks_listview);
        lv.setAdapter(new PostedTasksAdapter(getActivity(), title_list.toArray(tmp_titles), descript_list.toArray(tmp_descripts)));

        return prof_view;
    }
}
