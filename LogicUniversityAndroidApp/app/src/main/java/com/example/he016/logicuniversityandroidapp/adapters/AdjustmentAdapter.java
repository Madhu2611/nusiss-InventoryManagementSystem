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
import com.example.he016.logicuniversityandroidapp.model.AdjustmentVoucher;


import java.util.List;

public class AdjustmentAdapter extends ArrayAdapter<AdjustmentVoucher> {

    private List<AdjustmentVoucher> items;
    int resource;

    public AdjustmentAdapter(Context context, List<AdjustmentVoucher> items) {
        super(context, R.layout.row, items);
        this.resource = R.layout.row;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null);
        AdjustmentVoucher adjustmentVoucher = items.get(position);
        if (adjustmentVoucher != null) {
            int[] dest = new int[]{R.id.textView1, R.id.textView2};
            String[] src = new String[]{"description", "totalamount"};
            for (int n = 0; n < dest.length; n++) {
                TextView txt = v.findViewById(dest[n]);
                txt.setText(adjustmentVoucher.get(src[n]));
                txt.setTextColor(Color.BLACK);

                /*if (n == dest.length - 1) {
                    Locale locale = Locale.US;
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    double bookPrice = Double.parseDouble(inventory.get(src[n]));
                    txt.setText(fmt.format(bookPrice));
                }*/
            }
        }
        return v;
    }
}
