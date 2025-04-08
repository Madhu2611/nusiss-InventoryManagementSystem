package com.example.he016.logicuniversityandroidapp.approveRejectActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DHLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Request;

import java.util.List;

public class ListOfApproveRejectRequestsActivity extends AppCompatActivity {

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_approvereject_requests);
        list = (ListView) findViewById(R.id.listView1);

        new AsyncTask<String, Void, List<Request>>() {
            @Override
            protected List<Request> doInBackground(String... params) {
                List<Request> request = Request.ListRequestsByDepartmentId();
                return request;
            }

            @Override
            protected void onPostExecute(List<Request> request) {
                list.setAdapter(new SimpleAdapter
                        (getApplicationContext(), request, R.layout.rowlistapprovereject, new String[]{"requestId", "staffName", "approvedDate", "stdPrice"},
                                new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4}));
            }

        }.execute();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request selected = (Request) parent.getAdapter().getItem(position);
                Intent in = new Intent(getApplicationContext(), ApproveRejectActivity.class);
                in.putExtra("requestId", selected.get("requestId"));
                startActivityForResult(in, 111);
            }
        });

    }


    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK && requestCode == 111) {
            if (data.hasExtra("edited")) {
                int result = data.getExtras().getInt("edited");
                if (result == 1) {
                    recreate();
                }
            }
        }
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