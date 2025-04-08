package com.example.he016.logicuniversityandroidapp.requestActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.CartSession;
import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.landingActivity.DRLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.StaffLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.Request;
import com.example.he016.logicuniversityandroidapp.model.RequestDetail;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewRequestDetailActivity extends AppCompatActivity {
    ArrayList<RequestDetail> cartrds = new ArrayList<RequestDetail>();
    Button bAdd;
    Button bSubmit;
    String stat = "";
    private CartSession cartSession;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_detail);
        cartSession = new CartSession(getApplicationContext());
        Intent intent = getIntent();
        stat = intent.getStringExtra("status");
        bAdd = (Button) findViewById(R.id.addbutton);
        bSubmit = (Button) findViewById(R.id.submitbutton);
        list = (ListView) findViewById(R.id.listView2);


        //if intent came from clicking the list view in the MainActivity
        if (intent.hasExtra("requestid")) {
            bSubmit.setVisibility(View.GONE);
            final String rid = intent.getExtras().getString("requestid");
            TextView txt = findViewById(R.id.textView2);
            txt.setText(rid);
            if (!"pending".equals(stat)) {
                bAdd.setVisibility(View.GONE);
            }

            // on click event
            bAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ListOfItemsActivity.class);
                    intent.putExtra("requestid", rid);
                    startActivityForResult(intent, 456);

                }
            });

            new AsyncTask<Integer, Void, List<RequestDetail>>() {
                @Override
                protected List<RequestDetail> doInBackground(Integer... params) {
                    return RequestDetail.RetrieveRequestDetails(params[0]);
                }

                @Override
                protected void onPostExecute(List<RequestDetail> rds) {

                    list.setAdapter(new SimpleAdapter
                            (getApplicationContext(), rds, R.layout.rowviewrequestdetail, new String[]{"category", "description", "quantityNeed", "stdPrice"},
                                    new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4}));
                    if ("pending".equals(stat)) {
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                RequestDetail selected = (RequestDetail) parent.getAdapter().getItem(position);
                                Intent intent = new Intent(getApplicationContext(), EditRequestDetailActivity.class);
                                intent.putExtra("requestId", selected.get("requestId"));
                                intent.putExtra("index", selected.get("index"));
                                intent.putExtra("itemNo", selected.get("itemNo"));
                                intent.putExtra("description", selected.get("description"));
                                intent.putExtra("quantityNeed", selected.get("quantityNeed"));
                                startActivityForResult(intent, 123);
                            }
                        });
                    }
                }
            }.execute(Integer.parseInt(rid));
        }/*
        else{

            TextView txt2 = findViewById(R.id.textView2);
            txt2.setVisibility(View.INVISIBLE);
            TextView txt = findViewById(R.id.textView);
            txt.setVisibility(View.INVISIBLE);

            bAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ListOfItemsActivity.class);
                    startActivityForResult(intent, 456);
                    RequestDetail rd = cartSession.getRequestDetail();

                    boolean exist = false;
                    for(RequestDetail r: cartrds) {
                        if (rd.get("itemNo") == r.get("itemNo")) {
                            exist=true;
                            Toast t2 = Toast.makeText(getBaseContext(), "Item already existed in the cart", Toast.LENGTH_SHORT);
                            t2.show();
                            break;
                        }
                        }

                        if(exist==false){
                            cartrds.add(rd);
                        }
                    cartSession.saveCartArrayList(cartrds,"key");

                    }
            });

            cartrds = cartSession.getCartArrayList("key");
            ListView list = (ListView) findViewById(R.id.listView2);
            list.setAdapter(new SimpleAdapter
                    (getApplicationContext(), cartrds, R.layout.rowlistofviewrequests, new String[]{"category", "description", "quantityNeed", "stdPrice"},
                            new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4}));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RequestDetail selected = (RequestDetail) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(getApplicationContext(), EditRequestDetailActivity.class);
                    intent.putExtra("index", selected.get("index"));
                    intent.putExtra("itemNo", selected.get("itemNo"));
                    intent.putExtra("description", selected.get("description"));
                    intent.putExtra("quantityNeed", selected.get("quantityNeed"));
                    startActivityForResult(intent, 123);
                }
            });

            // on click event
            bSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String today = DateFormat.getDateInstance().format(new Date());
                    //get staffid and departmentid
                    Request request = new Request(null, "3","ENGL" , today, "pending", "Antonio Moreno");

                    //Converting request and request detail to request view model
                    //List<RequestDetail> requestView = RequestView.convertToRequestView(request, (List<RequestDetail>) cartrds);
                    //how to save all the request details in the request id
                    new AsyncTask<List<RequestDetail>, Void, Void>() {
                        @Override
                        protected Void doInBackground(List<RequestDetail>... params) {
                            RequestDetail.raiseNewRequest(params[0]);
                            return null;
                        }
                    }.execute(requestView);

                    Toast t = Toast.makeText(getBaseContext(), "Your request has been sent to the Department Head", Toast.LENGTH_SHORT);
                    t.show();
                }
            });
        }*/
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK && requestCode == 123) {
            if (data.hasExtra("edited")) {
                int result = data.getExtras().getInt("edited");
                if (result == 1) {
                    recreate();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == 456) {
            if (data.hasExtra("edited")) {
                int result = data.getExtras().getInt("edited");
                if (result == 1) {
                    recreate();
                }
            }
        }

    }

    // Hamburger Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamburger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (JSONParser.userRole.equals("SC")) {
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
        } else if (JSONParser.userRole.equals("SS")) {
            switch (item.getItemId()) {
                case R.id.hamburgerHome: {

                    if (JSONParser.userRole.equals("SM") || JSONParser.userRole.equals("SS")) {
                        Intent i = new Intent(getApplicationContext(), SMSSLandingActivity.class);
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
        } else {
            switch (item.getItemId()) {
                case R.id.hamburgerHome: {
                    Intent i;
                    if (JSONParser.userRole.equals("DR")) {
                        i = new Intent(getApplicationContext(), DRLandingActivity.class);
                    } else {
                        i = new Intent(getApplicationContext(), StaffLandingActivity.class);
                    }
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
}








