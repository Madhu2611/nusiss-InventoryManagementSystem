package com.example.he016.logicuniversityandroidapp.purchaseOrder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.PurchaseOrderDetail;

public class EditPOitemActivity extends AppCompatActivity {

    String id = "";
    String idd = "";
    String qty = "";
    String sts = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poitem);

        Intent intent = getIntent();
        id = intent.getStringExtra("poId");
        idd = intent.getStringExtra("itemNo");
        sts = intent.getStringExtra("status");


        new AsyncTask<String, Void, PurchaseOrderDetail>() {

            @Override
            protected PurchaseOrderDetail doInBackground(String... params) {
                return PurchaseOrderDetail.ReadItemNo(Integer.parseInt(params[0]), params[1]);
            }

            @Override
            protected void onPostExecute(PurchaseOrderDetail result) {
                show(result);
            }
        }.execute(id, idd);

        Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    EditText quantityText = (EditText) findViewById(R.id.editText1);
                    if (Integer.parseInt(quantityText.getText().toString().trim()) == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a non zero number", Toast.LENGTH_SHORT).show();
                    } else {
                        saveEdit();
                        Intent data = new Intent();
                        data.putExtra("edited", 1);
                        setResult(RESULT_OK, data); // we have finished this Activity
                        finish();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Input Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button button2 = findViewById(R.id.cancelButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(getApplicationContext(), ViewPODetailsActivity.class);
                //startActivity(intent);
                finish();

            }
        });


    }

    void show(PurchaseOrderDetail b) {
        int[] dest = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.editText1,
        };
        String[] src = new String[]{"itemNo", "description", "unitMeasure", "quantity"};

        for (int n = 0; n < dest.length; n++) {
            TextView txt = findViewById(dest[n]);
            txt.setText(b.get(src[n]));

        }
    }

    void saveEdit() {
        int[] src = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.editText1};

        String[] dest = new String[4];

        for (int n = 0; n < dest.length; n++) {

            if (n < 3) {
                TextView txt = findViewById(src[n]);
                dest[n] = txt.getText().toString();
            } else {
                EditText txt = findViewById(src[n]);
                dest[n] = txt.getText().toString();
            }
        }
        PurchaseOrderDetail b = new PurchaseOrderDetail(dest[0], dest[1], dest[2], dest[3]);

        qty = dest[3];
        new AsyncTask<String, Void, PurchaseOrderDetail>() {

            @Override
            protected PurchaseOrderDetail doInBackground(String... params) {
                PurchaseOrderDetail.saveItem(Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2]), params[3]);
                return null;
            }

        }.execute(id, idd, qty, sts);
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
