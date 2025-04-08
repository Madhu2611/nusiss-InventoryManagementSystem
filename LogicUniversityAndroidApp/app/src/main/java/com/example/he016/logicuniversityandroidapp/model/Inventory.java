package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory extends HashMap<String, String>

{
    //static String host = "172.23.200.41";
    static String baseURL;
    static String imageURL;

    static {
        baseURL = String.format("%s/api/department", JSONParser.RoutePrefix);
    }

    // Constructor
    public Inventory(String itemNo, String category, String description, String balance) {
        put("itemNo", itemNo);
        put("category", category);
        put("description", description);
        put("balance", balance);
    }

    public Inventory(String itemNo, String category, String description, String balance, String reorderLevel, String reorderQuantity, String unitMeasure, String stdPrice, String supplierId1, String supplierId2, String supplierId3) {
        put("itemNo", itemNo);
        put("category", category);
        put("description", description);
        put("balance", String.valueOf(balance));
        put("reorderLevel", reorderLevel);
        put("reorderQuantity", reorderQuantity);
        put("unitMeasure", unitMeasure);
        put("stdPrice", stdPrice);
        put("supplierId1", supplierId1);
        put("supplierId2", supplierId2);
        put("supplierId3", supplierId3);
    }

    // Method
    public static List<Inventory> ListItemsByCategory(String categoryId) {
        List<Inventory> list = new ArrayList<Inventory>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/ReadInventory/%s", baseURL, categoryId));
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Inventory(b.getString("itemNo"),
                        b.getString("category"),
                        b.getString("description"),
                        String.format("%d", b.getInt("balance")),
                        String.format("%d", b.getInt("reorderLevel")),
                        String.format("%d", b.getInt("reorderQuantity")),
                        b.getString("unitMeasure"),
                        String.format("%,.2f", b.getDouble("stdPrice")),
                        //b.getString("StdPrice"),
                        b.getString("supplierId1"),
                        b.getString("supplierId2"),
                        b.getString("supplierId3")));
            }
        } catch (Exception e) {
            Log.e("ListItemsByCategory", "JSONArray error");
        }
        return list;
    }

    public static Inventory ReadInventoryItem(String itemNo) {
        try {
            //must indicate the method for the method
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/%s", baseURL, itemNo));
            Inventory i = new Inventory(a.getString("ItemNo"),
                    a.getString("Category"),
                    a.getString("Description"),
                    a.getString("Balance"),
                    a.getString("ReorderLevel"),
                    a.getString("ReorderQuantity"),
                    a.getString("UnitMeasure"),
                    a.getString("StdPrice"),
                    a.getString("SupplierId1"),
                    a.getString("SupplierId2"),
                    a.getString("SupplierId3"));
            return i;
        } catch (Exception e) {
            Log.e("Inventory", "JSONArray error");
        }
        return null;
    }

    public static List<String> ListDescriptionbyCategory(String categoryId) {

        List<String> list = new ArrayList<String>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/ReadInventory/%s", baseURL, categoryId));
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(
                        b.getString("description")
                );
            }
        } catch (Exception e) {
            Log.e("ListItemsByCategory", "JSONArray error");
        }
        return list;
    }

    public static HashMap<String, String> ListitemNobyCategory(String categoryId) {

        HashMap<String, String> list = new HashMap<>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/ReadInventory/%s", baseURL, categoryId));
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.put(b.getString("description"),
                        b.getString("itemNo")
                );

            }
        } catch (Exception e) {
            Log.e("ListitemNoByCategory", "JSONArray error");
        }
        return list;
    }
}

