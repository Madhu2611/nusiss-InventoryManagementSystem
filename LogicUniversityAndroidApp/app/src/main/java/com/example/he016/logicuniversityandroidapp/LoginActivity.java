package com.example.he016.logicuniversityandroidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.landingActivity.DHLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.DRLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.StaffLandingActivity;

import org.json.JSONObject;

import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText UIDval = (EditText)findViewById(R.id.editTextUID);
        final EditText PWDval = (EditText)findViewById(R.id.editTextPWD);
        final TextView REGval = (TextView) findViewById(R.id.textViewREG);
        Button LGNbtn = (Button)findViewById(R.id.buttonLGN);

        LGNbtn.setOnClickListener(new View.OnClickListener(){
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

                           if(JSONParser.userRole.equals("SM") || JSONParser.userRole.equals("SS")){
                               Intent intent = new Intent(getApplicationContext(), SMSSLandingActivity.class);
                               intent.putExtra("userrole", JSONParser.userRole);
                               startActivity(intent);
                            }
                            else if(JSONParser.userRole.equals("SC")){
                                Intent intent = new Intent(getApplicationContext(), SCLandingActivity.class);
                                intent.putExtra("userrole", JSONParser.userRole);
                                startActivity(intent);
                            }
                            else if(JSONParser.userRole.equals("DH") || JSONParser.userRole.equals("Temp DH")){
                               Intent intent = new Intent(getApplicationContext(), DHLandingActivity.class);
                               intent.putExtra("userrole", JSONParser.userRole);
                               startActivity(intent);
                            }
                            else if(JSONParser.userRole.equals("DR")){
                               Intent intent = new Intent(getApplicationContext(), DRLandingActivity.class);
                               intent.putExtra("userrole", JSONParser.userRole);
                               startActivity(intent);
                            }
                            else if(JSONParser.userRole.equals("Staff")){
                               Intent intent = new Intent(getApplicationContext(), StaffLandingActivity.class);
                               intent.putExtra("userrole", JSONParser.userRole);
                               startActivity(intent);
                            }
                            else{
                               Toast.makeText(getApplicationContext(), "Error when logging in",
                                       Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error when logging in",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(UIDval.getText().toString(), PWDval.getText().toString());
            }
        });

        REGval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            Uri uri = Uri.parse("http://172.17.189.196/LogicUniversityApp/Account/Register");
                            Intent redirect = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(redirect);
                        }
                    }.execute();
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
