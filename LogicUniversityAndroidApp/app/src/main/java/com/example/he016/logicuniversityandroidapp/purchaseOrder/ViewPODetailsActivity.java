package com.example.he016.logicuniversityandroidapp.purchaseOrder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.LoginActivity;
import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.adapters.PODetailsAdapter;
import com.example.he016.logicuniversityandroidapp.landingActivity.SCLandingActivity;
import com.example.he016.logicuniversityandroidapp.landingActivity.SMSSLandingActivity;
import com.example.he016.logicuniversityandroidapp.model.PurchaseOrderDetail;

import java.util.List;

public class ViewPODetailsActivity extends AppCompatActivity {
    String txt = "";
    String sts = "";
    String ordDate = "";
    String delDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_po_details);

        Intent intent = getIntent();
        sts = intent.getStringExtra("status");
        ordDate = intent.getStringExtra("orderDate");
        delDate = intent.getStringExtra("deliveryDate");
        txt = intent.getStringExtra("poId");

        final TextView textview = findViewById(R.id.textView);
        textview.setText(txt);

        new AsyncTask<String, Void, List<PurchaseOrderDetail>>() {

            @Override
            protected List<PurchaseOrderDetail> doInBackground(String... strings) {
                String poId = strings[0];
                return PurchaseOrderDetail.ListItemsByPOID(Integer.parseInt(poId));
            }


            @Override
            protected void onPostExecute(List<PurchaseOrderDetail> result) {
                PODetailsAdapter adapter = new PODetailsAdapter(getApplicationContext(), result);
                ListView list = (ListView) findViewById(R.id.listView1);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PurchaseOrderDetail selected = (PurchaseOrderDetail) parent.getAdapter().getItem(position);
                        Intent intent = new Intent(getApplicationContext(), EditPOitemActivity.class);
                        intent.putExtra("poId", selected.get("poId"));
                        intent.putExtra("itemNo", selected.get("itemNo"));
                        intent.putExtra("status", sts);
                        startActivityForResult(intent, 123);
                    }
                });
            }
        }.execute(txt);

        registerForContextMenu(textview);

        Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePOStatus();
                Toast.makeText(ViewPODetailsActivity.this, getString(R.string.saveidlabel), Toast.LENGTH_SHORT).show();
                //finish();
                Intent intent1 = new Intent(getApplicationContext(), ListOfSentPOActivity.class);
                startActivity(intent1);
            }
        });

        Button button2 = findViewById(R.id.cancelButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewPODetailsActivity.this, getString(R.string.noidlabel), Toast.LENGTH_SHORT).show();
                //finish();
                Intent intent1 = new Intent(getApplicationContext(), ListOfSentPOActivity.class);
                startActivity(intent1);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 123) {
            if (data.hasExtra("edited")) {
                int result = data.getExtras().getInt("edited");
                if (result == 1) {
                    recreate();
                }
            }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pocontext, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context1:
                makeAlertDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    void makeAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Update PO status")
                .setMessage("Are you sure you want to set Po status to 'closed'?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        savePOStatus();
                        Toast.makeText(ViewPODetailsActivity.this, getString(R.string.saveidlabel), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ViewPODetailsActivity.this, getString(R.string.noidlabel), Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    void savePOStatus() {

        TextView txt = findViewById(R.id.textView);
        String dest = txt.getText().toString();

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                List<PurchaseOrderDetail> purchaseOrderDetails = PurchaseOrderDetail.ListItemsByPOID(Integer.parseInt(params[0]));
                PurchaseOrderDetail.savePOList(purchaseOrderDetails, ordDate, delDate, Integer.parseInt(params[0]));
                return null;
            }

        }.execute(dest);
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

