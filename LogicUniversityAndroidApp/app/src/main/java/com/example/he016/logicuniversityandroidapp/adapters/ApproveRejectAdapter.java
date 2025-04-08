package com.example.he016.logicuniversityandroidapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.example.he016.logicuniversityandroidapp.model.RequestDetail;
import java.util.ArrayList;
import java.util.List;
import com.example.he016.logicuniversityandroidapp.R;

public class ApproveRejectAdapter extends BaseAdapter implements ListAdapter {

    TextView category, description, quantity, price;
    private Context context;
    private List<RequestDetail> detailsList = new ArrayList<RequestDetail>();


    public ApproveRejectAdapter(Context context, List<RequestDetail> requestList){
        this.detailsList = requestList;
        this.context = context;

    }

    @Override
    public int getCount(){
        return detailsList.size();
    }

    @Override
    public Object getItem(int position){
        return detailsList.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row1, null);
        }
        RequestDetail req = detailsList.get(position);


        String categoryName = req.get("category");
        String desc = req.get("description");
        String qty = req.get("quantityNeed");
        String stdPrice = req.get("stdPrice");
        category = (TextView) view.findViewById(R.id.textView1);
        category.setText(categoryName);
        category.setTextColor(Color.BLACK);

        description = (TextView) view.findViewById(R.id.textView2);
        description.setText(desc);
        description.setTextColor(Color.BLACK);

        quantity = (TextView) view.findViewById(R.id.textView3);
        quantity.setText(qty);
        quantity.setTextColor(Color.BLACK);

        price = (TextView) view.findViewById(R.id.textView4);
        price.setText(stdPrice);
        price.setTextColor(Color.BLACK);


        return view;
    }

}

