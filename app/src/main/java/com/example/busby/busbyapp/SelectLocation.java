package com.example.busby.busbyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.busby.busbyapp.databinding.SelectLocationBinding;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Objects.Location;
import Objects.Notification;


public class SelectLocation extends AppCompatActivity {
    SelectLocationBinding selectLocationBinding;
    private AccessServiceAPI m_ServiceAccess;
    private ProgressDialog m_ProgressDialog;

    SearchView searchView;

    ListAdapter adapter;

    ArrayList<Location> location;
    ArrayList<Location> finalLocation;
    public String text;
    private int UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserID=getIntent().getIntExtra("UserID",0);
        selectLocationBinding = DataBindingUtil.setContentView(this, R.layout.select_location);

        m_ServiceAccess = new AccessServiceAPI();
        //Dummy data stored in location class
        location=new ArrayList<>();
        finalLocation=new ArrayList<>();
        new TaskSiteStore().execute();


        selectLocationBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //sets text in the search view (auto complete)
                text = adapterView.getItemAtPosition(i).toString();
                searchView.setQuery(text, false);
                Intent intent = new Intent(SelectLocation.this, New_Post.class);
                intent.putExtra("LocationName", text);
                intent.putExtra("UserID", UserID);
                //m_ProgressDialog.dismiss();
                startActivity(intent);
                //finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class TaskSiteStore extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            m_ProgressDialog = ProgressDialog.show(SelectLocation.this, "Please wait...", "Loading...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();

            JSONArray storeArray;
            try {
                storeArray= m_ServiceAccess.convertJSONString2Array(m_ServiceAccess.getJSONStringWithParam_POST(Common.Store_URL, param));
                JSONArray storeJSONObject=storeArray.getJSONArray(0);
                for(int x=0;x<storeJSONObject.length();x++){

                    String StoreName=storeJSONObject.getJSONObject(x).getString("StoreName");
                    int SiteID=storeJSONObject.getJSONObject(x).getInt("SiteID");
                    location.add(new Location(StoreName,"",SiteID));
                    Log.v("Store added",""+StoreName+"ID"+SiteID);
                }
                JSONArray siteArray;
                siteArray= m_ServiceAccess.convertJSONString2Array(m_ServiceAccess.getJSONStringWithParam_POST(Common.Site_URL, param));
                JSONArray siteJSONObject=siteArray.getJSONArray(0);
                for(int x=0;x<siteJSONObject.length();x++){
                    for(Location temp:location){
                        if(temp.getSiteID()==siteJSONObject.getJSONObject(x).getInt("SiteID")){
                            Log.v("These match",""+temp.getStore()+"-"+siteJSONObject.getJSONObject(x).getString("SiteName"));
                            finalLocation.add(new Location(temp.getStore(),siteJSONObject.getJSONObject(x).getString("SiteName"),siteJSONObject.getJSONObject(x).getInt("SiteID")));
                        }
                    }
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
            Toast.makeText(getApplicationContext(), "Loading success", Toast.LENGTH_LONG).show();
            ArrayList<String> locationArrayList= new ArrayList<>();
            for(Location temp:finalLocation){
                locationArrayList.add(temp.getLocationName());
            }
            adapter = new ListAdapter(locationArrayList);
            selectLocationBinding.listView.setAdapter(adapter);

            searchView = (SearchView) findViewById(R.id.search);
            searchView.setActivated(true);
            searchView.setQueryHint("Type Location here ");
            searchView.onActionViewExpanded();
            searchView.setIconified(false);
            searchView.clearFocus();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
    }
}
