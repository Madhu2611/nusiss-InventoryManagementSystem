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

import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.collectionPointActivity.ChangeCollectionPointActivity;
import com.example.he016.logicuniversityandroidapp.requestActivity.ListOfItemsActivity;
import com.example.he016.logicuniversityandroidapp.requestActivity.ListofViewRequestsActivity;
import com.example.he016.logicuniversityandroidapp.requestActivity.ViewRequestDetailActivity;

public class StaffLandingActivity extends AppCompatActivity {
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stafflanding);
        listView= (ListView)findViewById(R.id.listView1);
        String []values = {"Search Catalogue", "View Request"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.rowlandingpage, R.id.textView1, values);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long id) {
                String item = (String) av.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(), item + " selected",
                        Toast.LENGTH_SHORT).show();

                if(position==0)
                {
                    Intent i= new Intent(getApplicationContext(),ListOfItemsActivity.class);
                    startActivityForResult(i,0);
                }

                if(position==1)
                {
                    Intent i= new Intent(v.getContext(),ListofViewRequestsActivity.class);
                    startActivityForResult(i,1);
                }

/*                if(position==2)
                {
                    Intent i= new Intent(v.getContext(),ViewRequestDetailActivity.class);
                    startActivityForResult(i,2);
                }*/
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
                Intent i = new Intent(getApplicationContext(), StaffLandingActivity.class);
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

