package com.example.he016.logicuniversityandroidapp.retrieval;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Retrieval;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RetrievalListActivity extends AppCompatActivity {

    private ListView ListView_Retrieval;
    private TextView TextView_RetrievalDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieval_list);

        ListView_Retrieval = (ListView) findViewById(R.id.ListView_Retrieval);
        TextView_RetrievalDate = (TextView) findViewById(R.id.TextView_RetrievalDate);

        TextView_RetrievalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RetrievalListActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = year + "-" + month + "-" + day;
                TextView_RetrievalDate.setText(date);

                readDate(TextView_RetrievalDate.getText().toString());
            }
        };

        if (getIntent().hasExtra("DueDate")) {
            strDefult = getIntent().getStringExtra("DueDate");
            if (strDefult == null) {
                strDefult = getYesterdayDateString();
            } else {
                String[] parts = strDefult.split("T");
                strDefult = parts[0];
            }
        } else {
            strDefult = getYesterdayDateString();
        }
        TextView_RetrievalDate.setText(strDefult);
        readDate(TextView_RetrievalDate.getText().toString());
    }

    // set the default retrieval date as yesterday
    public String strDefult;

    private String getYesterdayDateString() {
        // String strDefult = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    private void readDate(String dueDate) {
        new AsyncTask<String, Void, List<Retrieval>>() {
            @Override
            protected List<Retrieval> doInBackground(String... strings) {
                // user service class
                List<Retrieval> retrievalList = RetrievalService.ViewRetrievalGoods(strings[0]);
                return retrievalList;
            }

            @Override
            protected void onPostExecute(List<Retrieval> retrievals) {

                // display in the list view here...
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        getApplicationContext(),
                        retrievals,
                        R.layout.row_retrieval_list,
                        new String[]{"description", "quantityTotalNeed"},
                        new int[]{R.id.textView_retrieval_row1, R.id.textView_retrieval_row2});

                // set the adapter
                ListView_Retrieval.setAdapter(simpleAdapter);

                // set the item click event here...
                ListView_Retrieval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Retrieval s = (Retrieval) parent.getAdapter().getItem(position);

                        // another intent here...
                        Intent intent = new Intent(getApplicationContext(), com.example.he016.logicuniversityandroidapp.retrieval.RetrievalDetailActivity.class);

                        intent.putExtra("itemNo", s.get("itemNo"));
                        intent.putExtra("DueDate", s.get("DueDate"));

                        //startActivity(intent);
                        startActivityForResult(intent, 1);
                    }
                });
            }
        }.execute(dueDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    Boolean bRes = data.getBooleanExtra("result", false);
                    if (bRes == false) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        strDefult = data.getStringExtra("DueDate");
                        Toast.makeText(getApplicationContext(), "Succeeded", Toast.LENGTH_SHORT).show();
                        Intent refresh = new Intent(this, RetrievalListActivity.class);
                        refresh.putExtra("DueDate", strDefult);
                        startActivity(refresh);
                        this.finish();
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
