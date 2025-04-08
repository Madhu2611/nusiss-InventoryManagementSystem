package com.example.he016.logicuniversityandroidapp.purchaseOrder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.adapters.POAdapter;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.PurchaseOrder;

import java.util.List;

public class ListOfSentPOActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_sent_po);

        new AsyncTask<Void, Void, List<PurchaseOrder>>() {
            @Override
            protected List<PurchaseOrder> doInBackground(Void... voids) {
                List<PurchaseOrder> pos = PurchaseOrder.ReadPurchaseOrder();
                return pos;
            }

            @Override
            protected void onPostExecute(List<PurchaseOrder> pos) {
                POAdapter poAdapter = new POAdapter(getApplicationContext(), pos);
                ListView list = (ListView) findViewById(R.id.listViewPOSent);
                list.setAdapter(poAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PurchaseOrder po = (PurchaseOrder) parent.getAdapter().getItem(position);
                        Intent i = new Intent(getApplicationContext(), ViewPODetailsActivity.class);
                        i.putExtra("poId", po.get("poId"));
                        i.putExtra("status", po.get("status"));
                        i.putExtra("orderDate", po.get("orderDate"));
                        i.putExtra("deliveryDate", po.get("deliveryDate"));
                        startActivity(i);
                    }
                });
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamburger, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hamburgerHome: {
                if(JSONParser.userRole.equals("SM") || JSONParser.userRole.equals("SS")){
                    Intent i = new Intent(getApplicationContext(), SMSSLandingActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    return true;}
                else if(JSONParser.userRole.equals("SC")){
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
