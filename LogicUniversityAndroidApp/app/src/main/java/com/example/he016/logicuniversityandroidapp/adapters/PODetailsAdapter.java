package com.example.he016.logicuniversityandroidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.he016.logicuniversityandroidapp.R;
import com.example.he016.logicuniversityandroidapp.model.PurchaseOrderDetail;

import java.util.List;

public class PODetailsAdapter extends ArrayAdapter<PurchaseOrderDetail> {

    private List<PurchaseOrderDetail> items;
    int resource;

    public PODetailsAdapter(Context context, List<PurchaseOrderDetail> items) {
        super(context, R.layout.crow, items);
        this.resource = R.layout.podetailsrow;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null);
        PurchaseOrderDetail pod = items.get(position);
        if (pod != null) {
            int[] dest = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4};
            String[] src = new String[]{"itemNo", "description", "unitMeasure", "quantity"};
            for (int n = 0; n < dest.length; n++) {
                TextView txt = v.findViewById(dest[n]);
                txt.setText(pod.get(src[n]));
                txt.setTextColor(Color.BLACK);
            }
        }
        return v;
    }
}

