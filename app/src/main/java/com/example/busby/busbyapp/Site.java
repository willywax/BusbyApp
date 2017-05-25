package com.example.busby.busbyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import Objects.Image;
import Objects.Notification;

/**
 * Created by hanop on 2017/05/15.
 */

public class Site extends AppCompatActivity {
    String storeName=null;
    private Set<String>Stores=new HashSet<>();
    private int UserID;
    private AccessServiceAPI m_ServiceAccess;
    private ProgressDialog m_ProgressDialog;
    private String localStoreName;
    private int localCycle;
    String siteName;
    ViewGroup vgLocation;
    private LinkedList<Image> imageSet =new LinkedList<>();
    private LinkedList<Image>storeImageSet=new LinkedList<>();
    private Boolean firstCallSite;
    private Boolean firstCallCycle;
    private Boolean emptyView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_ServiceAccess = new AccessServiceAPI();

        siteName = getIntent().getStringExtra("LocationName");
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
        firstCallSite=true;
        firstCallCycle=true;
        emptyView=false;
        UserID=getIntent().getIntExtra("UserID",0);
        Log.v("UserID",""+UserID);
        new TaskStores().execute();

    }
    private void locationMethod(String location){
        setContentView(R.layout.site);
        final TextView Location_name=(TextView)findViewById(R.id.Location_name);
        Location_name.setText(location);

        final Spinner locationSpinner=(Spinner)findViewById(R.id.location_spinner);
        final Spinner cycleSpinner=(Spinner)findViewById(R.id.cycle_spinner);
        String[] tempArray=new String[Stores.size()];
        int counter=0;
        for(String x:Stores){
            tempArray[counter]=x;
            counter++;
        }

        ArrayAdapter<String> newThreadsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempArray);
        newThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(newThreadsAdapter);
        if(storeName!=null){
            Log.v("Store name",storeName);
            int temp=0;
            for(int x=0;x<tempArray.length;x++){
                if(tempArray[x].equalsIgnoreCase(storeName)){
                    temp=x;
                }
            }
            locationSpinner.setSelection(temp);
        }
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(firstCallSite){
                    firstCallSite=false;
                }else{
                    localStoreName=locationSpinner.getSelectedItem().toString();
                    localCycle=cycleSpinner.getSelectedItemPosition();
                    new TaskImages().execute();
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });
        cycleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(firstCallCycle){
                    firstCallCycle=false;
                }else{
                    localStoreName=locationSpinner.getSelectedItem().toString();
                    localCycle=cycleSpinner.getSelectedItemPosition();
                    new TaskImages().execute();
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });


        String[]tempCycle=new String[]{"Cycle1","Cycle2","Cycle3"};

        ArrayAdapter<String> newCycleAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempCycle);
        newCycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cycleSpinner.setAdapter(newCycleAdapter);

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

        localStoreName=locationSpinner.getSelectedItem().toString();
        localCycle=cycleSpinner.getSelectedItemPosition();
        new TaskImages().execute();
    }
    private void makeLocationGUI(Image currentImage, int index) {
        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vgLocation=(ViewGroup)findViewById(R.id.LocationThreadTableLayout);
        vgLocation.removeAllViews();
        // inflate new_tag_view.xml to create new row and set up the contained Buttons
        View newTagView = inflater.inflate(R.layout.thread_view, vgLocation,false);

        // get newTagButton reference, set its text and register its listener
        Spinner locationThreadSpinner=(Spinner)newTagView.findViewById(R.id.newThreadSpinner);
        String[]tempThreads=new String[]{"Needs Review","Awaiting Response","Completed"};

        ArrayAdapter <String>newThreadsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempThreads);
        newThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationThreadSpinner.setAdapter(newThreadsAdapter);

        TextView newThreadTextView=(TextView) newTagView.findViewById(R.id.newThreadTextView);
        newThreadTextView.setText(currentImage.getComments().getFirst());//for now single comment
        ImageView tempImage=(ImageView)newTagView.findViewById(R.id.newThreadImage) ;
        new DownloadImageTask((tempImage)).execute("http://busby-web.gear.host/uploads/"+currentImage.getImageURL().substring(currentImage.getImageURL().lastIndexOf('/')+1));
        // add new tag and edit buttons to urlTableLayout at specified row number (index)
        vgLocation.addView(newTagView, index);
    }
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class TaskStores extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            m_ProgressDialog = ProgressDialog.show(Site.this, "Please wait...", "Processing...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();
            param.put("UserID", ""+UserID);
            param.put("SiteName", ""+siteName);

            JSONArray imageArray;
            try {
                imageArray= m_ServiceAccess.convertJSONString2Array(m_ServiceAccess.getJSONStringWithParam_POST(Common.IMAGE_PULL_GENERAL, param));
                JSONArray imageJSONObject=imageArray.getJSONArray(0);
                for(int x=0;x<imageJSONObject.length();x++){
                    int ImageID=imageJSONObject.getJSONObject(x).getInt("ImageID");
                    int ImageNumber=imageJSONObject.getJSONObject(x).getInt("ImageNumber");
                    String ImageURL=imageJSONObject.getJSONObject(x).getString("Image");
                    int StatusID=imageJSONObject.getJSONObject(x).getInt("StatusID");
                    String localSiteName=imageJSONObject.getJSONObject(x).getString("SiteName");
                    String localStoreName=imageJSONObject.getJSONObject(x).getString("StoreName");
                    int localCycleID=imageJSONObject.getJSONObject(x).getInt("CycleID");
                    int CampaignID=imageJSONObject.getJSONObject(x).getInt("CampaignID");
                    String time=imageJSONObject.getJSONObject(x).getString("TimeByDay");
                    String ActiveRecord=imageJSONObject.getJSONObject(x).getString("ActiveRecord");
                    storeImageSet.add(new Image(ImageID,ImageNumber, ImageURL,StatusID,UserID,localSiteName,localStoreName,localCycleID,CampaignID,time,ActiveRecord));
                    Log.v("Image added",""+ImageURL);
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
            for(Image temp:storeImageSet){
                Stores.add(temp.getStoreName());
            }
            locationMethod(siteName);
            Toast.makeText(getApplicationContext(), "Store Name success", Toast.LENGTH_LONG).show();

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class TaskImages extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            m_ProgressDialog = ProgressDialog.show(Site.this, "Please wait...", "Processing...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            imageSet.clear();
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();
            param.put("UserID", ""+UserID);
            param.put("SiteName",siteName);
            param.put("StoreName",localStoreName);
            param.put("CycleID",""+(localCycle+1));

            JSONArray imageArray;
            try {
                imageArray= m_ServiceAccess.convertJSONString2Array(m_ServiceAccess.getJSONStringWithParam_POST(Common.IMAGE_PULL, param));
                JSONArray imageJSONObject=imageArray.getJSONArray(0);
                if(imageJSONObject.length()==0){
                    emptyView=true;
                }
                for(int x=0;x<imageJSONObject.length();x++){
                    int ImageID=imageJSONObject.getJSONObject(x).getInt("ImageID");
                    int ImageNumber=imageJSONObject.getJSONObject(x).getInt("ImageNumber");
                    String ImageURL=imageJSONObject.getJSONObject(x).getString("Image");
                    int StatusID=imageJSONObject.getJSONObject(x).getInt("StatusID");
                    int CampaignID=imageJSONObject.getJSONObject(x).getInt("CampaignID");
                    String time=imageJSONObject.getJSONObject(x).getString("TimeByDay");
                    String ActiveRecord=imageJSONObject.getJSONObject(x).getString("ActiveRecord");
                    imageSet.add(new Image(ImageID,ImageNumber, ImageURL,StatusID,UserID,siteName,storeName,1,CampaignID,time,ActiveRecord));
                    Log.v("Image added",""+ImageURL);
                }
                for(Image temp:imageSet){
                    Map<String, String> comment = new HashMap<>();
                    comment.put("imageID", ""+temp.getImageID());
                    JSONArray commentArray;
                    commentArray= m_ServiceAccess.convertJSONString2Array(m_ServiceAccess.getJSONStringWithParam_POST(Common.COMMENT_PULL, comment));
                    JSONArray commentJSONObject=commentArray.getJSONArray(0);
                    for(int x=0;x<commentJSONObject.length();x++){
                        imageSet.get(x).addComments(commentJSONObject.getJSONObject(x).getString("comments"));
                        Log.v("length() is",""+commentJSONObject.length()+"x is "+x);
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
            int counter=0;
            for(Image temp:imageSet){
                makeLocationGUI(temp,counter);
                counter++;
            }
            if(emptyView){
                vgLocation=(ViewGroup)findViewById(R.id.LocationThreadTableLayout);
                vgLocation.removeAllViews();
                emptyView=false;
            }


            Toast.makeText(getApplicationContext(), "Notification success", Toast.LENGTH_LONG).show();

        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
