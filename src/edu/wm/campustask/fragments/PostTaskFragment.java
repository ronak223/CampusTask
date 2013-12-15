package edu.wm.campustask.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import edu.wm.campustask.R;

public class PostTaskFragment extends Fragment {
	
    public PostTaskFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View prof_view = inflater.inflate(R.layout.fragment_post_task, container, false);
        
        return prof_view;
    }
}