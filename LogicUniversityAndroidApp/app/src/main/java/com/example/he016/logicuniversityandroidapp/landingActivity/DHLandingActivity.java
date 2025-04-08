package com.example.he016.logicuniversityandroidapp.landingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.approveRejectActivity.ListOfApproveRejectRequestsActivity;
import com.example.he016.logicuniversityandroidapp.collectionPointActivity.ChangeDRCollectionPointActivity;

public class DHLandingActivity extends AppCompatActivity {
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhlanding);
        listView = (ListView) findViewById(R.id.listView1);
        String[] values = new String[]{"Approve/Reject Request", "Change of Rep/Collection Point"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.rowlandingpage, R.id.textView1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long id) {
                String item = (String) av.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(), item + " selected",
                        Toast.LENGTH_SHORT).show();

                if (position == 0) {
                    Intent i = new Intent(v.getContext(), ListOfApproveRejectRequestsActivity.class);
                    startActivityForResult(i, 1);
                }

                if (position == 1) {
                    Intent i = new Intent(v.getContext(), ChangeDRCollectionPointActivity.class);
                    startActivityForResult(i, 2);
                }
            }
        });
    }

    // Hamburger Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamburger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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


   /* public void onListItemClick(ListView l, View v, int position, long id) {
        com.example.logicuniversityapp.AddRequestDetailActivity.Category c = (com.example.logicuniversityapp.AddRequestDetailActivity.Category) getListAdapter().getItem(position);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("CategoryID", c.get("CategoryID"));
        startActivity(i);
    }*/

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent i1= new Intent(getApplicationContext(),DHLandingActivity.class);
                startActivityForResult(i1,0);
                return true;
            case R.id.item2:
                Intent i2= new Intent(getApplicationContext(),DHLandingActivity.class);
                startActivityForResult(i2,0);
                return true;
            case R.id.item3:
                Intent i3= new Intent(getApplicationContext(),DHLandingActivity.class);
                startActivityForResult(i3,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

}

