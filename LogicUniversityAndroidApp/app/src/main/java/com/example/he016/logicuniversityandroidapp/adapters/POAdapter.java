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
import com.example.he016.logicuniversityandroidapp.model.PurchaseOrder;

import java.util.List;

public class POAdapter extends ArrayAdapter<PurchaseOrder> {

    private List<PurchaseOrder> items;
    int resource;

    public POAdapter(Context context, List<PurchaseOrder> items) {
        super(context, R.layout.crow, items);
        this.resource = R.layout.porow;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null);
        PurchaseOrder po = items.get(position);
        if (po != null) {
            int[] dest = new int[]{R.id.textView1, R.id.textView2, R.id.textView3};
            String[] src = new String[]{"poId", "orderDate", "deliveryDate"};
            for (int n = 0; n < dest.length; n++) {
                TextView txt = v.findViewById(dest[n]);
                txt.setText(po.get(src[n]));
                txt.setTextColor(Color.BLACK);
            }
        }
        return v;
    }
}
