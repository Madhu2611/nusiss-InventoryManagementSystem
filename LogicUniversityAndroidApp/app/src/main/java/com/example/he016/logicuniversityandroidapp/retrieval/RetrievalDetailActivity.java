package com.example.he016.logicuniversityandroidapp.retrieval;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.he016.logicuniversityandroidapp.model.Retrieval;

public class RetrievalDetailActivity extends AppCompatActivity {

    TextView textview_retrieval_description;
    TextView textview_retrieval_location;
    TextView textview_retrieval_balance;
    TextView textview_retrieval_quantityTotalNeed;

    EditText editText_retrieval_qty;
    EditText editText_retrieval_damaged;
    EditText editText_retrieval_missing;

    Button button_retrieval_detail_submit;
    Button button_retrieval_detail_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieval_detail);

        textview_retrieval_description = findViewById(R.id.textview_retrieval_description);
        textview_retrieval_location = findViewById(R.id.textview_retrieval_location);
        textview_retrieval_balance = findViewById(R.id.textview_retrieval_balance);
        textview_retrieval_quantityTotalNeed = findViewById(R.id.textview_retrieval_quantityTotalNeed);

        editText_retrieval_qty = findViewById(R.id.editText_retrieval_qty);
        editText_retrieval_damaged = findViewById(R.id.editText_retrieval_damage);
        editText_retrieval_missing = findViewById(R.id.editText_retrieval_missing);

        button_retrieval_detail_submit = findViewById(R.id.button_retrieval_detail_submit);
        button_retrieval_detail_cancel = findViewById(R.id.button_retrieval_detail_cancel);

        String strItemNo = getIntent().getStringExtra("itemNo");//getIntent().getExtras().getString("itemNo");
        String strDueDate = getIntent().getStringExtra("DueDate");

        button_retrieval_detail_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        new AsyncTask<String, Void, Retrieval>() {
            @Override
            protected Retrieval doInBackground(String... strings) {
                Retrieval retrieval = RetrievalService.ViewRetrievalGood(strings[0], strings[1]);
                return retrieval;
            }

            @Override
            protected void onPostExecute(Retrieval retrieval) {
                if (retrieval != null) {
                    textview_retrieval_description.setText("Description: " + retrieval.get("description"));
                    textview_retrieval_location.setText("Shelf: " + retrieval.get("location"));
                    textview_retrieval_balance.setText("Balance: " + retrieval.get("balance"));
                    textview_retrieval_quantityTotalNeed.setText("Quantity Need: " + retrieval.get("quantityTotalNeed"));

                    editText_retrieval_qty.setText(retrieval.get("quantityRetrieval"));
                    editText_retrieval_damaged.setText(retrieval.get("quantityInstoreDamaged"));
                    editText_retrieval_missing.setText(retrieval.get("quantityInstoreMissing"));

                    final String DueDate = retrieval.get("DueDate");
                    final String itemNo = retrieval.get("itemNo");
                    final String quantityNeed = retrieval.get("quantityTotalNeed");

                    button_retrieval_detail_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String quantityRetrieval = editText_retrieval_qty.getText().toString();
                            String quantityInstoreDamaged = editText_retrieval_damaged.getText().toString();
                            String quantityInstoreMissing = editText_retrieval_missing.getText().toString();

                            if (quantityRetrieval.equals("") || quantityInstoreDamaged.equals("") || quantityInstoreMissing.equals("")) {
                                // Display some message
                                Toast.makeText(getApplicationContext(), "Please input number", Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(quantityRetrieval) == 0) {
                                Toast.makeText(getApplicationContext(), "Please enter a non zero number for retrieval quantity", Toast.LENGTH_SHORT).show();
                            } else {
                                // Async Task here
                                new AsyncTask<String, Void, Boolean>() {
                                    @Override
                                    protected Boolean doInBackground(String... strings) {
                                        // create a json object here...
                                        Boolean bResult = RetrievalService.AllocateGoods(strings[0], strings[1], strings[2], strings[3], strings[4]);
                                        return bResult;
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean aBoolean) {
                                        super.onPostExecute(aBoolean);

                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("result", aBoolean);
                                        resultIntent.putExtra("DueDate", DueDate);
                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    }
                                }.execute(DueDate, itemNo, quantityRetrieval, quantityInstoreDamaged, quantityInstoreMissing);
                            }
                        }
                    });
                }
            }
        }.execute(strItemNo, strDueDate);
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
