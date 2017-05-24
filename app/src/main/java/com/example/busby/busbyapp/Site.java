package com.example.busby.busbyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class Site extends AppCompatActivity {
    String storeName=null;

    private int UserID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String siteName = getIntent().getStringExtra("LocationName");
        if(siteName.indexOf("'")>0){
            String temp="";
            temp=siteName.substring(siteName.indexOf("'")+1,siteName.lastIndexOf("'"));
            siteName=temp;
        }
        if(siteName.indexOf("-")>0){
            String temp="";
            temp=siteName.substring(siteName.indexOf("-")+1,siteName.length());
            storeName=siteName.substring(0,siteName.indexOf("-"));
            siteName=temp;
        }
        UserID=getIntent().getIntExtra("UserID",0);
        Log.v("UserID",""+UserID);
        locationMethod(siteName);
    }
    private void locationMethod(String location){
        setContentView(R.layout.site);
        final TextView Location_name=(TextView)findViewById(R.id.Location_name);
        Location_name.setText(location);
        ViewGroup vgLocation=(ViewGroup)findViewById(R.id.LocationThreadTableLayout);
        final Spinner locationSpinner=(Spinner)findViewById(R.id.location_spinner);
        String[]tempThreads=new String[]{"Edgars","Markhams"};

        ArrayAdapter<String> newThreadsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempThreads);
        newThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(newThreadsAdapter);
        if(storeName!=null){
            Log.v("Store name",storeName);
            int temp=0;
            for(int x=0;x<tempThreads.length;x++){
                if(tempThreads[x].equalsIgnoreCase(storeName)){
                    temp=x;
                }
            }
            locationSpinner.setSelection(temp);
        }


        Spinner cycleSpinner=(Spinner)findViewById(R.id.cycle_spinner);
        String[]tempCycle=new String[]{"Cycle1","Cycle2"};

        ArrayAdapter<String> newCycleAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempCycle);
        newCycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cycleSpinner.setAdapter(newCycleAdapter);
        makeLocationGUI("Dummy site comment",0,vgLocation);
        makeLocationGUI("Dummy site comment",1,vgLocation);

        final Button newPostSpecific=(Button)findViewById(R.id.new_Post_Specific);
        newPostSpecific.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                TextView Location_name=(TextView)findViewById(R.id.Location_name);
                String locationName = ""+Location_name.getText();
                Intent intent = new Intent(Site.this,New_Post.class);
                intent.putExtra("SiteName",locationName);
                intent.putExtra("StoreName",locationSpinner.getSelectedItem().toString());
                intent.putExtra("UserID",UserID);
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
