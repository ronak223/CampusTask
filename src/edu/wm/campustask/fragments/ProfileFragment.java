package edu.wm.campustask.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import edu.wm.campustask.R;

public class ProfileFragment extends Fragment {
	
    public ProfileFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View prof_view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        ParseUser cur_user = ParseUser.getCurrentUser();
    	if(cur_user != null){
    		TextView name_title = (TextView)prof_view.findViewById(R.id.profile_name_text); 
    		TextView rep = (TextView)prof_view.findViewById(R.id.reputation_points);
    		TextView tasks_comp = (TextView)prof_view.findViewById(R.id.tasks_completed);
    		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GIDDYUPSTD.OTF");
    		name_title.setTypeface(tf, Typeface.BOLD);
    		name_title.setText(cur_user.getString("full_name"));
    		rep.setText("" + cur_user.getInt("reputation"));
    		tasks_comp.setText("" + cur_user.getInt("tasks_completed"));
    		
    	}
        
        return prof_view;
    }
    

}