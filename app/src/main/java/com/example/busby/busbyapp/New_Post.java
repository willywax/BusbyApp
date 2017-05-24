package com.example.busby.busbyapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanop on 2017/05/15.
 */

public class New_Post extends AppCompatActivity {
    private ImageButton postImageButton;
    private Button newPostSubmitButton;
    private AccessServiceAPI m_ServiceAccess;
    final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String locName = "New-Post";
    String StoreName;
    String SiteName;
    Boolean imageSelected=false;
    private File imageToUpload;
    private String imageName;
    private ProgressDialog m_ProgressDialog;
    private int UserID;
    private int SpinnerCycle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String temp = getIntent().getStringExtra("StoreName");
        Bundle bundle = getIntent().getExtras();
        locName = bundle.getString("LocationName");
        if(locName==null){
            if (temp != null) {
                locName = temp + "-" + getIntent().getStringExtra("SiteName");
            }
        }
        UserID=getIntent().getIntExtra("UserID",0);
        Log.v("UserID",""+UserID);
        m_ServiceAccess = new AccessServiceAPI();

        //For location Name
        StoreName=locName.substring(0,locName.indexOf('-'));
        Log.v("StoreName", StoreName);
        SiteName=locName.substring(locName.indexOf('-')+1);
        Log.v("SiteName", SiteName);
        newPostMethod();
    }

    private void newPostMethod() {
        setContentView(R.layout.new_post_view);
        postImageButton = (ImageButton) findViewById(R.id.postImageButton);
        postImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage();
            }
        });
        TextView newPostLabel = (TextView) findViewById(R.id.newPostLabel);
        newPostLabel.setText(locName);
        final Spinner newPostThreadSpinner = (Spinner) findViewById(R.id.newPostThreadSpinner);
        String[] tempThreads = new String[]{"Cycle1", "Cycle2", "Cycle3"};

        ArrayAdapter<String> ThreadsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tempThreads);
        ThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newPostThreadSpinner.setAdapter(ThreadsAdapter);
        newPostSubmitButton = (Button) findViewById(R.id.newPostSubmitButton);
        newPostSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    SpinnerCycle=Integer.parseInt(newPostThreadSpinner.getSelectedItem().toString().substring(newPostThreadSpinner.getSelectedItem().toString().lastIndexOf('e')+1));
                    if(imageSelected){
                        new TaskUpload().execute();
                    }else{
                        Toast.makeText(getApplicationContext(), "Select Image", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Log.v("Image not uploaded", "" + e);
                }

            }
        });
    }

    //from here its the image handling code taken from the link in build 1 resources and adapted
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(New_Post.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = New_Post.Utility.checkPermission(New_Post.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }//end of utility

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case New_Post.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Bitmap thumbnail = bm;
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + "_comp.jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    imageToUpload = destination;
                    imageSelected=true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        postImageButton.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            imageToUpload = destination;
            imageSelected=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        postImageButton.setImageBitmap(thumbnail);
    }

    public class TaskUpload extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            m_ProgressDialog = ProgressDialog.show(New_Post.this, "Please wait...", "Uploading...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            imageName=imageToUpload.toString().substring(imageToUpload.toString().lastIndexOf('/'));
            Log.v("Image name is:", "/uploads" + imageName);

            int ImageID=4;
            int ImageNumber=4;
            String ImageURL="/uploads"+imageName;
            int StatusID=1;//always default to needs review 2 await, 3 complete
            int CycleID=SpinnerCycle;
            int CampaignID=1;//default for now
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm");
            String currentDateandTime = sdf.format(new Date());
            String TimeByDay=currentDateandTime;
            String Active="TRUE";

            Map<String, String> param = new HashMap<>();
            param.put("Image", ImageURL);
            param.put("StatusID", ""+StatusID);
            param.put("UserID", ""+UserID);//gotten from extra
            param.put("SiteName", ""+SiteName);
            param.put("StoreName", ""+StoreName);
            param.put("CycleID", ""+CycleID);
            param.put("CampaignID", ""+CampaignID);
            param.put("TimeByDay", TimeByDay);
            param.put("ActiveRecord", Active);

            JSONObject jObjResult;

            try {
                jObjResult = m_ServiceAccess.convertJSONString2Obj(m_ServiceAccess.getJSONStringWithParam_POST(Common.IMAGE_PUSH_URL, param));
                if(jObjResult.getString("result").equalsIgnoreCase("Success")){
                    Log.v("Post","Success");
                }else{
                    Log.v("Post","Fail server side");
                }
            } catch (Exception e) {
                Log.v("Post","Fail");
            }
            SimpleFTP ftp = new SimpleFTP();


            // Connect to an FTP server on port 21.
            try {
                ftp.connect("ftp.gear.host", 21, "busby-web\\$busby-web", "MEwaqb6hRiegJRsHSYsxtGqWhw5cutwRWtiGNipNmtm9HPWQGNeoqqmpfJ8A");
                ftp.bin();
                ftp.cwd("/site/wwwroot/uploads/");
                Log.v("File is:", "" + imageToUpload);
                ftp.stor(imageToUpload);
                // Quit from the FTP server.
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        protected void onPostExecute(Void result) {
            m_ProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_LONG).show();

        }
    }
}
