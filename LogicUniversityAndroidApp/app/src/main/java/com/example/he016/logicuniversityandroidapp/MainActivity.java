package com.example.he016.logicuniversityandroidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.he016.logicuniversityandroidapp.landingActivity.DHLandingActivity;

import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Button buttonLogin;
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... strings) {
                        boolean bRes = Login(strings[0], strings[1]);
                        return bRes;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (aBoolean) {
                            Intent intent = new Intent(getApplicationContext(), DHLandingActivity.class);
                            startActivity(intent);
                        }
                    }
                }.execute(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });
    }

    // Login function here
    public static boolean Login(String strEmail, String strPassword) {
        try {
            String id = URLEncoder.encode(strEmail, "UTF-8");
            String pw = URLEncoder.encode(strPassword, "UTF-8");
            String credential = String.format("username=%s&password=%s&grant_type=password", id, pw);
            String tokenURL = JSONParser.RoutePrefix + "/Token";

            String result = JSONParser.postStream(tokenURL, false, credential);
            JSONObject res = new JSONObject(result);
            if (res.has("access_token") && res.has("userRole")) {
                JSONParser.access_token = res.getString("access_token");
                JSONParser.userRole = res.getString("userRole");
                return true;
            }
        } catch (Exception e) {
            JSONParser.access_token = "";
            JSONParser.userRole = "";
            Log.e("Login", e.toString());
        }
        return false;
    }
}
