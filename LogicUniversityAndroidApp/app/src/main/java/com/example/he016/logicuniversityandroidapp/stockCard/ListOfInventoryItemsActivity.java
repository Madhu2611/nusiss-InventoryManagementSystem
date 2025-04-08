package com.example.he016.logicuniversityandroidapp.stockCard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.adapters.InventoryItemsAdapter;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Inventory;

import java.util.List;

public class ListOfInventoryItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_inventory_items);

        Intent intent=getIntent();
        //String id=intent.getExtras().getString("category");
        String id=intent.getStringExtra("category");

        new AsyncTask<String, Void, List<Inventory>>(){

            @Override
            protected List<Inventory> doInBackground(String... strings) {
                String categoryID=strings[0];
                return Inventory.ListItemsByCategory(categoryID);
            }

            @Override
            protected void onPostExecute(List<Inventory> result){
                InventoryItemsAdapter adapter = new InventoryItemsAdapter(getApplicationContext(), result);
                ListView list = (ListView) findViewById(R.id.listView2);
                list.setAdapter(adapter);

            }


        }.execute(id);


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
