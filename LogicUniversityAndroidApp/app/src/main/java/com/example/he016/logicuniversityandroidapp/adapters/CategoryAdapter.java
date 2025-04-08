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
import com.example.he016.logicuniversityandroidapp.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private List<Category> items;
    int resource;

    public CategoryAdapter(Context context, List<Category> items) {
        super(context, R.layout.crow, items);
        this.resource = R.layout.categoryitemsrow;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null);
        Category category = items.get(position);
        if (category != null) {
            int[] dest = new int[]{R.id.textViewcategoryitemsrow};
            String[] src = new String[]{"category"};
            for (int n = 0; n < dest.length; n++) {
                TextView txt = v.findViewById(dest[n]);
                txt.setText(category.get(src[n]));
                txt.setTextColor(Color.BLACK);
                }
            }
        return v;
    }
}
