package com.example.busby.busbyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Objects.Notification;

/**
 * Created by hanop on 2017/05/15.
 */

public class Store_History extends AppCompatActivity {
    int idUser;
    private AccessServiceAPI m_ServiceAccess;
    private ProgressDialog m_ProgressDialog;
    private Set <Notification> notificationSet =new HashSet<>();
    ViewGroup vgNotification;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_history);
        String username = getIntent().getStringExtra("username");
        idUser = getIntent().getIntExtra("idUser",idUser);
        Log.v("UserID",""+idUser);
        m_ServiceAccess = new AccessServiceAPI();

        storeHistory(username);
    }
    private void storeHistory(String userText){


        final TextView welcome_UserText=(TextView)findViewById(R.id.welcome_UserText);
        final Button newPost=(Button)findViewById(R.id.new_Post);
        newPost.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Store_History.this,SelectLocation.class);
                Intent intent1 = new Intent(Store_History.this,New_Post.class);
                intent1.putExtra("UserID",idUser);
                startActivity(intent);
            }
        });
        String result="";
        if(userText.lastIndexOf("@")>0){
            result = userText.substring(0, userText.lastIndexOf("@"));//Separates the domain from the name
        }else{
            result=userText;
        }
        Log.v("idUser is: ", ""+idUser);
        vgNotification=(ViewGroup)findViewById(R.id.notificationTableLayout);
        new TaskNotification().execute();
        welcome_UserText.setText("Welcome: "+result);




        ViewGroup vgHistory=(ViewGroup)findViewById(R.id.historyTableLayout);
        makeHistoryGUI("Sandton City",0,vgHistory);
        makeHistoryGUI("Cresta",1,vgHistory);
        makeHistoryGUI("Menlyn",2,vgHistory);
        makeHistoryGUI("Random",3,vgHistory);

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
            Intent intent = new Intent(getApplicationContext(),Site.class);
            intent.putExtra("LocationName",buttonText);
            intent.putExtra("UserID",idUser);
            startActivity(intent);
        }
    };
    //sick code from another website
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class TaskNotification extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            m_ProgressDialog = ProgressDialog.show(Store_History.this, "Please wait...", "Processing...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();
            param.put("idUser", ""+idUser);

            JSONArray notificationArray;
            try {
                notificationArray= m_ServiceAccess.convertJSONString2Array(m_ServiceAccess.getJSONStringWithParam_POST(Common.NOTIFICATION_URL, param));
                JSONArray notificationJSONObject=notificationArray.getJSONArray(0);
                for(int x=0;x<notificationJSONObject.length();x++){

                    String notificationID=notificationJSONObject.getJSONObject(x).getString("NotificationID");
                    String storeID=notificationJSONObject.getJSONObject(x).getString("StoreID");
                    String notificationSite=notificationJSONObject.getJSONObject(x).getString("NotificationSite");
                    String notificationStore=notificationJSONObject.getJSONObject(x).getString("NotificationStore");
                    String notificationState=notificationJSONObject.getJSONObject(x).getString("NotificationState");
                    String time=notificationJSONObject.getJSONObject(x).getString("TimeOfDay");
                    notificationSet.add(new Notification(Integer.parseInt(notificationID),idUser,Integer.parseInt(storeID),notificationState,notificationSite,notificationStore,time));
                    Log.v("Notification added",""+notificationSite+" "+notificationStore);
                }
            } catch (Exception e) {
                Log.v("Error in background",""+e);
            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        protected void onPostExecute(Void result) {
            m_ProgressDialog.dismiss();
            int counter=0;
            for(Notification nS:notificationSet){
                makeNotificationGUI("Photo at '"+nS.getSite()+"-"+nS.getStore()+"' "+nS.getState(),counter,vgNotification);

            }
            Toast.makeText(getApplicationContext(), "Notification success", Toast.LENGTH_LONG).show();

        }
    }


}
