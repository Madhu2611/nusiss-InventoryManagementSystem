package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category extends HashMap<String, String> {

    //static String host = "172.23.200.41";
    static String baseURL;

    static {
        baseURL = String.format("%s/api/department", JSONParser.RoutePrefix);

    }

    // Constructor
    public Category(String categoryId, String location) {
        put("CategoryId", categoryId);
        put("Location", location);
    }

    public Category(String category) {
        put("category", category);
    }


    // Method
    // Get categories as string
    public static List<String> ListCategories() {
        List<String> list = new ArrayList<String>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/ReadCategories");
        try {
            for (int i = 0; i < a.length(); i++) {
                list.add(a.getString(i));
            }

        } catch (Exception e) {
            Log.e("CategoryList", "JSONArray error");
        }
        return list;
    }

    // Get categories as Category
    //get a list of category from category table with location
    public static List<Category> ReadCategory() {
        List<Category> list = new ArrayList<Category>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/ReadCategories", baseURL));
        try {
            for (int i = 0; i < a.length(); i++) {
                list.add(new Category(a.getString(i)));
                //list.add(new Category(b.toString()));
            }
        } catch (Exception e) {
            Log.e("CatalogueList", "JSONArray error");
        }
        return list;
    }
}

