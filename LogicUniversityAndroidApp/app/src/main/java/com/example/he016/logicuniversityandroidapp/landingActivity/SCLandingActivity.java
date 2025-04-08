package com.example.he016.logicuniversityandroidapp.landingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.disbursementList.DisbursementActivity;
import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.purchaseOrder.ListOfSentPOActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.requestActivity.ListOfItemsActivity;
import com.example.he016.logicuniversityandroidapp.requestActivity.ListofViewRequestsActivity;
import com.example.he016.logicuniversityandroidapp.retrieval.RetrievalListActivity;
import com.example.he016.logicuniversityandroidapp.stockCard.ListOfCategoryActivity;

public class SCLandingActivity extends AppCompatActivity {

    final String[] options = {"Create Adjustment Voucher", "View Adjustment Voucher", "Disbursement List",
            "Retrieval List", "View Stock Card", "Purchase Order", "Search Catalogue", "View Request"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sclanding);

        ListView listView = (ListView) findViewById(R.id.listViewLanding);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.row_landing,
                R.id.textViewrowlanding1,
                options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long id) {
                String item = (String) av.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(), item + " selected", Toast.LENGTH_SHORT).show();

                if (position == 0) {
                    //create adjustment
                    Intent i = new Intent(v.getContext(), com.example.he016.logicuniversityandroidapp.adjustmentVoucher.Activity_CreateAdjustmentVoucher.class);
                    startActivityForResult(i, 0);
                } else if (position == 1) {
                    //view adjustment
                    Intent i = new Intent(v.getContext(), com.example.he016.logicuniversityandroidapp.adjustmentVoucher.ListOfMonthlyAdjustmentActivity.class);
                    startActivityForResult(i, 1);
                } else if (position == 2) {
                    //disbursement
                    Intent i = new Intent(v.getContext(), DisbursementActivity.class);
                    startActivityForResult(i, 2);
                } else if (position == 3) {
                    //retrieval
                    Intent i = new Intent(v.getContext(), RetrievalListActivity.class);
                    startActivityForResult(i, 3);
                } else if (position == 4) {
                    //stock card
                    Intent i = new Intent(v.getContext(), ListOfCategoryActivity.class);
                    startActivityForResult(i, 4);
                } else if (position == 5) {
                    //purchase order
                    Intent i = new Intent(v.getContext(), ListOfSentPOActivity.class);
                    startActivityForResult(i, 5);
                } else if (position == 6) {
                    Intent i = new Intent(getApplicationContext(), ListOfItemsActivity.class);
                    startActivityForResult(i, 0);
                } else if (position == 7) {
                    Intent i = new Intent(v.getContext(), ListofViewRequestsActivity.class);
                    startActivityForResult(i, 1);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamburger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    }

}
