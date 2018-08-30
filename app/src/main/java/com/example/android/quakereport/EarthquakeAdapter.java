package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private int color;
    private Context context;
    private String mag;

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes, int color) {
        super(context, 0, earthquakes);
        this.context = context;
        this.color=color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        listItemView.setBackgroundColor(color);
        // Get the {@link AndroidFlavor} object located at this position in the list
        final Earthquake earthquake = getItem(position);
        mag = earthquake.getMag();

        TextView magTextView = (TextView) listItemView.findViewById(R.id.tvMag);
        magTextView.setText(mag);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.tvDate);
        dateTextView.setText(earthquake.getDate());

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.tvTime);
        timeTextView.setText(earthquake.getTime());

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.tvLocation);
        locationTextView.setText(earthquake.getPrimaryLocation());
        //locationTextView.setText(earthquake.getUrl());

        TextView locationOffTextView = (TextView) listItemView.findViewById(R.id.tvLocationOff);
        locationOffTextView.setText(earthquake.getLocationOff());

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText (context,"List Item Clicked",Toast.LENGTH_SHORT).show();
                Uri webpage = Uri.parse(earthquake.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                context.startActivity(intent);
            }
        });

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
