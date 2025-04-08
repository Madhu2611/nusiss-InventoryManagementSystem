package com.example.he016.logicuniversityandroidapp.collectionPointActivity;

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

import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DHLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Staff;

import java.util.ArrayList;


public class ListDRNamesActivity extends AppCompatActivity {

    ListView list;
    ArrayList<Staff> staffList = new ArrayList<Staff>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_drnames);


        Intent intent = getIntent();
        if (intent.hasExtra("staffList")) {
            staffList = (ArrayList<Staff>) intent.getExtras().getSerializable("staffList");
        } else if (intent.hasExtra("fillteredStaffNames")) {
            staffList = (ArrayList<Staff>) intent.getExtras().getSerializable("fillteredStaffNames");
        }

        list = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<Staff> drAdapter = new ArrayAdapter<Staff>(this, android.R.layout.simple_expandable_list_item_1, staffList);
        list.setAdapter(drAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Staff staff = (Staff) parent.getAdapter().getItem(position);
                Intent in = new Intent(ListDRNamesActivity.this, ChangeDRCollectionPointActivity.class);
                in.putExtra("staffName", staff.staffName);
                in.putExtra("staffId", staff.staffId);
                setResult(RESULT_OK, in);
                finish();
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
}
