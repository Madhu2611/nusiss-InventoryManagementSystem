package com.example.he016.logicuniversityandroidapp.disbursementList;

import android.content.Intent;
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

public class DisbursementDetailActivity extends AppCompatActivity {

    TextView tv_itemName;
    TextView tv_qtyPrepared;
    EditText et_disbursement_qtyActual;
    EditText et_disbursement_qtyDamaged;
    EditText et_disbursement_qtyMissing;
    Button button_disbursement_detail_submit;
    Button button_disbursement_detail_cancel;
    String strItemNo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_detail);

        // get all items here
        tv_itemName = findViewById(R.id.tv_itemName);
        tv_qtyPrepared = findViewById(R.id.tv_qtyPrepared);
        et_disbursement_qtyActual = findViewById(R.id.et_disbursement_qtyActual);
        et_disbursement_qtyDamaged = findViewById(R.id.et_disbursement_qtyDamaged);
        et_disbursement_qtyMissing = findViewById(R.id.et_disbursement_qtyMissing);
        button_disbursement_detail_submit = findViewById(R.id.button_disbursement_detail_submit);
        button_disbursement_detail_cancel = findViewById(R.id.button_disbursement_detail_cancel);

        // set intent info here...
        strItemNo = getIntent().getStringExtra("itemNo");
        tv_itemName.setText(getIntent().getStringExtra("description"));
        tv_qtyPrepared.setText(getIntent().getStringExtra("qtyRequired"));
        et_disbursement_qtyActual.setText(getIntent().getStringExtra("qtyActual"));
        et_disbursement_qtyDamaged.setText(getIntent().getStringExtra("qtyDamaged"));
        et_disbursement_qtyMissing.setText(getIntent().getStringExtra("qtyMissing"));

        //button_disbursement_detail_submit
        button_disbursement_detail_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_qtyActual = et_disbursement_qtyActual.getText().toString();
                String str_qtyDamaged = et_disbursement_qtyDamaged.getText().toString();
                String str_qtyMissing = et_disbursement_qtyMissing.getText().toString();

                String qtyPrepared = tv_qtyPrepared.getText().toString();

                if (str_qtyActual.equals("") || str_qtyDamaged.equals("") || str_qtyMissing.equals("")) {
                    // Display some message
                    Toast.makeText(getApplicationContext(), "Please input number", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int iQtyPrepared = Integer.parseInt(qtyPrepared);
                        int iQtyActual = Integer.parseInt(str_qtyActual);
                        int iQtyDamaged = Integer.parseInt(str_qtyDamaged);
                        int iQtyMissing = Integer.parseInt(str_qtyMissing);

                        if (iQtyPrepared == (iQtyActual + iQtyDamaged + iQtyMissing)
                                && iQtyPrepared >= iQtyActual
                                && iQtyPrepared >= iQtyDamaged
                                && iQtyPrepared >= iQtyMissing
                                ) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("itemNo", strItemNo);
                            resultIntent.putExtra("qtyActual", str_qtyActual);
                            resultIntent.putExtra("qtyDamaged", str_qtyDamaged);
                            resultIntent.putExtra("qtyMissing", str_qtyMissing);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please match the prepared quantity", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please input number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button_disbursement_detail_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });

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
                startActivity(i);
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
