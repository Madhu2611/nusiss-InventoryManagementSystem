package com.example.he016.logicuniversityandroidapp.approveRejectActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DHLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Request;
import com.example.he016.logicuniversityandroidapp.model.RequestDetail;


import java.util.List;

public class ApproveRejectActivity extends AppCompatActivity {

    ListView list;
    Button approve, reject;
    String reqId, status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_reject);
        list = (ListView) findViewById(R.id.listView1);
        approve = (Button) findViewById(R.id.approveBTN);
        reject = (Button) findViewById(R.id.rejectBTN);
        Intent intent = getIntent();
        final String id = intent.getExtras().getString("requestId");


        new AsyncTask<String, Void, List<RequestDetail>>() {
            @Override
            protected List<RequestDetail> doInBackground(String... params) {
                List<RequestDetail> requestDetails = RequestDetail.ListRequestDetailsById(id);
                return requestDetails;
            }

            @Override
            protected void onPostExecute(List<RequestDetail> request) {
                //ApproveRejectAdapter requestAdapter = new ApproveRejectAdapter(getApplicationContext(), request);

                list.setAdapter(new SimpleAdapter
                        (getApplicationContext(), request, R.layout.rowapprovereject, new String[]{"category", "description", "quantityNeed", "totalPrice"},
                                new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4}));
                reqId = request.get(0).get("requestId");
            }

        }.execute();


        //Set approve and reject listener
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "Approved";
                makeCustomDialog();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "Rejected";
                makeCustomDialog();
            }
        });
    }

    // Custom Dialog
    void makeCustomDialog() {
        final Dialog d = new Dialog(this);
        d.setTitle(getString(R.string.customdialogtitle));
        d.setContentView(R.layout.customdialog);
        d.setCancelable(true);
        Button submit = (Button) d.findViewById(R.id.okBTN);
        Button cancel = (Button) d.findViewById(R.id.cancelBTN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) d.findViewById(R.id.editText5);
                String remark = text.getText().toString();
                update(reqId, status, remark);
                d.dismiss();
                Intent data = new Intent();
                data.putExtra("edited", 1);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    void update(String requestId, String status, String remark) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                Request.updateRequestStatus(params[0], params[1], params[2]);
                return null;
            }
        }.execute(requestId, status, remark);
    }

    // Hamburger Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamburger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (JSONParser.userRole.equals("SM")) {
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
                    Intent i = new Intent(getApplicationContext(), DHLandingActivity.class);
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
