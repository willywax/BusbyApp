package com.example.busby.busbyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by hanop on 2017/05/15.
 */

public class Login extends AppCompatActivity {
    final String SPF_NAME = "login";
    final String USERNAME = "username";
    final String PASSWORD = "password";
    final boolean boxChecked = false;

    private JSONArray result;
    private ArrayList<String> users;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        realLogin();
    }
    private void realLogin(){
        final EditText username=(EditText)findViewById(R.id.username_input);
        final EditText password=(EditText)findViewById(R.id.password_input);
        final Button real_login_button = (Button) findViewById(R.id.real_login);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        SharedPreferences logInDetails = getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
        username.setText(logInDetails.getString(USERNAME,""));
        password.setText(logInDetails.getString(PASSWORD,""));
        checkBox.setChecked(logInDetails.getBoolean(String.valueOf(boxChecked),false));


        real_login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userString=username.getText().toString().trim();
                String passString = password.getText().toString().trim();

                //Saves the credentials
                if(checkBox.isChecked()){
                    boolean boxIsChecked = checkBox.isChecked();
                    //Adds the credential to SharedPreferences
                    SharedPreferences logInDetails = getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
                    logInDetails.edit().putString(USERNAME,userString).putString(PASSWORD,passString).putBoolean(String.valueOf(boxChecked),boxIsChecked).commit();
                }else  {
                    //deletes the credentials if the checkbox is unchecked
                    SharedPreferences logInDetails = getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
                    logInDetails.edit().clear().commit();
                }
                if(userString.equals("admin")&&password.getText().toString().equals("admin")){
                    Intent intent = new Intent(Login.this,Store_History.class);
                    intent.putExtra("Username",userString);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void getData(){
        StringRequest stringRequest = new StringRequest("your php script address",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("result");
                            getUsers(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getUsers(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                users.add(json.getString("Username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
