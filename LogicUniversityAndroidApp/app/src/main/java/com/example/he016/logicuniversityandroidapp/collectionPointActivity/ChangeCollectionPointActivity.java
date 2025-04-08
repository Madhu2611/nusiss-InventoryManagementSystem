package com.example.he016.logicuniversityandroidapp.collectionPointActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DRLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.CollectionPoint;
import com.example.he016.logicuniversityandroidapp.model.Department;

import java.util.List;

public class ChangeCollectionPointActivity extends AppCompatActivity {

    Button submit;
    CollectionPoint selectedCollectionPoint;
    Spinner spinnerCollectionPoint;
    TextView currentCollectionPoint;
    int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_collectionpoint);
        submit = (Button) findViewById(R.id.submitBTN);
        spinnerCollectionPoint = (Spinner) findViewById(R.id.spinner);
        currentCollectionPoint = (TextView) findViewById(R.id.currentCollectionPoint);

        getDepartmentDetails();

        // populate spinner
        new AsyncTask<Void, Void, List<CollectionPoint>>() {
            @Override
            protected List<CollectionPoint> doInBackground(Void... voids) {
                List<CollectionPoint> collectionPoints = CollectionPoint.ListAllCollectionPoints();
                return collectionPoints;
            }

            @Override
            protected void onPostExecute(List<CollectionPoint> collectionPoints) {

                // Populate spinner

                //spinnerCollectionPoint.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, collectionPoints));

                ArrayAdapter<CollectionPoint> adp1 = new ArrayAdapter<CollectionPoint>(ChangeCollectionPointActivity.this,
                        R.layout.spinner_item, collectionPoints);
                adp1.setDropDownViewResource(R.layout.spinner_item);
                spinnerCollectionPoint.setAdapter(adp1);
                spinnerCollectionPoint.setSelection(spinnerPosition - 1);
            }
        }.execute();

        spinnerCollectionPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCollectionPoint = (CollectionPoint) spinnerCollectionPoint.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(selectedCollectionPoint.collectionPointId);
                Toast.makeText(getApplicationContext(), "Changes made succesfully",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // Update DR and collection point
    protected void update(String collectionPointId) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... Params) {
                Department.saveCollectionPoint(Params[0]);
                return null;
            }
        }.execute(collectionPointId);
    }

    // Get Department Details
    protected void getDepartmentDetails() {
        new AsyncTask<Void, Void, Department>() {
            @Override
            protected Department doInBackground(Void... voids) {
                Department dep = Department.showDepartmentDetails();
                return dep;
            }

            @Override
            protected void onPostExecute(Department dep) {
                currentCollectionPoint.setText(dep.get("collectionPointDescription"));
                spinnerPosition = Integer.parseInt(dep.get("collectionPointId"));
            }
        }.execute();
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
                Intent i = new Intent(getApplicationContext(), DRLandingActivity.class);
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
