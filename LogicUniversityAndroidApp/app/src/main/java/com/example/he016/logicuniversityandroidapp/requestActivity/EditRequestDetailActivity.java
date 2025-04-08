package com.example.he016.logicuniversityandroidapp.requestActivity;

import android.app.Activity;
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

import com.example.he016.logicuniversityandroidapp.CartSession;
import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DRLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.StaffLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.RequestDetail;

public class EditRequestDetailActivity extends AppCompatActivity {
    private CartSession cartSession;
    boolean isNew = true;
    String RequestId = " ";
    String ItemNo = " ";
    String need = " ";
    String Description = " ";
    int QuantityNeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request_detail);
        Intent intent = getIntent();
        String Index = intent.getExtras().getString("index");
        RequestId = intent.getExtras().getString("requestId");
        ItemNo = intent.getExtras().getString("itemNo");
        need = intent.getExtras().getString("quantityNeed");
        QuantityNeed = Integer.parseInt(need);
        Description = intent.getExtras().getString("description");

        TextView txt = findViewById(R.id.textView2);
        final EditText txt2 = findViewById(R.id.editText);

        if (intent.hasExtra("index")) {
            txt.setText(Description);
            txt2.setText(need);
        }

        if (intent.hasExtra("requestId")) {
            isNew = false;
        }
        Button savebutton = (Button) findViewById(R.id.saveButton);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Integer.parseInt(txt2.getText().toString().trim()) == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a non zero number", Toast.LENGTH_SHORT).show();
                    } else {
                        saveEdit(isNew);
                        Intent data = new Intent();
                        data.putExtra("edited", 1);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Input Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(isNew);
                Intent data = new Intent();
                data.putExtra("edited", 1);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    void saveEdit(boolean isNew) {

        EditText txt = findViewById(R.id.editText);
        String quantityNeed = txt.getText().toString();

        if (isNew == false) {
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {
                    RequestDetail.saveEditRequestDetail(Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2]));
                    return null;
                }
            }.execute(RequestId, ItemNo, quantityNeed);
        } else {
            cartSession.setRequestDetail(ItemNo, QuantityNeed);
        }
    }

    void delete(boolean isNew) {


        if (isNew == false) {

            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {
                    RequestDetail.deleteEditRequestDetail(Integer.parseInt(params[0]), params[1]);
                    return null;
                }
            }.execute(RequestId, ItemNo);
        } else
            cartSession.deleteCartRequestDetail(ItemNo);
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


