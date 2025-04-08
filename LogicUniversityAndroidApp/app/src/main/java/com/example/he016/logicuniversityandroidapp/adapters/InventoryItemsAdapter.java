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
import com.example.he016.logicuniversityandroidapp.model.Inventory;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InventoryItemsAdapter extends ArrayAdapter<Inventory> {

    private List<Inventory> items;
    int resource;

    public InventoryItemsAdapter(Context context, List<Inventory> items) {
        super(context, R.layout.crow, items);
        this.resource = R.layout.inventoryitemsrow;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null);
        Inventory inventory = items.get(position);
        if (inventory != null) {
            int[] dest = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, };
            String[] src = new String[]{"itemNo", "description", "balance"};
            for (int n = 0; n < dest.length; n++) {
                TextView txt = v.findViewById(dest[n]);
                txt.setText(inventory.get(src[n]));
                txt.setTextColor(Color.BLACK);

                if (n == dest.length - 1) {
                    Locale locale = Locale.US;
                    NumberFormat fmt = NumberFormat.getIntegerInstance(locale);  //getting balance in integer
                    int bal = Integer.parseInt(inventory.get(src[n]));
                    txt.setText(fmt.format(bal));
                }
            }
        }




//        int[] dest1 = new int[]{R.id.textViewLabel1, R.id.textViewLabel2, R.id.textViewLabel3
//                };
//
//        for(int i = 0; i < 4; i++){
//
//            TextView txt = v.findViewById(dest1[i]);
//            txt.setTextColor(Color.RED);
//        }
        return v;
    }
}

