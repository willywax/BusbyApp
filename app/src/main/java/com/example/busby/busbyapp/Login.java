package com.example.busby.busbyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

/**
 * Created by hanop on 2017/05/15.
 */

public class Login extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        realLogin();
    }
    private void realLogin(){
        final EditText username=(EditText)findViewById(R.id.username_input);
        final EditText password=(EditText)findViewById(R.id.password_input);
        final Button real_login_button = (Button) findViewById(R.id.real_login);

        real_login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userString=username.getText().toString();
                if(userString.equals("admin")&&password.getText().toString().equals("admin")){
                    Intent intent = new Intent(Login.this,Store_History.class);
                    intent.putExtra("Username",userString);
                    startActivity(intent);

                    finish();
                }

            }
        });
    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
}
