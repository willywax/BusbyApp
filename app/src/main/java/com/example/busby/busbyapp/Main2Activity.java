package com.example.busby.busbyapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    private TableLayout notificationTableLayout;
    private TableLayout historyTableLayout;
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

    }
    private void realLogin(){
        final EditText username=(EditText)findViewById(R.id.username_input);
        final EditText password=(EditText)findViewById(R.id.password_input);
        final Button real_login_button = (Button) findViewById(R.id.real_login);

        real_login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userString=username.getText().toString();
                if(userString.equals("admin")&&password.getText().toString().equals("admin")){
                    setContentView(R.layout.store_history);
                    storeHistory(userString);
                }

            }
        });
    }
    private void storeHistory(String userText){
        final TextView welcome_UserText=(TextView)findViewById(R.id.welcome_UserText);
        welcome_UserText.setText("Welcome: "+userText);
    }
    private void makeNotificationGUI(String tag, int index, ViewGroup v) {
        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate new_tag_view.xml to create new row and set up the contained Buttons
        View newTagView = inflater.inflate(R.layout.notification_view, v);

        // get newTagButton reference, set its text and register its listener
        Button newNotificationButton = (Button) newTagView.findViewById(R.id.newNotification);
        newNotificationButton.setText(tag);

        newNotificationButton.setOnClickListener(storeButtonListener);

        // add new tag and edit buttons to urlTableLayout at specified row number (index)
        notificationTableLayout.addView(newTagView, index);
    }
    public View.OnClickListener storeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String buttonText = ((Button) v).getText().toString();
            Log.v("storeButtonListener","going to page"+buttonText);
        }
    };
}
