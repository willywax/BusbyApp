package com.example.busby.busbyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

public class Landing extends AppCompatActivity {
    private TableLayout notificationTableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        final Button login_button = (Button) findViewById(R.id.firstLoginButton);
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Landing.this,Login.class);

                startActivity(intent);
                finish();
            }
        });
        notificationTableLayout = (TableLayout) findViewById(R.id.notificationTableLayout);

    }

}
