package com.example.busby.busbyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by hanop on 2017/05/15.
 */

public class Location extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String locationName = getIntent().getStringExtra("LocationName");
        if(locationName.indexOf("'")>0){
            String temp="";
            temp=locationName.substring(locationName.indexOf("'")+1,locationName.lastIndexOf("'"));
            locationName=temp;
        }
        locationMethod(locationName);
    }
    private void locationMethod(String location){
        setContentView(R.layout.location);
        final TextView Location_name=(TextView)findViewById(R.id.Location_name);
        Location_name.setText(location);
        ViewGroup vgLocation=(ViewGroup)findViewById(R.id.LocationThreadTableLayout);
        Spinner locationSpinner=(Spinner)findViewById(R.id.location_spinner);
        String[]tempThreads=new String[]{"Edgars","Markham"};

        ArrayAdapter<String> newThreadsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempThreads);
        newThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(newThreadsAdapter);


        Spinner cycleSpinner=(Spinner)findViewById(R.id.cycle_spinner);
        String[]tempCycle=new String[]{"Cycle1","Cycle2"};

        ArrayAdapter<String> newCycleAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempCycle);
        newCycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cycleSpinner.setAdapter(newCycleAdapter);
        makeLocationGUI("Dummy location comment",0,vgLocation);
        makeLocationGUI("Dummy location comment",1,vgLocation);

        final Button newPostSpecific=(Button)findViewById(R.id.new_Post_Specific);
        newPostSpecific.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String locationName = getIntent().getStringExtra("LocationName");
                Intent intent = new Intent(Location.this,New_Post.class);
                intent.putExtra("LocationName",locationName);
                startActivity(intent);
                finish();
            }
        });



    }
    private void makeLocationGUI(String comment, int index, ViewGroup v) {
        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate new_tag_view.xml to create new row and set up the contained Buttons
        View newTagView = inflater.inflate(R.layout.thread_view, v,false);

        // get newTagButton reference, set its text and register its listener
        Spinner locationThreadSpinner=(Spinner)newTagView.findViewById(R.id.newThreadSpinner);
        String[]tempThreads=new String[]{"Needs Review","Awaiting Response","Completed"};

        ArrayAdapter <String>newThreadsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempThreads);
        newThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationThreadSpinner.setAdapter(newThreadsAdapter);

        TextView newThreadTextView=(TextView) newTagView.findViewById(R.id.newThreadTextView);
        newThreadTextView.setText(comment);
        ImageView tempImage=(ImageView)newTagView.findViewById(R.id.newThreadImage) ;
        tempImage.setImageResource(R.drawable.guess);
        // add new tag and edit buttons to urlTableLayout at specified row number (index)
        v.addView(newTagView, index);
    }
}
