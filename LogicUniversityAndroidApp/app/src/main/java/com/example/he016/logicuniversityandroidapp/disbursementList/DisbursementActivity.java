package com.example.he016.logicuniversityandroidapp.disbursementList;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Disbursement;
import com.example.he016.logicuniversityandroidapp.model.MyDepartment;

import java.util.List;

public class DisbursementActivity extends AppCompatActivity {

    Spinner spinner_myDepartments;
    TextView textView_departmentName;
    TextView textView_departmentRepName;
    TextView textView_collectionPointDescription;
    TextView textView_departmentPhone;
    ListView ListView_disbursementList;
    List<Disbursement> myDList;
    Button btn_disbursement_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement);

        spinner_myDepartments = findViewById(R.id.spinner_myDepartments);

        textView_departmentName = findViewById(R.id.textView_departmentName);
        textView_departmentRepName = findViewById(R.id.textView_departmentRepName);
        textView_collectionPointDescription = findViewById(R.id.textView_collectionPointDescription);
        textView_departmentPhone = findViewById(R.id.textView_departmentPhone);

        ListView_disbursementList = findViewById(R.id.ListView_disbursementList);

        btn_disbursement_submit = findViewById(R.id.btn_disbursement_submit);

        populateSpinnerDepartment();

        btn_disbursement_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<List<Disbursement>, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(List<Disbursement>... lists) {
                        Boolean bRes = DisbursementService.AcceptDisbursementList(lists[0]);

                        return bRes;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        //super.onPostExecute(aBoolean);
                        if (aBoolean) {
                            Toast.makeText(getApplicationContext(), "Transaction Succeeded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Transaction Failed", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                        startActivity(getIntent());
                    }
                }.execute(myDList);
            }
        });
    }

    public void populateSpinnerDepartment() {
        new AsyncTask<Void, Void, List<MyDepartment>>() {
            @Override
            protected List<MyDepartment> doInBackground(Void... voids) {

                List<MyDepartment> deps = DisbursementService.GetDepartments();
                return deps;
            }

            @Override
            protected void onPostExecute(List<MyDepartment> myDepartments) {
                //super.onPostExecute(myDepartments);

                // bind to the drop down list
                ArrayAdapter depAdapter = new ArrayAdapter(getApplicationContext(), R.layout.my_spinner, myDepartments);
                spinner_myDepartments.setAdapter(depAdapter);

                spinner_myDepartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // **do something here...
                        MyDepartment depTemp = (MyDepartment) parent.getAdapter().getItem(position);

                        // **change the textview and listview

                        if (depTemp != null) {
                            textView_departmentName.setText(depTemp.departmentName);
                            textView_departmentRepName.setText(depTemp.departmentRepName);
                            textView_collectionPointDescription.setText(depTemp.collectionPointDescription);
                            textView_departmentPhone.setText(depTemp.departmentPhone);

                            new AsyncTask<String, Void, List<Disbursement>>() {
                                @Override
                                protected List<Disbursement> doInBackground(String... strings) {
                                    List<Disbursement> disbursements = DisbursementService.ViewDisbursementList(strings[0]);
                                    return disbursements;
                                }

                                @Override
                                protected void onPostExecute(List<Disbursement> disbursements) {
                                    //super.onPostExecute(disbursements);
                                    myDList = disbursements;
                                    updateListView(myDList);
                                }
                            }.execute(depTemp.departmentId);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

        }.execute();

    }

    private void updateListView(List<Disbursement> disbursements) {
        String[] from = new String[]{
                "description",
                "qtyActual",
                "qtyDamaged",
                "qtyMissing"};
        int[] to = new int[]{
                R.id.textView_disbursement_row_itemName,
                R.id.textView_disbursement_row_qtyActual,
                R.id.textView_disbursement_row_qtyDamaged,
                R.id.textView_disbursement_qtyMissing};

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getApplicationContext(),
                disbursements,
                R.layout.row_disbursement_list,
                from,
                to);

        ListView_disbursementList.setAdapter(simpleAdapter);
        ListView_disbursementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Disbursement s = (Disbursement) parent.getAdapter().getItem(position);

                // another intent here...
                Intent intent = new Intent(getApplicationContext(), DisbursementDetailActivity.class);

                intent.putExtra("itemNo", s.get("itemNo"));
                intent.putExtra("description", s.get("description"));
                intent.putExtra("qtyRequired", s.get("qtyRequired"));
                intent.putExtra("qtyActual", s.get("qtyActual"));
                intent.putExtra("qtyDamaged", s.get("qtyDamaged"));
                intent.putExtra("qtyMissing", s.get("qtyMissing"));

                //startActivity(intent);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            switch (resultCode) {
                case RESULT_OK:
                    String str_itemNo = data.getStringExtra("itemNo");
                    String str_qtyActual = data.getStringExtra("qtyActual");
                    String str_qtyDamaged = data.getStringExtra("qtyDamaged");
                    String str_qtyMissing = data.getStringExtra("qtyMissing");

                    if (myDList != null) {
                        for (Disbursement d : myDList) {
                            if (d.get("itemNo").equals(str_itemNo)) {
                                d.put("qtyActual", str_qtyActual);
                                d.put("qtyDamaged", str_qtyDamaged);
                                d.put("qtyMissing", str_qtyMissing);
                                break;
                            }
                        }
                        updateListView(myDList);
                        Toast.makeText(getApplicationContext(), "Succeeded", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
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
