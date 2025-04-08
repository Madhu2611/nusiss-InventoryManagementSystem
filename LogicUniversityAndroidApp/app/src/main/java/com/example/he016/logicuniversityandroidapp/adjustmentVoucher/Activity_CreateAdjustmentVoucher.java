package com.example.he016.logicuniversityandroidapp.adjustmentVoucher;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.AdjustmentVoucher;
import com.example.he016.logicuniversityandroidapp.model.Category;
import com.example.he016.logicuniversityandroidapp.model.Inventory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Activity_CreateAdjustmentVoucher extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private int s_position;
    private String itemNo;
    private String[] dest = new String[6];
    boolean res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_adv);


        TextView date = (TextView) findViewById(R.id.data);
        date.setText(getCurrentDate());

        spinner1 = (Spinner) findViewById(R.id.spinner_category);
        spinner2 = (Spinner) findViewById(R.id.spinner_description);
        spinner3 = (Spinner) findViewById(R.id.spinner_reason);


        new MyAsyncTask().execute();


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String categoryId = (String) parent.getItemAtPosition(pos);
                new MyAsyncTask2().execute(categoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Activity_CreateAdjustmentVoucher.this, "You should select one category", Toast.LENGTH_SHORT).show();
            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Activity_CreateAdjustmentVoucher.this, "You should select one description", Toast.LENGTH_SHORT).show();
            }
        });


        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] reasons = getResources().getStringArray(R.array.advreason);
                Toast.makeText(Activity_CreateAdjustmentVoucher.this, "You Select:" + reasons[pos], Toast.LENGTH_SHORT).show();
                s_position = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Activity_CreateAdjustmentVoucher.this, "You should select one reason", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //populate spinner category
    class MyAsyncTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> categories = Category.ListCategories();
            return categories;
        }

        @Override
        protected void onPostExecute(List<String> categories) {

            spinner1.setAdapter(new ArrayAdapter(Activity_CreateAdjustmentVoucher.this, R.layout.my_spinner, categories));

        }

    }

    //populate spinner description
    private class MyAsyncTask2 extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            List<String> descriptions = Inventory.ListDescriptionbyCategory(params[0]);
            return descriptions;
        }

        @Override
        protected void onPostExecute(List<String> descriptions) {

            spinner2.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.my_spinner, descriptions));

        }

    }

    //save adjustmentvoucher
    private class MyAsyncTask3 extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HashMap<String, String> list = Inventory.ListitemNobyCategory(params[0]);
            itemNo = list.get(params[1]);
            AdjustmentVoucher a = new AdjustmentVoucher(dest[5], dest[0], dest[1], dest[2], dest[3], dest[4], itemNo);
            res = AdjustmentVoucher.saveAdjustmentVoucher(a);

            return res;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            if (res) {
                Intent i = new Intent(getApplicationContext(), SCLandingActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());
        String curDate = formatter.format(date);
        return curDate;
    }


    void saveCreate() {
        int[] src = new int[]{R.id.spinner_category, R.id.spinner_description, R.id.spinner_reason, R.id.editText_quantity, R.id.data};


        //get data
        for (int n = 0; n < 3; n++) {

            Spinner s = findViewById(src[n]);
            dest[n] = s.getSelectedItem().toString();

        }
        EditText txt1 = findViewById(src[3]);
        TextView txt2 = findViewById(src[4]);

        dest[3] = txt1.getText().toString().trim();
        dest[4] = txt2.getText().toString();

         try {
             if (!TextUtils.isEmpty(dest[3]) && !(Integer.parseInt(dest[3].trim())==0)) {


                 //switch qty
                 if (s_position == 0 || s_position == 1) {
                     dest[3] = Integer.toString(-Math.abs(Integer.parseInt(dest[3])));

                 }
                 if (s_position == 2) {
                     dest[3] = String.valueOf(Math.abs(Integer.parseInt(dest[3])));
                 }


                 //AdjustentVoucher.save(a)
                 new MyAsyncTask3().execute(dest[0], dest[1]);


             } else {
                 Toast.makeText(Activity_CreateAdjustmentVoucher.this, "Please enter a non zero number", Toast.LENGTH_SHORT).show();
             }
         }
         catch (Exception e)
         {
             Toast.makeText(Activity_CreateAdjustmentVoucher.this, "Input Error", Toast.LENGTH_SHORT).show();
         }
    }

    public void click(View v) {
        // TODO Auto-generated method stub

        //get id
        int id = v.getId();
        switch (id) {
            case R.id.advsubmitbutton:
                saveCreate();

                break;
            case R.id.advcancelbutton:
                Intent intent = new Intent(getApplicationContext(), SCLandingActivity.class);
                startActivity(intent);

                break;


            default:
                break;
        }
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
