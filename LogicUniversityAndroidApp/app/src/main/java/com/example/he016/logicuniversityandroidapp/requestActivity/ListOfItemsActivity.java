package com.example.he016.logicuniversityandroidapp.requestActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DRLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.StaffLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Category;
import com.example.he016.logicuniversityandroidapp.model.Inventory;

import java.util.List;

public class ListOfItemsActivity extends AppCompatActivity {
    private Spinner spinner;
    String requestId = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_items);
        Intent intent = getIntent();
        if (intent.hasExtra("requestid")) {
            requestId = intent.getExtras().getString("requestid");
        }
        final ListView list = (ListView) findViewById(R.id.listView2);
        spinner = (Spinner) findViewById(R.id.spinner);

        new AsyncTask<Void, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(Void... voids) {
                return Category.ListCategories();
            }

            @Override
            protected void onPostExecute(List<String> categories) {

                spinner.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, categories));

            }

        }.execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String catSelected = (String) parent.getItemAtPosition(position);

                new AsyncTask<String, Void, List<Inventory>>() {
                    @Override
                    protected List<Inventory> doInBackground(String... params) {
                        return Inventory.ListItemsByCategory(params[0]);
                    }

                    protected void onPostExecute(List<Inventory> result) {

                        list.setAdapter(new SimpleAdapter
                                (getApplicationContext(), result, R.layout.rowlistinventory, new String[]{"description", "stdPrice"},
                                        new int[]{R.id.textView1, R.id.textView2}));

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Inventory selected = (Inventory) parent.getAdapter().getItem(position);

                                if (requestId != " ") {
                                    //add condition for checking user role( restrict DH from opening AddRequestDetailsActivity)
                                    Intent intent = new Intent(getApplicationContext(), AddRequestDetailActivity.class);
                                    intent.putExtra("itemNo", selected.get("itemNo"));
                                    intent.putExtra("description", selected.get("description"));
                                    intent.putExtra("requestId", requestId);
                                    intent.putExtra("unitMeasure", selected.get("unitMeasure"));
                                    //startActivity(intent);
                                    startActivityForResult(intent, 456);
                                    Intent data = new Intent();
                                    data.putExtra("edited", 1);
                                    setResult(RESULT_OK, data);
                                }
                            }
                        });
                    }
                }.execute(catSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 456) {
            if (data.hasExtra("edited")) {
                int result = data.getExtras().getInt("edited");
                if (result == 1) {
                    finish();
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
