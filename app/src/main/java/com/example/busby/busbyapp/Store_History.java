package com.example.busby.busbyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    int idUser, notificationDelete;
    private String Username;
    private AccessServiceAPI m_ServiceAccess;
    private ProgressDialog m_ProgressDialog;
    private Set <Notification> notificationSet =new HashSet<>();
    private Set <String> historySet=new HashSet<>();
    ViewGroup vgNotification;
    ViewGroup vgHistory;
    Map NotificationTextSet=new HashMap<String,Integer>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_history);
        Username = getIntent().getStringExtra("Username");
        idUser = getIntent().getIntExtra("idUser",idUser);

        Log.v("UserID",""+idUser);
        m_ServiceAccess = new AccessServiceAPI();
        notificationDelete=-1;
        storeHistory(Username);

    }
    protected void onRestart() {
        super.onRestart();
        notificationDelete=-1;
        storeHistory(Username);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.option_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                m_ProgressDialog = ProgressDialog.show(Store_History.this, "Logging Out...","Log Out Success",true);
                this.finish();
                break;
        }
        return true;
    }

    private void storeHistory(String userText){


        final TextView welcome_UserText=(TextView)findViewById(R.id.welcome_UserText);
        final Button newPost=(Button)findViewById(R.id.new_Post);
        newPost.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Store_History.this,SelectLocation.class);
                //Intent intent1 = new Intent(Store_History.this,New_Post.class);
                //intent1.putExtra("UserID",idUser);
                intent.putExtra("UserID",idUser);
                intent.putExtra("Username",Username);
                startActivity(intent);
            }
        });
        String result;
        if(userText.lastIndexOf("@")>0){
            result = userText.substring(0, userText.lastIndexOf("@"));//Separates the domain from the name
        }else{
            result=userText;
        }
        Log.v("idUser is: ", ""+idUser);
        vgNotification=(ViewGroup)findViewById(R.id.notificationTableLayout);
        vgHistory=(ViewGroup)findViewById(R.id.historyTableLayout);
        vgHistory.removeAllViewsInLayout();
        vgNotification.removeAllViewsInLayout();
        NotificationTextSet.clear();
        notificationSet.clear();
        historySet.clear();
        new TaskNotification().execute();
        new TaskHistory().execute();
        welcome_UserText.setText("Welcome: "+result);





        //makeHistoryGUI("Sandton",0,vgHistory);
        //makeHistoryGUI("Eastgate",1,vgHistory);

    }
    private void makeNotificationGUI(String tag, int index, ViewGroup v, int ID) {
        // get a reference to the LayoutInflater service
        if(tag.indexOf("Review")>0){
            Log.v("Notification Ignored:","Tag:"+tag);
        }else{
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // inflate new_tag_view.xml to create new row and set up the contained Buttons
            View newTagView = inflater.inflate(R.layout.notification_view, v,false);

            // get newTagButton reference, set its text and register its listener
            Button newNotificationButton = (Button) newTagView.findViewById(R.id.newNotification);
            newNotificationButton.setText(tag);

            newNotificationButton.setOnClickListener(storeButtonListener);

            // add new tag and edit buttons to urlTableLayout at specified row number (index)
            NotificationTextSet.put(tag,ID);
            v.addView(newTagView, index);
        }

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
            //Log.v("Notification ID is ","nothing or "+NotificationTextSet.get(buttonText));
            try {
                notificationDelete = (int) NotificationTextSet.get(buttonText);
            }catch(NullPointerException e){

            }
            Intent intent = new Intent(getApplicationContext(),Site.class);
            intent.putExtra("LocationName",buttonText);
            intent.putExtra("UserID",idUser);
            intent.putExtra("Username",Username);
            intent.putExtra("NotificationID",notificationDelete);
            startActivity(intent);
        }
    };
    //sick code from another website
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class TaskNotification extends AsyncTask<Void, Void, Void> {
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
            param.put("UserID", ""+idUser);

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
                makeNotificationGUI("Photo at '"+nS.getSite()+"-"+nS.getStore()+"' "+nS.getState(),counter,vgNotification, nS.getID());
            }
            //Toast.makeText(getApplicationContext(), "Notification success", Toast.LENGTH_LONG).show();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class TaskHistory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();
            param.put("UserID", ""+idUser);

            JSONArray historyArray;
            try {
                historyArray= m_ServiceAccess.convertJSONString2Array(m_ServiceAccess.getJSONStringWithParam_POST(Common.HISTORY, param));
                JSONArray historyJSONObject=historyArray.getJSONArray(0);
                for(int x=0;x<historyJSONObject.length();x++){
                    historySet.add(historyJSONObject.getJSONObject(x).getString("SiteName"));
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
            for(String hS:historySet){
                makeHistoryGUI(hS,counter,vgHistory);
            }

        }
    }


}
