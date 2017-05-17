package com.example.busby.busbyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanop on 2017/05/15.
 */

public class Login extends AppCompatActivity {
    final String SPF_NAME = "login";
    final String USERNAME = "username";
    final String PASSWORD = "password";
    final boolean boxChecked = false;
    private EditText username;
    private EditText password;
    private AccessServiceAPI m_ServiceAccess;
    private ProgressDialog m_ProgressDialog;

    private JSONArray result;
    private ArrayList<String> users;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        m_ServiceAccess = new AccessServiceAPI();
        username=(EditText)findViewById(R.id.username_input);
        password=(EditText)findViewById(R.id.password_input);
        realLogin();
    }
    private void realLogin(){

        final Button real_login_button = (Button) findViewById(R.id.real_login);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        SharedPreferences logInDetails = getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
        username.setText(logInDetails.getString(USERNAME,""));
        password.setText(logInDetails.getString(PASSWORD,""));
        checkBox.setChecked(logInDetails.getBoolean(String.valueOf(boxChecked),false));


        real_login_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onClick(View v) {
                String userString=username.getText().toString().trim();
                String passString = password.getText().toString().trim();
                if ("".equals(username.getText().toString())) {
                    username.setError("Username is required!");
                    return;
                }
                if ("".equals(password.getText().toString())) {
                    password.setError("Password is required!");
                    return;
                }
                //Call async task to login
                new TaskLogin().execute(username.getText().toString(), password.getText().toString());
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
                //Validate input
            }
        });
    }
    //sick code from another website
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class TaskLogin extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            m_ProgressDialog = ProgressDialog.show(Login.this, "Please wait...", "Processing...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();
            param.put("action", "login");
            param.put("username", params[0]);
            param.put("password", params[1]);

            JSONObject jObjResult;
            try {

                jObjResult = m_ServiceAccess.convertJSONString2Obj(m_ServiceAccess.getJSONStringWithParam_POST(Common.SERVICE_API_URL, param));
                return jObjResult.getInt("result");
            } catch (Exception e) {
                return Common.RESULT_ERROR;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            m_ProgressDialog.dismiss();
            if(Common.RESULT_SUCCESS == result) {
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Store_History.class);
                i.putExtra("username", username.getText().toString());
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
            }
        }
    }
}
