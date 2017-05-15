package com.example.busby.busbyapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
    private TableLayout notificationTableLayout;
    private TableLayout historyTableLayout;
    private String userChoosenTask;
    private ImageButton postImageButton;
    private Button newPostSubmitButton;
    final int REQUEST_CAMERA=0,SELECT_FILE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        notificationTableLayout = (TableLayout) findViewById(R.id.notificationTableLayout);
        final Button login_button = (Button) findViewById(R.id.firstLoginButton);

        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.login_screen);
                realLogin();
            }
        });
        notificationTableLayout = (TableLayout) findViewById(R.id.notificationTableLayout);

    }
    private void realLogin(){
        final EditText username=(EditText)findViewById(R.id.username_input);
        final EditText password=(EditText)findViewById(R.id.password_input);
        final Button real_login_button = (Button) findViewById(R.id.real_login);

        real_login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userString=username.getText().toString();
                if(userString.equals("admin")&&password.getText().toString().equals("admin")){

                    storeHistory(userString);
                }

            }
        });
    }
    private void storeHistory(String userText){
        setContentView(R.layout.store_history);
        final TextView welcome_UserText=(TextView)findViewById(R.id.welcome_UserText);
        final Button newPost=(Button)findViewById(R.id.new_Post);
        newPost.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                newPostMethod();
            }
        });
        welcome_UserText.setText("Welcome: "+userText);
        ViewGroup vgNotification=(ViewGroup)findViewById(R.id.notificationTableLayout);
        makeNotificationGUI("Dummy notification",0,vgNotification);
        ViewGroup vgHistory=(ViewGroup)findViewById(R.id.historyTableLayout);
        makeHistoryGUI("Dummy History",0,vgHistory);
    }
    private void newPostMethod(){
        setContentView(R.layout.new_post_view);
        postImageButton=(ImageButton)findViewById(R.id.postImageButton);
        postImageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                selectImage();
            }
        });

        final Spinner newPostThreadSpinner=(Spinner)findViewById(R.id.newPostThreadSpinner);
        String[]tempThreads=new String[]{"Cycle1","Cycle2","Cycle3"};

        ArrayAdapter <String>ThreadsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempThreads);
        ThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newPostThreadSpinner.setAdapter(ThreadsAdapter);
        newPostSubmitButton=(Button)findViewById(R.id.newPostSubmitButton);
        newPostSubmitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.v("Spinner item is: ",""+newPostThreadSpinner.getSelectedItem().toString());
            }
        });
    }
    private void locationMethod(String location){
        setContentView(R.layout.location);
        final TextView Location_name=(TextView)findViewById(R.id.Location_name);
        Location_name.setText(location);
        ViewGroup vgLocation=(ViewGroup)findViewById(R.id.LocationThreadTableLayout);
        Spinner locationSpinner=(Spinner)findViewById(R.id.location_spinner);
        String[]tempThreads=new String[]{"Edgars","Markham"};

        ArrayAdapter <String>newThreadsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempThreads);
        newThreadsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(newThreadsAdapter);
        makeLocationGUI("Dummy location comment",0,vgLocation);

        final Button newPostSpecific=(Button)findViewById(R.id.new_Post_Specific);
        newPostSpecific.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                newPostMethod();
            }
        });



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

        // add new tag and edit buttons to urlTableLayout at specified row number (index)
        v.addView(newTagView, index);
    }

    public View.OnClickListener storeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String buttonText = ((Button) v).getText().toString();
            Log.v("storeButtonListener","going to page "+buttonText);
            locationMethod(buttonText);
        }
    };
    //from here its the image handeling code taken from the link in build 1 resources and adapted
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Main2Activity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
            {
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
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
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
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        postImageButton.setImageBitmap(thumbnail);
    }
}
