package com.example.he016.logicuniversityandroidapp.requestActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.adjustmentVoucher.Activity_CreateAdjustmentVoucher;
import com.example.he016.logicuniversityandroidapp.landingActivity.DRLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.StaffLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.RequestDetail;

public class AddRequestDetailActivity extends AppCompatActivity {

    String itemNo = " ";
    String RequestId = " ";
    String des = " ";
    String need = "1";
    String uom = " ";
    TextView txt;
    EditText txt2;
    TextView txt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request_detail);

        Intent intent = getIntent();
        if (intent.hasExtra("itemNo")) {
            itemNo = intent.getStringExtra("itemNo");
            des = intent.getStringExtra("description");
            uom = intent.getStringExtra("unitMeasure");

            // if request Id is null should add a new request
            if (intent.hasExtra("requestId")) {
                RequestId = intent.getExtras().getString("requestId");
            }
        }


        //setting the text in the activity
        txt = findViewById(R.id.itemeditText);
        txt2 = findViewById(R.id.qtyeditText);
        txt3 = findViewById(R.id.uomeditText);
        txt.setText(des);
        txt2.setText(need);
        txt3.setText(uom);


        Button addbutton = (Button) findViewById(R.id.addButton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Integer.parseInt(txt2.getText().toString().trim()) == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a non zero number", Toast.LENGTH_SHORT).show();
                    } else {
                        saveEdit();
                        Intent data = new Intent();
                        data.putExtra("edited", 1);
                        setResult(RESULT_OK, data); // we have finished this Activity
                        // data has changed
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Input Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancelbutton = (Button) findViewById(R.id.cancelButton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void saveEdit() {
        String QtyNeed = txt2.getText().toString();

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                RequestDetail.insertRequestDetail(Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2]));
                return null;
            }
        }.execute(RequestId, itemNo, QtyNeed);
    }


    // Hamburger Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamburger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (JSONParser.userRole.equals("SC")) {
            switch (item.getItemId()) {
                case R.id.hamburgerHome: {
                    if (JSONParser.userRole.equals("SM") || JSONParser.userRole.equals("SS")) {
                        Intent i = new Intent(getApplicationContext(), SMSSLandingActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        return true;
                    } else if (JSONParser.userRole.equals("SC")) {
                        Intent i = new Intent(getApplicationContext(), SCLandingActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        return true;
                    }
                }

                case R.id.hamburgerLogout: {
                    JSONParser.access_token = "";
                    JSONParser.userRole = "";
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    return true;
                }

                default:
                    return super.onOptionsItemSelected(item);
            }
        } else if (JSONParser.userRole.equals("SS")) {
            switch (item.getItemId()) {
                case R.id.hamburgerHome: {

                    if (JSONParser.userRole.equals("SM") || JSONParser.userRole.equals("SS")) {
                        Intent i = new Intent(getApplicationContext(), SMSSLandingActivity.class);
                        startActivity(i);
                        finish();
                        return true;
                    } else if (JSONParser.userRole.equals("SC")) {
                        Intent i = new Intent(getApplicationContext(), SCLandingActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        return true;
                    }
                }

                case R.id.hamburgerLogout: {
                    JSONParser.access_token = "";
                    JSONParser.userRole = "";
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    return true;
                }

                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            switch (item.getItemId()) {
                case R.id.hamburgerHome: {
                    Intent i;
                    if (JSONParser.userRole.equals("DR")) {
                        i = new Intent(getApplicationContext(), DRLandingActivity.class);
                    } else {
                        i = new Intent(getApplicationContext(), StaffLandingActivity.class);
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    return true;
                }

                case R.id.hamburgerLogout: {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    return true;
                }

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    }
}
