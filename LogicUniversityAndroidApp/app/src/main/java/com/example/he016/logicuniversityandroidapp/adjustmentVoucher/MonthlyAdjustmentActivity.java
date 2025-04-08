package com.example.he016.logicuniversityandroidapp.adjustmentVoucher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.adapters.AdjustmentAdapter;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.AdjustmentVoucher;


import java.util.Calendar;
import java.util.List;

public class MonthlyAdjustmentActivity extends AppCompatActivity {

    ListView listView;
    int lastdate, currentdate;
    float[] amountList;
    String userRole;
    //static Integer[] amountValue;
    int year, month;
    //String[] adjust = new String[100];
    final static String value = "250";
    static List<AdjustmentVoucher> adjustmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_adjustment_ui);

        Intent intent = getIntent();
        final String yearextra = intent.getStringExtra("year");
        final String monthextra = intent.getStringExtra("month");
        final String monthselected = intent.getStringExtra("monthselected");

        year = Integer.parseInt(yearextra);
        month = Integer.parseInt(monthextra);


        TextView textView = (TextView) findViewById(R.id.matextView);
        String text = monthselected + " " + yearextra + " " + "Adjustment";
        textView.setText(text);

        Calendar calendar = Calendar.getInstance();
        int nodays = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, nodays);

        lastdate = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        currentdate = cal.get(Calendar.DAY_OF_MONTH);


        new AsyncTask<String, Void, List<AdjustmentVoucher>>() {


            @Override
            protected List<AdjustmentVoucher> doInBackground(String... strings) {

                List<AdjustmentVoucher> adList = AdjustmentVoucher.FindGeneralAdjustmentVoucher(year, month);
                return adList;
            }

            protected void onPostExecute(List<AdjustmentVoucher> result) {
                AdjustmentAdapter adapter = new AdjustmentAdapter(getApplicationContext(), result);
                ListView list = (ListView) findViewById(R.id.malistView);
                list.setAdapter(adapter);
            }
        }.execute();
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
