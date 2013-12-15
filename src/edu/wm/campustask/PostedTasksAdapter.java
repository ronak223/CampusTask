package edu.wm.campustask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostedTasksAdapter extends BaseAdapter {

    Context context;
    String[] data;
    String[] descriptions;
    private static LayoutInflater inflater = null;

    public PostedTasksAdapter(Context context, String[] data, String[] descriptions) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.descriptions = descriptions;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView text = (TextView) vi.findViewById(R.id.row_title_text);
        TextView descript_text = (TextView) vi.findViewById(R.id.row_description_text);
        text.setText(data[position]);
        descript_text.setText(descriptions[position]);
        return vi;
    }
}
