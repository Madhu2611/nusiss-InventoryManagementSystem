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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DHLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.CollectionPoint;
import com.example.he016.logicuniversityandroidapp.model.Department;
import com.example.he016.logicuniversityandroidapp.model.Staff;

import java.util.ArrayList;
import java.util.List;

public class ChangeDRCollectionPointActivity extends AppCompatActivity {

    EditText empname;
    Button checkNames, submit;
    CollectionPoint selectedCollectionPoint;
    String name, drName, drId;
    ArrayList<Staff> staffNames;
    ArrayList<Staff> fillteredStaffNames;
    Spinner spinnerCollectionPoint;
    Boolean checked = false;
    TextView currentRep, currentCollectionPoint;
    int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changedr_collectionpoint);
        empname = (EditText) findViewById(R.id.empName);
        checkNames = (Button) findViewById(R.id.checkNameBTN);
        submit = (Button) findViewById(R.id.submitBTN);
        spinnerCollectionPoint = (Spinner) findViewById(R.id.spinner);
        currentRep = (TextView) findViewById(R.id.currentRep);
        currentCollectionPoint = (TextView) findViewById(R.id.currentCollectionPoint);

        searchStaff();

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

                ArrayAdapter<CollectionPoint> adp1 = new ArrayAdapter<CollectionPoint>(ChangeDRCollectionPointActivity.this,
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

        // Checking names
        checkNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = empname.getText().toString();
                if (name.isEmpty()) {
                    Intent intent = new Intent(ChangeDRCollectionPointActivity.this, ListDRNamesActivity.class);
                    intent.putExtra("staffList", staffNames);
                    startActivityForResult(intent, 147);
                } else {
                    fillteredStaffNames = new ArrayList<Staff>();
                    for (int j = 0; j < staffNames.size(); j++) {
                        if (staffNames.get(j).staffName.toUpperCase().contains(name.toUpperCase())) {
                            fillteredStaffNames.add(staffNames.get(j));
                        }
                    }
                    Intent intent1 = new Intent(ChangeDRCollectionPointActivity.this, ListDRNamesActivity.class);
                    intent1.putExtra("fillteredStaffNames", fillteredStaffNames);
                    startActivityForResult(intent1, 147);
                }

            }
        });

        // Submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked == true) {
                    update(selectedCollectionPoint.collectionPointId, drId);
                    finish();
                } else {
                    //Toast.makeText(getApplicationContext(), "Please check the name and choose the staff before you submit",
                    //  Toast.LENGTH_SHORT).show();
                    update(selectedCollectionPoint.collectionPointId, currentRep.getText().toString());
                    finish();
                }
                Toast.makeText(getApplicationContext(), "Changes made succesfully",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update DR and collection point
    protected void update(String collectionPointId, String staffDR) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... Params) {
                Department.saveCollectionPoint(Params[0]);
                Department.saveDepartmentRep(Params[1]);
                return null;
            }
        }.execute(collectionPointId, staffDR);
    }

    // Result of check name
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent drIntent) {
        if (resultCode == RESULT_OK && requestCode == 147) {
            if (drIntent.hasExtra("staffName") && drIntent.hasExtra("staffId")) {
                drName = drIntent.getStringExtra("staffName");
                drId = drIntent.getStringExtra("staffId");
                empname.setText(drName);
                name = empname.toString();
                checked = true;
            }
        }
    }

    // Search staff
    protected void searchStaff() {
        staffNames = new ArrayList<>();
        new AsyncTask<String, Void, List<Staff>>() {
            @Override
            protected List<Staff> doInBackground(String... params) {
                List<Staff> staff = Staff.ListStaffByDepartment();
                return staff;
            }

            @Override
            protected void onPostExecute(List<Staff> staff) {
                staffNames.addAll(staff);
            }
        }.execute();
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
                currentRep.setText(dep.get("DRName"));
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
