package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuakeDataAdapter extends ArrayAdapter<QuakeData> {

    public QuakeDataAdapter(Context context, ArrayList<QuakeData> earthquakes) {
        super(context, 0, earthquakes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }
        QuakeData currentWord = getItem(position);

        //get mag
        TextView magnitude = (TextView) listViewItem.findViewById(R.id.magnitude_text);
        magnitude.setText(currentWord.getMagnitude());

        //get location
        TextView location = (TextView) listViewItem.findViewById(R.id.location_text);
        location.setText(currentWord.getLocation());

        //get time
        TextView time = (TextView) listViewItem.findViewById(R.id.time_text);
        time.setText(currentWord.getTime());

        return listViewItem;
    }
}
