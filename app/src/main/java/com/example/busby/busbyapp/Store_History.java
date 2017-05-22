package com.example.busby.busbyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hanop on 2017/05/15.
 */

public class Store_History extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_history);
        String username = getIntent().getStringExtra("username");
        storeHistory(username);
    }
    private void storeHistory(String userText){
        setContentView(R.layout.store_history);
        final TextView welcome_UserText=(TextView)findViewById(R.id.welcome_UserText);
        final Button newPost=(Button)findViewById(R.id.new_Post);
        newPost.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Store_History.this,New_Post.class);
                startActivity(intent);
            }
        });
        String result="";
        if(userText.lastIndexOf("@")>0){
            result = userText.substring(0, userText.lastIndexOf("@"));//Separates the domain from the name
        }else{
            result=userText;
        }

        welcome_UserText.setText("Welcome: "+result);
        ViewGroup vgNotification=(ViewGroup)findViewById(R.id.notificationTableLayout);
        makeNotificationGUI("Photo at 'Sandton City' awaiting response",0,vgNotification);

        ViewGroup vgHistory=(ViewGroup)findViewById(R.id.historyTableLayout);
        makeHistoryGUI("Sandton City",0,vgHistory);
        makeHistoryGUI("Cresta",1,vgHistory);
        makeHistoryGUI("Menlyn",2,vgHistory);
    }
    private void makeNotificationGUI(String tag, int index, ViewGroup v) {
        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate new_tag_view.xml to create new row and set up the contained Buttons
        View newTagView = inflater.inflate(R.layout.notification_view, v,false);

        // get newTagButton reference, set its text and register its listener
        Button newNotificationButton = (Button) newTagView.findViewById(R.id.newNotification);
        newNotificationButton.setText(tag);

        newNotificationButton.setOnClickListener(storeButtonListener);

        // add new tag and edit buttons to urlTableLayout at specified row number (index)
        v.addView(newTagView, index);
    }
    private void makeHistoryGUI(String tag, int index, ViewGroup v) {
        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate new_tag_view.xml to create new row and set up the contained Buttons
        View newTagView = inflater.inflate(R.layout.history_view, v,false);

        // get newTagButton reference, set its text and register its listener
        Button newHistoryButton = (Button) newTagView.findViewById(R.id.newHistory);
        newHistoryButton.setText(tag);

        newHistoryButton.setOnClickListener(storeButtonListener);

        // add new tag and edit buttons to urlTableLayout at specified row number (index)
        v.addView(newTagView, index);
    }
    public View.OnClickListener storeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String buttonText = ((Button) v).getText().toString();
            Log.v("storeButtonListener","going to page "+buttonText);
            Intent intent = new Intent(getApplicationContext(),Location.class);
            intent.putExtra("LocationName",buttonText);
            startActivity(intent);
        }
    };


}
