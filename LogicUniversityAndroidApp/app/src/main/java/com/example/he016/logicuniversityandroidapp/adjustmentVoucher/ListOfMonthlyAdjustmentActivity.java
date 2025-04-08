package com.example.he016.logicuniversityandroidapp.adjustmentVoucher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;

import java.util.Calendar;

public class ListOfMonthlyAdjustmentActivity extends AppCompatActivity {
    ListView listView;
    String adjustment = "Adjustment";
    Spinner yearspinner, monthspinner;
    String yearSelected,monthSelected;
    int currentYear, currentMonth, year, month;
    Button submitbutton;
    int test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_monthly_adjustment);

        String[] years={"2019","2018","2017"};
        String[] months={"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};

        yearspinner= (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> yearAdapter=new ArrayAdapter<String>(ListOfMonthlyAdjustmentActivity.this, android.R.layout.simple_spinner_dropdown_item,years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearspinner.setAdapter(yearAdapter);

        monthspinner= (Spinner)findViewById(R.id.spinner3);
        final ArrayAdapter<String> monthAdapter=new ArrayAdapter<String>(ListOfMonthlyAdjustmentActivity.this, android.R.layout.simple_spinner_dropdown_item,months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthspinner.setAdapter(monthAdapter);


        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                monthSelected=monthspinner.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                yearSelected=yearspinner.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitbutton=(Button) findViewById(R.id.submitbutton);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                currentYear=calendar.get(Calendar.YEAR);
                currentMonth =calendar.get(Calendar.MONTH)+1 ;

                if(monthSelected=="January") {
                    month = 1;
                }
                if(monthSelected=="February") {
                    month = 2;
                }
                if(monthSelected=="March") {
                    month = 3;
                }
                if(monthSelected=="April") {
                    month = 4;
                }
                if(monthSelected=="May") {
                    month = 5;
                }
                if(monthSelected=="June") {
                    month = 6;
                }
                if(monthSelected=="July") {
                    month = 7;
                }
                if(monthSelected=="August") {
                    month = 8;
                }
                if(monthSelected=="September") {
                    month = 9;
                }
                if(monthSelected=="October") {
                    month = 10;
                }
                if(monthSelected=="November") {
                    month = 11;
                }
                if(monthSelected=="December") {
                    month = 12;
                }

                year=Integer.parseInt(yearSelected);
                if((year==currentYear)&&((month>currentMonth))){

                    Toast.makeText(getApplicationContext(),"Select Valid month" , Toast.LENGTH_LONG).show();
                }
                else
                {

                    String finalmonth=Integer.toString(month);
                    Intent intent = new Intent(getApplicationContext(), com.example.he016.logicuniversityandroidapp.adjustmentVoucher.MonthlyAdjustmentActivity.class);
                    intent.putExtra("year", yearSelected);
                    intent.putExtra("month", finalmonth);
                    intent.putExtra("monthselected", monthSelected);
                    startActivityForResult(intent, 123);
                }

                //Toast.makeText(getApplicationContext(),"Select Valid month" + yearSelected , Toast.LENGTH_LONG).show();

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





