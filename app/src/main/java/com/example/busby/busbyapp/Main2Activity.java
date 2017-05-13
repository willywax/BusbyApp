package com.example.busby.busbyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);

        final Button button = (Button) findViewById(R.id.firstLoginButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.login_screen);
            }
        });
    }
}
